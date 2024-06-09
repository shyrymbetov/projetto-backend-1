package kz.innlab.mainservice.service

import kz.innlab.mainservice.dto.Status
import kz.innlab.mainservice.model.Work
import java.util.*

interface WorkService {
    fun createWork(work: Work, userId: UUID): Status
    fun editWork(work: Work, workId: UUID): Status
    fun likeWork(workId: UUID, userId: UUID): Status
    fun unlikeWork(workId: UUID, userId: UUID): Status
    fun deleteWork(id: UUID): Status
    fun getWorkList(userId: UUID): List<Work>
    fun getWorkById(id: UUID, userId: UUID): Optional<Work>
    fun getWorksByAuthorId(authorId: UUID, userId: UUID): List<Work>
    fun getLikedWorks(userId: UUID): List<Work>
}
