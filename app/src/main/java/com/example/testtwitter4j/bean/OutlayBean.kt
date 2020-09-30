package com.example.testtwitter4j.bean

import java.math.BigDecimal
import java.util.*

data class OutlayBean (
    var userId: String = "", // TwitterのID（@ 以降の）
    var addedDate: Date = Date(), // 登録日時
    var category: String = "", // 項目名
    var amount: Long = 0 // 額
)