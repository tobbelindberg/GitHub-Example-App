package com.github.utils

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentTransaction
import com.github.R

class FragmentTransitionAnimations(
    private val context: Context?,
    private val openCloseType: OpenCloseType
) {

    enum class OpenCloseType {
        IN_OUT_RIGHT,
        IN_OUT_BOTTOM
    }

    fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return when (transit) {
            FragmentTransaction.TRANSIT_FRAGMENT_FADE -> if (enter) {
                AnimationUtils.loadAnimation(context, R.anim.fade_in)
            } else {
                AnimationUtils.loadAnimation(context, R.anim.fade_out)
            }
            FragmentTransaction.TRANSIT_FRAGMENT_CLOSE -> when (openCloseType) {
                OpenCloseType.IN_OUT_RIGHT -> if (enter) {
                    AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
                } else {
                    AnimationUtils.loadAnimation(context, R.anim.slide_out_right)
                }
                OpenCloseType.IN_OUT_BOTTOM -> if (enter) {
                    AnimationUtils.loadAnimation(context, R.anim.fade_in)
                } else {
                    AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom)
                }
            }
            FragmentTransaction.TRANSIT_FRAGMENT_OPEN -> when (openCloseType) {
                OpenCloseType.IN_OUT_RIGHT -> if (enter) {
                    AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                } else {
                    AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
                }
                OpenCloseType.IN_OUT_BOTTOM -> if (enter) {
                    AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
                } else {
                    AnimationUtils.loadAnimation(context, R.anim.fade_out)
                }
            }
            FragmentTransaction.TRANSIT_NONE -> null
            else -> if (enter) {
                AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
            } else {
                AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
            }
        }
    }
}