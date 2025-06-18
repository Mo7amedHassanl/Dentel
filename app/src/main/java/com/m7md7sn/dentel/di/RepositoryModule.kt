package com.m7md7sn.dentel.di

import com.m7md7sn.dentel.data.repository.ArticleRepository
import com.m7md7sn.dentel.data.repository.ArticleRepositoryImpl
import com.m7md7sn.dentel.data.repository.VideoRepository
import com.m7md7sn.dentel.data.repository.VideoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindVideoRepository(
        videoRepositoryImpl: VideoRepositoryImpl
    ): VideoRepository

    @Binds
    @Singleton
    abstract fun bindArticleRepository(
        articleRepositoryImpl: ArticleRepositoryImpl
    ): ArticleRepository
}
