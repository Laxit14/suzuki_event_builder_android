package com.multitv.eventbuilder.base

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    open fun getStatusBarView(): View? = null
    open fun getBottomBarView(): View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Apply insets only for Android 14+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

                getStatusBarView()?.setPadding(
                    getStatusBarView()?.paddingLeft ?: 0,
                    systemBars.top,
                    getStatusBarView()?.paddingRight ?: 0,
                    getStatusBarView()?.paddingBottom ?: 0
                )

                getBottomBarView()?.setPadding(
                    getBottomBarView()?.paddingLeft ?: 0,
                    getBottomBarView()?.paddingTop ?: 0,
                    getBottomBarView()?.paddingRight ?: 0,
                    systemBars.bottom
                )

                WindowInsetsCompat.CONSUMED
            }

            ViewCompat.requestApplyInsets(view)
        }
    }
}

