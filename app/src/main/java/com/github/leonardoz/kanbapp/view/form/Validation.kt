package com.github.leonardoz.kanbapp.view.form

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.util.format
import com.github.leonardoz.kanbapp.util.formatNullable
import com.github.leonardoz.kanbapp.view.form.ValidationType.*
import java.util.*

interface BaseRule {
    fun matchesError(): Boolean
    fun message(): String
}

// Validation Types
enum class ValidationType {
    REQUIRED,
    MAX_LENGTH,
    MIN_LENGTH, // String
    IS_NEGATIVE, // Int
    IS_IN_PAST,
    IS_IN_FUTURE // Date
}

// Validation Rules
class RequiredRule(val input: String, var message: String?) : BaseRule {
    override fun message(): String = message!!

    override fun matchesError(): Boolean = input.isBlank()
}

class MaxLengthRule(val input: String, val limit: Int, var message: String?) : BaseRule {
    override fun message(): String = format(message!!, limit)

    override fun matchesError(): Boolean = input.length > limit
}

class MinLengthRule(val input: String, val limit: Int, var message: String?) : BaseRule {
    override fun message(): String = format(message!!, limit)

    override fun matchesError(): Boolean = input.length < limit
}

class NonNegativeRule(val input: Int, var message: String?) : BaseRule {
    override fun message(): String = message!!

    override fun matchesError(): Boolean = input < 0
}

class IsInPastRule(val input: Date, var message: String?) : BaseRule {
    override fun message(): String = message!!

    override fun matchesError(): Boolean = input.after(Date())
}

class IsInFutureRule(val input: Date, var message: String?) : BaseRule {
    override fun message(): String = message!!

    override fun matchesError(): Boolean = input.before(Date())
}

// Validator Engine

class FormValidator internal constructor(
    private val messages: Map<ValidationType, String>
) {

    private val errorsFound = HashMap<String, MutableSet<String>>()

    fun validate(
        field: String,
        type: ValidationType,
        input: String
    ) = validate(field = field, type = type, strInput = input)

    fun validate(
        field: String,
        type: ValidationType,
        input: String,
        limit: Int
    ) = validate(field = field, type = type, strInput = input, limit = limit)

    fun validate(
        field: String,
        type: ValidationType,
        input: Int
    ) = validate(field = field, type = type, intInput = input)

    fun validate(
        field: String,
        type: ValidationType,
        input: Date
    ) = validate(field = field, type = type, dateInput = input)


    private fun validate(
        field: String,
        type: ValidationType,
        strInput: String = "",
        intInput: Int = 0,
        limit: Int = 0,
        dateInput: Date = Date()
    ): FormValidator {
        val rule: BaseRule = when (type) {
            REQUIRED -> RequiredRule(strInput, messages[REQUIRED])
            MAX_LENGTH -> MaxLengthRule(strInput, limit, messages[MAX_LENGTH])
            MIN_LENGTH -> MinLengthRule(strInput, limit, messages[MIN_LENGTH])
            IS_NEGATIVE -> NonNegativeRule(intInput, messages[IS_NEGATIVE])
            IS_IN_PAST -> IsInPastRule(dateInput, messages[IS_IN_PAST])
            IS_IN_FUTURE -> IsInFutureRule(dateInput, messages[IS_IN_FUTURE])
        }

        if (rule.matchesError()) {
            val message = rule.message()
            errorsFound.getOrPut(field) { mutableSetOf(message) }.add(message)
        }
        return this
    }

    fun isValid() = errorsFound.isEmpty()

    fun isValid(field: String) = errorsFound[field]?.isEmpty() ?: true

    fun errorsFound(field: String): Set<String>? =
        errorsFound[field]?.toSet()

    fun errorsFoundCompacted(field: String) =
        if (errorsFound.isEmpty()) ""
        else errorsFound[field]?.let { it ->
            it.toList().sorted().joinToString(separator = "\n")
        } ?: ""

    fun clear(fieldName: String) = errorsFound.remove(fieldName)

    fun reset() = errorsFound.clear()
}

// Validator Factory
open class FormValidatorFactory(context: Context) {

    @VisibleForTesting
    var messages = mapOf<ValidationType, String>(
        REQUIRED to context.getString(R.string.required),
        MAX_LENGTH to context.getString(R.string.max_length),
        MIN_LENGTH to context.getString(R.string.min_length),
        IS_NEGATIVE to context.getString(R.string.is_negative),
        IS_IN_PAST to context.getString(R.string.past_date),
        IS_IN_FUTURE to context.getString(R.string.future_date)
    )

    fun validator() = FormValidator(messages)

}
