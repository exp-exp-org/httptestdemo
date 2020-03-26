package io.hkhc.httptest

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager


/**
 * Perform unit test to DogService without real server backend
 * Approach 1 : Have a mock web server within unit test to return mock response.
 * Pros: We are running real client code, no mock code at client side, including networking code
 * Cons: More complicated setup to emulate real life environment, e.g. https. But the prize is that we can test
 * something lower level like certificate pinning, or https tunnel via CONNECT
 */
@Suppress("unused")
class DogServiceMockServerTest {

    private val tlsBuilder = LocalTlsBuilder()
    private var server: MockWebServer? = null

    private var clientBuilder = OkHttpClient.Builder()
        .localTrust(tlsBuilder)

    private lateinit var dogService: DogService

    @Before
    fun setUp() {
        server = MockWebServer().localTrust(tlsBuilder).also {
            it.start()
            dogService = DogService(
                DogApiFactory(clientBuilder, it.url("").toString())
                    .createApi()
            )
        }
    }

    @After
    fun tearDown() {
        server?.shutdown()
    }

    @Test
    fun `we shall get http 200 response with valid request`() {

        // WHEN server get request, it shall give positive response

        val expectedBody = """
        {
            "message": [
                "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg",
                "https://images.dog.ceo/breeds/hound-afghan/n02088094_1007.jpg"
            ]
        }
        """.trimIndent()
        server?.enqueue(MockResponse().setBody(expectedBody))

        // THEN dogService shall get the response

        assertThat(dogService.listDogsByType("hound")).isEqualTo(expectedBody)

    }

    @Test
    fun `we shall get http 404 response if request URL is not correct`() {

        // WHEN server get request, it shall give 404 response

        server?.enqueue(MockResponse().setResponseCode(404))

        // THEN dogService shall throw DogNotFoundException

        assertThatThrownBy {
            dogService.listDogsByType("alien")
        }
        .isInstanceOf(DogNotFoundException::class.java)

    }

    @Test
    fun `we shall get exception if request hostname is not correct`() {

        // WHEN the server is ready

        val expectedBody = """
        {
            "message": [
                "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg",
                "https://images.dog.ceo/breeds/hound-afghan/n02088094_1007.jpg"
            ]
        }
        """.trimIndent()
        server?.enqueue(MockResponse().setBody(expectedBody))

        // AND client connect to somewhere else

        dogService = DogService(
            DogApiFactory(clientBuilder, "https://dog-non-existence-host.ceo")
                .createApi()
        )

        // THEN dogService shall throw UnknownDogException

        assertThatThrownBy {
            dogService.listDogsByType("hound")
        }
        .isInstanceOf(UnknownDogException::class.java)

    }

}
