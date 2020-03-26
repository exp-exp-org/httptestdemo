package io.hkhc.httptest

class DogNotFoundException(
    msg: String = "",
    throwable: Throwable? = null
): DogException(msg, throwable)
