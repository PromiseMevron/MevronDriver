package com.mevron.rides.driver.documentcheck.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.documentcheck.data.model.CarProperties
import com.mevron.rides.driver.documentcheck.data.model.Document
import com.mevron.rides.driver.documentcheck.data.model.GetDocumentResponse
import com.mevron.rides.driver.documentcheck.domain.usecase.GetDocumentsStatusUseCase
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentCheckViewModel @Inject constructor(
    private val useCase: GetDocumentsStatusUseCase,
) : ViewModel() {

    private val mutableState: MutableStateFlow<DocumentState> =
        MutableStateFlow(DocumentState.EMPTY)

    val state: StateFlow<DocumentState>
        get() = mutableState

    fun onEvent(event: DocumentEvent) {
        when (event) {
            DocumentEvent.GetStatus -> getDocumentStatus()
        }
    }

    private fun getDocumentStatus() {
        updateState(loading = true)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase()) {
                is DomainModel.Success -> {
                    val data = result.data as GetDocumentResponse
                    updateState(
                        loading = false,
                        errorMessage = "",
                        documentStatus = data.docSuccess.docData.documents.toMutableList(),
                        carProperties = data.docSuccess.docData.carProperties
                    )
                }
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        loading = false,
                        error = "Failure to get documents",
                    )
                }
            }
        }
    }

    fun updateState(
        loading: Boolean? = null,
        errorMessage: String? = null,
        carProperties: CarProperties? = null,
        documentStatus: MutableList<Document>? = null,
    ) {
        val currentState = mutableState.value
        mutableState.update {
            currentState.copy(
                loading = loading ?: currentState.loading,
                error = errorMessage ?: currentState.error,
                carProperties = carProperties ?: currentState.carProperties,
                documentStatus = documentStatus ?: currentState.documentStatus
            )
        }
    }

}