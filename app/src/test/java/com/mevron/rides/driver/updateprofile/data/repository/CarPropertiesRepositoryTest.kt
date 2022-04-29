package com.mevron.rides.driver.updateprofile.data.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.mockError
import com.mevron.rides.driver.updateprofile.data.model.CarMakesResponse
import com.mevron.rides.driver.updateprofile.data.model.CarMakesSuccessData
import com.mevron.rides.driver.updateprofile.data.model.CarModelResponse
import com.mevron.rides.driver.updateprofile.data.model.CarModelSuccessData
import com.mevron.rides.driver.updateprofile.data.model.CarYearData
import com.mevron.rides.driver.updateprofile.data.model.CarYearResponse
import com.mevron.rides.driver.updateprofile.data.model.CarYearSuccessResponseData
import com.mevron.rides.driver.updateprofile.data.network.CarPropertiesApi
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

class CarPropertiesRepositoryTest {

    private val api: CarPropertiesApi = mockk()
    private val repository = CarPropertiesRepository(api)

    @Test
    fun `when CarPropertiesRepository#getCarMakes is invoked with success - returns CarMakes from api`(): Unit =
        runBlocking {
            val carMakesResponse = CarMakesResponse(
                carMakesSuccessData = CarMakesSuccessData(
                    carMakesList = listOf(),
                    message = "test",
                    status = ""
                )
            )

            val response: Response<CarMakesResponse> = mockk {
                every { body() }.returns(carMakesResponse)
                every { isSuccessful }.returns(true)
            }

            coEvery { api.getCarMakes() }.coAnswers { response }

            val result = repository.getCarMakes()

            assert(result is DomainModel.Success)
        }

    @Test
    fun `when CarPropertiesRepository#getCarMakes fails - returns error`(): Unit =
        runBlocking {

            val response: Response<CarMakesResponse> = mockError()

            coEvery { api.getCarMakes() }.coAnswers { response }

            val result = repository.getCarMakes()

            assert(result is DomainModel.Error)
        }

    @Test
    fun `when CarPropertiesRepository#getCarModels is invoked - returns CarModels from api`(): Unit =
        runBlocking {
            val body = CarModelResponse(
                carModelSuccessData = CarModelSuccessData(
                    carModelList = listOf(),
                    message = "test",
                    status = "SUCCESS"
                )
            )

            val response: Response<CarModelResponse> = mockk {
                every { body() }.returns(body)
                every { isSuccessful }.returns(true)
            }

            coEvery { api.getCarModels(any()) }.coAnswers { response }

            val result = repository.getCarModels(hashMapOf(Pair("test", "test")))

            assert(result is DomainModel.Success)
        }

    @Test
    fun `when CarPropertiesRepository#getCarModels fails - returns error`(): Unit =
        runBlocking {

            val response: Response<CarModelResponse> = mockError()

            coEvery { api.getCarModels(any()) }.coAnswers { response }

            val result = repository.getCarModels(hashMapOf())

            assert(result is DomainModel.Error)
        }

    @Test
    fun `when CarPropertiesRepository#getCarYears is invoked - returns CarYears from api`() =
        runBlocking {
            val body = CarYearResponse(
                carYearSuccessData = CarYearSuccessResponseData(
                    carYearDataList = listOf(CarYearData(year = "2000", id = 1)),
                    message = "test",
                    status = "SUCCESS"
                )
            )

            val response: Response<CarYearResponse> = mockk {
                every { body() }.returns(body)
                every { isSuccessful }.returns(true)
            }

            coEvery { api.getCarYear(any()) }.coAnswers { response }

            val result = repository.getCarYears(hashMapOf(Pair("test", "test")))

            assert(result is DomainModel.Success)
        }

    @Test
    fun `when CarPropertiesRepository#getCarYears fails - returns error`(): Unit =
        runBlocking {

            val response: Response<CarYearResponse> = mockError()

            coEvery { api.getCarYear(any()) }.coAnswers { response }

            val result = repository.getCarYears(hashMapOf())

            assert(result is DomainModel.Error)
        }
}