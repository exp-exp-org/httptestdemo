package io.hkhc.httptest

class UnknownDogException(
    msg: String = "",
    throwable: Throwable? = null
): DogException(msg, throwable)
