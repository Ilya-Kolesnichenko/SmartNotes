package com.ilyakoles.smartnotes.domain.folders

import com.ilyakoles.smartnotes.domain.users.IUserRepository
import javax.inject.Inject

class SaveFolderUseCase @Inject constructor(private val repository: IFolderRepository) {

    suspend operator fun invoke(id: Int, parentId: Int, name: String, userId: Int, isShared: Int,
                                description: String, level: Int) =
        repository.saveFolder(id, parentId, name, userId, isShared, description, level)
}