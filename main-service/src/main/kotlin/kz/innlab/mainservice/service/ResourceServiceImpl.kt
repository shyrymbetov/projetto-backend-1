package kz.innlab.mainservice.service


import kz.innlab.mainservice.dto.Status
import kz.innlab.mainservice.model.Resource
import kz.innlab.mainservice.repository.ResourceRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ResourceServiceImpl: ResourceService {

    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: ResourceRepository

    override fun createResource(resource: Resource): Status {
        val status = Status()

        repository.save(resource)
        status.status = 1
        status.message = String.format("Resource: %s has been created", resource.titleEn)
        status.value = resource.titleEn
        log.info(String.format("Resource: %s has been created", resource.titleEn))
        return status
    }

    override fun editResource(resource: Resource, resourceId: UUID): Status {
        val status = Status()
        repository.findById(resourceId).ifPresentOrElse({
            it.titleEn = resource.titleEn
            it.titleRu = resource.titleRu
            it.image = resource.image
            it.file = resource.file
            it.type = resource.type
            repository.save(it)
            status.status = 1
            status.message = String.format("Resource %s has been edited", it.id)
            status.value = it.id
        }, {
            status.message = String.format("Resource does not exist")
        })
        return status
    }

    override fun deleteResource(id: UUID): Status {
        val status = Status()
        status.message = String.format("Resource %s does not exist", id)
        repository.findById(id)
            .ifPresent { resource ->
                repository.delete(resource)
                status.status = 1
                status.message = String.format("Resource %s has been deleted", id)
                log.info(String.format("Resource %s has been deleted", id))
            }
        return status
    }

    override fun getResourceList(userId: UUID): List<Resource> {
        return repository.findAll().map {
            it.isFavorite = it.likedUsers.contains(userId)
            it
        }
    }

    override fun getResourceById(id: UUID, userId: UUID): Optional<Resource> {
        return repository.findById(id).map {
            it.isFavorite = it.likedUsers.contains(userId)
            it
        }
    }

    override fun getResourceByType(type: String, userId: UUID): List<Resource> {
        return repository.findAllByType(type).map {
            it.isFavorite = it.likedUsers.contains(userId)
            it
        }
    }

    override fun likeResource(resourceId: UUID, userId: UUID): Status {
        val status = Status()
        repository.findById(resourceId).ifPresentOrElse({
            if (!it.likedUsers.contains(userId)) {
                it.likedUsers = it.likedUsers.toMutableList().apply { add(userId) }.toTypedArray()
                repository.save(it)
                status.status = 1
                status.message = String.format("Resource %s has been edited", it.id)
                status.value = it.id
            } else {
                status.status = 0
                status.message = String.format("Already Liked", it.id)
                status.value = it.id
            }


        }, {
            status.message = String.format("Resource does not exist")
        })
        return status
    }

    override fun unlikeResource(resourceId: UUID, userId: UUID): Status {
        val status = Status()
        repository.findById(resourceId).ifPresentOrElse({
            if (it.likedUsers.contains(userId)) {
                it.likedUsers = it.likedUsers.toMutableList().apply { remove(userId) }.toTypedArray()
                repository.save(it)
                status.status = 1
                status.message = String.format("Resource %s has been edited", it.id)
                status.value = it.id
            } else {
                status.status = 0
                status.message = String.format("Already has no like", it.id)
                status.value = it.id
            }


        }, {
            status.message = String.format("Resource does not exist")
        })
        return status
    }

    override fun getLikedResource(userId: UUID): List<Resource> {
        return repository.findAllByLikedUsersContains(userId).map {
            it.isFavorite = true
            it
        }
    }
}
