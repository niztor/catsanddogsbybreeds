package ar.com.nestor.ejercicioretrofit.cats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ar.com.nestor.ejercicioretrofit.R
import ar.com.nestor.ejercicioretrofit.StartAnimationsInLayerDrawable
import ar.com.nestor.ejercicioretrofit.StopAnimationsInLayerDrawable
import kotlinx.android.synthetic.main.fragment_cats_breeds.*
import kotlinx.android.synthetic.main.fragment_cats_breeds.view.*


private const val ARG_BREEDSLIST = "ar.com.nestor.ejercicioretrofit.cats.breedslist"


class CatsBreedsFragment : Fragment() {

    private var adapter: CatsBreedsAdapter? = null
    private var breeds: List<CatBreed>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            breeds = it.getSerializable(ARG_BREEDSLIST) as? ArrayList<CatBreed>
        }
        adapter = CatsBreedsAdapter(breeds, null)

        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cats_breeds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        if (breeds == null)
            showLoading()
        else
            hideLoading()
    }

    fun initRecycler() {
        adapter?.setListener(activity as? CatsBreedsAdapter.AdapterListener)
        view?.catsbreeds_fragment_recycler?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(activity)
        }
    }

    fun setCatsBreedsList(list: List<CatBreed>) {
        breeds = list
        adapter?.setList(list)
        hideLoading()
    }


    private fun showLoading() {
        StartAnimationsInLayerDrawable(catsbreeds_fragment_loading.background)
        catsbreeds_fragment_loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        StopAnimationsInLayerDrawable(catsbreeds_fragment_loading.background)
        catsbreeds_fragment_loading.visibility = View.GONE
    }


    companion object {
        var INSTANCE : CatsBreedsFragment? = null
        @JvmStatic
        fun newInstance(breeds: List<CatBreed>?) =
            CatsBreedsFragment().apply {
                INSTANCE = this
                arguments = Bundle().apply {
                    putSerializable(ARG_BREEDSLIST, breeds as? ArrayList<CatBreed>)
                 }
            }
    }
}