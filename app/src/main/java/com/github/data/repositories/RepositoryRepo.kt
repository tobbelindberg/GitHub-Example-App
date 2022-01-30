package com.github.data.repositories

import com.github.domain.model.PullRequest
import com.github.domain.model.Repository
import com.github.domain.services.RepositoryService
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In the repos is where I would also inject a cache that would cache the repositories and such.
 */
@Singleton
class RepositoryRepo
@Inject constructor(private val repositoryService: RepositoryService) {

    fun getTopRepositories(page: Int, pageSize: Int): Observable<List<Repository>> {
        return repositoryService.getTopRepositories(page, pageSize)
    }

    fun getPullRequests(owner: String, repo: String): Observable<List<PullRequest>> {
        return repositoryService.getPullRequests(owner, repo)
    }

}