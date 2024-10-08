package app.dotoo.data.validation

import io.konform.validation.ValidationResult

interface Validatable<T> {
    fun validate(): ValidationResult<T>
}
