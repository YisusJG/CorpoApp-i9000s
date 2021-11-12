package com.corpogas.gogasmanagement.Entities.Helpers


import java.text.DecimalFormat

class CurrencyFormatter() {
    var mFormat: DecimalFormat

    init {
        mFormat = DecimalFormat("$ ###,###,##0.0")
    }
}