package kz.innlab.bookservice.progress.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.progress.model.BookProgress
import kz.innlab.bookservice.progress.model.BookStatistics
import java.util.*

interface BookProgressService {
    fun createBookProgress(statisticsId: UUID, bookProgress: BookProgress, userId: UUID): BookProgress
}
