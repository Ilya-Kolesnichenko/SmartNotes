package com.ilyakoles.smartnotes.data.mapper

import com.ilyakoles.smartnotes.data.network.model.AnswerDto
import com.ilyakoles.smartnotes.data.network.model.FolderDto
import com.ilyakoles.smartnotes.domain.Answer
import com.ilyakoles.smartnotes.domain.folders.Folder
import javax.inject.Inject

class FolderMapper @Inject constructor() {

    fun mapFolderDtoToEntity(dtoModel: FolderDto) = Folder(
        folderId = dtoModel.id,
        name = dtoModel.name,
        description = dtoModel.description,
        countElement = dtoModel.countelem,
        level = dtoModel.level,
        parentId = dtoModel.parentid
    )
}