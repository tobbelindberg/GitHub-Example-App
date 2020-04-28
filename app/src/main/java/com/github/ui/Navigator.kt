package com.github.ui

import com.github.domain.model.Repository

interface Navigator {

    fun openRepository(repository: Repository)

    fun onBackPressed()
}