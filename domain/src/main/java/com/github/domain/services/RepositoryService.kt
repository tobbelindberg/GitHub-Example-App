package com.github.domain.services

import com.github.domain.model.Repository
import io.reactivex.Observable

interface RepositoryService {

    fun getTopRepositories(): Observable<List<Repository>>
}