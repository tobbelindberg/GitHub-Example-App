package com.github.data.network

import com.github.data.network.model.PullRequestDTO
import com.github.data.network.model.SearchResponseDTO
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


interface Endpoints {

    @GET("/search/repositories?o=desc&s=stars&q=stars:>=50000")
    fun getTopRepositories(): Observable<SearchResponseDTO>

    @GET("/repos/{owner}/{repo}/pulls?sort=updated&direction=desc&state=all")
    fun getPullRequests(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Observable<List<PullRequestDTO>>

}
