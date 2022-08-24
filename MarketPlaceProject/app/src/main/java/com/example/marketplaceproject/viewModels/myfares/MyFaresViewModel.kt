package com.example.marketplaceproject.viewModels.myfares

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplaceproject.TokenApplication
import com.example.marketplaceproject.models.Order
import com.example.marketplaceproject.repository.Repository
import kotlinx.coroutines.launch

class MyFaresViewModel(private val repository: Repository) : ViewModel() {
    var orders: MutableLiveData<MutableList<Order>> = MutableLiveData()
    var newOrder : MutableLiveData<Order> = MutableLiveData()
    var adapterPosition = 0
    var newOrderID : MutableLiveData<String> = MutableLiveData()
    var modosultNewOrderID = false

    init {
        getOrders()
        newOrder.value = Order()
    }

    fun getOrders() {
        viewModelScope.launch {
            try {
                val response = repository.getOrders(TokenApplication.token, Int.MAX_VALUE)

                orders.value = response.orders

            } catch (e: Exception) {
                Log.d("xxx", "MyFaresViewModel getOrders: ")
            }
        }
    }

    fun addOrder() {
        viewModelScope.launch {
            try {
                val response = repository.addOrder(
                    token = TokenApplication.token,
                    title = newOrder.value!!.title,
                    description = newOrder.value!!.description,
                    price_per_unit = newOrder.value!!.price_per_unit,
                    units = newOrder.value!!.units,
                    owner_username = newOrder.value!!.owner_username
                )

                modosultNewOrderID = true
                newOrderID.value = response.order_id

                Log.d("xxx", "addOrder: Sikeresen hozza lett adva a rendeles!")

            } catch (e: Exception) {
                Log.d("xxx", "Timelineviewmodel addOrder: ")
            }
        }
    }
}