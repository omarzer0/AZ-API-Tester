package az.zero.instabugtaskaz.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import az.zero.instabugtaskaz.databinding.ItemListMapBinding
import az.zero.instabugtaskaz.domain.models.ListMapItem
import az.zero.instabugtaskaz.presentation.adapters.ListMapAdapter.ListMapViewHolder

/**
 * Data will not be updated, changed or deleted so using RecyclerView.Adapter is
 * more suitable than ListAdapter in this scenario
 * */
class ListMapAdapter : RecyclerView.Adapter<ListMapViewHolder>() {
    private var items: List<ListMapItem> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun changeItems(newItems: List<ListMapItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMapViewHolder {
        val binding = ItemListMapBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListMapViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListMapViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = items.size


    inner class ListMapViewHolder(private val binding: ItemListMapBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListMapItem) {
            binding.apply {
                tvKey.text = item.key
                tvValue.text = item.value
            }
        }

    }
}