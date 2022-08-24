package com.example.marketplaceproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplaceproject.MainActivity
import com.example.marketplaceproject.R
import com.example.marketplaceproject.TokenApplication
import com.example.marketplaceproject.models.Product
import com.example.marketplaceproject.viewholders.MarketRecyclerViewHolder

class MarketAdapter(
    private var list: ArrayList<Product>,
    private val context: Context,
    private val activity : MainActivity,
    private val listener : OnItemClickListener
): RecyclerView.Adapter<MarketRecyclerViewHolder>() {

    interface OnItemClickListener {
        fun onDetailsClick(position: Int)
        fun onDeleteClick(position: Int)
        fun onOrderNowClick(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketRecyclerViewHolder {
       return when(viewType){
            R.layout.owner_item_layout ->{
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.owner_item_layout, parent, false)
                MarketRecyclerViewHolder.OwnerViewHolder(itemView,listener)//itt meghivom az OwnerViewHoldert
            }
            R.layout.customer_item_layout ->{
                val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.customer_item_layout, parent, false)
                MarketRecyclerViewHolder.CustomerViewHolder(itemView,activity,listener)//itt meghivom a CustomerViewHoldert
            }
           else -> throw Exception("MarketAdapter: Nincs lekezelve az adott viewType: $viewType")
       }
    }

    override fun onBindViewHolder(holder: MarketRecyclerViewHolder, position: Int) {
        val currentItem = list[position]

        when(holder){
            is MarketRecyclerViewHolder.CustomerViewHolder -> holder.bind(currentItem)
            is MarketRecyclerViewHolder.OwnerViewHolder -> holder.bind(currentItem)
        }
    }

    override fun getItemCount()= list.size

    override fun getItemViewType(position: Int): Int {
        if(list[position].username == TokenApplication.username) {
            //ha a kurens lista elem termeke az altalunk feltett termek,akkor visszateritjuk az Owner_item_layout-ot!
            return R.layout.owner_item_layout
        }

        //ellenkezo esetben a customer layout-jat teritem vissza (a lista kurens termeke nem a mienk)
        return R.layout.customer_item_layout
    }

    // Update the list
    fun setData(newlist: ArrayList<Product>) {
        list = newlist
    }
}