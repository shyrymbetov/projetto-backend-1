package kz.innlab.bookservice.progress.service

import kz.innlab.bookservice.progress.model.BookProgress
import kz.innlab.bookservice.progress.repository.BookProgressRepository
import kz.innlab.bookservice.progress.repository.BookStatisticsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class BookStatisticsServiceImpl: BookProgressService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: BookProgressRepository

    @Autowired
    lateinit var statisticsRepository: BookStatisticsRepository

    override fun createBookProgress(bookId: UUID, bookProgress: BookProgress, userId: UUID): BookProgress {
        val statistics = statisticsRepository.findByBookIdAndReaderIdAndDeletedAtIsNull(bookId, userId)
            .orElseThrow { EntityNotFoundException("BookStatistics not found for ID: $bookId") }

        // Set the BookStatistics for the BookProgress
        bookProgress.bookStatistics = statistics

        // Set other properties of bookProgress as needed
        bookProgress.endTime = Timestamp(System.currentTimeMillis())

        // You can also validate the data and perform other operations as required

        // Save the bookProgress entity
        val savedBookProgress = repository.save(bookProgress)

        return savedBookProgress
    }




}
