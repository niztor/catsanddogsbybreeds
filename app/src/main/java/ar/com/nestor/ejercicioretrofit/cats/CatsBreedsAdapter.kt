package ar.com.nestor.ejercicioretrofit.cats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.com.nestor.ejercicioretrofit.R
import coil.api.clear
import kotlinx.android.synthetic.main.catsbreeds_item.view.*

class CatsBreedsAdapter(
    private var list: List<CatBreed>?,
    private var listener: AdapterListener?
) : RecyclerView.Adapter<CatsBreedsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(breed: CatBreed, listener: AdapterListener?) {
            itemView.catsbreeds_item_name.text = breed.name
            itemView.catsbreeds_item_image.clear()
            listener?.onGetBreedImage(breed, itemView.catsbreeds_item_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.catsbreeds_item, parent, false)
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

    fun setList(list: List<CatBreed>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setListener(listener: AdapterListener?) {
        this.listener = listener
    }

    interface AdapterListener {
        fun onGetBreedImage(breed: CatBreed, view: View)
        fun onBreedSelect(breed: CatBreed)
    }
}