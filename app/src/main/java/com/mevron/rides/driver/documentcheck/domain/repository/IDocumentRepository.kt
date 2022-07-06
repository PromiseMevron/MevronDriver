package com.mevron.rides.driver.documentcheck.domain.repository

import com.mevron.rides.driver.domain.DomainModel

interface IDocumentRepository {
    suspend fun getDocumentStatus(): DomainModel
}