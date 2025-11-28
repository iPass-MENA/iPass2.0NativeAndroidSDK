package com.ipassplus.ui.base

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
