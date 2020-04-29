package com.github.ui.toprepositories.repository

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.github.BR
import com.github.R
import com.github.base.BaseFragment
import com.github.base.GitHubApplication
import com.github.bindings.tatarka.BindingRecyclerViewAdapterIds
import com.github.bindings.tatarka.GitHubBindingRecyclerViewAdapter
import com.github.data.parcelable.RepositoryParcelable
import com.github.databinding.FragmentRepositoryBinding
import com.github.domain.model.Repository
import com.github.ui.toprepositories.TopRepositoriesFragment
import com.github.ui.toprepositories.repository.vm.PullRequestItemViewModel
import com.github.utils.FragmentTransitionAnimations
import com.github.utils.bindingProvider
import com.github.utils.viewModelProvider
import com.github.widgets.itemdecorators.IndicesSkippingDividerItemDecoration
import me.tatarka.bindingcollectionadapter2.ItemBinding


class RepositoryFragment : BaseFragment<RepositoryViewModel>(),
    GitHubBindingRecyclerViewAdapter.OnAdapterCountChangedListener {

    override val viewModel: RepositoryViewModel by viewModelProvider(
        { viewModelFactory },
        false
    )

    private val binding: FragmentRepositoryBinding by bindingProvider(R.layout.fragment_repository)

    private lateinit var transitionAnimations: FragmentTransitionAnimations

    val itemBinding =
        ItemBinding.of<PullRequestItemViewModel>(BR.viewModel, R.layout.item_pull_request)

    val itemIds = BindingRecyclerViewAdapterIds

    val adapter = GitHubBindingRecyclerViewAdapter<PullRequestItemViewModel>()
    private var restoredRecyclerViewState: Parcelable? = null

    val diffConfig = AsyncDifferConfig.Builder(object :
        DiffUtil.ItemCallback<PullRequestItemViewModel>() {
        override fun areItemsTheSame(
            oldItem: PullRequestItemViewModel,
            newItem: PullRequestItemViewModel
        ): Boolean {
            return oldItem.itemId() == newItem.itemId()
        }

        override fun areContentsTheSame(
            oldItem: PullRequestItemViewModel,
            newItem: PullRequestItemViewModel
        ): Boolean {
            return oldItem.pullRequest.updatedAt == newItem.pullRequest.updatedAt
        }
    }).build()

    lateinit var itemDecoration: IndicesSkippingDividerItemDecoration

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

        itemDecoration = IndicesSkippingDividerItemDecoration(
            requireContext(),
            IndicesSkippingDividerItemDecoration.VERTICAL,
            ContextCompat.getDrawable(requireContext(), R.drawable.divider_grey_100)!!
        )
        adapter.setOnAdapterCountChangedCallback(this)
        binding.fragment = this
        binding.viewModel = viewModel

        return binding.root
    }

    fun onNavigationBackClick(view: View) {
        navigator?.onBackPressed()
    }

    fun onErrorRetry(view: View) {
        viewModel.onRefresh()
    }

    override fun onAdapterCountChanged(count: Int) {
        if (count > 0) {
            itemDecoration.setSkipIndices(setOf(count - 1))
            restoredRecyclerViewState?.also { recyclerViewState ->
                binding.recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(
            TopRepositoriesFragment.RECYCLER_VIEW_STATE,
            Bundle().apply {
                putParcelable(
                    TopRepositoriesFragment.RECYCLER_VIEW_STATE,
                    binding.recyclerView.layoutManager?.onSaveInstanceState()
                )
            }
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        restoredRecyclerViewState =
            savedInstanceState?.getBundle(TopRepositoriesFragment.RECYCLER_VIEW_STATE)
                ?.getParcelable(TopRepositoriesFragment.RECYCLER_VIEW_STATE)
    }

    companion object {
        const val RECYCLER_VIEW_STATE = "recycler_view_state"
        private const val REPOSITORY_PARCELABLE = "repository_parcelable"

        fun newInstance(repository: Repository) = RepositoryFragment().apply {
            arguments =
                bundleOf(REPOSITORY_PARCELABLE to RepositoryParcelable.fromRepository(repository))
        }

        fun newInstance() = RepositoryFragment()
    }

}
