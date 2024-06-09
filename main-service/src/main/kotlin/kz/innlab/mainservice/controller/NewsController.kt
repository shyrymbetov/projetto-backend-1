package kz.innlab.mainservice.controller

import kz.innlab.mainservice.dto.Status
import kz.innlab.mainservice.model.News
import kz.innlab.mainservice.service.NewsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/news")
class NewsController {

    @Autowired
    lateinit var newsService: NewsService

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    fun createNews(@RequestBody news: News, principal: Principal): Status {
        return newsService.createNews(news)
    }

    @PutMapping("/edit/{newsId}")
    @PreAuthorize("isAuthenticated()")
    fun editNews(@RequestBody news: News, @PathVariable newsId: UUID, principal: Principal): Status {
        return newsService.editNews(news, newsId)
    }

    @PostMapping("/like/{newsId}")
    @PreAuthorize("isAuthenticated()")
    fun likeNews(@PathVariable newsId: UUID, principal: Principal): Status {
        return newsService.likeNews(newsId, UUID.fromString(principal.name))
    }

    @PostMapping("/unlike/{newsId}")
    @PreAuthorize("isAuthenticated()")
    fun unlikeNews(@PathVariable newsId: UUID, principal: Principal): Status {
        return newsService.unlikeNews(newsId, UUID.fromString(principal.name))
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    fun deleteNews(@PathVariable id: UUID, principal: Principal): Status {
        return newsService.deleteNews(id)
    }

    @GetMapping("/list")
    fun getNewsList(principal: Principal?): List<News> {
        return newsService.getNewsList(UUID.fromString(principal?.name ?: UUID.randomUUID().toString()))
    }

    @GetMapping("/{id}")
    fun getNewsById(@PathVariable id: UUID, principal: Principal?): Optional<News> {
        return newsService.getNewsById(id, UUID.fromString(principal?.name ?: UUID.randomUUID().toString()))
    }

    @GetMapping("/liked")
    @PreAuthorize("isAuthenticated()")
    fun getLikedNews(principal: Principal): List<News> {
        return newsService.getLikedNews(UUID.fromString(principal.name))
    }
}
