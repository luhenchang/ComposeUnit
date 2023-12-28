package com.example.composeunit.ui.compose.other.custom

data class StickyHeaderBean(
    val code: Int,
    val msg: String,
    val rows: List<Row>,
    val total: Int
)

data class Row(
    val name: String,
    val phoneNumber: String? = "14321221212",
    val place: String? = "北京市昌平区医院"
)
