package com.mevron.rides.driver.documentcheck.di

import com.mevron.rides.driver.documentcheck.data.network.DocumentAPI
import com.mevron.rides.driver.documentcheck.data.repository.DocumentRepository
import com.mevron.rides.driver.documentcheck.domain.repository.IDocumentRepository
import com.mevron.rides.driver.util.Constants.MEVRON_CALL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DocumentModule {

    @Provides
    @Singleton
    fun provideDocApi(@Named(MEVRON_CALL) retrofit: Retrofit) =
        retrofit.create<DocumentAPI>()

    @Provides
    @Singleton
    fun provideGoogleRepository(api: DocumentAPI): IDocumentRepository = DocumentRepository(api)
}