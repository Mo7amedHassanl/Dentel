package com.m7md7sn.dentel.di

import com.m7md7sn.dentel.data.repository.ArticleRepository
import com.m7md7sn.dentel.data.repository.ArticleRepositoryImpl
import com.m7md7sn.dentel.data.repository.FavoritesRepository
import com.m7md7sn.dentel.data.repository.FavoritesRepositoryImpl
import com.m7md7sn.dentel.data.repository.HomeRepository
import com.m7md7sn.dentel.data.repository.HomeRepositoryImpl
import com.m7md7sn.dentel.data.repository.ProfileRepository
import com.m7md7sn.dentel.data.repository.ProfileRepositoryImpl
import com.m7md7sn.dentel.data.repository.SectionRepository
import com.m7md7sn.dentel.data.repository.SectionRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindSectionRepository(
        sectionRepositoryImpl: SectionRepositoryImpl
    ): SectionRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl
    ): FavoritesRepository
}
