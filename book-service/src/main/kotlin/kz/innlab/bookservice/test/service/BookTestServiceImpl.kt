package kz.innlab.bookservice.test.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.hyperlink.model.BookHyperlink
import kz.innlab.bookservice.hyperlink.repository.BookHyperlinkRepository
import kz.innlab.bookservice.system.service.PermissionService
import kz.innlab.bookservice.test.model.BookTest
import kz.innlab.bookservice.test.repository.BookTestRepository
import kz.innlab.bookservice.test.repository.TestQuestionsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class BookTestServiceImpl: BookTestService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: BookTestRepository

    @Autowired
    lateinit var questionRepository: TestQuestionsRepository

    @Autowired
    lateinit var permissionService: PermissionService

    override fun getBookTestByBookId(bookId: UUID, name: String): List<BookTest> {
        return repository.findAllByBookIdAndDeletedAtIsNull(bookId)
    }

    override fun getBookTestById(id: UUID, name: String): Optional<BookTest> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

    override fun createBookTest(book: BookTest, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "create")) {
//            status.message = "Permission"
//            return status
//        }
        repository.save(book)
        book.questions.forEach {question ->
            question.testId = book.id
            questionRepository.save(question)
        }
        status.status = 1
        status.message= String.format("Book: %s has been created", book.name)
        status.value = book.id
        log.info(String.format("Book: %s has been created", book.name))
        return status
    }

    override fun editBookTest(newBook: BookTest, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "update")) {
//            status.message = "Permission"
//            return status
//        }
        repository.findByIdAndDeletedAtIsNull(newBook.id!!).ifPresentOrElse({

            newBook.questions.forEach {question ->
                questionRepository.save(question)
            }

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

    override fun deleteBookTest(id: UUID, userId: String): Status {
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
