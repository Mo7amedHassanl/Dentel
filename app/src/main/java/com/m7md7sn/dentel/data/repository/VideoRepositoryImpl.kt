package com.m7md7sn.dentel.data.repository

import com.m7md7sn.dentel.data.model.Video
import com.m7md7sn.dentel.presentation.ui.section.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepositoryImpl @Inject constructor() : VideoRepository {

    override fun getVideoDetails(videoId: String): Flow<Video?> = flow {
        // In a real implementation, this would fetch data from a data source
        // For now, we'll return mock data or null
        emit(null)
    }

    override fun getVideoFromTopic(topic: Topic): Flow<Video?> = flow {
        // Convert Topic to Video
        if (topic.videoUrl!!.isNotEmpty()) {
            emit(Video(
                id = topic.id,
                title = topic.title,
                videoUrl = topic.videoUrl,
                description = topic.content
            ))
        } else {
            emit(null)
        }
    }
}
