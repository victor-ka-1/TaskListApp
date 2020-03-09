package com.example.tasklistproject.ui

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseFullScreenBottomSheetDialogFragment : BottomSheetDialogFragment() {

    // HACK: Open dialog in EXPANDED mode (instead of COLLAPSING)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { d ->
            val sheet: ViewGroup =
                (d as BottomSheetDialog).findViewById(com.google.android.material.R.id.design_bottom_sheet)!!

            val params = sheet.layoutParams
            var statusBarHeight = 0
            val resource = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resource > 0) {
                statusBarHeight = resources.getDimensionPixelSize(resource)
            }
            val screenHeight = getScreenHeight() - statusBarHeight
            params.height = screenHeight
            sheet.layoutParams = params

            BottomSheetBehavior.from(sheet).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
                isHideable = true
            }
        }

        return dialog
    }

    protected open fun onSlide(bottomSheet: View, slideOffset: Float) {
    }

    protected open fun onStateChanged(bottomSheet: View, newState: Int) {
    }

    private fun getScreenHeight(): Int =
        Resources.getSystem().displayMetrics.heightPixels

}