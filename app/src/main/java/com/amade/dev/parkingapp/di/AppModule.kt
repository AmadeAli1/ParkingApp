package com.amade.dev.parkingapp.di

import com.amade.dev.parkingapp.service.AuthService
import com.amade.dev.parkingapp.service.ParkingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.time.Duration
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val LOCALHOST = "http://192.168.43.128:8080/api/"
    private const val IP = "http://26.23.254.172:8080/api/"
    private const val VORTEX_IP = "https://userc438776303b6b5f.app.vtxhub.com/api/"

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(IP)
            .client(
                OkHttpClient.Builder()
                    .writeTimeout(Duration.ofSeconds(60))
                    .callTimeout(Duration.ofSeconds(40))
                    .connectTimeout(Duration.ofSeconds(40))
                    .readTimeout(Duration.ofSeconds(40))
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun providesParkingService(retrofit: Retrofit): ParkingService {
        return retrofit.create()
    }


    @Provides
    @Singleton
    fun providesHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(WebSockets) {
                contentConverter =
                    KotlinxWebsocketSerializationConverter(Json { ignoreUnknownKeys = true })
            }
        }
    }

}