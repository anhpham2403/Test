package com.example.anh.exchangerate.source.data.local.sqlite

class DBContract {
  class Currency {
    companion object {
      const val COLUMN_CURRENCY_ID = "id"
      const val COLUMN_CURRENCY_NAME = "currencyName"
      const val COLUMN_CURRENCY_SYMBOL = "currencySymbol"
      const val COLUMN_NATION_NAME = "nationName"
      const val COLUMN_TABLE_NAME = "currency"
    }
  }

  class Favorite {
    companion object {
      const val COLUMN_CURRENCY_ID = "id"
    }
  }
}