package com.m7md7sn.dentel.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.m7md7sn.dentel.data.model.Section
import com.m7md7sn.dentel.data.model.SuggestedTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository interface for home screen data
 */
interface HomeRepository {
    /**
     * Get all available sections for display on home screen
     */
    fun getSections(): Flow<List<Section>>

    /**
     * Get suggested topics for display on home screen
     */
    fun getSuggestedTopics(): Flow<List<SuggestedTopic>>

    /**
     * Get the daily reminder message
     */
    fun getDailyReminder(): Flow<ReminderMessage>
}

/**
 * Implementation of HomeRepository
 */
@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : HomeRepository {

    override fun getSections(): Flow<List<Section>> = flow {
        // In a real app, this might come from a database or remote API
        emit(com.m7md7sn.dentel.data.model.sectionsData)
    }

    override fun getSuggestedTopics(): Flow<List<SuggestedTopic>> = flow {
        try {
            // Fetch suggested topics from Firestore "suggestions" collection
            val snapshot = firestore.collection("suggestions")
                .get()
                .await()

            val suggestedTopics = snapshot.documents.mapNotNull { document ->
                document.toObject(SuggestedTopic::class.java)?.copy(id = document.id)
            }

            emit(suggestedTopics)
        } catch (e: Exception) {
            // Log the error for debugging purposes
            Log.e("HomeRepository", "Error fetching suggested topics: ${e.message}", e)

            // Propagate the error to the ViewModel to handle appropriately
            throw Exception("Failed to load suggested topics: ${e.message}", e)
        }
    }

    override fun getDailyReminder(): Flow<ReminderMessage> = flow {
        // In a real app, this might rotate daily or come from a content API
        emit(ReminderMessage(
            title = "تنظيف الأسنان بانتظام وبطريقة صحيحة",
            description = "يضمن لك أسنان سليمة ويحفظها من كافة الأمراض والمشاكل ",
            imageResId = com.m7md7sn.dentel.R.drawable.ic_tooth_cleaning
        ))
    }
}

/**
 * Data class representing a reminder message
 */
data class ReminderMessage(
    val title: String,
    val description: String,
    val imageResId: Int
)
