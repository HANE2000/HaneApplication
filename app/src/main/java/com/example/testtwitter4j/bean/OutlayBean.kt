package com.example.testtwitter4j.bean

import java.math.BigDecimal
import java.util.*

data class OutlayBean (
    var userId: String = "", // TwitterのID（@ 以降の）
    var addedDate: Date = Date(),
    var category: String = "",
    var amount: Long = 0
)