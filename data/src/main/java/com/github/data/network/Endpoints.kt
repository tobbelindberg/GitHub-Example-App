package com.github.data.network

import com.github.data.network.model.PullRequestDTO
import com.github.data.network.model.SearchResponseDTO
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Endpoints {

    @GET("/search/repositories?o=desc&s=stars&q=stars:>=60000")
    fun getTopRepositories(
        @Query("page") currentPage: Int,
        @Query("per_page") pageSize: Int
    ): Observable<SearchResponseDTO>

    @GET("/repos/{owner}/{repo}/pulls?sort=updated&direction=desc&state=all")
    fun getPullRequests(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Observable<List<PullRequestDTO>>

}
