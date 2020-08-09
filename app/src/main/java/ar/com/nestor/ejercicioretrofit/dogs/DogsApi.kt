package ar.com.nestor.ejercicioretrofit.dogs

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DogsApi {

    companion object {
        const val BASE_URL = "https://dog.ceo/api/"
        const val SUBBREED_SEPARATOR = "-"
    }

    @GET("breed/{breed}/images")
    suspend fun getDogsByBreedImages(@Path("breed") breed : String) : Response<DogsByBreedImagesResponse>

    @GET("breed/{breed}/{subbreed}/images")
    suspend fun getDogsBySubBreedImages(@Path("breed") breed : String,
                                @Path("subbreed") subbreed : String) : Response<DogsByBreedImagesResponse>

    @GET("breeds/list/all")
    suspend fun getDogsBreedsList() : Response<DogsBreedsListResponse>

    @GET("breed/{breed}/images/random")
    suspend fun getBreedRandomImage(@Path("breed") breed : String) : Response<BreedImageResponse>

    @GET("breed/{breed}/{subbreed}/images/random")
    suspend fun getSubBreedRandomImage(@Path("breed") breed : String,
                               @Path("subbreed") subbreed: String) : Response<BreedImageResponse>
}