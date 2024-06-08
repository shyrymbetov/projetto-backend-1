package kz.innlab.mainservice.service

import kz.innlab.mainservice.model.Education
import kz.innlab.mainservice.dto.Status
import kz.innlab.mainservice.model.News
import java.util.*

interface NewsService {
    fun createNews(education: News): Status
    fun editNews(education: News, newsId: UUID): Status
    fun likeNews(newsId: UUID, userId: UUID): Status
    fun unlikeNews(newsId: UUID, userId: UUID): Status
    fun deleteNews(id: UUID): Status
    fun getNewsList(userId: UUID): List<News>
    fun getNewsById(id: UUID, userId: UUID): Optional<News>
}
