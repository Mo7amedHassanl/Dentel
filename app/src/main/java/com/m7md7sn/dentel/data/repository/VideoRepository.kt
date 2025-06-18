package com.m7md7sn.dentel.data.repository

import com.m7md7sn.dentel.data.model.Video
import com.m7md7sn.dentel.presentation.ui.section.Topic
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun getVideoDetails(videoId: String): Flow<Video?>
    fun getVideoFromTopic(topic: Topic): Flow<Video?>
}