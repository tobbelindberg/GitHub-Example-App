package com.github.bindings

import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.BindingAdapter

@BindingAdapter("goneUnless")
fun View.setGoneUnless(visible: Boolean) {
    if (parent is MotionLayout) {
        val layout = parent as MotionLayout
        val newVisibility = if (visible) View.VISIBLE else View.GONE
        for (constraintId in layout.constraintSetIds) {
            val constraint = layout.getConstraintSet(constraintId)
            if (constraint != null) {
                constraint.setVisibility(id, newVisibility)
                constraint.applyTo(layout)
            }

        }
    } else {
        visibility = if (visible) View.VISIBLE else View.GONE
    }
}