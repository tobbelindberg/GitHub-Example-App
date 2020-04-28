package com.github.data.repositories

import com.github.domain.model.Repository
import com.github.domain.services.RepositoryService
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In the repositories is where I would also inject a cache that would cache the repositories and such.
 */
@Singleton
class RepositoryRepo
@Inject constructor(private val repositoryService: RepositoryService) {

    fun getTopRepositories(): Observable<List<Repository>> {
        return repositoryService.getTopRepositories()
    }

}