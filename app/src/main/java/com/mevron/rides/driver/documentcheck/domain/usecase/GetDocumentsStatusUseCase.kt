package com.mevron.rides.driver.documentcheck.domain.usecase

import com.mevron.rides.driver.documentcheck.domain.repository.IDocumentRepository
import javax.inject.Inject

class GetDocumentsStatusUseCase @Inject constructor(private val repository: IDocumentRepository) {
    suspend operator fun invoke() = repository.getDocumentStatus()
}