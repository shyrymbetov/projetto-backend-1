package kz.innlab.bookservice.categories.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.categories.model.BookCategory
import java.util.*

interface BookCategoryService {
    fun getListBookCategory(): List<BookCategory>
    fun getBookCategoryById(id: UUID): Optional<BookCategory>
    fun createBookCategory(category: BookCategory): Status
    fun editBookCategory(category: BookCategory): Status
    fun deleteBookCategory(id: UUID): Status
}
