package com.mevron.rides.driver.domain

sealed interface DomainModel {
    data class Success(val data: Any) : DomainModel
    data class Error(val error: Throwable) : DomainModel
}