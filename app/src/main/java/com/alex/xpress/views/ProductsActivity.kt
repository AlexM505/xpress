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
import android.widget.EditText
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.alex.xpress.Utils.Utils
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt


class ProductsActivity : AppCompatActivity() {

    lateinit var dbHelper : DbHelper

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
            var notf = dialogAddProduct()
            if(notf == 0)
                prodAdapter.notifyItemInserted(R.layout.cardview_productos)
        }
    }

    override fun onResume() {
        super.onResume()

        //Bienvenida al usuario, cuando aun no ha agregado productos
        showFabPrompt()
    }


    fun dialogAddProduct():Int {
        var notf = 1
        val li = LayoutInflater.from(applicationContext)
        val promptsView = li.inflate(R.layout.dialog_add_product, null)

        val alertDialogBuilder = AlertDialog.Builder(
            this@ProductsActivity
        )

        alertDialogBuilder.setView(promptsView)

        val newProduct = Product()

        val etName = promptsView.findViewById(R.id.etNombre) as EditText
        val etCant = promptsView.findViewById(R.id.etCant) as EditText
        val btnVcto = promptsView.findViewById(R.id.btnFechaVcto) as TextView

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

        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton("Agregar") { dialog, id ->
                if(etName.text.toString() == "")
                    Utils.dialogStandar(this@ProductsActivity,"Advertencia","El producto que trato de registrar no cuenta con todos los parametros, intente nuevamente ingresando todo lo solicitado.")
                else if(etCant.text.toString() == "")
                    Utils.dialogStandar(this@ProductsActivity,"Advertencia","El producto que trato de registrar no cuenta con todos los parametros, intente nuevamente ingresando todo lo solicitado.")
                else if (newProduct.expirationDate == "")
                    Utils.dialogStandar(this@ProductsActivity,"Advertencia","El producto que trato de registrar no cuenta con todos los parametros, intente nuevamente ingresando todo lo solicitado.")
                else{
                    newProduct.nameProduct = etName.text.toString()
                    newProduct.cantProduct = Integer.parseInt(etCant.text.toString())
                    newProduct.icon = R.drawable.dateevent
                    cal = Calendar.getInstance()
                    newProduct.today = sdf.format(cal.time)
                    newProduct.state = Utils.validateStateProduct(newProduct.expirationDate)
                    newProduct.indNotification = 2

                    dbHelper.insertProduct(newProduct)
                    dataListProduct?.add(newProduct)
                    notf = 0
                }
            }
            .setNegativeButton("Cancelar",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        if(dataListProduct!!.size == 0){
            Utils.dialogStandar(this@ProductsActivity, "Perfecto", "Aqui tienes que ingresar el nombre del producto, la cantidad que hay en stock de productos que vencen la misma fecha y su fecha de vencimiento. Adelante!")
        }

        return notf
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
