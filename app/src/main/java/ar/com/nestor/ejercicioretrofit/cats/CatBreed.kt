package ar.com.nestor.ejercicioretrofit.cats

import java.io.Serializable

data class CatBreed (
    val id: String,
    val name: String,
    val origin: String
) : Serializable {

    override fun toString() : String {
        return name
    }
}

