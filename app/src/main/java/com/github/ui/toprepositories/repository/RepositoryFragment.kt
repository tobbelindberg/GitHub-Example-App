package com.github.ui.toprepositories.repository

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.core.os.bundleOf
import com.github.R
import com.github.base.BaseFragment
import com.github.base.GitHubApplication
import com.github.data.parcelable.RepositoryParcelable
import com.github.databinding.FragmentRepositoryBinding
import com.github.domain.model.Repository
import com.github.utils.FragmentTransitionAnimations
import com.github.utils.bindingProvider
import com.github.utils.viewModelProvider


class RepositoryFragment : BaseFragment<RepositoryViewModel>() {

    override val viewModel: RepositoryViewModel by viewModelProvider(
        { viewModelFactory },
        false
    )

    private val binding: FragmentRepositoryBinding by bindingProvider(R.layout.fragment_repository)

    private lateinit var transitionAnimations: FragmentTransitionAnimations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transitionAnimations = FragmentTransitionAnimations(
            requireContext(),
            FragmentTransitionAnimations.OpenCloseType.IN_OUT_RIGHT
        )
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return transitionAnimations.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val repository =
            requireArguments().getParcelable<RepositoryParcelable>(REPOSITORY_PARCELABLE)!!
                .toRepository()

        (requireActivity().application as GitHubApplication).appComponent.repositoryBuilder()
            .setRepository(repository)
            .build().inject(this)
    }

    override fun onDetach() {
        super.onDetach()
        navigator = null
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.toolbar.setNavigationOnClickListener(::onNavigationBackClick)
        binding.fragment = this
        binding.viewModel = viewModel

        return binding.root
    }

    fun onNavigationBackClick(view: View) {
        navigator?.onBackPressed()
    }

    companion object {

        private const val REPOSITORY_PARCELABLE = "repository_parcelable"

        fun newInstance(repository: Repository) = RepositoryFragment().apply {
            arguments =
                bundleOf(REPOSITORY_PARCELABLE to RepositoryParcelable.fromRepository(repository))
        }

        fun newInstance() = RepositoryFragment()
    }

}
