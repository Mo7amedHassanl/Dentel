package com.m7md7sn.dentel.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.m7md7sn.dentel.data.model.Article
import com.m7md7sn.dentel.data.model.Video
import com.m7md7sn.dentel.data.model.FavoriteItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

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
class FavoritesRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val authRepository: AuthRepository
) : FavoritesRepository {
    private fun getUserId(): String? = authRepository.currentUser?.uid

    override fun getFavoriteArticles(): Flow<List<FavoriteItem>> = callbackFlow {
        val userId = getUserId() ?: run { trySend(emptyList()); close(); return@callbackFlow }
        val ref = db.collection("favorites").document(userId).collection("articles")
        val listener = ref.addSnapshotListener { snapshot, _ ->
            val items = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(FavoriteItem::class.java)?.copy(id = doc.id, type = FavoriteType.ARTICLE)
            } ?: emptyList()
            trySend(items)
        }
        awaitClose { listener.remove() }
    }

    override fun getFavoriteVideos(): Flow<List<FavoriteItem>> = callbackFlow {
        val userId = getUserId() ?: run { trySend(emptyList()); close(); return@callbackFlow }
        val ref = db.collection("favorites").document(userId).collection("videos")
        val listener = ref.addSnapshotListener { snapshot, _ ->
            val items = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(FavoriteItem::class.java)?.copy(id = doc.id, type = FavoriteType.VIDEO)
            } ?: emptyList()
            trySend(items)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun toggleFavorite(item: FavoriteItem): Boolean {
        val userId = getUserId() ?: return false
        val typeStr = when (item.type) {
            FavoriteType.ARTICLE -> "articles"
            FavoriteType.VIDEO -> "videos"
        }
        val docRef = db.collection("favorites").document(userId).collection(typeStr).document(item.id)
        val snapshot = docRef.get().await()
        return if (snapshot.exists()) {
            docRef.delete().await()
            false
        } else {
            docRef.set(item.copy(type = item.type)).await()
            true
        }
    }
}
