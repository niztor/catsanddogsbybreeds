package ar.com.nestor.ejercicioretrofit.dogs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import ar.com.nestor.ejercicioretrofit.R
import kotlinx.android.synthetic.main.fragment_dogs.view.*


private const val ARG_DOGSLIST = "ar.com.nestor.ejercicioretrofit.dogs.dogslist"
private const val ARG_BREED = "ar.com.nestor.ejercicioretrofit.dogs.breed"


class DogsFragment : Fragment() {
    private val TAG = "DogsFragment"
    
    private var dogs: List<String>? = null
    private var breeds: List<String>? = null
    private var breedSelected: String? = null

    private var listener : FragmentListener? = null
    private var adapter : DogsAdapter? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? FragmentListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dogs = it.getSerializable(ARG_DOGSLIST) as? ArrayList<String>
            breedSelected = it.getString(ARG_BREED)
        }
        adapter = DogsAdapter(dogs)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dogs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (breeds == null)
            listener?.onGetDogsBreedsList()

        initRecycler()
        initSearchView()

        view.dogsfragment_searchview.setQuery(breedSelected, false)

        view.dogsfragment_searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    listener?.let {
                        it.onGetDogsByBreed(query)
                        return true
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    fun setDogsList(list: List<String>) {
        dogs = list
        adapter?.setList(list)
    }


    fun setDogsBreedsList(list: List<String>) {
        breeds = list
        initSearchView()
    }

    @SuppressLint("RestrictedApi")
    private fun initSearchView() {
        val context = activity
        val list = breeds
        if (context != null && list!= null) {
            view?.let {
                val searchView = it.dogsfragment_searchview
                val autoComplete: SearchView.SearchAutoComplete =
                    searchView.findViewById(androidx.appcompat.R.id.search_src_text)
                val autoCompleteAdapter = ArrayAdapter<String>(
                    context,
                    android.R.layout.select_dialog_item,
                    list
                )
                autoComplete.threshold = 1
                autoComplete.setAdapter(autoCompleteAdapter)
                autoComplete.setOnItemClickListener { parent, _, position, _ ->
                    val s = parent.adapter.getItem(position) as? String
                    if (s is String) {
                        searchView.setQuery(s, true)
                        breedSelected = s
                    }
                }

            }
        }
    }


    private fun initRecycler() {
        view?.dogsfragment_recycler?.let{
            it.adapter = this.adapter
            it.layoutManager = LinearLayoutManager(it.context)
        }
    }

    interface FragmentListener {
        fun onGetDogsByBreed(breed: String)
        fun onGetDogsBreedsList()
    }



    companion object {
        var INSTANCE : DogsFragment? = null
        @JvmStatic
        fun newInstance(dogsList: List<String>?, breed: String?) =
            DogsFragment().apply {
                INSTANCE = this
                arguments = Bundle().apply {
                    putSerializable(ARG_DOGSLIST, dogsList as? ArrayList<String>)
                    putString(ARG_BREED, breed)
                }
            }
    }


}