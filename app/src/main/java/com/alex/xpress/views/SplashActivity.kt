package com.alex.xpress.views

import android.content.Intent
import android.content.pm.PackageInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.alex.xpress.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val manager = packageManager
        val info: PackageInfo
        info = manager.getPackageInfo(this.packageName, 0)
        textViewVersion.text = "V "+ info.versionName

        //3 segundos - splash time
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        },2000)
    }
}
