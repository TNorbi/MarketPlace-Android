package com.example.marketplaceproject.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketplaceproject.R
import com.example.marketplaceproject.models.Product

class TimelineAdapter(
    private var list: ArrayList<Product>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<TimelineAdapter.DataViewHolder>() {
    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
        val productPriceView: TextView = itemView.findViewById(R.id.product_price_textview)
        val ownerImageView: ImageView = itemView.findViewById(R.id.owner_picture_imageView)
        val ownerNameView: TextView = itemView.findViewById(R.id.owner_name_textview)
        val productNameView: TextView = itemView.findViewById(R.id.product_name_textview)
        private val orderButtonView: Button = itemView.findViewById(R.id.orderButton)

        init {
            itemView.setOnClickListener(this)
            orderButtonView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {

                if (orderButtonView.isPressed) {
                    //hogyha a User megnyomja az "Order Now" gombot, akkor a productot meg fogja tudni vasarolni
                    return
                }

                //hogyha a User ranyom maga a CardView-ra(Productra) akkor meg fogja jeleniteni ennek reszleteit
                Log.d("xxx", "OnDetailsClick adapterben")
                listener.onDetailsClick(position)

            }
        }
    }

    interface OnItemClickListener {
        fun onDetailsClick(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.customer_item_layout, parent, false)

        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentItem = list[position] //itt lekerem a listabol a kurens pozicioban levo elemet

        val priceText =
            currentItem.price_per_unit + " " + currentItem.price_type + "/" + currentItem.amount_type
        holder.ownerNameView.text = currentItem.username
        holder.productNameView.text = currentItem.title
        holder.productPriceView.text = priceText

        val images = currentItem.images
        if (images != null && images.size > 0) {
            Log.d("xxx", "#num_images: ${images.size}")
        }

        Glide.with(this.context)
            .load(R.drawable.ic_bazaar_launcher_foreground)
            .override(200, 200)
            .into(holder.productImageView)

        Glide.with(this.context)
            .load(R.drawable.ic_bazaar_launcher_foreground)
            .override(200, 200)
            .into(holder.ownerImageView)
    }

    override fun getItemCount() = list.size

    // Update the list
    fun setData(newlist: ArrayList<Product>) {
        list = newlist
    }
}