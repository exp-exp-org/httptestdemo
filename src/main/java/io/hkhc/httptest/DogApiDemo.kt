package io.hkhc.httptest

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager


/**
 *
 * Call to Dog API (https://dog.ceo/dog-api/)
 *
 * This is a simple client to make requests as a demonstration the client code.
 * We issue 3 requests here:
 * - a successful 200 request, which return a JSON string
 * - a file not found 404 request, which also return a JSON string with status code 404
 * - a network error, that in turn throws an IOException
 *
 * This is not means to be bullet proof code, it is a playground to demonstrate how to do unit tests
 * with this client.
 *
 * We are going to create unit tests of DogService class for various http result
 *
 * This is a live demo to show the hehavior of DogService with real backend
 *
 */
fun main(args: Array<String>) {

    var service = DogService(
        DogApiFactory(OkHttpClient.Builder(), "http://dog.ceo")
            .createApi()
    )


    try {
        System.out.println("***** Successful request")
        System.out.println(service.listDogsByType("hound"))
    }
    catch (e: Throwable) {
        System.out.println("Should not reach here.")
    }


    try {
        System.out.println("***** File not found request")
        System.out.println(service.listDogsByType("alien"))
    }
    catch (e: DogNotFoundException) {
        System.out.println("Dog not found")
    }
    catch (e: Throwable) {
        System.out.println("Should not reach here.")
    }

    service = DogService(DogApiFactory(OkHttpClient.Builder(), "http://dog-zyx.ceo").createApi())

    try {
        System.out.println("****** Network error")
        System.out.println(service.listDogsByType("hound"))
    }
    catch (e: UnknownDogException) {
        System.out.println("Failed to access dog api")
    }
    catch (e: Throwable) {
        System.out.println("Should not reach here.")
    }


}
