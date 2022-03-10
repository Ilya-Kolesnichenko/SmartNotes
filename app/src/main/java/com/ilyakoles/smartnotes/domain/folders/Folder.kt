package com.ilyakoles.smartnotes.domain.folders

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Folder (
    val folderId: Int,
    val name: String,
    val description: String,
    var countElement: Int,
    val level: Int,
    val parentId: Int
) : Parcelable