package com.github.data.mapper

import com.github.data.network.model.PullRequestDTO
import com.github.domain.mapper.ObjectMapper
import com.github.domain.model.PullRequest
import javax.inject.Inject

class PullRequestMapper
@Inject constructor() : ObjectMapper<PullRequestDTO, PullRequest> {
    override fun map(model: PullRequestDTO): PullRequest {
        return PullRequest(
            model.id,
            model.title,
            model.number,
            model.user.login,
            model.updated_at,
            getState(model.state)
        )
    }

    private fun getState(state: PullRequestDTO.State): PullRequest.State {
        return when (state) {
            PullRequestDTO.State.open -> PullRequest.State.OPEN
            PullRequestDTO.State.closed -> PullRequest.State.CLOSED
        }
    }
}