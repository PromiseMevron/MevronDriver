package com.mevron.rides.driver.home.domain

import kotlinx.coroutines.flow.Flow

interface ISurgeRepository {
    val surgeUrl: Flow<String>
    fun setSurgeUrl(url: String)
}