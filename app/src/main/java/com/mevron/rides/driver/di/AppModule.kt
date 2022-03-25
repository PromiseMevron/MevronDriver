package com.mevron.rides.driver.di

import android.content.Context
import androidx.room.Room
import com.mevron.rides.driver.App
import com.mevron.rides.driver.localdb.MevronDao
import com.mevron.rides.driver.localdb.MevronDatabase
import com.mevron.rides.driver.remote.MevronAPI
import com.mevron.rides.driver.remote.MevronRepo
import com.mevron.rides.driver.util.Constants.BASE_URL
import com.mevron.rides.driver.util.Constants.SHARED_PREF_KEY
import com.mevron.rides.driver.util.Constants.TOKEN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApi(): MevronAPI {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().followRedirects(true)
            .retryOnConnectionFailure(true)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain ->
                val sPref= App.ApplicationContext.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
               // val token = sPref.getString(TOKEN, null)
                val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiZW1haWwiOiIiLCJuYW1lIjoiIiwidXVpZCI6Ijg3Yjg2YTA1LTQ1Y2ItNDBkZS1hMWJmLTkyZmQ4MzYyNTg4OCIsInBob25lTnVtYmVyIjoiMjM0NzAzMzUwNTAxMyIsInR5cGUiOiJkcml2ZXIiLCJpYXQiOjE2NDYzNTY4NDh9.icyUCSLOxulAxTsNl_AqJzY4E4_9YXxl9zf_1LaTXcQ"
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(newRequest)
                // return response
            }).readTimeout(180, TimeUnit.SECONDS)
            .connectTimeout(180, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
            .create(MevronAPI::class.java)
    }


    @Singleton
    @Provides
    fun mainReop(api: MevronAPI, dao: MevronDao): MevronRepo {
        return MevronRepo(api = api, dao = dao)
    }


    @Singleton
    @Provides
    fun provideMevronDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, MevronDatabase::class.java, "mevron_database_rider")
        .fallbackToDestructiveMigration()
        .build()



    @Singleton
    @Provides
    fun provideMevronDao(
        database: MevronDatabase
    ) = database.addDAO()

}
