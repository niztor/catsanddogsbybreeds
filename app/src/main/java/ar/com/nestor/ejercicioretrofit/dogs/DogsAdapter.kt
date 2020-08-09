package ar.com.nestor.ejercicioretrofit.dogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.com.nestor.ejercicioretrofit.R
import coil.api.load
import kotlinx.android.synthetic.main.dogs_item.view.*

class DogsAdapter(
    private var list : List<String>?
) : RecyclerView.Adapter<DogsAdapter.ViewHolder>()  {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(url: String){
            itemView.dogs_item_image.load(url) {
                placeholder(R.drawable.progress_animation)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.dogs_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.let {
            holder.bind(it[position])
        }
    }

    fun setList(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }
}