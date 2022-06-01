package com.mevron.rides.driver.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.home.domain.model.StateMachineDomainData
import com.mevron.rides.driver.home.domain.usecase.GetStateMachineStateUseCase
import com.mevron.rides.driver.home.ui.state.StateMachineState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GetStateMachineViewModel @Inject constructor(
    private val useCase: GetStateMachineStateUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<StateMachineState> =
        MutableStateFlow(StateMachineState.EMPTY)

    val stateMachineState: StateFlow<StateMachineState>
        get() = mutableState

    private val currentState: StateMachineState
        get() = stateMachineState.value

    fun getStateMachine() {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(isLoading = true, error = "") }
            when (val result = useCase()) {
                is DomainModel.Success -> {
                    setState {
                        copy(
                            isLoading = false,
                            data = result.data as StateMachineDomainData,
                            error = ""
                        )
                    }
                }
                is DomainModel.Error -> {
                    setState {
                        copy(isLoading = false, error = result.error.message.toString())
                    }
                }
            }
        }
    }

    private fun setState(reducer: StateMachineState.() -> StateMachineState) {
        val newState = currentState.reducer()
        mutableState.value = newState
    }
}