package ar.com.nestor.ejercicioretrofit.dogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ar.com.nestor.ejercicioretrofit.R
import ar.com.nestor.ejercicioretrofit.StartAnimationsInLayerDrawable
import ar.com.nestor.ejercicioretrofit.StopAnimationsInLayerDrawable
import kotlinx.android.synthetic.main.fragment_dogs_breeds.*
import kotlinx.android.synthetic.main.fragment_dogs_breeds.view.*


private const val ARG_BREEDSLIST = "ar.com.nestor.ejercicioretrofit.dogs.breedslist"


class DogsBreedsFragment : Fragment() {
    private val TAG = "DogsBreedsFragment"
    private var breeds: List<String>? = null
    private var listener: FragmentListener? = null
    private var adapter: DogsBreedsAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? FragmentListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            breeds = it.getSerializable(ARG_BREEDSLIST) as? ArrayList<String>
        }
        adapter = DogsBreedsAdapter(breeds, null)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dogs_breeds, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        if (breeds == null)
            showLoading()
        else
            hideLoading()
    }

    private fun initRecycler() {
        adapter?.setListener(activity as? DogsBreedsAdapter.AdapterListener)
        view?.dogsbreeds_fragment_recycler?.let {
            it.adapter = this.adapter
            it.layoutManager = LinearLayoutManager(it.context)
        }
    }


    fun setDogsBreedsList(list: List<String>) {
        breeds = list
        adapter?.setList(list)
        hideLoading()
    }

    private fun showLoading() {
        StartAnimationsInLayerDrawable(dogsbreeds_fragment_loading.background)
        dogsbreeds_fragment_loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        StopAnimationsInLayerDrawable(dogsbreeds_fragment_loading.background)
        dogsbreeds_fragment_loading.visibility = View.GONE
    }

    interface FragmentListener {
        fun onBreedSelect(breed: String)
    }

    companion object {
        var INSTANCE: DogsBreedsFragment? = null
        @JvmStatic
        fun newInstance(breedsList: List<String>?) =
            DogsBreedsFragment().apply {
                INSTANCE = this
                arguments = Bundle().apply {
                    putSerializable(ARG_BREEDSLIST, breedsList as? ArrayList<String>)
                }
            }
    }
}