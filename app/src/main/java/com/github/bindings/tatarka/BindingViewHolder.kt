package com.github.bindings.tatarka

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.github.base.vm.ItemViewModel
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

open class BindingViewHolder<T : ItemViewModel>(
    binding: ViewDataBinding,
    private val adapter: BindingRecyclerViewAdapter<T>,
    vararg onItemClickedListeners: Pair<Int, Function3<View, T, Int, Unit>>
) : RecyclerView.ViewHolder(binding.root) {

    private val itemClickListeners = onItemClickedListeners.toMap()

    init {
        binding.root.setOnClickListener { view ->
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListeners[adapter.getItemViewType(adapterPosition)]?.also { onItemClickedListener ->
                    adapter.getAdapterItem(adapterPosition)?.also { item ->
                        onItemClickedListener.invoke(view, item, adapterPosition)
                    }
                }
            }
        }
    }
}