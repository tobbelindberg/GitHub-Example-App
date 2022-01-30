package com.github.data.services

import com.github.data.network.Endpoints
import com.github.data.network.model.PullRequestDTO
import com.github.data.network.model.RepositoryDTO
import com.github.domain.mapper.ObjectMapper
import com.github.domain.model.PullRequest
import com.github.domain.model.Repository
import com.github.domain.services.RepositoryService
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RepositoryNetworkService
@Inject constructor(
    private val restEndpoints: Endpoints,
    private val repositoryMapper: ObjectMapper<RepositoryDTO, Repository>,
    private val pullRequestMapper: ObjectMapper<PullRequestDTO, PullRequest>
) : RepositoryService {

    override fun getTopRepositories(page: Int, pageSize: Int): Observable<List<Repository>> {
        return restEndpoints.getTopRepositories(page, pageSize)
            .map {
                it.items.map(repositoryMapper::map)
            }
    }


    override fun getPullRequests(owner: String, repo: String): Observable<List<PullRequest>> {
        return restEndpoints.getPullRequests(owner, repo)
            .map {
                it.map(pullRequestMapper::map)
            }
    }
}