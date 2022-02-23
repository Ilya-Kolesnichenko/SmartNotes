package com.ilyakoles.smartnotes.data.mapper

import androidx.lifecycle.LiveData
import com.ilyakoles.smartnotes.data.network.model.AnswerDto
import com.ilyakoles.smartnotes.domain.Answer
import javax.inject.Inject

class AnswerMapper @Inject constructor() {

    fun mapDtoModelToEntity(dtoModel: AnswerDto) = Answer(
        error = dtoModel.error,
        message = dtoModel.message,
        id = dtoModel.id
    )

}