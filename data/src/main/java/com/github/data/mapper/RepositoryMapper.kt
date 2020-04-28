package com.github.data.mapper

import com.github.data.network.model.RepositoryDTO
import com.github.domain.mapper.ObjectMapper
import com.github.domain.model.Repository
import javax.inject.Inject

class RepositoryMapper
@Inject constructor() : ObjectMapper<RepositoryDTO, Repository> {
    override fun map(model: RepositoryDTO): Repository {
        return Repository(
            model.id,
            model.name,
            model.owner.login,
            model.stargazers_count,
            model.forks_count,
            model.watchers_count,
            model.open_issues_count,
            model.language,
            model.updated_at
        )
    }
}