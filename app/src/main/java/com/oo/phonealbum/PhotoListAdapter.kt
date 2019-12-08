package com.oo.phonealbum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PhotoListAdapter(diffCallback: DiffUtil.ItemCallback<PhotoDataModel>) :
    ListAdapter<PhotoDataModel, PhotoListAdapter.PhotoViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.photo_view_layout, parent, false)
        return PhotoViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        Glide.with(holder.imageView)
            .load(currentList[position].uri)
            .into(holder.imageView)

        holder.textview.text = currentList[position].name
    }







    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView:ImageView = itemView.findViewById(R.id.image_view)
        val textview:TextView = itemView.findViewById(R.id.text_view)
    }
}