package com.example.myapplication.di

import android.content.Context
import coil.ImageLoader
import coil.util.DebugLogger
import com.example.myapplication.data.repository.ScheduleRepositoryImpl
import com.example.myapplication.domain.repository.ScheduleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .allowHardware(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideScheduleRepository(
        @ApplicationContext context: Context,
        json: Json
    ): ScheduleRepository =
        ScheduleRepositoryImpl(context = context, json = json)

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
}
