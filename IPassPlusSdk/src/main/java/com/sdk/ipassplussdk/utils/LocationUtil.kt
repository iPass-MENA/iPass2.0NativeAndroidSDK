package com.sdk.ipassplussdk.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.sdk.ipassplussdk.model.request.initiate_data.LocationData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object LocationUtil {

    @SuppressLint("MissingPermission")
    fun getLocation(context: Context, ipAddress: String? = null): LocationData {
        val capturedAt = getIso8601Timestamp()
        val formattedIp = ipAddress?.takeIf { it.isNotBlank() }

        val hasLocationFeature = try {
            context.packageManager?.hasSystemFeature(PackageManager.FEATURE_LOCATION) ?: true
        } catch (_: Exception) {
            true
        }

        if (!hasLocationFeature) {
            return LocationData(
                latitude = null,
                longitude = null,
                accuracy = null,
                locationSource = "UNAVAILABLE",
                permissionStatus = "unavailable",
                ipAddress = formattedIp,
                capturedAt = capturedAt,
            )
        }

        val finePermission = try {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } catch (_: Exception) {
            false
        }

        val coarsePermission = try {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } catch (_: Exception) {
            false
        }

        val hasPermission = finePermission || coarsePermission

        if (!hasPermission) {
            return LocationData(
                latitude = null,
                longitude = null,
                accuracy = null,
                locationSource = "IP",
                permissionStatus = "denied",
                ipAddress = formattedIp,
                capturedAt = capturedAt,
            )
        }

        // Permission is granted
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            ?: return LocationData(
                latitude = null,
                longitude = null,
                accuracy = null,
                locationSource = "IP",
                permissionStatus = "granted",
                ipAddress = formattedIp,
                capturedAt = capturedAt,
            )

        val bestLocation = getBestLastKnownLocation(locationManager)

        return if (bestLocation != null) {
            LocationData(
                latitude = bestLocation.latitude,
                longitude = bestLocation.longitude,
                accuracy = if (bestLocation.hasAccuracy()) bestLocation.accuracy else null,
                locationSource = "GPS",
                permissionStatus = "granted",
                ipAddress = formattedIp,
                capturedAt = capturedAt,
            )
        } else {
            LocationData(
                latitude = null,
                longitude = null,
                accuracy = null,
                locationSource = "IP",
                permissionStatus = "granted",
                ipAddress = formattedIp,
                capturedAt = capturedAt,
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getBestLastKnownLocation(locationManager: LocationManager): Location? {
        val providers = try {
            val list = locationManager.getProviders(true).toMutableList()
            if (!list.contains(LocationManager.GPS_PROVIDER)) list.add(LocationManager.GPS_PROVIDER)
            if (!list.contains(LocationManager.NETWORK_PROVIDER)) list.add(LocationManager.NETWORK_PROVIDER)
            if (!list.contains(LocationManager.PASSIVE_PROVIDER)) list.add(LocationManager.PASSIVE_PROVIDER)
            list
        } catch (_: Exception) {
            listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER)
        }

        var bestLocation: Location? = null

        for (provider in providers) {
            try {
                val loc = locationManager.getLastKnownLocation(provider)
                if (loc != null) {
                    val current = bestLocation
                    if (current == null) {
                        bestLocation = loc
                    } else if (loc.time > current.time) {
                        bestLocation = loc
                    } else if (loc.hasAccuracy() && current.hasAccuracy() && loc.accuracy < current.accuracy) {
                        bestLocation = loc
                    }
                }
            } catch (_: Exception) {
            }
        }

        // Reject stale cached locations older than 1 minute (60,000 ms) and request fresh fix
        val maxStaleAgeMs = 60 * 1000L
        val currentLoc = bestLocation
        val isStale = currentLoc == null || (System.currentTimeMillis() - currentLoc.time) > maxStaleAgeMs

        if (isStale) {
            val freshLocation = requestFreshLocationFix(locationManager, providers)
            if (freshLocation != null) {
                bestLocation = freshLocation
            }
        }

        return bestLocation
    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission")
    private fun requestFreshLocationFix(locationManager: LocationManager, providers: List<String>): Location? {
        var fetchedLocation: Location? = null
        val latch = CountDownLatch(1)
        val handler = Handler(Looper.getMainLooper())

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                fetchedLocation = location
                latch.countDown()
            }
            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        handler.post {
            for (provider in providers) {
                try {
                    if (locationManager.isProviderEnabled(provider)) {
                        locationManager.requestSingleUpdate(provider, listener, Looper.getMainLooper())
                        break
                    }
                } catch (_: Exception) {
                }
            }
        }

        try {
            latch.await(2000, TimeUnit.MILLISECONDS)
        } catch (_: Exception) {
        } finally {
            handler.post {
                try {
                    locationManager.removeUpdates(listener)
                } catch (_: Exception) {}
            }
        }

        return fetchedLocation
    }

    private fun getIso8601Timestamp(timeMs: Long = System.currentTimeMillis()): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date(timeMs))
    }
}
