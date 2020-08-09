package ar.com.nestor.ejercicioretrofit.cats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ar.com.nestor.ejercicioretrofit.R
import coil.api.load
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CatsActivity : AppCompatActivity() ,
    CatsBreedsAdapter.AdapterListener,
    CatsFragment.FragmentListener {
    private val TAG = "CatsActivity"

    private var breeds = listOf<CatBreed>()
    private var breedsImages = mutableMapOf<String,String>()

    private lateinit var catsApi : CatsApi
    private lateinit var retrofit : Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cats)

        retrofit = Retrofit
            .Builder()
            .baseUrl(CatsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        catsApi = retrofit.create(CatsApi::class.java)

        if (savedInstanceState == null)
            initialize()
    }

    fun initialize() {
        launchFragment(CatsBreedsFragment.newInstance(null))
        getCatsBreedsList()
    }

    private fun launchFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val current = fm.findFragmentById(R.id.cats_frame_container)
        if (current == null) {
            fm.beginTransaction()
                .replace(R.id.cats_frame_container, fragment)
                .commit()
        } else {
            fm.beginTransaction()
                .hide(current)
                .add(R.id.cats_frame_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }


    private fun showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, msg, duration).show()
    }


    private fun getCatsByBreed(breed: CatBreed) = MainScope().launch() {
        val respApi = try {
            catsApi.getCatsImagesSearchList(breed.id, 100, null)
        } catch(e: Exception) {
            showToast("Error loading Cats Images By Breeds: ${e.message}")
            return@launch
        }

        if (!respApi.isSuccessful) {
            showToast("Call not succesfull ${respApi.code()}")
            return@launch
        }

        val list = mutableListOf<CatImage>()
        val resp = respApi.body()
        if ((resp != null) && (resp.size > 0)) {
            for (b in resp)
                list.add(CatImage(b.id, b.height, b.width, b.url))
            CatsFragment.INSTANCE?.setCatsList(list)
        }

    }


    private fun getCatsBreedsList() = MainScope().launch() {
        val respApi =
            try {
                catsApi.getCatsBreedsList()
            } catch(e: Exception) {
                showToast("Error loading Cats Breeds: ${e.message}")
                e.printStackTrace()
                return@launch
            }


        if (!respApi.isSuccessful)
            showToast("Call not succesfull ${respApi.code()}")


        val resp = respApi.body()
        val list = mutableListOf<CatBreed>()
        resp?.let {

            for (breed in it)
                list.add(CatBreed(breed.id, breed.name, breed.origin))
        }
        breeds = list
        CatsBreedsFragment.INSTANCE?.setCatsBreedsList(list)
        CatsFragment.INSTANCE?.setCatsBreedsList(list)

    }



    private fun getBreedImage(breed: CatBreed, view: View) = MainScope().launch() {
        val respApi = try {
            catsApi.getCatsImagesSearchList(breed.id, 1, null)
        } catch(e: Exception) {
            e.printStackTrace()
            showToast("Error loading Cat Breed Image $breed")
            return@launch
        }

        if (!respApi.isSuccessful) {
            showToast("Call not succesfull ${respApi.code()}")
            return@launch
        }

        val resp = respApi.body()
        if ((resp != null) && (resp.size > 0)) {
            val url = resp.get(0).url
            (view as? ImageView)?.let {
                breedsImages[breed.id] = url
                it.load(url) {
                    crossfade(true)
                    placeholder(R.drawable.progress_animation)
                }
            }

        }

    }

    override fun onGetBreedImage(breed: CatBreed, view: View) {
        if (breed.id in breedsImages) {
            (view as ImageView).load(breedsImages[breed.id]) {
                crossfade(true)
                placeholder(R.drawable.progress_animation)
            }
        } else
            getBreedImage(breed, view)
    }

    override fun onBreedSelect(breed: CatBreed) {
        launchFragment(CatsFragment.newInstance(null, breed))
        getCatsByBreed(breed)
    }

    override fun onGetCatsByBreed(breed: CatBreed) {
        getCatsByBreed(breed)
    }

    override fun onGetCatsBreedsList() {
        getCatsBreedsList()
    }
}