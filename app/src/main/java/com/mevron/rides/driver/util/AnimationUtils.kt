package com.mevron.rides.driver.util

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

object AnimationUtils {

    fun polyLineAnimator(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofInt(0, 100)
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 2000
        return valueAnimator
    }

    fun cabAnimator(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration =4000
        valueAnimator.interpolator = LinearInterpolator()
        return valueAnimator
    }

}