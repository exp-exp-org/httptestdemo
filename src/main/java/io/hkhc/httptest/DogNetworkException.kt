package io.hkhc.httptest

class DogNetworkException(
    msg: String = "",
    throwable: Throwable? = null
): DogException(msg, throwable)
