package kz.innlab.bookservice.progress.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.progress.model.BookStatistics
import kz.innlab.bookservice.progress.repository.BookStatisticsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class BookProgressServiceImpl: BookStatisticsService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: BookStatisticsRepository


    override fun getBookStatisticsByBookIdAndReaderId(bookId: UUID, readerId: UUID): Optional<BookStatistics> {
        return repository.findByBookIdAndReaderIdAndDeletedAtIsNull(bookId, readerId)
    }


    override fun createBookStatistics(bookStatistics: BookStatistics, userId: UUID): Status {

        val status = Status()

        // Try to find an existing book progress entry
        val existingStatistics = repository.findByBookIdAndReaderIdAndDeletedAtIsNull(bookStatistics.bookId!!, userId)
        if (existingStatistics.isPresent) {
            val progress = existingStatistics.get()

            // Update the existing book progress entry
//            progress.readerId = userId
            progress.currentPage = bookStatistics.currentPage
            repository.save(progress)

            status.status = 1
            status.message = "Book Progress for ${progress.bookId} has been edited"
            status.value = progress
            log.info("Book Progress: ${progress.id} has been edited")
        } else {
            bookStatistics.readerId = userId
            repository.save(bookStatistics)
            status.message = "Book Statistics for ${bookStatistics.bookId} has been created"
            status.value = bookStatistics
            log.info("Book Statistics: ${bookStatistics.bookId} has been created")
        }

        return status


    }

    override fun deleteBookStatisticsByBookIdAndReaderId(bookId: UUID, readerId: UUID): Status {
        val status = Status()
        status.message = String.format("BookProgress %s does not exist", bookId)
        repository.findByBookIdAndReaderIdAndDeletedAtIsNull(bookId, readerId)
            .ifPresent {
                it.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(it)
                status.status = 1
                status.message = String.format("BookProgress %s has been deleted", bookId)
                log.info(String.format("BookProgress %s has been deleted", bookId))
            }
        return status
    }


}
