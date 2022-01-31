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
import com.github.base.vm.ItemViewModel
import com.github.base.vm.LoadingFooterItemViewModel
import com.github.bindings.tatarka.BindingRecyclerViewAdapterIds
import com.github.bindings.tatarka.BindingViewHolder
import com.github.bindings.tatarka.GitHubBindingRecyclerViewAdapter
import com.github.data.parcelable.RepositoryParcelable
import com.github.databinding.FragmentTopRepositoriesBinding
import com.github.ui.toprepositories.vm.RepositoryItemViewModel
import com.github.utils.ConsumableState
import com.github.utils.bindingProvider
import com.github.utils.paging.PagingScrollListener
import com.github.utils.viewModelProvider
import com.github.widgets.itemdecorators.IndicesSkippingDividerItemDecoration
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class TopRepositoriesFragment : BaseFragment<TopRepositoriesViewModel>(),
    GitHubBindingRecyclerViewAdapter.OnAdapterCountChangedListener {

    private companion object {
        private const val VISIBLE_THRESHOLD = 3
        private const val RECYCLER_VIEW_STATE = "recycler_view_state"
    }

    override val viewModel: TopRepositoriesViewModel by viewModelProvider(
        { viewModelFactory },
        true
    )

    private val binding: FragmentTopRepositoriesBinding by bindingProvider(R.layout.fragment_top_repositories)

    val itemBinding = OnItemBindClass<ItemViewModel>()
        .map(RepositoryItemViewModel::class.java, BR.viewModel, R.layout.item_repository)
        .map(LoadingFooterItemViewModel::class.java, ItemBinding.VAR_NONE, R.layout.item_loading_footer)

    val itemIds = BindingRecyclerViewAdapterIds

    val adapter = GitHubBindingRecyclerViewAdapter<ItemViewModel>()
    private var restoredRecyclerViewState: Parcelable? = null

    lateinit var pagingScrollListener: PagingScrollListener

    val diffConfig = AsyncDifferConfig.Builder(object :
        DiffUtil.ItemCallback<ItemViewModel>() {
        override fun areItemsTheSame(
            oldItem: ItemViewModel,
            newItem: ItemViewModel
        ): Boolean {
            return if (oldItem::class == newItem::class) {
                oldItem.itemId() == newItem.itemId()
            } else {
                false
            }
        }

        override fun areContentsTheSame(
            oldItem: ItemViewModel,
            newItem: ItemViewModel
        ): Boolean {
            return if (oldItem is RepositoryItemViewModel) {
                newItem as RepositoryItemViewModel

                oldItem.repository.updatedAt == newItem.repository.updatedAt
            } else {
                true
            }
        }
    }).build()

    val viewHolderFactory = BindingRecyclerViewAdapter.ViewHolderFactory { binding ->
        BindingViewHolder(
            binding,
            adapter,
            R.layout.item_repository to { _, repositoryItem, _ ->
                repositoryItem as RepositoryItemViewModel
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

        pagingScrollListener = PagingScrollListener(
            binding.recyclerView.layoutManager!!,
            VISIBLE_THRESHOLD, viewModel::onLoadNextPage
        )
        adapter.setOnAdapterCountChangedCallback(this)
        binding.fragment = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.paginationErrorData.observe(viewLifecycleOwner, ::onPaginationError)
    }

    private fun onPaginationError(paginationError: ConsumableState<Throwable>) {
        paginationError.consume {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.error_title)
                .setMessage(R.string.error_message)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
        }
    }

    fun onErrorRetry(@Suppress("UNUSED_PARAMETER") view: View) {
        viewModel.onRefresh()
    }

    fun onRefresh() {
        viewModel.onRefresh(swipeRefreshing = true)
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
}
