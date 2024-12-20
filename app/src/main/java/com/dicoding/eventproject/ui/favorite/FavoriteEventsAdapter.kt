package com.dicoding.eventproject.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.eventproject.database.FavoriteEventEntity
import com.dicoding.eventproject.databinding.ItemEventBinding

class FavoriteEventsAdapter(private val onClick: (FavoriteEventEntity) -> Unit) :
    ListAdapter<FavoriteEventEntity, FavoriteEventsAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onClick)
    }

    class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavoriteEventEntity, onClick: (FavoriteEventEntity) -> Unit) {
            binding.eventName.text = event.name
            Glide.with(binding.eventImage.context).load(event.imageLogo).into(binding.eventImage)
            binding.root.setOnClickListener { onClick(event) }
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<FavoriteEventEntity>() {
        override fun areItemsTheSame(oldItem: FavoriteEventEntity, newItem: FavoriteEventEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteEventEntity, newItem: FavoriteEventEntity): Boolean {
            return oldItem == newItem
        }
    }
}
