package com.murgupluoglu.request

import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
class RequestTransformUnitTest {

    private val mockWebServer = MockWebServer()
    private val api = mockWebServer.getTestApi()

    @Test
    fun `should given http 200 and PeopleResponse transform to PeopleTransformedResponse`() =
        runBlocking {

            mockWebServer.enqueueResponse("peoples-200.json", 200)


            requestTransformFlow(
                suspendFun = { api.getPeoples(1) },
                transform = { response ->
                    return@requestTransformFlow PeopleTransformedResponse(response.info!!.page + 1452)
                },
                cacheListener = null
            ).collectIndexed { index, response ->
                println(response)
                when (index) {
                    0 -> {
                        assert(response is Request.Loading)
                        val loadingResponse = response as Request.Loading
                        TestCase.assertTrue(loadingResponse.isLoading)
                    }
                    1 -> {
                        assert(response is Request.Success)
                        val successResponse = response as Request.Success
                        TestCase.assertEquals(successResponse.data, PeopleTransformedResponse(1453))
                    }
                    2 -> {
                        assert(response is Request.Loading)
                        val loadingResponse = response as Request.Loading
                        TestCase.assertFalse(loadingResponse.isLoading)
                    }
                }
            }

        }

    @After
    fun cleanup() {
        mockWebServer.shutdown()
    }
}