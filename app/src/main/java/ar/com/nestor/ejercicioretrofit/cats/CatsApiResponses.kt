package ar.com.nestor.ejercicioretrofit.cats


data class CatsBreedsListResponse (
    val breeds : List<CatBreedResponse>
)

data class CatBreedResponse(
    val id : String,
    val name : String ,
    val origin: String
)

data class CatImagesSearchResponse(
    val breeds : List<CatBreedResponse>,
    val height: Int,
    val width: Int,
    val id: String,
    val url: String
)