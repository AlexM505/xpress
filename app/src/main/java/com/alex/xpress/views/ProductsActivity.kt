package com.alex.xpress.views

import android.app.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.xpress.R
import com.alex.xpress.adapters.ProductoAdapter
import com.alex.xpress.database.DbHelper
import com.alex.xpress.models.Product

import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.toolbar.*
import android.content.DialogInterface
import android.os.Build
import android.widget.EditText
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.alex.xpress.Utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt


class ProductsActivity : AppCompatActivity() {

    lateinit var dbHelper : DbHelper

    private var mBottomSheetDialog: BottomSheetDialog? = null
    var dataListProduct: ArrayList<Product>? = null
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        setSupportActionBar(toolbar)
        title = "Productos"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        dbHelper = DbHelper(this)
        dataListProduct = dbHelper.readAllProducts()

        val recyclerView = findViewById<RecyclerView>(R.id.productos_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?

        val prodAdapter = ProductoAdapter(dataListProduct!!, this)
        recyclerView.adapter = prodAdapter

        fab.setOnClickListener { view ->
            dialogAddProduct(prodAdapter)
        }
    }

    override fun onResume() {
        super.onResume()

        //Bienvenida al usuario, cuando aun no ha agregado productos
        showFabPrompt()
    }


    private fun dialogAddProduct(prodAdapter:ProductoAdapter) {
        val bottomSheetLayout = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

        val etName = bottomSheetLayout.findViewById(R.id.etNombre) as EditText
        val etCant = bottomSheetLayout.findViewById(R.id.etCant) as EditText
        val btnVcto = bottomSheetLayout.findViewById(R.id.btnFechaVcto) as TextView
        val tvFechaVcto = bottomSheetLayout.findViewById(R.id.txtFechaVcto) as TextView
        val newProduct = Product()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            var tiempo = sdf.format(cal.time)
            btnVcto.text = tiempo
            newProduct.expirationDate = tiempo
        }

        btnVcto.setOnClickListener {
            cal = Calendar.getInstance()
            DatePickerDialog(this@ProductsActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        bottomSheetLayout.findViewById<Button>(R.id.button_ok)
            .setOnClickListener{
                if(etName.text.toString() == "")
                    etName.error = "Debe digitar el nombre del producto!"
                else if(etCant.text.toString() == "")
                    etCant.error = "Digite la cantidad que hay en stock"
                else if (newProduct.expirationDate == "")
                    tvFechaVcto.setTextColor(resources.getColor(R.color.colorRed))
                else{
                    newProduct.nameProduct = etName.text.toString()
                    newProduct.cantProduct = Integer.parseInt(etCant.text.toString())
                    newProduct.icon = R.drawable.calendar
                    cal = Calendar.getInstance()
                    newProduct.today = sdf.format(cal.time)
                    newProduct.state = Utils.validateStateProduct(newProduct.expirationDate)
                    newProduct.indNotification = 2

                    dbHelper.insertProduct(newProduct)
                    dataListProduct?.add(newProduct)
                    prodAdapter.notifyItemInserted(R.layout.cardview_productos)
                    mBottomSheetDialog!!.dismiss()
                }
            }

        bottomSheetLayout.findViewById<Button>(R.id.button_close)
            .setOnClickListener { mBottomSheetDialog!!.dismiss() }

        mBottomSheetDialog = BottomSheetDialog(this)
        mBottomSheetDialog!!.setContentView(bottomSheetLayout)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mBottomSheetDialog!!.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        mBottomSheetDialog!!.show()
        mBottomSheetDialog!!.setOnDismissListener{ mBottomSheetDialog = null}
    }

    private fun showFabPrompt(){
        if(dataListProduct!!.size == 0){
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(fab)
                .setPrimaryText("Presioname!")
                .setSecondaryText("Soy un boton flotante y aqui es donde podras agregar nuevos productos :)")
                .setBackButtonDismissEnabled(true)
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_product, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.filter_product -> {
                Toast.makeText(this,"En desarrollo..", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
