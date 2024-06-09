package kz.innlab.mainservice.service

import kz.innlab.mainservice.dto.Status
import kz.innlab.mainservice.model.Resource
import java.util.*

interface ResourceService {
    fun createResource(resource: Resource): Status
    fun editResource(resource: Resource, resourceId: UUID): Status
    fun deleteResource(id: UUID): Status
    fun getResourceList(userId: UUID): List<Resource>
    fun getResourceById(id: UUID, userId: UUID): Optional<Resource>
    fun getResourceByType(type: String, userId: UUID): List<Resource>
    fun likeResource(resourceId: UUID, userId: UUID): Status
    fun unlikeResource(resourceId: UUID, userId: UUID): Status
    fun getLikedResource(userId: UUID): List<Resource>
}
