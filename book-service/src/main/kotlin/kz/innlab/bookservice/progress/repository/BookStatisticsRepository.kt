package kz.innlab.bookservice.progress.repository

import kz.innlab.bookservice.progress.model.BookStatistics
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface BookStatisticsRepository: JpaRepository<BookStatistics, UUID>, JpaSpecificationExecutor<BookStatistics> {
    fun findByBookIdAndReaderIdAndDeletedAtIsNull(id: UUID, readerId: UUID): Optional<BookStatistics>
    fun findAllByIdInAndDeletedAtIsNull(bookIds: List<UUID>): Optional<BookStatistics>
    fun findAllByDeletedAtIsNull(): Optional<BookStatistics>
}
