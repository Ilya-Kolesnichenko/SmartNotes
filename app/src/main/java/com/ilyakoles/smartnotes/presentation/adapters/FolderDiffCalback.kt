package com.ilyakoles.smartnotes.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.ilyakoles.smartnotes.domain.folders.Folder

object FolderDiffCalback: DiffUtil.ItemCallback<Folder>() {

    override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean {
        return oldItem.folderId == newItem.folderId
    }

    override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean {
        return oldItem == newItem
    }
}
