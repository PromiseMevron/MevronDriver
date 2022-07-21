package com.mevron.rides.driver.documentcheck.data.repository

import com.mevron.rides.driver.documentcheck.data.model.GetDocumentResponse
import com.mevron.rides.driver.documentcheck.data.network.DocumentAPI
import com.mevron.rides.driver.documentcheck.domain.repository.IDocumentRepository
import com.mevron.rides.driver.domain.DomainModel

class DocumentRepository(private val api: DocumentAPI) : IDocumentRepository {
    override suspend fun getDocumentStatus(): DomainModel = api.getDocumentStatus().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Document not found"))
        } else {
            DomainModel.Error(Throwable(it.errorBody().toString()))
        }
    }

    private fun GetDocumentResponse.toDomainModel() = DomainModel.Success(data = this)
}