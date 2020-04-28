package com.github.di

import android.content.Context
import android.content.res.Resources
import com.github.BuildConfig
import com.github.data.network.Endpoints
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Provides components and modules used all over the place here.
 */
@Module(includes = [NetworkModule::class])
open class AppModule(private val appContext: Context) {

    companion object {
        const val BASE_URL = "https://api.github.com"
        const val ACCEPT = "Accept"
        const val APPLICATION_VND_V3 = "application/vnd.github.v3+json"
    }

    @Provides
    fun provideAppContext(): Context {
        return appContext
    }


    @Provides
    fun provideResources(): Resources {
        return appContext.resources
    }

    @Provides
    open fun provideBaseUrl(): String {
        return BASE_URL
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create()
    }

    @Provides
    open fun provideOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor())
        }
        builder.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .addHeader(ACCEPT, APPLICATION_VND_V3).build()

            chain.proceed(request)
        }

        return builder.build()
    }

    @Provides
    internal fun provideEndpints(
        gson: Gson,
        baseUrl: String,
        okHttpClient: OkHttpClient
    ): Endpoints {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(Endpoints::class.java)
    }

}