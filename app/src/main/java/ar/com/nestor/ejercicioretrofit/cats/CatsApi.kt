package ar.com.nestor.ejercicioretrofit.cats

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsApi {

    companion object {
        const val BASE_URL = "https://api.thecatapi.com/v1/"
    }


    @GET("breeds")
    suspend fun getCatsBreedsList() : Response<List<CatBreedResponse>>

    @GET("images/search")
    suspend fun getCatsImagesSearchList(
        @Query("breed_id") breed_id : String,
        @Query("limit") limit: Int?,
        @Query("page") page: Int?
    ) : Response<List<CatImagesSearchResponse>>
}