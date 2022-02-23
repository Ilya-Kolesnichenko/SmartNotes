package com.ilyakoles.smartnotes.domain.folders

data class Folder (
    val folderId: Int,
    val parentId: Int,
    val name: String,
    val userId: Int,
    val isShared: Int,
    var countElement: Int
)