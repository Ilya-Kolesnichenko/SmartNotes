package com.ilyakoles.smartnotes.data.network.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "folders")
data class FolderListDto @JvmOverloads constructor(

    @field:ElementList(inline = true, required = false)
    var folders: MutableList<FolderDto>? = null
)