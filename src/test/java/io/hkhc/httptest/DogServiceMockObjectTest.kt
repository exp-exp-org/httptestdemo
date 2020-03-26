package io.hkhc.httptest

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.Single
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import retrofit2.Response

/**
 * It perform same test cases from different level. It mock the API interface so that the behaviour of DogService
 * can be tested.
 */
class DogServiceMockObjectTest {

    @MockK
    lateinit var api: DogApi

    private lateinit var dogService: DogService

    val expectedBody = """
        {
            "message": [
                "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg",
                "https://images.dog.ceo/breeds/hound-afghan/n02088094_1007.jpg"
            ]
        }
        """.trimIndent()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        dogService = DogService(api)



    }

    private fun createResponse(isSuccess: Boolean, resultCode: Int, result: String): Single<Response<String>> {
        val response = mockk<Response<String>>()
        every { response.isSuccessful() } returns isSuccess
        every { response.code() } returns resultCode
        every { response.body() } returns result
        return Single.just(response)
    }

    @Test
    fun `we shall get http 200 response with valid request`() {

        // WHEN server get request, it shall give positive response

        every { api.listDogsByType("hound") } answers {
            createResponse(isSuccess = true, resultCode = 200, result = expectedBody)
        }

        // THEN dogService shall get the response

        Assertions.assertThat(dogService.listDogsByType("hound")).isEqualTo(expectedBody)

    }

    @Test
    fun `we shall get http 404 response if request URL is not correct`() {

        // WHEN server get request, it shall give 404 response

        every { api.listDogsByType("alien") } answers {
            createResponse(isSuccess = false, resultCode = 404, result = "")
        }

        // THEN dogService shall throw DogNotFoundException

        Assertions.assertThatThrownBy {
                dogService.listDogsByType("alien")
            }
            .isInstanceOf(DogNotFoundException::class.java)

    }

    @Test
    fun `we shall get exception if request hostname is not correct`() {

        // WHEN the server is ready
        // THEN dogService shall throw UnknownDogException
        every { api.listDogsByType("alien") } throws UnknownDogException()

        Assertions.assertThatThrownBy {
                dogService.listDogsByType("hound")
            }
            .isInstanceOf(UnknownDogException::class.java)

    }

}