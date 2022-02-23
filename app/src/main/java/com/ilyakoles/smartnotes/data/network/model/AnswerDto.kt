package com.ilyakoles.smartnotes.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AnswerDto (
    @SerializedName("error")
    @Expose
    val error: Boolean?,

    @SerializedName("message")
    @Expose
    val message: String?,

    @SerializedName("id")
    @Expose
    val id: Int?
)


