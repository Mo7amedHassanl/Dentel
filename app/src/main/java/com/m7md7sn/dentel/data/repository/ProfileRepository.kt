package com.m7md7sn.dentel.data.repository

import com.m7md7sn.dentel.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

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
}

/**
 * Implementation of ProfileRepository
 */
@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository
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

    override fun getFavoriteItems(type: FavoriteType): Flow<List<FavoriteItem>> {
        // In a real app, this would fetch favorites from a database or remote source
        // For now, we'll return dummy data
        val favorites = when (type) {
            FavoriteType.ARTICLE -> listOf(
                FavoriteItem("1", "Teeth Whitening", FavoriteType.ARTICLE),
                FavoriteItem("2", "Root Canal Treatment", FavoriteType.ARTICLE),
                FavoriteItem("3", "Dental Implants", FavoriteType.ARTICLE)
            )
            FavoriteType.VIDEO -> listOf(
                FavoriteItem("4", "Brushing Techniques", FavoriteType.VIDEO),
                FavoriteItem("5", "Flossing Guide", FavoriteType.VIDEO),
                FavoriteItem("6", "Dental Care Tips", FavoriteType.VIDEO)
            )
        }

        return flow { emit(favorites) }
    }
}

/**
 * Types of favorite items
 */
enum class FavoriteType {
    ARTICLE,
    VIDEO
}

/**
 * Data class for favorite items
 */
data class FavoriteItem(
    val id: String,
    val title: String,
    val type: FavoriteType
)
