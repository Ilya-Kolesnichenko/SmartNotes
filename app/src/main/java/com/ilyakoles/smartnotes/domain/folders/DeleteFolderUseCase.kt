package com.ilyakoles.smartnotes.domain.folders

import javax.inject.Inject

class DeleteFolderUseCase @Inject constructor(private val repository: IFolderRepository) {

    suspend operator fun invoke(id: Int) = repository.deleteFolder(id)
}
