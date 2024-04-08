package com.gettasksdone.gettasksdone.io


import com.gettasksdone.gettasksdone.io.requests.CheckItemRequest
import com.gettasksdone.gettasksdone.io.requests.ContextRequest
import com.gettasksdone.gettasksdone.io.requests.LoginRequest
import com.gettasksdone.gettasksdone.io.requests.NoteRequest
import com.gettasksdone.gettasksdone.io.requests.ProjectRequest
import com.gettasksdone.gettasksdone.io.requests.RegisterRequest
import com.gettasksdone.gettasksdone.io.requests.TagRequest
import com.gettasksdone.gettasksdone.io.requests.TaskRequest
import com.gettasksdone.gettasksdone.io.requests.UpdateUserRequest
import com.gettasksdone.gettasksdone.io.requests.UserDataRequest
import com.gettasksdone.gettasksdone.model.CheckItem
import com.gettasksdone.gettasksdone.model.Context
import com.gettasksdone.gettasksdone.model.Note
import com.gettasksdone.gettasksdone.model.Project
import com.gettasksdone.gettasksdone.model.Tag
import com.gettasksdone.gettasksdone.model.Task
import com.gettasksdone.gettasksdone.model.User
import com.gettasksdone.gettasksdone.model.UserInfo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

fun provideGson(): Gson {
    val builder = GsonBuilder()
    builder.setLenient()
    return builder.create()
}
interface ApiService {
    //---AUTHENTICATION---
    @POST(value = "/auth/login")
    fun postLogin(@Body request: LoginRequest): Call<String>

    @POST(value = "/auth/register")
    fun postRegister(@Body request: RegisterRequest): Call<String>

    //---USER MANAGEMENT---
    @GET(value = "/user/authed")
    fun getAuthedUser(@Header("Authorization") authHeader: String?): Call<User>

    @PATCH(value = "/user/update/{id}")
    fun updateUserCredentials(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?,
        @Body request: UpdateUserRequest
    ): Call<String>
    //---END OF AUTHENTICATION---
    //---USER DATA---
    @GET(value = "/userData/authed")
    fun getUserData(@Header("Authorization") authHeader: String?): Call<UserInfo>

    @POST(value = "/userData/create")
    fun completeRegister(
        @Header("Authorization") authHeader: String?,
        @Body request: UserDataRequest
    ): Call<String>

    @PATCH(value = "/userData/update/{id}")
    fun updateUserData(
        @Path("id") userID: Long,
        @Header("Authorization") authHeader: String?,
        @Body request: UserDataRequest
    ): Call<String>
    //---END OF USER DATA---
    //---TASKS---
    @GET(value = "/task/authed")
    fun getTasks(@Header("Authorization") authHeader: String?): Call<List<Task>>

    @GET(value = "/task/{id}")
    fun findTask(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?
    ): Call<Task>

    @POST(value = "/task/create")
    fun createTask(
        @Header("Authorization") authHeader: String?,
        @Query("ProjectID") projectId: Int, // Aquí se agrega el parámetro ProjectID
        @Body request: TaskRequest
    ): Call<String>

    @PATCH(value = "/task/update/{id}")
    fun updateTask(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?,
        @Body request: TaskRequest
    ): Call<String>

    @DELETE(value = "/task/delete/{id}")
    fun deleteTask(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?
    ): Call<String>

    @PATCH(value = "/task/addTag/{id}")
    fun addTagToTask(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?,
        @Query("TagID") tagId: Long
    ): Call<String>

    @PATCH(value = "/task/removeTag/{id}")
    fun deleteTagFromTask(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?,
        @Query("TagID") tagId: Long
    ): Call<String>
    //---END OF TASKS---
    //---CONTEXTS---
    @GET(value= "/context/authed")
    fun getContexts(@Header("Authorization") authHeader: String?): Call<List<Context>>

    @GET(value= "/context/{id}")
    fun findContext(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?
    ): Call<Context>

