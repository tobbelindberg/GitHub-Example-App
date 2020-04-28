package com.github.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.github.base.GitHubApplication
import com.github.domain.model.Repository
import com.github.ui.toprepositories.TopRepositoriesFragment
import com.github.ui.toprepositories.repository.RepositoryFragment
import com.github.utils.simpleName

class MainActivity : AppCompatActivity(), Navigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as GitHubApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            replaceChildFragment(
                fragment = TopRepositoriesFragment.newInstance(),
                addToBackStack = false
            )
        }
    }

    override fun openRepository(repository: Repository) {
        replaceChildFragment(
            fragment = RepositoryFragment.newInstance(repository),
            transition = FragmentTransaction.TRANSIT_FRAGMENT_OPEN
        )
    }

    private fun replaceChildFragment(
        fragment: Fragment,
        transition: Int = FragmentTransaction.TRANSIT_NONE,
        addToBackStack: Boolean = true
    ) {
        val tag = fragment.simpleName

        var transaction = supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment, tag)
            .setTransition(transition)

        transaction = if (addToBackStack) {
            transaction.addToBackStack(tag)
        } else {
            transaction
        }

        transaction.commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}
