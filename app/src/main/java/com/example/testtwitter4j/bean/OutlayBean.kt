package com.example.testtwitter4j.bean

import java.math.BigDecimal
import java.util.*

data class OutlayBean (
    var addedDate: Date = Date(),
    var title: String = "",
    var price: BigDecimal = BigDecimal(0)
)