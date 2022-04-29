package com.mevron.rides.driver.updateprofile.data.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.mockDefaultResponse
import com.mevron.rides.driver.mockError
import com.mevron.rides.driver.updateprofile.data.network.UpdateProfileApi
import com.mevron.rides.driver.updateprofile.domain.model.AddVehicleRequest
import com.mevron.rides.driver.updateprofile.domain.model.SecurityNumRequest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import org.junit.Test

class UpdateProfileRepositoryTest {

    private val api: UpdateProfileApi = mockk()
    private val repository = UpdateProfileRepository(api)

    @Test
    fun `when UpdateProfileRepository#addVehicle is invoked - calls api to add vehicle`(): Unit =
        runBlocking {
            coEvery { api.addVehicle(any()) }.coAnswers { mockDefaultResponse() }
            val request = AddVehicleRequest(
                color = "GREEN",
                make = "Tesla",
                model = "test",
                plateNumber = "123",
                preference = "test",
                year = "1988"
            )
            val result = repository.addVehicle(request)
            assert(result is DomainModel.Success)
        }

    @Test
    fun `when UpdateProfileApi#addVehicle fails - returns error`(): Unit =
        runBlocking {
            coEvery { api.addVehicle(any()) }.coAnswers { mockError() }
            val request = AddVehicleRequest(
                color = "GREEN",
                make = "Tesla",
                model = "test",
                plateNumber = "123",
                preference = "test",
                year = "1988"
            )
            val result = repository.addVehicle(request)
            assert(result is DomainModel.Error)
        }

    @Test
    fun `when UpdateProfileRepository#uploadLicence is invoked - api uploads profile`(): Unit =
        runBlocking {
            coEvery { api.uploadProfile(any()) }.coAnswers { mockDefaultResponse() }
            val request: MultipartBody.Part = mockk()
            val result = repository.uploadProfile(request)
            assert(result is DomainModel.Success)
        }

    @Test
    fun `when UpdateProfileRepository#uploadLicence fails- returns error`(): Unit =
        runBlocking {
            coEvery { api.uploadProfile(any()) }.coAnswers { mockError() }
            val request: MultipartBody.Part = mockk()
            val result = repository.uploadProfile(request)
            assert(result is DomainModel.Error)
        }

    @Test
    fun `when UpdateProfileRepository#uploadSticker is invoked - api uploads sticker`(): Unit =
        runBlocking {
            coEvery { api.uploadSticker(any()) }.coAnswers { mockDefaultResponse() }
            val request: MultipartBody.Part = mockk()
            val result = repository.uploadSticker(request)
            assert(result is DomainModel.Success)
        }

    @Test
    fun `when UpdateProfileRepository#uploadSticker fails- returns error`(): Unit =
        runBlocking {
            coEvery { api.uploadSticker(any()) }.coAnswers { mockError() }
            val request: MultipartBody.Part = mockk()
            val result = repository.uploadSticker(request)
            assert(result is DomainModel.Error)
        }

    @Test
    fun `when UpdateProfileRepository#uploadProfile is invoked - api uploads profile`(): Unit =
        runBlocking {
            coEvery { api.uploadProfile(any()) }.coAnswers { mockDefaultResponse() }
            val request: MultipartBody.Part = mockk()
            val result = repository.uploadProfile(request)
            assert(result is DomainModel.Success)
        }

    @Test
    fun `when UpdateProfileRepository#uploadProfile fails- returns error`(): Unit =
        runBlocking {
            coEvery { api.uploadProfile(any()) }.coAnswers { mockError() }
            val request: MultipartBody.Part = mockk()
            val result = repository.uploadProfile(request)
            assert(result is DomainModel.Error)
        }

    @Test
    fun `when UpdateProfileRepository#uploadInsurance is invoked - api uploads insurance`(): Unit =
        runBlocking {
            coEvery { api.uploadInsurance(any()) }.coAnswers { mockDefaultResponse() }
            val request: MultipartBody.Part = mockk()
            val result = repository.uploadInsurance(request)
            assert(result is DomainModel.Success)
        }

    @Test
    fun `when UpdateProfileRepository#uploadInsurance fails- returns error`(): Unit =
        runBlocking {
            coEvery { api.uploadInsurance(any()) }.coAnswers { mockError() }
            val request: MultipartBody.Part = mockk()
            val result = repository.uploadInsurance(request)
            assert(result is DomainModel.Error)
        }

    @Test
    fun `when UpdateProfileRepository#uploadSecurityNumber is invoked - api uploads securityNumber`(): Unit =
        runBlocking {
            coEvery { api.uploadSecurityNumber(any()) }.coAnswers { mockDefaultResponse() }
            val request = SecurityNumRequest(securityNumber = "455")
            val result = repository.uploadSecurityNumber(request)
            assert(result is DomainModel.Success)
        }

    @Test
    fun `when UpdateProfileRepository#uploadSecurityNumber fails - returns error`(): Unit =
        runBlocking {
            coEvery { api.uploadSecurityNumber(any()) }.coAnswers { mockError() }
            val request = SecurityNumRequest(securityNumber = "455")
            val result = repository.uploadSecurityNumber(request)
            assert(result is DomainModel.Error)
        }
}