package me.capcom.smsgateway.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.util.Date
import me.capcom.smsgateway.data.entities.Message
import me.capcom.smsgateway.databinding.ItemMessageBinding
import me.capcom.smsgateway.ui.styles.color

class MessagesAdapter(private val onItemClickListener: OnItemClickListener<Message>) :
    ListAdapter<Message, MessagesAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder =
        MessageViewHolder.create(parent).also { holder ->
            holder.itemView.setOnClickListener {
                val message = getItem(holder.adapterPosition)
                onItemClickListener.onItemClick(message)
            }
        }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)

        holder.bind(message)
    }

    class MessageViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.textViewId.text = message.id
            binding.textViewDate.text =
                DateFormat.getDateTimeInstance().format(Date(message.createdAt))
            binding.textViewState.text = message.state.name
            binding.imageViewState.setColorFilter(message.state.color)
        }

        companion object {
            fun create(parent: ViewGroup): MessageViewHolder = MessageViewHolder(
                ItemMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    class MessageDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean = oldItem == newItem
    }

    interface OnItemClickListener<T> {
        fun onItemClick(item: T)
    }
}
