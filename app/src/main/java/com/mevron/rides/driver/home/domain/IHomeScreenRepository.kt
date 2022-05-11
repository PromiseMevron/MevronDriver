package com.mevron.rides.driver.home.domain

import com.mevron.rides.driver.domain.DomainModel

interface IHomeScreenRepository {
    suspend fun toggleStatus(): DomainModel
    suspend fun getDocumentStatus(): DomainModel
}