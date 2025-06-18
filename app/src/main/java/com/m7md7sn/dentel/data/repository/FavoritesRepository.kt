package com.m7md7sn.dentel.data.repository

import com.m7md7sn.dentel.data.model.Article
import com.m7md7sn.dentel.data.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository interface for favorites-related data operations
 */
interface FavoritesRepository {
    /**
     * Get all favorite articles
     */
    fun getFavoriteArticles(): Flow<List<FavoriteItem>>

    /**
     * Get all favorite videos
     */
    fun getFavoriteVideos(): Flow<List<FavoriteItem>>

    /**
     * Toggle favorite status for an item
     */
    suspend fun toggleFavorite(item: FavoriteItem): Boolean
}

/**
 * Implementation of FavoritesRepository interface
 */
@Singleton
class FavoritesRepositoryImpl @Inject constructor() : FavoritesRepository {

    // In a real app, this would come from a database or remote API
    // Simulating persistent storage with in-memory lists for demonstration
    private val favoriteArticles = mutableListOf<FavoriteItem>(
        FavoriteItem("1", "Teeth Whitening Guide", FavoriteType.ARTICLE),
        FavoriteItem("2", "Dental Hygiene Tips", FavoriteType.ARTICLE),
        FavoriteItem("3", "Flossing Techniques", FavoriteType.ARTICLE),
        FavoriteItem("4", "Foods for Healthy Teeth", FavoriteType.ARTICLE)
    )

    private val favoriteVideos = mutableListOf<FavoriteItem>(
        FavoriteItem("5", "Proper Brushing Tutorial", FavoriteType.VIDEO),
        FavoriteItem("6", "Preventing Gum Disease", FavoriteType.VIDEO),
        FavoriteItem("7", "Children's Dental Care", FavoriteType.VIDEO)
    )

    override fun getFavoriteArticles(): Flow<List<FavoriteItem>> = flow {
        emit(favoriteArticles)
    }

    override fun getFavoriteVideos(): Flow<List<FavoriteItem>> = flow {
        emit(favoriteVideos)
    }

    override suspend fun toggleFavorite(item: FavoriteItem): Boolean {
        return when (item.type) {
            FavoriteType.ARTICLE -> {
                val existing = favoriteArticles.find { it.id == item.id }
                if (existing != null) {
                    favoriteArticles.remove(existing)
                    false // No longer a favorite
                } else {
                    favoriteArticles.add(item)
                    true // Is now a favorite
                }
            }
            FavoriteType.VIDEO -> {
                val existing = favoriteVideos.find { it.id == item.id }
                if (existing != null) {
                    favoriteVideos.remove(existing)
                    false // No longer a favorite
                } else {
                    favoriteVideos.add(item)
                    true // Is now a favorite
                }
            }
        }
    }
}
