package com.alex.xpress.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import com.alex.xpress.R
import com.alex.xpress.Utils.NotificationUtils
import com.alex.xpress.database.DbHelper
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.CirclePromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.CirclePromptFocal


class MainActivity : AppCompatActivity(){

    lateinit var dbHelper : DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "> Xpress"

        dbHelper = DbHelper(this)
        val cantProdNotExp:Int = dbHelper.notifProductsExpired()
        if(cantProdNotExp > 0) {
            NotificationUtils.showTimerExpired(this)
            dbHelper.updateProductIndNotif()
        }

        btnProducto.setOnClickListener {
                val intent = Intent(this, ProductsActivity::class.java)
                startActivity(intent)
        }

        btnProductoExpirado.setOnClickListener {
                val intent = Intent(this, ExpiredActivity::class.java)
                startActivity(intent)
        }

        btnEstadisticas.setOnClickListener {
                Toast.makeText(this, "En desarrollo.. :D", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onResume() {
        super.onResume()

        val prefManager = PreferenceManager.getDefaultSharedPreferences(this)
        if (!prefManager.getBoolean("didShowPrompt", false)) {
            btnProducto.isEnabled = false
            btnProductoExpirado.isEnabled = false
            btnEstadisticas.isEnabled = false
            showGestionPrompt()
        }
    }

    private fun showGestionPrompt(){
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(ivGestion)
                .setPrimaryText("Bienvenido a XPRESS")
                .setSecondaryText("Aqui podras hacer gestiones con los productos, agregarlos, verificar el tiempo que haga falta para caducar, entre otras cosa :D")
                .setBackButtonDismissEnabled(true)
                .setPromptBackground(CirclePromptBackground())
                .setPromptFocal(CirclePromptFocal())
                .setPromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED ||
                        state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){
                        showCaducadosPrompt()
                    }
                }
                .show()
    }

    private fun showCaducadosPrompt(){
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(ivCaducados)
            .setPrimaryText("Productos Caducados")
            .setSecondaryText("Aqui podras ver los productos que ya hayan caducado y tienes que ir a remover de los entantes, vitrinas u otro lugar :(")
            .setBackButtonDismissEnabled(true)
            .setPromptBackground(CirclePromptBackground())
            .setPromptFocal(CirclePromptFocal())
            .setPromptStateChangeListener { prompt, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED ||
                    state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){
                    showEstadisticasPrompt()
                }
            }
            .show()
    }

    private fun showEstadisticasPrompt(){
        val prefManager = PreferenceManager.getDefaultSharedPreferences(this)
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(ivEstadisticas)
            .setPrimaryText("Estadisticas")
            .setSecondaryText("Estamos trabajando en esto :,(")
            .setBackButtonDismissEnabled(true)
            .setPromptBackground(CirclePromptBackground())
            .setPromptFocal(CirclePromptFocal())
            .setPromptStateChangeListener { prompt, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED ||
                        state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){
                    val prefEditor = prefManager.edit()
                    prefEditor.putBoolean("didShowPrompt", true)
                    prefEditor.apply()
                    btnProducto.isEnabled = true
                    btnProductoExpirado.isEnabled = true
                    btnEstadisticas.isEnabled = true
                }
            }
            .show()
    }

}
