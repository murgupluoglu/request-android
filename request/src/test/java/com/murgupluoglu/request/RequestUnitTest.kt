package com.murgupluoglu.request

import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
class RequestUnitTest {

    private val mockWebServer = MockWebServer()
    private val api = mockWebServer.getTestApi()

    @Test
    fun `should given http 200 and PeopleResponse`() = runBlocking {

        mockWebServer.enqueueResponse("peoples-200.json", 200)

        val expected = PeopleResponse(Info(1, 1, "seed_info", "1.3"))

        requestFlow {
            api.getPeoples(1)
        }.collectIndexed { index, response ->
            println(response)
            when (index) {
                0 -> {
                    assert(response is Request.Loading)
                    val loadingResponse = response as Request.Loading
                    assertTrue(loadingResponse.isLoading)
                }
                1 -> {
                    assert(response is Request.Success)
                    val successResponse = response as Request.Success
                    assertEquals(successResponse.data, expected)
                }
                2 -> {
                    assert(response is Request.Loading)
                    val loadingResponse = response as Request.Loading
                    assertFalse(loadingResponse.isLoading)
                }
            }
        }

    }

    @Test
    fun `should given http 400 and error response`() = runBlocking {

        mockWebServer.enqueueResponse("peoples-200.json", 400)

        requestFlow {
            api.getPeoples(1)
        }.collectIndexed { index, response ->
            println(response)
            when (index) {
                0 -> {
                    assert(response is Request.Loading)
                    val loadingResponse = response as Request.Loading
                    assertTrue(loadingResponse.isLoading)
                }
                1 -> {
                    assert(response is Request.Error)
                    val errorResponse = response as Request.Error
                    assertEquals(errorResponse.error.code, 400)
                }
                2 -> {
                    assert(response is Request.Loading)
                    val loadingResponse = response as Request.Loading
                    assertFalse(loadingResponse.isLoading)
                }
            }
        }

    }

    @Test
    fun `corrupt json`() = runBlocking {

        mockWebServer.enqueueResponse("peoples-corrupt.json", 200)

        requestFlow {
            api.getPeoples(1)
        }.collectIndexed { index, response ->
            println(response)
            when (index) {
                0 -> {
                    assert(response is Request.Loading)
                    val loadingResponse = response as Request.Loading
                    assertTrue(loadingResponse.isLoading)
                }
                1 -> {
                    assert(response is Request.Error)
                    val errorResponse = response as Request.Error
                    assertEquals(errorResponse.error.code, -2)
                }
                2 -> {
                    assert(response is Request.Loading)
                    val loadingResponse = response as Request.Loading
                    assertFalse(loadingResponse.isLoading)
                }
            }
        }

    }

    @Test
    fun `socket timeout exception`() = runBlocking {

        mockWebServer.enqueueResponse("notexist.json", 200)

        requestFlow {
            api.getPeoples(1)
        }.collectIndexed { index, response ->
            println(response)
            when (index) {
                0 -> {
                    assert(response is Request.Loading)
                    val loadingResponse = response as Request.Loading
                    assertTrue(loadingResponse.isLoading)
                }
                1 -> {
                    assert(response is Request.Error)
                    val errorResponse = response as Request.Error
                    assertEquals(errorResponse.error.message, "timeout")
                }
                2 -> {
                    assert(response is Request.Loading)
                    val loadingResponse = response as Request.Loading
                    assertFalse(loadingResponse.isLoading)
                }
            }
        }

    }

    @After
    fun cleanup() {
        mockWebServer.shutdown()
    }
}
