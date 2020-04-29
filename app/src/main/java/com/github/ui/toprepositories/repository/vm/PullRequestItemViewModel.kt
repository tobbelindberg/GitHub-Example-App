package com.github.ui.toprepositories.repository.vm

import android.content.res.Resources
import android.text.format.DateFormat
import com.github.R
import com.github.base.vm.ItemViewModel
import com.github.domain.model.PullRequest


class PullRequestItemViewModel(val pullRequest: PullRequest, resources: Resources) :
    ItemViewModel {

    val title = pullRequest.title
    val userName = pullRequest.userName

    val number = pullRequest.number.toString()

    val updatedAt = resources.getString(
        R.string.last_updated,
        DateFormat.format("MMM d, yyyy HH:mm", pullRequest.updatedAt)
    )

    val state: String = when (pullRequest.state) {
        PullRequest.State.OPEN -> resources.getString(R.string.open)
        PullRequest.State.CLOSED -> resources.getString(R.string.closed)
    }

    override fun itemId(): Long {
        return pullRequest.id
    }
}