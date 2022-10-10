package com.mevron.rides.driver.documentcheck.data.repository

import com.mevron.rides.driver.documentcheck.data.model.GetDocumentResponse
import com.mevron.rides.driver.documentcheck.data.network.DocumentAPI
import com.mevron.rides.driver.documentcheck.domain.repository.IDocumentRepository
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.remote.HTTPErrorHandler
import com.mevron.rides.driver.util.Constants

class DocumentRepository(private val api: DocumentAPI) : IDocumentRepository {
    override suspend fun getDocumentStatus(): DomainModel = api.getDocumentStatus().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
        } else {
            val error = HTTPErrorHandler.handleErrorWithCode(it)
            DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
        }
    }

    private fun GetDocumentResponse.toDomainModel() = DomainModel.Success(data = this)
}