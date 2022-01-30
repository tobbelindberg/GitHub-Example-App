package com.github.bindings.tatarka

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.github.base.vm.ItemViewModel
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

open class BindingViewHolder<T : ItemViewModel>(
    binding: ViewDataBinding,
    private val adapter: BindingRecyclerViewAdapter<T>,
    vararg onItemClickedListeners: Pair<Int, ((View, T, Int) -> Unit)>
) : RecyclerView.ViewHolder(binding.root) {

    private val itemClickListeners = onItemClickedListeners.toMap()

    init {
        binding.root.setOnClickListener { view ->
            adapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.also { pos ->
                itemClickListeners[adapter.getItemViewType(pos)]?.also { onItemClickedListener ->
                    adapter.getAdapterItem(pos)?.also { item ->
                        onItemClickedListener.invoke(view, item, pos)
                    }
                }
            }
        }
    }
}