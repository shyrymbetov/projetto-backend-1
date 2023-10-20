package kz.innlab.bookservice.progress.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.progress.model.BookStatistics
import java.util.*

interface BookStatisticsService {
    fun getBookStatisticsByBookIdAndReaderId(bookId: UUID, readerId: UUID): Optional<BookStatistics>
    fun createBookStatistics(bookStatistics: BookStatistics, userId: UUID): Status
    fun deleteBookStatisticsByBookIdAndReaderId(bookId: UUID, readerId: UUID): Status
}
