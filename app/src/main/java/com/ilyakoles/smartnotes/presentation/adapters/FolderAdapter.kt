package com.ilyakoles.smartnotes.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ilyakoles.smartnotes.databinding.ItemFolderBinding
import com.ilyakoles.smartnotes.domain.folders.Folder

class FolderAdapter (
    private val context: Context
) : ListAdapter<Folder, FolderViewHolder>(FolderDiffCalback) {

    var onFolderClickListener: OnFolderClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ItemFolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
       val folder = getItem(position)
       with(holder.binding) {
            with(folder) {
                tvFolderName.text = name
                tvCount.text = countElement.toString()
                tvFolderDesc.text = description
                root.setOnClickListener {
                    onFolderClickListener?.onFolderClick(this)
                }
            }
        }
    }

    interface OnFolderClickListener {
        fun onFolderClick(folder: Folder)
    }
}