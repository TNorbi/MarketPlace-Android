package com.example.marketplaceproject.models

import com.squareup.moshi.JsonClass

data class Order(
    var order_id: String = "",
    var username: String = "",
    var status: String = "",
    var owner_username: String = "",
    var price_per_unit: String = "",
    var units: String = "",
    var description: String = "",
    var title: String = "",
    var creation_time: Long = 0
)

//---------------------------GET ORDERS DATA CLASS--------------------------------------

@JsonClass(generateAdapter = true)
data class GetOrdersResponse(
    var item_count : Int,
    var orders : MutableList<Order>,
    var timestamp : Long
)

//--------------------------------------------------------------------------------------

@JsonClass(generateAdapter = true)
data class AddOrderResponse(
    var creation : String,
    var order_id: String,
    var username: String,
    var status: String,
    var owner_username: String,
    var price_per_unit: String,
    var units: String,
    var description: String,
    var title: String,
    var creation_time: Long
)
