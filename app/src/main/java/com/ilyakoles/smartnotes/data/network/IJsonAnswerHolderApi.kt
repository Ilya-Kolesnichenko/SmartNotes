package com.ilyakoles.smartnotes.data.network

import com.ilyakoles.smartnotes.data.network.model.AnswerDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface IJsonAnswerHolderApi {
   // @FormUrlEncoded
    @Headers("Content-Type: application/json")
    @POST("/ApiUsers.php?apicall=getuserbydata")
    suspend fun isWrightUser(@Body body: String): Response<AnswerDto>

    @Headers("Content-Type: application/json")
    @POST("/ApiUsers.php?apicall=adduser")
    suspend fun addUser(@Body body: String): Response<AnswerDto>
}