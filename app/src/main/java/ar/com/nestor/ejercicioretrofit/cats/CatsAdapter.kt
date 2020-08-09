package ar.com.nestor.ejercicioretrofit.cats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.com.nestor.ejercicioretrofit.R
import coil.api.clear
import coil.api.load
import kotlinx.android.synthetic.main.cats_item.view.*

class CatsAdapter(
    private var list: List<CatImage>?
) : RecyclerView.Adapter<CatsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(cat: CatImage) {
            itemView.cats_item_image.clear()
            itemView.cats_item_image.load(cat.url) {
                crossfade(true)
                placeholder(R.drawable.progress_animation)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cats_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.let{
            val cat = it[position]
            holder.bind(cat)
        }
    }

    fun setList(list: List<CatImage>) {
        this.list = list
        notifyDataSetChanged()
    }
}