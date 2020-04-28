package com.github.data.network

import com.github.data.network.model.SearchResponseDTO
import io.reactivex.Observable
import retrofit2.http.GET


interface Endpoints {

    @GET("/search/repositories?o=desc&s=stars&q=stars:>=50000")
    fun getTopRepositories(): Observable<SearchResponseDTO>

}
