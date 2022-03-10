package com.ilyakoles.smartnotes.data.repository

import android.util.Log
import android.util.Xml
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ilyakoles.smartnotes.data.mapper.AnswerMapper
import com.ilyakoles.smartnotes.data.mapper.FolderMapper
import com.ilyakoles.smartnotes.data.network.NetworkFactory
import com.ilyakoles.smartnotes.data.network.model.AnswerDto
import com.ilyakoles.smartnotes.data.network.model.FolderDto
import com.ilyakoles.smartnotes.domain.Answer
import com.ilyakoles.smartnotes.domain.folders.Folder
import com.ilyakoles.smartnotes.domain.folders.IFolderRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.xmlpull.v1.XmlSerializer
import java.io.StringWriter
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val mapperFolder: FolderMapper,
    private val mapperAnswer: AnswerMapper
) : IFolderRepository {

    override suspend fun getFoldersByUser(userId: Int, parentFolderId: Int, level: Int):
            LiveData<List<Folder>> {
        var serializer: XmlSerializer = Xml.newSerializer();
        var stringXml = StringWriter()
        serializer.setOutput(stringXml)
        serializer.startDocument("UTF-8", true);
        serializer.startTag("", "folder_list");
        serializer.startTag("", "userid")
        serializer.text(userId.toString())
        serializer.endTag("", "userid")
        serializer.startTag("", "parentid")
        serializer.text(parentFolderId.toString())
        serializer.endTag("", "parentid")
        serializer.startTag("", "level")
        serializer.text(level.toString())
        serializer.endTag("", "level")
        serializer.endTag("", "folder_list")
        serializer.endDocument();

        val requestBody = stringXml.toString().toRequestBody("text/xml".toMediaTypeOrNull())

        val call = NetworkFactory
            .getXMLApi()
            .getFoldersByUser(requestBody)

        Log.d("call_folder", call.body().toString())

        var listFolders: MutableLiveData<List<FolderDto>> = MutableLiveData(call.body()?.folders)

        if (call.body()?.folders?.size ?: 0 == 0) return MutableLiveData(listOf())
        else
            return Transformations.map(listFolders) {
                it.map {
                    mapperFolder.mapFolderDtoToEntity(it)
                }
            }
    }

    override suspend fun getParentFolderById(isParent: Int, folderId: Int): LiveData<Folder> {
        var serializer: XmlSerializer = Xml.newSerializer();
        var stringXml = StringWriter()
        serializer.setOutput(stringXml)
        serializer.startDocument("UTF-8", true);
        serializer.startTag("", "folder")
        serializer.startTag("", "isparent")
        serializer.text(isParent.toString())
        serializer.endTag("", "isparent")
        serializer.startTag("", "id")
        serializer.text(folderId.toString())
        serializer.endTag("", "id")
        serializer.endTag("", "folder")
        serializer.endDocument();

        val requestBody = stringXml.toString().toRequestBody("text/xml".toMediaTypeOrNull())

        val call = NetworkFactory
            .getXMLApi()
            .getFolderById(requestBody)

        Log.d("call_folder", call.body().toString())

        var listFolders: MutableLiveData<FolderDto> = MutableLiveData(call.body())

        return Transformations.map(listFolders) {
                mapperFolder.mapFolderDtoToEntity(it)
            }
    }

    override suspend fun saveFolder(id: Int, parentId: Int, name: String, userId: Int,
                                    isShared: Int, description: String, level: Int): Answer? {
        var vResult: Answer? = null

        var serializer: XmlSerializer = Xml.newSerializer();
        var stringXml = StringWriter()
        serializer.setOutput(stringXml)
        serializer.startDocument("UTF-8", true);
        serializer.startTag("", "folder_data");
        serializer.startTag("", "id")
        serializer.text(id.toString())
        serializer.endTag("", "id")
        serializer.startTag("", "parent_id")
        serializer.text(parentId.toString())
        serializer.endTag("", "parent_id")
        serializer.startTag("", "name")
        serializer.text(name)
        serializer.endTag("", "name")
        serializer.startTag("", "user_id")
        serializer.text(userId.toString())
        serializer.endTag("", "user_id")
        serializer.startTag("", "is_shared")
        serializer.text(isShared.toString())
        serializer.endTag("", "is_shared")
        serializer.startTag("", "description")
        serializer.text(description)
        serializer.endTag("", "description")
        serializer.startTag("", "level")
        serializer.text(level.toString())
        serializer.endTag("", "level")
        serializer.endTag("", "folder_data")
        serializer.endDocument();

        val requestBody = stringXml.toString().toRequestBody("text/xml".toMediaTypeOrNull())

        try {
            val call = NetworkFactory
                .getXMLApi()
                .saveFolder(requestBody)

            val answer: AnswerDto? = call.body()

            vResult = answer?.let { mapperAnswer.mapDtoModelToEntity(it) }//executeCallAsync(call);
        } catch (e: Exception) {
            Log.d("IsLogin_Error", e.message.toString())
        }

        Log.d("call_good_log", vResult.toString())
        return vResult
    }

    override suspend fun deleteFolder(id: Int): Answer? {
        var vResult: Answer? = null

        var serializer: XmlSerializer = Xml.newSerializer();
        var stringXml = StringWriter()
        serializer.setOutput(stringXml)
        serializer.startDocument("UTF-8", true);
        serializer.startTag("", "folder_data");
        serializer.startTag("", "id")
        serializer.text(id.toString())
        serializer.endTag("", "id")
        serializer.endTag("", "folder_data")
        serializer.endDocument();

        val requestBody = stringXml.toString().toRequestBody("text/xml".toMediaTypeOrNull())

        try {
            val call = NetworkFactory
                .getXMLApi()
                .deleteFolder(requestBody)

            val answer: AnswerDto? = call.body()

            vResult = answer?.let { mapperAnswer.mapDtoModelToEntity(it) }//executeCallAsync(call);
        } catch (e: Exception) {
            Log.d("IsLogin_Error", e.message.toString())
        }

        Log.d("call_good_log", vResult.toString())
        return vResult
    }

}