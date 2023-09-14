package kz.innlab.bookservice.test.model

import java.util.*

class TestQuestionWithUserAnswer {
    var id: UUID? = null
    var index: Int? = null
    var description: String? = null
    var answers: Array<String> = arrayOf()
    var userAnswers: Array<String> = arrayOf()
    var correct: Boolean? = false
    var point: Double? = 0.0
}
