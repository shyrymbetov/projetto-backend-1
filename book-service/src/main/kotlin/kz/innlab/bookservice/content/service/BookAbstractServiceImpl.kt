package kz.innlab.bookservice.content.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.content.model.BookAbstracts
import kz.innlab.bookservice.content.repository.BookAbstractsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class BookAbstractServiceImpl : BookAbstractService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: BookAbstractsRepository

    override fun getBookAbstractList(bookId: UUID, username: String): List<BookAbstracts> {
        return repository.findByUserIdAndBookIdAndDeletedAtIsNull(UUID.fromString(username), bookId)
    }

    override fun createBookAbstract(bookAbstract: BookAbstracts, username: String): Status {
        val status = Status()
        bookAbstract.userId = UUID.fromString(username)
        repository.save(bookAbstract)

        status.status = 1
        status.message = String.format("BookAbstract has been created")
        status.value = bookAbstract.id
        return status
    }

    override fun deleteBookAbstract(id: UUID, name: String): Status {
        val status = Status()
        status.message = String.format("BookAbstract %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id).ifPresent {
            it.deletedAt = Timestamp(System.currentTimeMillis())
            repository.save(it)
            status.status = 1
            status.message = String.format("BookAbstract %s has been deleted", id)
        }
        return status
    }


}
