package io.hkhc.httptest

open class DogException(
    msg: String = "",
    throwable: Throwable? = null
): Exception(msg, throwable)
