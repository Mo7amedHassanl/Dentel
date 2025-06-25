package com.m7md7sn.dentel.data.repository

import com.m7md7sn.dentel.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.callbackFlow
import com.m7md7sn.dentel.data.model.FavoriteItem

/**
 * Repository interface for profile-related operations
 */
interface ProfileRepository {
    /**
     * Get the current user profile information
     */
    fun getUserProfile(): Flow<User>

    /**
     * Get list of favorite items
     */
    fun getFavoriteItems(type: FavoriteType): Flow<List<FavoriteItem>>

    /**
     * Delete all user profile data from Firestore for a given userId
     */
    suspend fun deleteUserProfileData(userId: String)
}

/**
 * Implementation of ProfileRepository
 */
@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val favoritesRepository: FavoritesRepository,
    private val db: FirebaseFirestore
) : ProfileRepository {

    override fun getUserProfile(): Flow<User> = flow {
        val user = authRepository.currentUser?.let {
            User(
                id = it.uid,
                name = it.displayName ?: "",
                email = it.email ?: "",
                photoUrl = it.photoUrl?.toString()
            )
        } ?: User()

        emit(user)
    }

    override fun getFavoriteItems(type: FavoriteType): Flow<List<FavoriteItem>> = when (type) {
        FavoriteType.ARTICLE -> favoritesRepository.getFavoriteArticles()
        FavoriteType.VIDEO -> favoritesRepository.getFavoriteVideos()
    }

    override suspend fun deleteUserProfileData(userId: String) {
        // Delete user document from 'users' collection
        db.collection("users").document(userId).delete().await()
        // Delete all favorites (articles and videos)
        db.collection("favorites").document(userId).delete().await()
        // Add more collections as needed
    }
}

/**
 * Types of favorite items
 */
enum class FavoriteType {
    ARTICLE,
    VIDEO
}
