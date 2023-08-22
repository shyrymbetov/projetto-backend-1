package kz.innlab.bookservice.book.service

import kz.innlab.bookservice.book.repository.AuthorRepository
import kz.innlab.bookservice.system.service.PermissionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl: AuthorService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: AuthorRepository

    @Autowired
    lateinit var permissionService: PermissionService
}
