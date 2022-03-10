package com.ilyakoles.smartnotes.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.connection.ConnectInterceptor.intercept
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

object NetworkFactory {

   // private const val BASE_URL = "http://10.0.2.2:80/"
    private const val BASE_URL = "http://www.ilyakoles.ru/"
   // private const val BASE_URL = "http://www.архангельское-2.рф/"

    //TODO While release in Google Play Change the Level to NONE
    private fun createIntrceptor(): OkHttpClient {
        var interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        var client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()
        return client
    }
    /*    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

   // Только в режиме отладки
       if(BuildConfig.DEBUG){
           loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY );
       }

       OkHttpClient okClient = new OkHttpClient.Builder()
       .addInterceptor(new ResponseInterceptor())
       .addInterceptor(loggingInterceptor)
       .build();*/

    private var mRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        //.addConverterFactory(ScalarsConverterFactory.create())
        .client(createIntrceptor())
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()

    fun getXMLApi(): IXMLAnswerHolderApi {
        return mRetrofit.create(IXMLAnswerHolderApi::class.java)
    }
}


