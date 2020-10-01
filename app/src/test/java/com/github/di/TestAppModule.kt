package com.github.di

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest


/**
 * Provides Test components and modules used all over the place here and hosts a mock server
 * that delivers the provided response.
 *
 * @param mockResponses The first string in the pair is the endpoint path and the second one is it's
 * corresponding response.
 */
class TestAppModule(
    mockAppContext: Context,
    private val okHttpIdleCallback: Runnable,
    vararg mockResponses: Pair<String, String>
) :
    AppModule(mockAppContext) {

    private val mockWebServer = MockWebServer()

    val mockResponseMap = mockResponses.toMap()

    init {
        mockWebServer.dispatcher = object : Dispatcher() {
            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockResponseMap[request.path]?.let { response ->
                    MockResponse().setBody(response)
                } ?: MockResponse().setResponseCode(404)
            }
        }
        mockWebServer.start()
    }

    fun shutDownServer() {
        mockWebServer.shutdown()
    }

    override fun provideBaseUrl(): String {
        return mockWebServer.url("/").toString()
    }

    override fun provideOkHttp(): OkHttpClient {
        val okHttp = super.provideOkHttp()
        okHttp.dispatcher.idleCallback = okHttpIdleCallback
        return okHttp
    }

}