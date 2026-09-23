package com.app.ipassplus.ui.dashboard.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.app.ipassplus.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DualPoiDisclaimerDialog(
    private val onContinue: () -> Unit
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.dialog_dual_poi_disclaimer,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnContinue = view.findViewById<Button>(R.id.btnContinue)

        btnContinue.setOnClickListener {

            dismiss()

            // Notify DashboardFragment that user tapped Continue
            onContinue.invoke()
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels * 0.92).toInt(),
                (resources.displayMetrics.heightPixels * 0.95).toInt()
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}