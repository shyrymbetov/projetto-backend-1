package kz.innlab.bookservice.videolecture.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.system.service.PermissionService
import kz.innlab.bookservice.videolecture.model.BookVideoLecture
import kz.innlab.bookservice.videolecture.repository.BookVideoLectureRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class BookVideoLectureServiceImpl: BookVideoLectureService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: BookVideoLectureRepository

    @Autowired
    lateinit var permissionService: PermissionService

    override fun getBookVideoLectureByBookId(bookId: UUID, name: String): List<BookVideoLecture> {
        return repository.findAllByBookIdAndDeletedAtIsNull(bookId)
    }

    override fun getVideoLectureById(id: UUID, name: String?): Optional<BookVideoLecture> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

    override fun createBookVideoLecture(book: BookVideoLecture, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "create")) {
//            status.message = "Permission"
//            return status
//        }
        repository.save(book)
        status.status = 1
        status.message= String.format("Book: %s has been created", book.name)
        status.value = book.id
        log.info(String.format("Book: %s has been created", book.name))
        return status
    }

    override fun editBookVideoLecture(newBook: BookVideoLecture, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "update")) {
//            status.message = "Permission"
//            return status
//        }
        repository.findByIdAndDeletedAtIsNull(newBook.id!!).ifPresentOrElse({

            repository.save(newBook)
            status.status = 1
            status.message = String.format("Book %s has been edited", it.id)
            log.info(String.format("Book %s has been edited", it.id))
            status.value = it.id
        },{

            status.message = String.format("Book %s does not exist", newBook.id)
            log.info(String.format("Book %s does not exist", newBook.id))
        })

        return status
    }

    override fun deleteBookVideoLecture(id: UUID, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "delete")) {
//            status.message = "Permission"
//            return status
//        }
        status.message = String.format("Book %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent {
                it.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(it)
                status.status = 1
                status.message = String.format("Book %s has been deleted", id)
                log.info(String.format("Book %s has been deleted", id))
            }
        return status
    }


}
