package com.ilyakoles.smartnotes.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "response")
data class AnswerDto @JvmOverloads constructor(
    @field:Element(name = "error")
    var error: Boolean = true,

    @field:Element(name = "message")
    var message: String = "",

    @field:Element(name = "id")
    var id: Int = 0
)


