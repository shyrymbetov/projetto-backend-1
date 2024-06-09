package kz.innlab.mainservice.service


import kz.innlab.mainservice.dto.Status
import kz.innlab.mainservice.model.Education
import kz.innlab.mainservice.model.News
import kz.innlab.mainservice.repository.NewsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class NewsServiceImpl: NewsService {

    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: NewsRepository


    override fun createNews(education: News): Status {
        val status = Status()

        repository.save(education)
        status.status = 1
        status.message = String.format("News: %s has been created", education.titleEn)
        status.value = education.titleEn
        log.info(String.format("News: %s has been created", education.titleEn))
        return status
    }

    override fun editNews(news: News, newsId: UUID): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull(newsId).ifPresentOrElse({
            it.titleEn = news.titleEn
            it.titleRu = news.titleRu
            it.contentEn = news.contentEn
            it.contentRu = news.contentRu
            it.image = news.image
            repository.save(it)
            status.status = 1
            status.message = String.format("News %s has been edited", it.id)
            status.value = it.id
        }, {
            status.message = String.format("News does not exist")
        })
        return status
    }

    override fun likeNews(newsId: UUID, userId: UUID): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull(newsId).ifPresentOrElse({
            if (!it.likedUsers.contains(userId)) {
                it.likedUsers = it.likedUsers.toMutableList().apply { add(userId) }.toTypedArray()
                repository.save(it)
                status.status = 1
                status.message = String.format("News Like %s has been edited", it.id)
                status.value = it.id
            } else {
                status.status = 0
                status.message = String.format("Already Liked", it.id)
                status.value = it.id
            }


        }, {
            status.message = String.format("News does not exist")
        })
        return status
    }

    override fun unlikeNews(newsId: UUID, userId: UUID): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull(newsId).ifPresentOrElse({
            if (it.likedUsers.contains(userId)) {
                it.likedUsers = it.likedUsers.toMutableList().apply { remove(userId) }.toTypedArray()
                repository.save(it)
                status.status = 1
                status.message = String.format("News Like %s has been edited", it.id)
                status.value = it.id
            } else {
                status.status = 0
                status.message = String.format("Already has no like", it.id)
                status.value = it.id
            }


        }, {
            status.message = String.format("News does not exist")
        })
        return status
    }

    override fun deleteNews(id: UUID): Status {
        val status = Status()
        status.message = String.format("News %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent { education ->
                education.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(education)

                status.status = 1
                status.message = String.format("News %s has been deleted", id)
                log.info(String.format("News %s has been deleted", id))
            }
        return status
    }

    override fun getNewsList(userId: UUID): List<News> {
        return repository.findAllByDeletedAtIsNull().map {
            it.isFavorite = it.likedUsers.contains(userId)
            it
        }
    }

    override fun getNewsById(id: UUID, userId: UUID): Optional<News> {
        return repository.findByIdAndDeletedAtIsNull(id).map {
            it.isFavorite = it.likedUsers.contains(userId)
            it
        }
    }

    override fun getLikedNews(userId: UUID): List<News> {
        return repository.findAllByLikedUsersContains(userId).map {
            it.isFavorite = true
            it
        }
    }

}
