package com.m7md7sn.dentel.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.m7md7sn.dentel.data.model.Article
import com.m7md7sn.dentel.data.model.Section
import com.m7md7sn.dentel.data.model.Video
import com.m7md7sn.dentel.data.model.sectionsData
import com.m7md7sn.dentel.presentation.ui.section.Topic
import com.m7md7sn.dentel.presentation.ui.section.TopicType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of SectionRepository interface
 * Handles all data operations related to sections and topics
 */
@Singleton
class SectionRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : SectionRepository {

    override suspend fun getSections(): List<Section> = sectionsData

    override suspend fun getSectionById(sectionId: String): Section? {
        return sectionsData.find { it.id == sectionId }
    }

    override suspend fun getTopicsForSection(sectionId: String): List<Topic> = withContext(Dispatchers.IO) {
        val topicsList = mutableListOf<Topic>()
        try {
            // Fetch articles
            val articlesSnapshot = db.collection("sections")
                .document(sectionId)
                .collection("articles")
                .get()
                .await()
            for (doc in articlesSnapshot) {
                val article = doc.toObject(Article::class.java).copy(id = doc.id)
                topicsList.add(
                    Topic(
                        id = article.id,
                        title = article.title,
                        subtitle = article.subtitle,
                        type = TopicType.Article,
                        content = article.content,
                        imageUrl = article.imageUrl
                    )
                )
            }
            // Fetch videos
            val videosSnapshot = db.collection("sections")
                .document(sectionId)
                .collection("videos")
                .get()
                .await()
            for (doc in videosSnapshot) {
                val video = doc.toObject(Video::class.java).copy(id = doc.id)
                topicsList.add(
                    Topic(
                        id = video.id,
                        title = video.title,
                        subtitle = video.subtitle,
                        type = TopicType.Video,
                        videoUrl = video.videoUrl,
                        thumbnailUrl = video.thumbnailUrl,
                        content = video.description
                    )
                )
            }
            return@withContext topicsList
        } catch (e: Exception) {
            throw SectionException("Error loading topics: ${e.message}")
        }
    }

    override suspend fun getTopicById(topicId: String, topicType: TopicType): Topic? = withContext(Dispatchers.IO) {
        try {
            // We need to search across all sections since we don't know which section contains the topic
            val sectionsRef = db.collection("sections")
            val sectionsSnapshot = sectionsRef.get().await()

            // Get the appropriate collection name based on type
            val collectionName = when (topicType) {
                TopicType.Article -> "articles"
                TopicType.Video -> "videos"
            }

            // Search in the appropriate collection type only
            for (sectionDoc in sectionsSnapshot.documents) {
                val sectionId = sectionDoc.id

                try {
                    // Try to get the document by direct path
                    val docRef = sectionsRef.document(sectionId)
                        .collection(collectionName)
                        .document(topicId)

                    val docSnapshot = docRef.get().await()

                    if (docSnapshot.exists()) {
                        return@withContext when (topicType) {
                            TopicType.Article -> {
                                val article = docSnapshot.toObject(Article::class.java)
                                if (article != null) {
                                    Topic(
                                        id = topicId,
                                        title = article.title,
                                        subtitle = article.subtitle,
                                        type = TopicType.Article,
                                        content = article.content,
                                        imageUrl = article.imageUrl
                                    )
                                } else null
                            }
                            TopicType.Video -> {
                                val video = docSnapshot.toObject(Video::class.java)
                                if (video != null) {
                                    Topic(
                                        id = topicId,
                                        title = video.title,
                                        subtitle = video.subtitle,
                                        type = TopicType.Video,
                                        videoUrl = video.videoUrl,
                                        thumbnailUrl = video.thumbnailUrl,
                                        content = video.description
                                    )
                                } else null
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Continue to the next section if there's an error
                    continue
                }
            }

            return@withContext null
        } catch (e: Exception) {
            throw SectionException("Error fetching topic: ${e.message}")
        }
    }

    override fun filterTopics(allTopics: List<Topic>, type: TopicType, query: String): List<Topic> {
        return allTopics.filter { topic ->
            topic.type == type && (
                query.isBlank() ||
                topic.title.contains(query, ignoreCase = true) ||
                topic.subtitle.contains(query, ignoreCase = true)
            )
        }
    }
}

class SectionException(message: String) : Exception(message)
