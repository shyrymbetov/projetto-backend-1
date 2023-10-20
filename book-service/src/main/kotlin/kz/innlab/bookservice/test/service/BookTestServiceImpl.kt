package kz.innlab.bookservice.test.service

import kz.innlab.bookservice.book.dto.Status
import kz.innlab.bookservice.system.service.PermissionService
import kz.innlab.bookservice.test.model.BookTest
import kz.innlab.bookservice.test.model.BookTestUser
import kz.innlab.bookservice.test.repository.BookTestRepository
import kz.innlab.bookservice.test.repository.BookTestUserRepository
import kz.innlab.bookservice.test.repository.TestQuestionsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class BookTestServiceImpl: BookTestService {
    private var log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var repository: BookTestRepository

    @Autowired
    lateinit var questionRepository: TestQuestionsRepository

    @Autowired
    lateinit var testUserRepository: BookTestUserRepository

    @Autowired
    lateinit var permissionService: PermissionService

    override fun getBookTestByBookId(bookId: UUID, name: String): List<BookTest> {
        val tests = repository.findAllByBookIdAndDeletedAtIsNull(bookId)

        val questions = questionRepository.findByTestIdInAndDeletedAtIsNull(tests.mapNotNull { it.id })
            .groupBy { it.testId }

        tests.forEach {test ->
            if (questions.containsKey(test.id)) {
                test.questions = questions[test.id] ?: listOf()
            }
        }
        return repository.findAllByBookIdAndDeletedAtIsNull(bookId)
    }


    override fun getBookTestProgressByBookId(bookId: UUID, testUserId: UUID): Any {
        // Find all tests for the given book
        val tests = repository.findAllByBookIdAndDeletedAtIsNull(bookId)

        // Extract and map test IDs from the tests
        val testIds = tests.map { it.id }

        // Find user test progress records for each test ID and the specified user
        val userTestComplete = mutableListOf<Optional<BookTestUser>>()

        for (testId in testIds) {
            val userTestProgress = testUserRepository.findAllByAndTestIdAndUserIdAndDeletedAtIsNull(testId!!, testUserId)
            userTestComplete.add(userTestProgress)
        }

        return mapOf("complete" to userTestComplete.size, "testCount" to tests.size)
    }

    override fun getBookTestById(id: UUID, name: String): Optional<BookTest> {
        val questions = questionRepository.findByTestIdAndDeletedAtIsNull(id)
        val bookTest = repository.findByIdAndDeletedAtIsNull(id)
        bookTest.ifPresent { b -> b.questions = questions }
        return bookTest
    }

    override fun createBookTest(test: BookTest, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "create")) {
//            status.message = "Permission"
//            return status
//        }
        repository.save(test)

        test.questions.forEach {question ->
            question.testId = test.id
            questionRepository.save(question)
        }
        status.status = 1
        status.message= String.format("Book: %s has been created", test.name)
        status.value = test.id
        log.info(String.format("Book: %s has been created", test.name))
        return status
    }

    override fun completeBookTest(testUser: BookTestUser, name: String): Status {
        val status = Status()

        getBookTestById(testUser.testId!!, name).ifPresent {
            //check answers
            testUser.questions.forEach {
                println("${it.correct}   ${it.userAnswers.joinToString(",")}")
            }
            checkUserAnswers(it, testUser)
            testUser.questions.forEach {
                println("${it.correct}   ${it.userAnswers.joinToString(",")}")
            }
            testUserRepository.save(testUser)

            status.status = 1
            status.message= String.format("Book Test has been complete")
            status.value = testUser.id
        }

        return status
    }

    private fun checkUserAnswers(bookTest: BookTest, testUser: BookTestUser) {
        val questions = bookTest.questions.associateBy { it.id }
        testUser.questions.forEach {question ->
            if (!questions.containsKey(question.id)) {
                question.correct = false
                return@forEach
            }
            var count = 0
            questions[question.id]!!.correctAnswers.forEach { correct ->
                if (question.userAnswers.contains(correct)) {
                    count++
                }
            }
            question.correct = count == questions[question.id]!!.correctAnswers.size
        }
    }

    override fun editBookTest(test: BookTest, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "update")) {
//            status.message = "Permission"
//            return status
//        }
        repository.findByIdAndDeletedAtIsNull(test.id!!).ifPresentOrElse({

            test.questions.forEach {question ->
                question.testId = test.id
                questionRepository.save(question)
            }

            repository.save(test)
            status.status = 1
            status.message = String.format("Book %s has been edited", it.id)
            log.info(String.format("Book %s has been edited", it.id))
            status.value = it.id
        },{

            status.message = String.format("Book %s does not exist", test.id)
            log.info(String.format("Book %s does not exist", test.id))
        })

        return status
    }

    override fun deleteBookTest(id: UUID, userId: String): Status {
        val status = Status()
//        if (!permissionService.permission(userId, "book-list",  "delete")) {
//            status.message = "Permission"
//            return status
//        }
        status.message = String.format("Book %s does not exist", id)
        repository.findByIdAndDeletedAtIsNull(id)
            .ifPresent {
                it.deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(it)
                status.status = 1
                status.message = String.format("Book %s has been deleted", id)
                log.info(String.format("Book %s has been deleted", id))
            }
        return status
    }


}
