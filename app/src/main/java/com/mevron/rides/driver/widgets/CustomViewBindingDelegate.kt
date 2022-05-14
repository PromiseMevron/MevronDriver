package com.mevron.rides.driver.widgets

import android.view.View
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T : ViewBinding> View.viewBinding(initializer: (View) -> T) = CustomViewBindingDelegate(initializer)

class CustomViewBindingDelegate<T : ViewBinding>(
    private val initializer: (View) -> T
) : ReadOnlyProperty<View, T> {

    /**
     * initiate variable for binding view
     */
    private var binding: T? = null

    override fun getValue(thisRef: View, property: KProperty<*>): T {
        binding?.let { return it }

        /**
         * Bind layout
         */
        val bindingView = initializer(thisRef)

        return bindingView.also { this.binding = it }
    }
}