package com.mevron.rides.driver.home.data.repository

import com.mevron.rides.driver.home.domain.ISurgeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SurgeRepository : ISurgeRepository {

    private val surgeObtained = MutableStateFlow("")

    override val surgeUrl: Flow<String>
        get() = surgeObtained

    override fun setSurgeUrl(url: String) {
        surgeObtained.value = url
    }
}