    @POST(value = "/context/createContext")
    fun createContext(
        @Header("Authorization") authHeader: String?,
        @Body request: ContextRequest
    ): Call<String>

    @PATCH(value = "/context/update/{id}")
    fun updateContext(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?,
        @Body request: ContextRequest
    ): Call<String>

    @DELETE(value = "/context/delete/{id}")
    fun deleteContext(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?
    ): Call<String>
    //---END OF CONTEXTS---
    //---CHECK ITEMS---
    @GET(value = "/check/authed")
    fun getChecks(@Header("Authorization") authHeader: String?): Call<List<CheckItem>>
    @GET(value= "/check/{id}")
    fun findCheck(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?
    ): Call<CheckItem>
    @POST(value = "/check/create")
    fun createCheck(
        @Header("Authorization") authHeader: String?,
        @Body request: CheckItemRequest,
        @Query("TaskID") taskID: Long
    ): Call<String>

    @PATCH(value = "/check/update/{id}")
    fun updateCheck(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?,
        @Body request: CheckItemRequest
    ): Call<String>
    @DELETE(value = "/check/delete/{id}")
    fun deleteCheck(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?
    ): Call<String>
    //---END OF CHECK ITEMS---
    //---TAGS---
    @GET(value = "/tag/authed")
    fun getTags(@Header("Authorization") authHeader: String?): Call<List<Tag>>
    @GET(value= "/tag/{id}")
    fun findTag(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?
    ): Call<Tag>
    @POST(value = "/tag/createTag")
    fun createTag(
        @Header("Authorization") authHeader: String?,
        @Body request: TagRequest
    ): Call<String>

    @PATCH(value = "/tag/update/{id}")
    fun updateTag(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?,
        @Body request: TagRequest
    ): Call<String>
    @DELETE(value = "/tag/delete/{id}")
    fun deleteTag(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?
    ): Call<String>
    //---END OF TAGS---
    //---NOTES---
    @GET(value = "/note/authed")
    fun getNotes(@Header("Authorization") authHeader: String?): Call<List<Note>>
    @GET(value= "/note/{id}")
    fun findNote(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?
    ): Call<Note>
    @POST(value = "/note/create")
    fun createNote(
        @Header("Authorization") authHeader: String?,
        @Body request: NoteRequest,
        @Query("Target") target: String, //Task/Project
        @Query("ID") id: Long
    ): Call<String>

    @PATCH(value = "/note/update/{id}")
    fun updateNote(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?,
        @Body request: NoteRequest
    ): Call<String>
    @DELETE(value = "/note/delete/{id}")
    fun deleteNote(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?
    ): Call<String>
    //---END OF NOTES---
    //---PROJECTS---
    @GET(value = "/project/authed")
    fun getProjects(@Header("Authorization") authHeader: String?): Call<List<Project>>
    @GET(value= "/project/{id}")
    fun findProject(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?
    ): Call<Project>
    @POST(value = "/project/create")
    fun createProject(
        @Header("Authorization") authHeader: String?,
        @Body request: ProjectRequest
    ): Call<String>

    @PATCH(value = "/project/update/{id}")
    fun updateProject(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?,
        @Body request: ProjectRequest
    ): Call<String>
    @DELETE(value = "/project/delete/{id}")
    fun deleteProject(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?
    ): Call<String>
    @PATCH(value = "/project/addTag/{id}")
    fun addTagToProject(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?,
        @Query("TagID") tagId: Long
    ): Call<String>

    @PATCH(value = "/project/removeTag/{id}")
    fun deleteTagFromProject(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String?,
        @Query("TagID") tagId: Long
    ): Call<String>
    //---END OF PROJECTS---
    //---TOOLS---
    @GET(value = "/api/ping")
    fun ping(): Call<String>
    //---END OF TOOLS---
    //---API CONNECTION SETTINGS---
    companion object Factory {
        private const val BASE_URL = "https://lopezgeraghty.com:8080"
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .build()
            return retrofit.create((ApiService::class.java))
        }
    }
    //---END OF API CONNECTION SETTINGS---
}