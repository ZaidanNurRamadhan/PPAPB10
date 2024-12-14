package com.example.tugas10

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas10.databinding.ItemDisasterBinding

class DisasterAdapter(
    private var disasterList: MutableList<Disaster>,
    private val onBookmarkClick: (Disaster) -> Unit
) : RecyclerView.Adapter<DisasterAdapter.DisasterViewHolder>() {

    inner class DisasterViewHolder(private val binding: ItemDisasterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(disaster: Disaster) {
            binding.imageView.setImageResource(disaster.imageResource)
            binding.textView.text = disaster.name
            binding.bookmarkButton.setImageResource(
                if (disaster.isBookmarked) R.drawable.ic_bookmarked else R.drawable.ic_bookmark
            )

            binding.bookmarkButton.setOnClickListener {
                onBookmarkClick(disaster) // Trigger ke ViewModel
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisasterViewHolder {
        val binding = ItemDisasterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DisasterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DisasterViewHolder, position: Int) {
        holder.bind(disasterList[position])
    }

    override fun getItemCount(): Int = disasterList.size

    // Update data di adapter
    fun updateData(newList: List<Disaster>) {
        disasterList.clear()
        disasterList.addAll(newList)
        notifyDataSetChanged()
    }
}
