package kz.innlab.bookservice.test.repository

import kz.innlab.bookservice.test.model.TestQuestions
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TestQuestionsRepository: JpaRepository<TestQuestions, UUID> {
    fun findByTestIdAndDeletedAtIsNull(bookId: UUID): List<TestQuestions>
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<TestQuestions>
}
