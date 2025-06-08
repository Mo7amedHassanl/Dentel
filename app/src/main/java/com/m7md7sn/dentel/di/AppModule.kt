package com.m7md7sn.dentel.di

import android.content.Context
import com.m7md7sn.dentel.data.repository.AuthRepository
import com.m7md7sn.dentel.data.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context
} 