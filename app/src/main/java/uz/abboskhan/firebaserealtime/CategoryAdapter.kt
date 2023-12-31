package uz.abboskhan.firebaserealtime

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.abboskhan.firebaserealtime.databinding.RewItemBinding

class CategoryAdapter(private var mList: List<CountryData>, val c: Context) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private var binding: RewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bing(data: CountryData) {

            Picasso.get().load(data.imageUrl).into(binding.itemImg)

            binding.itemTxt.text = data.country


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = RewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = mList[position]
        val country = currentItem.country
        holder.bing(currentItem)
        holder.itemView.setOnClickListener {
            val i  = Intent(c, MainActivity::class.java)
            i.putExtra("country",country)
            c.startActivity(i)

        }

//        with(holder.binding) {
//            with(mList[position]) {
//                Picasso.get().load(this).into(imageView)
//            }
//        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }


}