package kz.innlab.fileservice.repository

import kz.innlab.fileservice.model.File
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * @project edm-spring
 * @author Yerassyl Shyrymbetov on 12.02.2022
 */
interface FileRepository: JpaRepository<File, UUID> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<File>

    fun findByHashCodeAndDeletedAtIsNull(hashSum: String): Optional<File>
}
