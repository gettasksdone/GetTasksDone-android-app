package com.gettasksdone.gettasksdone.io


import com.gettasksdone.gettasksdone.io.Requests.CompleteRegisterRequest
import com.gettasksdone.gettasksdone.io.Requests.Contexto
import com.gettasksdone.gettasksdone.io.Requests.CreateTaskRequest
import com.gettasksdone.gettasksdone.io.Requests.LoginRequest
import com.gettasksdone.gettasksdone.io.Requests.RegisterRequest
import com.gettasksdone.gettasksdone.io.Responses.GetContextResponse
import com.gettasksdone.gettasksdone.io.Responses.GetTasksResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

fun provideGson(): Gson {
    val builder = GsonBuilder()
    builder.setLenient()
    return builder.create()
}
interface ApiService {
    @POST(value = "/auth/login")
    fun postLogin(@Body request: LoginRequest): Call<String>

    @POST(value = "/auth/register")
    fun postRegister(@Body request: RegisterRequest): Call<String>

    @POST(value = "/userData/create")
    fun completeRegister(
        @Header("Authorization") authHeader: String?,
        @Body request: CompleteRegisterRequest
    ): Call<String>

    @POST(value = "/task/create")
    fun createTask(
        @Header("Authorization") authHeader: String?,
        @Body request: CreateTaskRequest
    ): Call<String>

    @GET(value = "/task/authed")
    fun getTasks(
        @Header("Authorization") authHeader: String?
    ): Call<GetTasksResponse>

    @GET(value= "/context/authed")
    fun getContexts(
        @Header("Authorization") authHeader: String?
    ): Call<GetContextResponse>

    @POST(value = "/context/createContext")
    fun createContext(
        @Header("Authorization") authHeader: String?,
        @Body request: Contexto
    ): Call<String>


    companion object Factory{
        private const val BASE_URL = "https://lopezgeraghty.com:8080"
        fun create(): ApiService{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .build()
            return retrofit.create((ApiService::class.java))
        }
    }


}