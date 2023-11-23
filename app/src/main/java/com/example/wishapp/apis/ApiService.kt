package com.example.wishapp.apis

import com.example.wishapp.models.RequestAddWish
import com.example.wishapp.models.RequestDeleteWish
import com.example.wishapp.models.RequestRegisterOrLogin
import com.example.wishapp.models.RequestUpdateWish
import com.example.wishapp.models.ResponseMessage
import com.example.wishapp.models.UserResponse
import com.example.wishapp.models.Wish
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import java.net.CacheRequest

class Constants {
    companion object {
        private const val BASE_URL = "https://bestwishesserver-production.up.railway.app/api/"
        private const val BASE_URL_BACKUP =  "http://bestwishes-ct274.vercel.app/api/"

        fun getInstance() : ApiService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(ApiService::class.java)
        }
    }
}

interface ApiService {

    @POST("users/register")
    suspend fun registerUser(
        @Body request: RequestRegisterOrLogin
    ) : Response<UserResponse>

    @POST("users/login")
    suspend fun loginUser(
        @Body request : RequestRegisterOrLogin
    ) : Response<UserResponse>

    @GET("wishes")
    suspend fun getWishList() : Response<List<Wish>>

    @POST("wishes")
    suspend fun addWish(
        @Body addWish : RequestAddWish
    ) : Response<ResponseMessage>

    @PATCH("wishes")
    suspend fun updateWish(
        @Body updateWish : RequestUpdateWish
    ) : Response<ResponseMessage>

    @HTTP(method = "DELETE", path = "wishes", hasBody = true)
    suspend fun deletWish(
        @Body deleteWish : RequestDeleteWish
    ): Response<ResponseMessage>
}