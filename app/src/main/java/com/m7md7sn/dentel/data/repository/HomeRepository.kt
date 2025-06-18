package com.m7md7sn.dentel.data.repository

import com.m7md7sn.dentel.data.model.Section
import com.m7md7sn.dentel.data.model.SuggestedTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
class HomeRepositoryImpl @Inject constructor() : HomeRepository {

    override fun getSections(): Flow<List<Section>> = flow {
        // In a real app, this might come from a database or remote API
        emit(com.m7md7sn.dentel.data.model.sectionsData)
    }

    override fun getSuggestedTopics(): Flow<List<SuggestedTopic>> = flow {
        // In a real app, this might come from a backend API
        emit(listOf(
            SuggestedTopic(" حشوات الأسنان \nكل ما تريد معرفته عنها"),
            SuggestedTopic("Root Canal Treatment"),
            SuggestedTopic("Teeth Whitening"),
            SuggestedTopic("Orthodontics"),
            SuggestedTopic("Cosmetic Dentistry"),
            SuggestedTopic("Oral Hygiene Tips")
        ))
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
