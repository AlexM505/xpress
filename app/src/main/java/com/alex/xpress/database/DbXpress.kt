package com.alex.xpress.database

import android.provider.BaseColumns

object DbExpress {

    class ProductEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "products"
            val COLUMN_PRODUCT_ID = "id_product"
            val COLUMN_NAME = "name_product"
            val COLUMN_CANT = "cant_product"
            val COLUMN_ICON = "icon_product"
            val COLUMN_TODAY = "today_product"
            val COLUMN_EXPIRATION_DATE = "expiration_date"
            val COLUMN_STATE = "state"
            val COLUMN_IND_NOTIF = "ind_notification"
        }
    }
}