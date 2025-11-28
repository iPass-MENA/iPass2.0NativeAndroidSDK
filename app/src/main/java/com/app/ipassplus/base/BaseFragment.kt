package com.ipassplus.ui.base

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  Apply system bar insets (e.g., bottom padding for buttons near gesture nav)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply only bottom padding to avoid gesture overlap
            v.setPadding(
                v.paddingLeft,
                v.paddingTop,
                v.paddingRight,
                systemBars.bottom
            )

            insets // Do NOT consume here
        }
    }
}
