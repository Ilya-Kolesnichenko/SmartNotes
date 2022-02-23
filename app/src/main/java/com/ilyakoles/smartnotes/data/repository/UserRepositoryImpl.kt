package com.ilyakoles.smartnotes.data.repository

import android.support.annotation.NonNull
import android.util.Log
import androidx.lifecycle.Transformations
import com.ilyakoles.smartnotes.data.mapper.AnswerMapper
import com.ilyakoles.smartnotes.data.network.NetworkFactory
import com.ilyakoles.smartnotes.data.network.model.AnswerDto
import com.ilyakoles.smartnotes.domain.Answer
import com.ilyakoles.smartnotes.domain.users.IUserRepository
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val mapper: AnswerMapper
) : IUserRepository {

    override suspend fun logined(login: String, password: String): Answer? {
        var vResult: Answer? = null

        var jsonObj = JSONObject()
        jsonObj.put("login", login)
        jsonObj.put("password", password)

        try {
            val call = NetworkFactory
                .getJSONApi()
                .isWrightUser(jsonObj.toString())

            val answer: AnswerDto? = call.body()

            vResult = answer?.let { mapper.mapDtoModelToEntity(it) }//exe
        } catch (e: JSONException) {
            Log.d("IsLogin_Error", e.message.toString())
        }

        return vResult
    }

    override suspend fun createUser(
        login: String, password: String, nicName: String?, lastName: String?,
        firstName: String?, email: String?
    ): Answer? {
        var vResult: Answer? = null

        var jsonObj = JSONObject()
        jsonObj.put("login", login)
        jsonObj.put("password", password)
        jsonObj.put("nicname", nicName)
        jsonObj.put("lastname", lastName)
        jsonObj.put("firstname", firstName)
        jsonObj.put("email", email)

        try {
            val call = NetworkFactory
                .getJSONApi()
                .addUser(jsonObj.toString())

            val answer: AnswerDto? = call.body()

            vResult = answer?.let { mapper.mapDtoModelToEntity(it) }//executeCallAsync(call);
        } catch (e: JSONException) {
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