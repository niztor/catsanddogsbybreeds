package ar.com.nestor.ejercicioretrofit.dogs

import com.google.gson.JsonObject

data class DogsByBreedImagesResponse (
    val message : List<String> ,
    val status: String
)

data class DogsBreedsListResponse (
    val message : JsonObject,
    val status : String
)

data class BreedImageResponse (
    val message : String ,
    val status: String
)