package com.mevron.rides.driver.home

import app.cash.turbine.test
import com.mevron.rides.driver.authentication.domain.usecase.GetSharedPreferenceUseCase
import com.mevron.rides.driver.authentication.domain.usecase.GetSurgeUseCase
import com.mevron.rides.driver.home.domain.usecase.GetDocumentStatusUseCase
import com.mevron.rides.driver.home.domain.usecase.GetMapTripStateUseCase
import com.mevron.rides.driver.home.domain.usecase.ToggleOnlineStatusUseCase
import com.mevron.rides.driver.home.trip_management.domain.usecase.TripManagementActionUseCase
import com.mevron.rides.driver.home.ui.event.HomeViewEvent
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

// TODO Complete this test when the document status is mapped correctly
class HomeViewModelTest {

    private val onlineStatusUseCase: ToggleOnlineStatusUseCase = mockk()
    private val getDocumentStatusUseCase: GetDocumentStatusUseCase = mockk()
    private val getMapStateUseCase: GetMapTripStateUseCase = mockk()
    private val tripManageUseCase: TripManagementActionUseCase = mockk()
    private val preferenceUseCase: GetSharedPreferenceUseCase = mockk()
    private val getSurgeUseCase: GetSurgeUseCase = mockk()

    private val viewModel = HomeViewModel(
        onlineStatusUseCase,
        getDocumentStatusUseCase,
        getMapStateUseCase,
        tripManageUseCase,
        preferenceUseCase,
        getSurgeUseCase
    )

    @Test
    fun `when viewModel receives event - viewModel handles event`(): Unit = runBlocking {
        viewModel.onEventReceived(HomeViewEvent.OnDriveClick)
        viewModel.state.test {
            assert(awaitItem().isDriveActive)
        }
    }
}