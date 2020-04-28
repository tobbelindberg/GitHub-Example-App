package com.github.data.services

import com.github.data.network.Endpoints
import com.github.data.network.model.RepositoryDTO
import com.github.domain.mapper.ObjectMapper
import com.github.domain.model.Repository
import com.github.domain.services.RepositoryService
import io.reactivex.Observable
import javax.inject.Inject

class RepositoryNetworkService
@Inject constructor(
    private val restEndpoints: Endpoints,
    private val repositoryMapper: ObjectMapper<RepositoryDTO, Repository>
) : RepositoryService {

    override fun getTopRepositories(): Observable<List<Repository>> {
        return restEndpoints.getTopRepositories()
            .map {
                it.items.map(repositoryMapper::map)
            }
    }
}