package com.ilyakoles.smartnotes.data.network.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "folder")
data class FolderDto @JvmOverloads constructor(
    @field:Element(name = "id")
    var id: Int = 0,

    @field:Element(name = "name")
    var name: String = "",

    @field:Element(name = "description")
    var description: String = "",

    @field:Element(name = "countelem")
    var countelem: Int = 0,

    @field:Element(name = "level")
    var level: Int = 0,

    @field:Element(name = "parentid")
    var parentid: Int = 0
)