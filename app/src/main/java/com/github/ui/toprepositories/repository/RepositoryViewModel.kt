package com.github.ui.toprepositories.repository

import android.content.res.Resources
import android.text.format.DateFormat
import com.github.R
import com.github.base.vm.BaseViewModel
import com.github.domain.model.Repository
import javax.inject.Inject

class RepositoryViewModel
@Inject constructor(
    resources: Resources,
    private val repository: Repository
) : BaseViewModel() {

    val title = repository.name

    val owner = repository.owner
    val updatedAt = resources.getString(
        R.string.last_updated,
        DateFormat.format("MMM d, yyyy HH:mm", repository.updatedAt)
    )

    val starCount = repository.starCount.toString()
    val forksCount = repository.forksCount.toString()
    val watcherCount = repository.watchersCount.toString()
    val openIssuesCount = repository.openIssuesCount.toString()
    val language = repository.language

}


