package kz.innlab.bookservice.categories.model

import kz.innlab.bookservice.system.model.Auditable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "book_category")
class BookCategory: Auditable<String?>() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    var nameKZ: String? = null
    var nameRU: String? = null
    var nameEN: String? = null

    @OneToMany
    @JoinColumn(name = "categoryId")
    var subCategories: Set<BookSubCategory> = setOf()
}
