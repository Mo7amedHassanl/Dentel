package com.m7md7sn.dentel.data.repository

import com.m7md7sn.dentel.data.model.Section
import com.m7md7sn.dentel.presentation.ui.section.Topic
import com.m7md7sn.dentel.presentation.ui.section.TopicType

/**
 * Repository interface for handling section and topics data
 * Following the repository pattern for better separation of concerns
 */
interface SectionRepository {
    /**
     * Get all available sections
     */
    suspend fun getSections(): List<Section>

    /**
     * Get a specific section by id
     */
    suspend fun getSectionById(sectionId: String): Section?

    /**
     * Get all topics for a specific section
     * @throws SectionException if there's an error loading the topics
     */
    suspend fun getTopicsForSection(sectionId: String): List<Topic>

    /**
     * Filter topics by type and search query
     */
    fun filterTopics(allTopics: List<Topic>, type: TopicType, query: String): List<Topic>

    /**
     * Get a specific topic by id
     * @param topicId The ID of the topic to fetch
     * @param topicType The type of the topic (Article or Video)
     * @throws SectionException if there's an error loading the topic
     */
    suspend fun getTopicById(topicId: String, topicType: TopicType): Topic?
}
