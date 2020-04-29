package com.github.domain.services

import com.github.domain.model.PullRequest
import com.github.domain.model.Repository
import io.reactivex.Observable

interface RepositoryService {

    fun getTopRepositories(): Observable<List<Repository>>

    fun getPullRequests(owner: String, repo: String): Observable<List<PullRequest>>

}