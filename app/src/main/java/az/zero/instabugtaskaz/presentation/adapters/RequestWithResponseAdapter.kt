package az.zero.instabugtaskaz.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import az.zero.instabugtaskaz.data.db.RequestWithResponseEntity
import az.zero.instabugtaskaz.databinding.ItemRequestWithResponseBinding
import az.zero.instabugtaskaz.presentation.adapters.RequestWithResponseAdapter.RequestWithResponseViewHolder
import az.zero.instabugtaskaz.utils.getDateWithTime

class RequestWithResponseAdapter(val onItemClick: (RequestWithResponseEntity) -> Unit) :
    ListAdapter<RequestWithResponseEntity, RequestWithResponseViewHolder>(DiffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestWithResponseViewHolder {
        val binding = ItemRequestWithResponseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RequestWithResponseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestWithResponseViewHolder, position: Int) {
        val currentItem = getItem(position) ?: return
        holder.bind(currentItem)
    }


    inner class RequestWithResponseViewHolder(private val binding: ItemRequestWithResponseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Creates the minimum amount of listeners equals the number of viewHolders created
         * not every onBind get called
         * */

        init {
            binding.root.setOnClickListener {
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                onItemClick(getItem(adapterPosition))
            }
        }

        fun bind(item: RequestWithResponseEntity) {
            binding.apply {
                tvRequestType.text = item.request.requestType
                tvLink.text = item.request.uri
                tvExecutionTime.text = getDateWithTime(item.timestamp)
                tvResponseCode.text = "${item.response.responseCode}"
            }
        }

    }


    companion object {
        private val DiffUtil = object : DiffUtil.ItemCallback<RequestWithResponseEntity>() {
            override fun areItemsTheSame(
                oldItem: RequestWithResponseEntity,
                newItem: RequestWithResponseEntity
            ) =
                oldItem.timestamp == newItem.timestamp

            override fun areContentsTheSame(
                oldItem: RequestWithResponseEntity,
                newItem: RequestWithResponseEntity
            ) =
                oldItem == newItem
        }
    }

}