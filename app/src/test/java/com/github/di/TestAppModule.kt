package com.github.di

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer


/**
 * Provides Test components and modules used all over the place here and hosts a mock server
 * that delivers the provided response.
 *
 * @param mockResponse A json mock response that the mock server should return for the given unit
 * test.
 */
class TestAppModule(
    mockAppContext: Context,
    mockResponse: String,
    private val okHttpIdleCallback: Runnable
) :
    AppModule(mockAppContext) {

    private val mockWebServer = MockWebServer()

    init {
        mockWebServer.enqueue(MockResponse().setBody(mockResponse))
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