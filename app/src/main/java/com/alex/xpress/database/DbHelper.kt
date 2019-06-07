package com.alex.xpress.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.alex.xpress.models.Product


class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertProduct(product: Product): Boolean {
        val db = writableDatabase

        val values = ContentValues()
//        values.put(DbExpress.ProductEntry.COLUMN_PRODUCT_ID, product.idProduct)
        values.put(DbExpress.ProductEntry.COLUMN_NAME, product.nameProduct)
        values.put(DbExpress.ProductEntry.COLUMN_CANT, product.cantProduct)
        values.put(DbExpress.ProductEntry.COLUMN_ICON, product.icon)
        values.put(DbExpress.ProductEntry.COLUMN_TODAY, product.today)
        values.put(DbExpress.ProductEntry.COLUMN_EXPIRATION_DATE, product.expirationDate)
        values.put(DbExpress.ProductEntry.COLUMN_STATE, product.state)
        values.put(DbExpress.ProductEntry.COLUMN_IND_NOTIF, product.indNotification)

        db.insert(DbExpress.ProductEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteProduct(name: String, cant:Int): Boolean {
        val db = writableDatabase

        val sql = "Delete from "+ DbExpress.ProductEntry.TABLE_NAME +
                " Where "+ DbExpress.ProductEntry.COLUMN_NAME + " = '"+ name +
                "' and " + DbExpress.ProductEntry.COLUMN_CANT + " = " + cant

        db.execSQL(sql)

        return true
    }

    fun readProduct(idProduct: Int): ArrayList<Product> {
        val products = ArrayList<Product>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DbExpress.ProductEntry.TABLE_NAME + " WHERE " + DbExpress.ProductEntry.COLUMN_PRODUCT_ID + "= " + idProduct, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var id: Int
        var name: String
        var cant: Int
        var icon: Int
        var today: String
        var expiration: String
        var state: String
        var indNotification: Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getInt(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_PRODUCT_ID))
                name = cursor.getString(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_NAME))
                cant = cursor.getInt(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_CANT))
                icon = cursor.getInt(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_ICON))
                today = cursor.getString(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_TODAY))
                expiration = cursor.getString(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_EXPIRATION_DATE))
                state = cursor.getString(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_STATE))
                indNotification = cursor.getInt(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_IND_NOTIF))

                products.add(Product(id, name, cant, icon, today, expiration, state, indNotification))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return products
    }

    fun readAllProductsVigentes(): ArrayList<Product> {
        val products = ArrayList<Product>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DbExpress.ProductEntry.TABLE_NAME + " where state = 'VIGENTE';", null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var id: Int
        var name: String
        var cant: Int
        var icon: Int
        var today: String
        var expiration: String
        var state: String
        var indNotification: Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getInt(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_PRODUCT_ID))
                name = cursor.getString(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_NAME))
                cant = cursor.getInt(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_CANT))
                icon = cursor.getInt(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_ICON))
                today = cursor.getString(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_TODAY))
                expiration = cursor.getString(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_EXPIRATION_DATE))
                state = cursor.getString(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_STATE))
                indNotification = cursor.getInt(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_IND_NOTIF))

                products.add(Product(id, name, cant, icon, today, expiration, state, indNotification))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return products
    }

    fun updateProductState(product: Product): Boolean {
        val db = writableDatabase

        val sql = "Update " + DbExpress.ProductEntry.TABLE_NAME +
                " Set "+ DbExpress.ProductEntry.COLUMN_STATE + " = '" + product.state +"', "+
                DbExpress.ProductEntry.COLUMN_IND_NOTIF + " = 1 "+
                " where "+ DbExpress.ProductEntry.COLUMN_PRODUCT_ID + " = "+ product.idProduct

        db.execSQL(sql)

        return true
    }

    fun readAllProductsExpired(): ArrayList<Product> {
        val products = ArrayList<Product>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            val sql = "select * from " + DbExpress.ProductEntry.TABLE_NAME +
                    " where "+ DbExpress.ProductEntry.COLUMN_STATE + " = 'CADUCADO'" +
                    " order by today_product asc"
            cursor = db.rawQuery(sql, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var id: Int
        var name: String
        var cant: Int
        var icon: Int
        var today: String
        var expiration: String
        var state: String
        var indNotification: Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getInt(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_PRODUCT_ID))
                name = cursor.getString(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_NAME))
                cant = cursor.getInt(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_CANT))
                icon = cursor.getInt(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_ICON))
                today = cursor.getString(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_TODAY))
                expiration = cursor.getString(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_EXPIRATION_DATE))
                state = cursor.getString(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_STATE))
                indNotification = cursor.getInt(cursor.getColumnIndex(DbExpress.ProductEntry.COLUMN_IND_NOTIF))

                products.add(Product(id, name, cant, icon, today, expiration, state, indNotification))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return products
    }

    fun notifProductsExpired(): Int {
        var count:Int = 0
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            val sql = "select count(*)\n" +
                    " from products\n" +
                    " where state = 'CADUCADO' and ind_notification = 2"
            cursor = db.rawQuery(sql, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return count
        }

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                count = cursor.getInt(0)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return count
    }

    fun updateProductIndNotif(): Boolean {
        val db = writableDatabase

        val sql = "Update " + DbExpress.ProductEntry.TABLE_NAME +
                " Set "+  DbExpress.ProductEntry.COLUMN_IND_NOTIF + " = 1 "+
                " where state = 'CADUCADO' and ind_notification = 2"

        db.execSQL(sql)

        return true
    }

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "xpress.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DbExpress.ProductEntry.TABLE_NAME+ " (" +
                    DbExpress.ProductEntry.COLUMN_PRODUCT_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DbExpress.ProductEntry.COLUMN_NAME + " TEXT," +
                    DbExpress.ProductEntry.COLUMN_CANT + " INTEGER," +
                    DbExpress.ProductEntry.COLUMN_ICON + " INTEGER," +
                    DbExpress.ProductEntry.COLUMN_TODAY + " TEXT, " +
                    DbExpress.ProductEntry.COLUMN_EXPIRATION_DATE + " TEXT, " +
                    DbExpress.ProductEntry.COLUMN_STATE + " TEXT, " +
                    DbExpress.ProductEntry.COLUMN_IND_NOTIF + " INTEGER);"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DbExpress.ProductEntry.TABLE_NAME
    }

}