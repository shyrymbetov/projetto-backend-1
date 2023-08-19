package kz.innlab.bookservice.book.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.book.model.Genre
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.util.*

interface GenreService {
    fun getPageListGenre(page: PageRequest, search: String): Page<Genre>
    fun getGenreById(id: UUID): Optional<Genre>
    fun createGenre(genre: Genre): Status
    fun editGenre(genre: Genre): Status
    fun deleteGenre(id: UUID): Status
    //    fun update_root_ide_package_.kz.innlab.bookservice.book.model.GenreBookNumber(genreId: UUID)
    fun getGenresInList(genreIds: List<UUID>): List<Genre>
    fun getListGenre(): List<Genre>
    fun getGenreByGenreOrder(genreOrder: Int): Optional<Genre>
}
