package com.github.data.parcelable

import android.os.Parcelable
import com.github.domain.model.Repository
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class RepositoryParcelable(
    val id: Long,
    val name: String,
    val owner: String,
    val starCount: Int,
    val forksCount: Int,
    val watchersCount: Int,
    val openIssuesCount: Int,
    val language: String?,
    val updatedAt: Date
) : Parcelable {

    fun toRepository(): Repository {
        return Repository(
            id, name, owner, starCount, forksCount, watchersCount, openIssuesCount,
            language, updatedAt
        )
    }

    companion object {
        fun fromRepository(repository: Repository): RepositoryParcelable {
            return RepositoryParcelable(
                repository.id, repository.name, repository.owner, repository.starCount,
                repository.forksCount, repository.watchersCount, repository.openIssuesCount,
                repository.language, repository.updatedAt
            )
        }
    }
}