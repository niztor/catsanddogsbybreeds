package ar.com.nestor.ejercicioretrofit.dogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ar.com.nestor.ejercicioretrofit.R
import coil.api.load
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.*

class DogsActivity
    : AppCompatActivity(),
    DogsFragment.FragmentListener,
    DogsBreedsAdapter.AdapterListener,
    DogsBreedsFragment.FragmentListener {
    private val TAG = "MainActivity"

    private lateinit var retrofit : Retrofit
    private lateinit var dogsApi : DogsApi

    private var breeds : List<String>? = null
    private var breedsImages = mutableMapOf<String,String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dogs)

        retrofit = Retrofit
            .Builder()
            .baseUrl(DogsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        dogsApi = retrofit.create(DogsApi::class.java)

        if (savedInstanceState == null) {
            initialize()
        }
    }

    private fun initialize() {
        launchFragment(DogsBreedsFragment.newInstance(breeds))
        getDogsBreedsList()
    }

    private fun launchFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val current = fm.findFragmentById(R.id.dogs_frame_container)
        if (current == null) {
            fm.beginTransaction()
                .replace(R.id.dogs_frame_container, fragment)
                .commit()
        } else {
            fm.beginTransaction()
                .hide(current)
                .add(R.id.dogs_frame_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, msg, duration).show()
    }


    /*
     Aqui use Coroutines
     */
    private fun getBreedImage(breed: String, view: View) = MainScope().launch {
        val respApi =
            try {
                if (breed.contains(DogsApi.SUBBREED_SEPARATOR)) {
                    breed.split(DogsApi.SUBBREED_SEPARATOR).let {
                        dogsApi.getSubBreedRandomImage(it[0], it[1])
                    }
                } else
                    dogsApi.getBreedRandomImage(breed)
            } catch (e: Exception) {
                showToast("Error loading $breed")
                return@launch
            }


        if (!respApi.isSuccessful) {
            showToast("Call not succesfull ${respApi.code()}")
            return@launch
        }

        val resp = respApi.body()
        resp?.let {
            if (it.status == "success") {
                breedsImages[breed] = it.message
                (view as ImageView).load(it.message) {
                    crossfade(true)
                    placeholder(R.drawable.progress_animation)
                }
            } else
                showToast("Error loading $breed ${it.status}")
        }
    }



    /*
       No use Coroutines aqui, sino Callbacks. Solo estoy probando.
     */
    private fun getDogsBreedsList() = MainScope().launch() {

        val respApi = try {
            dogsApi.getDogsBreedsList()
        } catch(e: Exception) {
            showToast("Error on call.. ${e.message}" )
            e.printStackTrace()
            return@launch
        }

        if (!respApi.isSuccessful) {
            showToast("Call not succesfull ${respApi.code()}")
            return@launch
        }

        val list = mutableListOf<String>()
        val resp = respApi.body()
        resp?.let {
            if (it.status == "success") {
                val breeds = it.message.keySet()
                for (breed in breeds) {
                    list.add(breed.toString())
                    val subBreeds = it.message.getAsJsonArray(breed)
                    if (subBreeds.size() > 0)
                        for (subbreed in subBreeds)
                            list.add(breed + DogsApi.SUBBREED_SEPARATOR + subbreed.asString)
                }
            } else
                showToast("Response: ${it.status}")
        }
        breeds = list
        DogsFragment.INSTANCE?.setDogsBreedsList(list)
        DogsBreedsFragment.INSTANCE?.setDogsBreedsList(list)
    }


    private fun getDogsByBreed(breed: String) = MainScope().launch() {

        val _breed = breed.toLowerCase(Locale.ROOT)

        val respApi = try {
            if (_breed.contains(DogsApi.SUBBREED_SEPARATOR)) {
                _breed.split(DogsApi.SUBBREED_SEPARATOR).let {
                    dogsApi.getDogsBySubBreedImages(it[0], it[1])
                }
            } else
                dogsApi.getDogsByBreedImages(_breed)
        } catch (e: Exception) {
            showToast("Error on Call... ${e.message}")
            e.printStackTrace()
            return@launch
        }

        if (!respApi.isSuccessful) {
            showToast("Call not succesfull ${respApi.code()}")
            return@launch
        }

        val resp = respApi.body()
        resp?.let {
            if (it.status == "success") {
                DogsFragment.INSTANCE?.setDogsList(it.message)
                showToast("Call Sucess!")
            } else
                showToast("Response: ${it.status}")
        }
    }

    /*
        No use Coroutines aqui, sino Callbacks. Solo estoy probando.
    */
/*    private fun getDogsByBreed(breed: String) {

        val _breed = breed.toLowerCase(Locale.ROOT)

        val call = if(_breed.contains(DogsApi.SUBBREED_SEPARATOR)) {
                    _breed.split(DogsApi.SUBBREED_SEPARATOR).let {
                        dogsApi.getDogsBySubBreedImages(it[0], it[1])
                    }
                } else
                    dogsApi.getDogsByBreedImages(_breed)

        call.enqueue(object : Callback<DogsByBreedImagesResponse> {
            override fun onFailure(call: Call<DogsByBreedImagesResponse>, t: Throwable) {
                showToast("Error on Call..." + t.toString())
            }

            override fun onResponse(
                call: Call<DogsByBreedImagesResponse>,
                response: Response<DogsByBreedImagesResponse>
            ) {
                if (!response.isSuccessful) {
                    showToast("Response CODE ${response.code()}")
                    return
                }
                val resp = response.body()

                resp?.let {
                    if (it.status == "success") {
                        DogsFragment.INSTANCE?.setDogsList(resp.message)
                        showToast("Call Sucess!")
                    } else
                        showToast("Response: ${it.status}")
                }
            }
        })
    }*/



    override fun onGetDogsBreedsList() {
        getDogsBreedsList()
    }

    override fun onGetDogsByBreed(breed: String) {
        getDogsByBreed(breed)
    }

    override fun onGetBreedImage(breed: String, view: View) {
        if (breed in breedsImages) {
            (view as ImageView).load(breedsImages[breed]) {
                crossfade(true)
                placeholder(R.drawable.progress_animation)
            }
        } else
            getBreedImage(breed, view)
    }

    override fun onBreedSelect(breed: String) {
        launchFragment(DogsFragment.newInstance(null, breed))
        getDogsByBreed(breed)
    }

}