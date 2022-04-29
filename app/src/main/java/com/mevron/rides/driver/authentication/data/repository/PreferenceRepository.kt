package com.mevron.rides.driver.authentication.data.repository

import android.content.Context
import com.mevron.rides.driver.authentication.domain.repository.IPreferenceRepository
import com.mevron.rides.driver.util.Constants

class PreferenceRepository( context: Context) : IPreferenceRepository {

    private val sPref= context.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)
    private val editor = sPref.edit()

    override fun getStringForKey(key: String): String = sPref.getString(key, "") ?: ""

    override fun setStringForKey(key: String, value: String) {
        editor.putString(key, value)
        editor?.apply()
    }

    override fun clear() {
        editor.clear()
    }
}