package kz.innlab.bookservice.categories.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.categories.model.BookCategory
import kz.innlab.bookservice.categories.repository.BookCategoryRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class BookCategoryServiceImpl: BookCategoryService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: BookCategoryRepository

    override fun getListBookCategory(): List<BookCategory> {
        return repository.findAllByDeletedAtIsNull()
    }

    override fun getBookCategoryById(id: UUID): Optional<BookCategory> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

    override fun createBookCategory(category: BookCategory): Status {
        val status = Status()
        repository.save(category)
        status.status = 1
        status.message = String.format("Genre: %s has been created", category.id)
        log.info(String.format("Genre: %s has been created", category.id))
        status.value = category
        return status
    }

    override fun editBookCategory(category: BookCategory): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull(category.id!!)
            .ifPresentOrElse(
                {
                    repository.save(it)
                    status.status = 1
                    status.message = String.format("Genre: %s has been edited", it.id)
                    status.value = it
                    log.info(String.format("Genre: %s has been edited", it.id))
                },
                {
                    status.message = String.format("Genre: %s does not exist", category.id)
                    log.info(String.format("Genre: %s does not exist", category.id))
                }
            )
        return status
    }

    override fun deleteBookCategory(id: UUID): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresentOrElse(
                {
                    it.deletedAt = Timestamp(System.currentTimeMillis())
                    repository.save(it)
                    status.status = 1
                    status.message = String.format("Genre: %s has been deleted", it.id)
                },
                {
                    status.message = String.format("Genre: %s does not exist",id)
                    log.info(String.format("Genre: %s does not exist",id))
                }
            )
        return status
    }

}
