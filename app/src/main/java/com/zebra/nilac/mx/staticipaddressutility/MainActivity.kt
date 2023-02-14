package com.zebra.nilac.mx.staticipaddressutility

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.symbol.emdk.EMDKResults
import com.zebra.nilac.emdkloader.interfaces.EMDKManagerInitCallBack
import com.zebra.nilac.emdkloader.interfaces.ProfileLoaderResultCallback

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        processProfile()
    }

    private fun processProfile() {
        EMDKUtility.initEMDKManager(DefaultApplication.getInstance(),
            object : EMDKManagerInitCallBack {
                override fun onFailed(message: String) {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to init EMDK Manager!",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onSuccess() {
                    //TODO
                }
            })
    }
}