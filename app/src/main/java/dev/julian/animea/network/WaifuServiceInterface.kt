package dev.julian.animea.network

import com.skydoves.sandwich.ApiResponse
import dev.julian.animea.data.WaifuImageResponse
import retrofit2.http.GET

interface WaifuServiceInterface {

    @GET("search?many=true")
    suspend fun getRandomImages() : ApiResponse<WaifuImageResponse>

}