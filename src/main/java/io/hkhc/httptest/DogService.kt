package io.hkhc.httptest

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient

class DogService(private val api: DogApi) {

    /**
     * Make call to Dog API with error handling.
     * For the sake of demonstration, we have dedicated exception for 404 eror
     */
    @Throws(DogException::class)
    fun listDogsByType(dogType: String): String {

        val response = try {
            api.listDogsByType(dogType)
                .observeOn(Schedulers.trampoline())
                .blockingGet()
        }
        // Catch network error here
        catch (throwable: Throwable) {
            throw UnknownDogException(msg = "Unknown error ${throwable.message}", throwable = throwable)
        }

        if (response.isSuccessful)
            return response.body()?:""
        else {
            // Catch HTTP error here
            when (response.code()) {
                404 -> { throw DogNotFoundException(msg = "Dog not found") }
                else -> { throw UnknownDogException(msg = "Unknown error ${response.code()}") }
            }
        }

    }

}