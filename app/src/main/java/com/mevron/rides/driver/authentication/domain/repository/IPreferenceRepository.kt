package com.mevron.rides.driver.authentication.domain.repository

interface IPreferenceRepository {
    fun getStringForKey(key: String): String
    fun setStringForKey(key: String, value: String)
}