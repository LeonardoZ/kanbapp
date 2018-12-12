package com.github.leonardoz.kanbapp.view.form

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.util.format
import com.github.leonardoz.kanbapp.util.mockMessages
import com.github.leonardoz.kanbapp.view.form.ValidationType.*
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import java.util.*

@RunWith(JUnit4::class)
class ValidationTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    var appContext = mock(Context::class.java)

    @Before
    fun preTests() {
        mockMessages(appContext)
    }

    @Test
    fun shouldCreate_Validator() {
        val validator = createFormValidator()
        assertNotNull(validator)
    }

    @Test
    fun shouldValidate_AgainstEmptyStrings() {
        val validator = createFormValidator()
        val field = "Message content"
        validator.validate(field, REQUIRED, "")
        validator.validate(field, REQUIRED, "Not empty")

        assertFalse(validator.isValid())

        val firstError = validator.errorsFound(field)?.first()
        assertNotNull(firstError)
        assertEquals(getErrorMessage(REQUIRED), firstError)
    }

    @Test
    fun shouldValidate_MaxLength() {
        val validator = createFormValidator()
        val with4chars = "1234"
        val with5chars = "12345"
        val field = "Message content"

        validator.validate(field, MAX_LENGTH, with4chars, 4)
        validator.validate(field, MAX_LENGTH, with5chars, 4)

        assertFalse(validator.isValid())

        val firstError = validator.errorsFound(field)?.first()
        assertNotNull(firstError)

        val errorMessage = format(getErrorMessage(MAX_LENGTH), 4)
        assertEquals(errorMessage, firstError)
    }

    @Test
    fun shouldValidate_MinLength() {
        val validator = createFormValidator()
        val with4chars = "1234"
        val with5chars = "12345"
        val field = "Message content"

        validator.validate(field, MIN_LENGTH, with4chars, 4)
        validator.validate(field, MIN_LENGTH, with5chars, 6)

        assertFalse(validator.isValid())

        val errorMessage = format(getErrorMessage(MIN_LENGTH), 6)
        val firstError = validator.errorsFound(field)?.first()

        assertNotNull(firstError)
        assertEquals(errorMessage, firstError)
    }

    @Test
    fun shouldValidate_Negative() {

        val validator = createFormValidator()
        val neg = -123
        val pos = 123
        val field = "Input Number"
        validator.validate(field, IS_NEGATIVE, neg)
        validator.validate(field, IS_NEGATIVE, pos)

        assertFalse(validator.isValid())

        val errorMessage = getErrorMessage(IS_NEGATIVE)
        val firstError = validator.errorsFound(field)?.first()

        assertNotNull(firstError)
        assertEquals(errorMessage, firstError)
    }

    @Test
    fun shouldValidate_PastDate() {

        val validator = createFormValidator()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1)
        val future = calendar.time
        val field = "Input Number"
        validator.validate(field, IS_IN_PAST, future)

        assertFalse(validator.isValid())

        val errorMessage = getErrorMessage(IS_IN_PAST)
        val firstError = validator.errorsFound(field)?.first()

        assertNotNull(firstError)
        assertEquals(errorMessage, firstError)
    }

    @Test
    fun shouldValidate_FutureDate() {
        val validator = createFormValidator()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 2)
        val future = calendar.time
        val field = "Input Number"
        validator.validate(field, IS_IN_FUTURE, future)

        assertFalse(validator.isValid())

        val errorMessage = getErrorMessage(IS_IN_FUTURE)
        val firstError = validator.errorsFound(field)?.first()

        assertNotNull(firstError)
        assertEquals(errorMessage, firstError)
    }

    @Test
    fun shouldValidate_FourField() {
        val validator = createFormValidator()
        val field1 = "F1"
        val field2 = "F2"
        val field3 = "F3"
        val field4 = "F4"

        val input1 = "My message"
        val input2 = -123
        val input3 = "Limited"
        val input4 = 14555
        val input4v2 = "Limit!"

        validator
            .validate(field1, REQUIRED, input1)
            .validate(field2, REQUIRED, input2.toString())
            .validate(field2, IS_NEGATIVE, input2) // Invalid
            .validate(field3, MIN_LENGTH, input3, 2)
            .validate(field3, MAX_LENGTH, input3, 6) // Invalid
            .validate(field4, IS_NEGATIVE, input4)

        assertFalse(validator.isValid())
        val negativeError = getErrorMessage(IS_NEGATIVE)
        val maxLenError = format(getErrorMessage(MAX_LENGTH), 6)

        assertEquals(negativeError, validator.errorsFound(field2)?.first())
        assertEquals(maxLenError, validator.errorsFound(field3)?.first())

        validator.clear(field3)

        validator.validate(field4, MAX_LENGTH, input4v2, 6) // Valid

        assertFalse(validator.isValid()) // Still has an invalid entry

        assertNull(validator.errorsFound(field3))

        validator.reset()

        assertTrue(validator.isValid())
    }

    @Test
    fun shouldValidate_WithANice_ErrorMessage() {
        val validator = createFormValidator()
        val field1 = "F1"

        val input1 = ""
        val input2 = -123
        val input3 = "Limited"

        validator
            .validate(field1, REQUIRED, input1)
            .validate(field1, IS_NEGATIVE, input2) // Invalid
            .validate(field1, MAX_LENGTH, input3, 6) // Invalid

        assertFalse(validator.isValid())
        val requiredError = getErrorMessage(REQUIRED)
        val negativeError = getErrorMessage(IS_NEGATIVE)
        val maxLenError = format(getErrorMessage(MAX_LENGTH), 6)
        validator.errorsFound(field1)?.let {
            assertTrue(it.contains(requiredError))
            assertTrue(it.contains(negativeError))
            assertTrue(it.contains(maxLenError))
        }
        val errorsStr = validator.errorsFoundCompacted(field1)
        assertNotNull(errorsStr)

        val combined = maxLenError + "\n" + requiredError + "\n" + negativeError
        assertEquals(combined, errorsStr!!)
    }

    private fun getErrorMessage(type: ValidationType) = when (type) {
        REQUIRED -> appContext.getString(R.string.required)
        MAX_LENGTH -> appContext.getString(R.string.max_length)
        MIN_LENGTH -> appContext.getString(R.string.min_length)
        IS_NEGATIVE -> appContext.getString(R.string.is_negative)
        IS_IN_PAST -> appContext.getString(R.string.past_date)
        IS_IN_FUTURE -> appContext.getString(R.string.future_date)
    }


    private fun createFormValidator() = FormValidatorFactory(appContext).validator()

}