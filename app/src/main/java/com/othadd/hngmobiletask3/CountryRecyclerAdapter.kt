package com.othadd.hngmobiletask3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.othadd.hngmobiletask3.databinding.AlphabetListItemBinding
import com.othadd.hngmobiletask3.databinding.CountryListItemBinding
import com.othadd.hngmobiletask3.models.UICountry

const val ALPHABET = 0
const val COUNTRY = 1

class CountryRecyclerAdapter(val onItemClick: (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataList = mutableListOf<Any>()
    fun submitList(dataList: List<Any>) {
        this.dataList = dataList as MutableList<Any>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ALPHABET -> AlphabetViewHolder(AlphabetListItemBinding.inflate(LayoutInflater.from(parent.context)))
            COUNTRY -> CountryViewHolder(CountryListItemBinding.inflate(LayoutInflater.from(parent.context)))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = dataList[position]){
            is String -> (holder as AlphabetViewHolder).bind(item)
            is UICountry -> {
                (holder as CountryViewHolder).bind(item)
                holder.itemView.setOnClickListener { onItemClick(item.name) }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataList[position]) {
            is String -> ALPHABET
            else -> COUNTRY
        }
    }

    class AlphabetViewHolder(var binding: AlphabetListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alphabet: String) {
            binding.alphabetTextView.text = alphabet
        }
    }

    class CountryViewHolder(var binding: CountryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(country: UICountry) {
            binding.country = country
        }
    }
}