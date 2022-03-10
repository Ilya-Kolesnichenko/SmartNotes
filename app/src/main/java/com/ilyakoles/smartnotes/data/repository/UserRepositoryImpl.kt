package com.ilyakoles.smartnotes.data.repository

import android.support.annotation.NonNull
import android.util.Log
import android.util.Xml
import com.ilyakoles.smartnotes.data.mapper.AnswerMapper
import com.ilyakoles.smartnotes.data.network.NetworkFactory
import com.ilyakoles.smartnotes.data.network.model.AnswerDto
import com.ilyakoles.smartnotes.domain.Answer
import com.ilyakoles.smartnotes.domain.users.IUserRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.xmlpull.v1.XmlSerializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringWriter
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val mapper: AnswerMapper
) : IUserRepository {

    override suspend fun logined(login: String, password: String): Answer? {
        var vResult: Answer? = null

        try {
            var serializer: XmlSerializer = Xml.newSerializer();
            var stringXml = StringWriter()
            serializer.setOutput(stringXml)
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "user_login");
            serializer.startTag("", "login")
            serializer.text(login)
            serializer.endTag("", "login")
            serializer.startTag("", "password")
            serializer.text(password)
            serializer.endTag("", "password")
            serializer.endTag("", "user_login")
            serializer.endDocument();

            val requestBody = stringXml.toString().toRequestBody("text/xml".toMediaTypeOrNull())

            val call = NetworkFactory
                .getXMLApi()
                .isWrightUser(requestBody)

            val answer: AnswerDto? = call.body()

            vResult = answer?.let { mapper.mapDtoModelToEntity(it) }//exe
        } catch (e: Exception) {
            Log.d("IsLogin_Error", e.message.toString())
        }

        return vResult
    }

    override suspend fun createUser(
        login: String, password: String, nicName: String?, lastName: String?,
        firstName: String?, email: String?
    ): Answer? {
        var vResult: Answer? = null

        var serializer: XmlSerializer = Xml.newSerializer();
        var stringXml = StringWriter()
        serializer.setOutput(stringXml)
        serializer.startDocument("UTF-8", true);
        serializer.startTag("", "user_data");
        serializer.startTag("", "login")
        serializer.text(login)
        serializer.endTag("", "login")
        serializer.startTag("", "password")
        serializer.text(password)
        serializer.endTag("", "password")
        serializer.startTag("", "nicname")
        serializer.text(nicName)
        serializer.endTag("", "nicname")
        serializer.startTag("", "lastname")
        serializer.text(lastName)
        serializer.endTag("", "lastname")
        serializer.startTag("", "firstname")
        serializer.text(firstName)
        serializer.endTag("", "firstname")
        serializer.startTag("", "email")
        serializer.text(email)
        serializer.endTag("", "email")
        serializer.endTag("", "user_data")
        serializer.endDocument();

        val requestBody = stringXml.toString().toRequestBody("text/xml".toMediaTypeOrNull())

        try {
            val call = NetworkFactory
                .getXMLApi()
                .addUser(requestBody)

            val answer: AnswerDto? = call.body()

            vResult = answer?.let { mapper.mapDtoModelToEntity(it) }//executeCallAsync(call);
        } catch (e: Exception) {
            Log.d("IsLogin_Error", e.message.toString())
        }

        Log.d("call_good_log", vResult.toString())
        return vResult
    }

    private fun executeCallSync(call: Call<AnswerDto>): Answer? {
        Log.d("call_count", "1")

        val response = call.execute()
        val answer: AnswerDto? = response.body()

        Log.d("call_good", answer?.message ?: "")

        return answer?.let { mapper.mapDtoModelToEntity(it) }
    }

    private fun executeCallAsync(call: Call<AnswerDto>): Answer? {
        var vResult: AnswerDto? = null

        Log.d("call_count", "1")
        call.enqueue(object : Callback<AnswerDto> {
            override fun onResponse(
                @NonNull call: Call<AnswerDto>,
                @NonNull response: Response<AnswerDto>
            ) {
                val answer = response.body()
                Log.d("call_good", answer?.message ?: "")
                vResult = answer
            }

            override fun onFailure(
                @NonNull call: Call<AnswerDto?>,
                @NonNull t: Throwable
            ) {
                Log.d("call_error", t.message.toString())

            }
        })

        return vResult?.let { mapper.mapDtoModelToEntity(it) }
    }
}