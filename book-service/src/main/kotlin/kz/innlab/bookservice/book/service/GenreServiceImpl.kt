package kz.innlab.bookservice.book.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.book.model.Genre
import kz.innlab.bookservice.book.repository.GenreRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.sql.Timestamp
import java.util.*

@Service
class GenreServiceImpl: GenreService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: GenreRepository

    override fun getPageListGenre(page: PageRequest, search: String): Page<Genre> {
        return repository.getPageListGenres(search, page)
    }

    override fun getGenreById(id: UUID): Optional<Genre> {
        return repository.findByIdAndDeletedAtIsNull(id)
    }

    override fun createGenre(genre: Genre): Status {
        val status = Status()
        repository.save(genre)
        status.status = 1
        status.message = String.format("Genre: %s has been created", genre.id)
        log.info(String.format("Genre: %s has been created", genre.id))
        status.value = genre.id
        return status
    }

    override fun editGenre(genre: Genre): Status {
        Assert.notNull(genre.id, "Genre id can not null")
        val status = Status()
        if (genre.id != null) {
            repository.findByIdAndDeletedAtIsNull(genre.id!!)
                .ifPresentOrElse(
                    {
                        it.nameKZ = genre.nameKZ
                        it.nameEN = genre.nameEN
                        it.genreOrder = genre.genreOrder
                        repository.save(it)
                        status.status = 1
                        status.message = String.format("Genre: %s has been edited", it.id)
                        status.value = genre.id
                        log.info(String.format("Genre: %s has been edited", it.id))
                    },
                    {
                        status.message = String.format("Genre: %s does not exist", genre.id)
                        log.info(String.format("Genre: %s does not exist", genre.id))
                    }
                )
        }
        return status
    }

    override fun deleteGenre(id: UUID): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresentOrElse(
                {
                    it.deletedAt = Timestamp(System.currentTimeMillis())
                    repository.save(it)
                    status.status = 1
                    status.message = String.format("Genre: %s has been deleted", it.id)
                },
                {
                    status.message = String.format("Genre: %s does not exist",id)
                    log.info(String.format("Genre: %s does not exist",id))
                }
            )
        return status
    }

    override fun getGenresInList(genreIds: List<UUID>): List<Genre> {
        return repository.findByIdInAndDeletedAtIsNull(genreIds)
    }

    override fun getListGenre(): List<Genre> {
        return repository.findAllByDeletedAtIsNull()
    }

    override fun getGenreByGenreOrder(genreOrder: Int): Optional<Genre> {
        return repository.findByGenreOrderAndDeletedAtIsNull(genreOrder)
    }

}
