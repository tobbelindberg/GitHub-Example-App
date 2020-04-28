package com.github.ui.toprepositories.vm

import android.content.res.Resources
import android.text.format.DateFormat
import com.github.R
import com.github.base.vm.ItemViewModel
import com.github.domain.model.Repository


class RepositoryItemViewModel(val repository: Repository, resources: Resources) : ItemViewModel {

    val title = repository.name

    val owner = repository.owner
    val updatedAt = resources.getString(
        R.string.last_updated,
        DateFormat.format("MMM d, yyyy HH:mm", repository.updatedAt)
    )

    val starCount = repository.starCount.toString()


    override fun itemId(): Long {
        return repository.id
    }
}