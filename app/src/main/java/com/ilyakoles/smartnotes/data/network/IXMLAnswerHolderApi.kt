package com.ilyakoles.smartnotes.data.network

import androidx.lifecycle.LiveData
import com.ilyakoles.smartnotes.data.network.model.AnswerDto
import com.ilyakoles.smartnotes.data.network.model.FolderDto
import com.ilyakoles.smartnotes.data.network.model.FolderListDto
import com.ilyakoles.smartnotes.domain.folders.Folder
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


interface IXMLAnswerHolderApi {
    // @FormUrlEncoded
    @Headers("Content-type: text/xml")
    @POST("/ApiUsers.php?apicall=getuserbydata")
    suspend fun isWrightUser(@Body body: RequestBody): Response<AnswerDto>

    @Headers("Content-type: text/xml")
    @POST("/ApiUsers.php?apicall=adduser")
    suspend fun addUser(@Body body: RequestBody): Response<AnswerDto>

    @Headers("Content-type: text/xml")
    @POST("/ApiFolders.php?apicall=getfolders")
    suspend fun getFoldersByUser(@Body body: RequestBody): Response<FolderListDto>

    @Headers("Content-type: text/xml")
    @POST("/ApiFolders.php?apicall=getfolderbyid")
    suspend fun getFolderById(@Body body: RequestBody): Response<FolderDto>

    @Headers("Content-type: text/xml")
    @POST("/ApiFolders.php?apicall=savefolder")
    suspend fun saveFolder(@Body body: RequestBody): Response<AnswerDto>

    @Headers("Content-type: text/xml")
    @POST("/ApiFolders.php?apicall=deletefolder")
    suspend fun deleteFolder(@Body body: RequestBody): Response<AnswerDto>
}