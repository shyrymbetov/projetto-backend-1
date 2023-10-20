package kz.innlab.bookservice.test.repository

import kz.innlab.bookservice.test.model.BookTestUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BookTestUserRepository: JpaRepository<BookTestUser, UUID> {
    fun findFirstByTestIdAndUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(testId: UUID, userId: UUID): Optional<BookTestUser>
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<BookTestUser>
    fun findAllByAndTestIdAndUserIdAndDeletedAtIsNull(testId: UUID, userId: UUID): Optional<BookTestUser>
}
