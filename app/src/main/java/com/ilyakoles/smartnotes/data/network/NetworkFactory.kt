package com.ilyakoles.smartnotes.data.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object NetworkFactory {

    private const val BASE_URL = "http://10.0.2.2:80/"

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private var mRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun getJSONApi(): IJsonAnswerHolderApi {
        return mRetrofit.create(IJsonAnswerHolderApi::class.java)
    }
}


