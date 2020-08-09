package ar.com.nestor.ejercicioretrofit.cats

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import ar.com.nestor.ejercicioretrofit.R
import kotlinx.android.synthetic.main.fragment_cats.view.*

private const val ARG_CATSLIST = "ar.com.nestor.ejercicioretrofit.cats.catslist"
private const val ARG_BREED = "ar.com.nestor.ejercicioretrofit.cats.breed"

class CatsFragment : Fragment() {
    private val TAG = "CatsFragment"

    private var cats: List<CatImage>? = null
    private var breeds: List<CatBreed>? = null
    private var breedSelected: CatBreed? = null

    private var adapter: CatsAdapter? = null
    private var listener: FragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? FragmentListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cats = it.getSerializable(ARG_CATSLIST) as? ArrayList<CatImage>
            breedSelected = it.getSerializable(ARG_BREED) as? CatBreed
        }
        adapter = CatsAdapter(cats)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (breeds == null)
            listener?.onGetCatsBreedsList()
        else
            Log.d(TAG, "onViewCreated: already breeds!")

        initRecycler()
        initSearchView()

        view.catsfragment_searchview.setQuery(breedSelected?.name, false)

        view.catsfragment_searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val breed = breeds?.find { it.name.toLowerCase() == query?.toLowerCase() }
                if (breed != null) {
                    listener?.onGetCatsByBreed(breed)
                    return true
                } else
                    Toast.makeText(activity, "No se encontro la razza indicada", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    fun initRecycler() {
        view?.catsfragment_recycler?.let {
            it.adapter = this.adapter
            it.layoutManager = LinearLayoutManager(it.context)
        }
    }

    fun setCatsList(list: List<CatImage>) {
        Log.d(TAG, "setCatsList: starts")
        cats = list
        adapter?.setList(list)
    }



    fun setCatsBreedsList(list: List<CatBreed>) {
        Log.d(TAG, "setCatsBreedsList: starts!")
        breeds = list
        initSearchView()
    }


    @SuppressLint("RestrictedApi")
    private fun initSearchView() {
        val context = activity
        val list = breeds
        if (context != null && list!= null) {
            view?.let {
                Log.d(TAG, "initSearchView: initSearchView!")
                val searchView = it.catsfragment_searchview
                val autoComplete: SearchView.SearchAutoComplete =
                    searchView.findViewById(androidx.appcompat.R.id.search_src_text)
                val autoCompleteAdapter = ArrayAdapter<CatBreed>(
                    context,
                    android.R.layout.select_dialog_item,
                    list
                )
                autoComplete.threshold = 1
                autoComplete.setAdapter(autoCompleteAdapter)
                autoComplete.setOnItemClickListener { parent, _, position, _ ->
                    val b = parent.adapter.getItem(position) as? CatBreed
                    b?.let {
                        searchView.setQuery(b.name, false)
                        breedSelected = b
                        listener?.onGetCatsByBreed(b)
                    }
                }

            }
        }
    }

    interface FragmentListener {
        fun onGetCatsByBreed(breed: CatBreed)
        fun onGetCatsBreedsList()
    }

    override fun onDestroy() {
        super.onDestroy()
        INSTANCE = null
    }


    companion object {
        var INSTANCE : CatsFragment? = null
        @JvmStatic
        fun newInstance(cats: List<CatImage>?, breed: CatBreed?) =
            CatsFragment().apply {
                INSTANCE = this
                arguments = Bundle().apply {
                    putSerializable(ARG_CATSLIST, cats as? ArrayList<CatImage>)
                    putSerializable(ARG_BREED, breed)
                }
            }
    }
}