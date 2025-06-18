package com.m7md7sn.dentel.data.repository

import com.m7md7sn.dentel.data.model.Article
import com.m7md7sn.dentel.presentation.ui.section.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface ArticleRepository {
    fun getArticleDetails(articleId: String): Flow<Article?>
    fun getArticleFromTopic(topic: Topic): Flow<Article?>
}

@Singleton
class ArticleRepositoryImpl @Inject constructor() : ArticleRepository {

    override fun getArticleDetails(articleId: String): Flow<Article?> = flow {
        // In a real implementation, this would fetch data from a data source
        // For now, we'll return mock data or null
        emit(null)
    }

    override fun getArticleFromTopic(topic: Topic): Flow<Article?> = flow {
        // Convert Topic to Article if it contains article content
        if (topic.content!!.isNotEmpty()) {
            emit(Article(
                id = topic.id,
                title = topic.title,
                content = topic.content,
                imageUrl = topic.imageUrl
            ))
        } else {
            emit(null)
        }
    }
}
