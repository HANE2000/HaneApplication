package com.example.testtwitter4j.bean

import java.math.BigDecimal
import java.util.*

data class OutlayBean (
    var addedDate: Date = Date(),
    var category: String = "",
    var amount: Int = 0
)