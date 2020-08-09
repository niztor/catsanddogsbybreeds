package ar.com.nestor.ejercicioretrofit.dogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.com.nestor.ejercicioretrofit.R
import coil.api.clear
import kotlinx.android.synthetic.main.dogsbreeds_item.view.*

class DogsBreedsAdapter(
    private var list: List<String>?,
    private var listener: AdapterListener?
) : RecyclerView.Adapter<DogsBreedsAdapter.ViewHolder>() {

    private val TAG = "BreedsAdapter"

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val TAG = ".ViewHolder"
        fun bind(breed: String, listener: AdapterListener?) {
            itemView.dogsbreeds_item_name.text =
                breed.replace(DogsApi.SUBBREED_SEPARATOR,"\n").capitalize()
            itemView.dogsbreeds_item_image.clear()
            listener?.onGetBreedImage(breed, itemView.dogsbreeds_item_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dogsbreeds_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.let {
            val breed = it[position]
            holder.bind(breed, listener)
            holder.itemView.setOnClickListener {
                listener?.onBreedSelect(breed)
            }
        }
    }

    fun setList(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setListener(listener: AdapterListener?) {
        this.listener = listener
    }

    interface AdapterListener {
        fun onGetBreedImage(breed: String, view: View)
        fun onBreedSelect(breed: String)
    }

}