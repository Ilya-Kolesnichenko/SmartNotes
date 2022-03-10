package com.ilyakoles.smartnotes.domain.folders

import javax.inject.Inject

class GetFoldersUseCase @Inject constructor(private val repository: IFolderRepository) {

    suspend operator fun invoke(userId: Int, parentFolderId: Int, level: Int) =
        repository.getFoldersByUser(userId, parentFolderId, level)

    suspend operator fun invoke(isParent: Int, folderId: Int) =
        repository.getParentFolderById(isParent, folderId)
}