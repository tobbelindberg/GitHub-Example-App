package com.github.ui.toprepositories

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.github.BR
import com.github.R
import com.github.base.BaseFragment
import com.github.base.GitHubApplication
import com.github.bindings.tatarka.BindingRecyclerViewAdapterIds
import com.github.bindings.tatarka.BindingViewHolder
import com.github.bindings.tatarka.GitHubBindingRecyclerViewAdapter
import com.github.data.parcelable.RepositoryParcelable
import com.github.databinding.FragmentTopRepositoriesBinding
import com.github.ui.toprepositories.vm.RepositoryItemViewModel
import com.github.utils.bindingProvider
import com.github.utils.viewModelProvider
import com.github.widgets.itemdecorators.IndicesSkippingDividerItemDecoration
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding


class TopRepositoriesFragment : BaseFragment<TopRepositoriesViewModel>(),
    GitHubBindingRecyclerViewAdapter.OnAdapterCountChangedListener {
    override val viewModel: TopRepositoriesViewModel by viewModelProvider(
        { viewModelFactory },
        true
    )

    private val binding: FragmentTopRepositoriesBinding by bindingProvider(R.layout.fragment_top_repositories)

    val itemBinding =
        ItemBinding.of<RepositoryItemViewModel>(BR.viewModel, R.layout.item_repository)

    val itemIds = BindingRecyclerViewAdapterIds

    val adapter = GitHubBindingRecyclerViewAdapter<RepositoryItemViewModel>()
    private var restoredRecyclerViewState: Parcelable? = null

    val diffConfig = AsyncDifferConfig.Builder(object :
        DiffUtil.ItemCallback<RepositoryItemViewModel>() {
        override fun areItemsTheSame(
            oldItem: RepositoryItemViewModel,
            newItem: RepositoryItemViewModel
        ): Boolean {
            return oldItem.itemId() == newItem.itemId()
        }

        override fun areContentsTheSame(
            oldItem: RepositoryItemViewModel,
            newItem: RepositoryItemViewModel
        ): Boolean {
            return oldItem.repository.updatedAt == newItem.repository.updatedAt
        }
    }).build()

    val viewHolderFactory = BindingRecyclerViewAdapter.ViewHolderFactory { binding ->
        BindingViewHolder(
            binding,
            adapter,
            R.layout.item_repository to { _, repositoryItem, _ ->
                val direction =
                    TopRepositoriesFragmentDirections.actionTopRepositoriesFragmentToRepositoryFragment(
                        RepositoryParcelable.fromRepository(repositoryItem.repository)
                    )

                navController.navigate(direction)
            })
    }

    lateinit var itemDecoration: IndicesSkippingDividerItemDecoration

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as GitHubApplication).appComponent.topRepositoriesBuilder()
            .build().inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.toolbar.setupWithNavController(
            navController,
            AppBarConfiguration(navController.graph)
        )

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
        outState.putBundle(RECYCLER_VIEW_STATE,
            Bundle().apply {
                putParcelable(
                    RECYCLER_VIEW_STATE,
                    binding.recyclerView.layoutManager?.onSaveInstanceState()
                )
            }
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        restoredRecyclerViewState = savedInstanceState?.getBundle(RECYCLER_VIEW_STATE)
            ?.getParcelable(RECYCLER_VIEW_STATE)
    }

    companion object {

        private const val RECYCLER_VIEW_STATE = "recycler_view_state"

    }

}
