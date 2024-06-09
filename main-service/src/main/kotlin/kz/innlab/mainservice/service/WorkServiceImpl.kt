package kz.innlab.mainservice.service


import kz.innlab.mainservice.dto.Status
import kz.innlab.mainservice.model.Work
import kz.innlab.mainservice.repository.WorkRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class WorkServiceImpl: WorkService {

    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: WorkRepository

    override fun createWork(work: Work, userId: UUID): Status {
        val status = Status()
        work.author = userId
        repository.save(work)
        status.status = 1
        status.message = String.format("Work: %s has been created", work.titleEn)
        status.value = work.titleEn
        log.info(String.format("Work: %s has been created", work.titleEn))
        return status
    }

    override fun editWork(work: Work, workId: UUID): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull(workId).ifPresentOrElse({
            it.titleEn = work.titleEn
            it.titleRu = work.titleRu
            it.image = work.image
            it.file = work.file
            repository.save(it)
            status.status = 1
            status.message = String.format("Work %s has been edited", it.id)
            status.value = it.id
        }, {
            status.message = String.format("Work does not exist")
        })
        return status
    }

    override fun likeWork(workId: UUID, userId: UUID): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull(workId).ifPresentOrElse({
            if (!it.likedUsers.contains(userId)) {
                it.likedUsers = it.likedUsers.toMutableList().apply { add(userId) }.toTypedArray()
                repository.save(it)
                status.status = 1
                status.message = String.format("Work Like %s has been edited", it.id)
                status.value = it.id
            } else {
                status.status = 0
                status.message = String.format("Already Liked", it.id)
                status.value = it.id
            }


        }, {
            status.message = String.format("Work does not exist")
        })
        return status
    }

    override fun unlikeWork(workId: UUID, userId: UUID): Status {
        val status = Status()
        repository.findByIdAndDeletedAtIsNull(workId).ifPresentOrElse({
            if (it.likedUsers.contains(userId)) {
                it.likedUsers = it.likedUsers.toMutableList().apply { remove(userId) }.toTypedArray()
                repository.save(it)
                status.status = 1
                status.message = String.format("Work Like %s has been edited", it.id)
                status.value = it.id
            } else {
                status.status = 0
                status.message = String.format("Already has no like", it.id)
                status.value = it.id
            }


        }, {
            status.message = String.format("Work does not exist")
        })
        return status
    }

    override fun deleteWork(id: UUID): Status {
        val status = Status()
        status.message = String.format("Work %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent { work ->
                work.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(work)

                status.status = 1
                status.message = String.format("Work %s has been deleted", id)
                log.info(String.format("Work %s has been deleted", id))
            }
        return status
    }

    override fun getWorkList(userId: UUID): List<Work> {
        return repository.findAllByDeletedAtIsNull().map {
            it.isFavorite = it.likedUsers.contains(userId)
            it
        }
    }

    override fun getWorkById(id: UUID, userId: UUID): Optional<Work> {
        return repository.findByIdAndDeletedAtIsNull(id).map {
            it.isFavorite = it.likedUsers.contains(userId)
            it
        }
    }

    override fun getWorksByAuthorId(authorId: UUID, userId: UUID): List<Work> {
        return repository.findByAuthorAndDeletedAtIsNull(authorId).map {
            it.isFavorite = it.likedUsers.contains(userId)
            it
        }
    }

    override fun getLikedWorks(userId: UUID): List<Work> {
        return repository.findAllByLikedUsersContains(userId).map {
            it.isFavorite = true
            it
        }
    }

}
