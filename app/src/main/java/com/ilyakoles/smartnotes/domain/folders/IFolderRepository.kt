package com.ilyakoles.smartnotes.domain.folders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ilyakoles.smartnotes.data.network.model.FolderDto
import com.ilyakoles.smartnotes.domain.Answer

interface IFolderRepository {

    suspend fun getFoldersByUser(userId: Int, parentFolderId: Int, level: Int) : LiveData<List<Folder>>
    suspend fun getParentFolderById(isParent: Int, folderId: Int) : LiveData<Folder>

    suspend fun saveFolder(id: Int, parentId: Int, name: String, userId: Int, isShared: Int,
                           description: String, level: Int) : Answer?

    suspend fun deleteFolder(id: Int) : Answer?

}