package com.zebra.nilac.mx.staticipaddressutility

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.symbol.emdk.EMDKResults
import com.zebra.nilac.emdkloader.interfaces.EMDKManagerInitCallBack
import com.zebra.nilac.emdkloader.interfaces.ProfileLoaderResultCallback
import com.zebra.nilac.mx.staticipaddressutility.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinder: ActivityMainBinding
    private lateinit var wpaModesAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinder.root)

        wpaModesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            resources.getStringArray(R.array.WPAModes)
        )

        mBinder.wpaModeInput.setAdapter(wpaModesAdapter)
        mBinder.processButton.setOnClickListener {
            processWifiRemovalProfile(mBinder.ssidInput.text.toString())
        }
    }

    private fun processWifiRemovalProfile(ssid: String) {
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
                    EMDKUtility.processProfile(
                        ssid,
                        object : EMDKUtility.ProcessProfileResult {
                            override fun onProcessed(isProcessed: Boolean) {
                                processWifiProfile(
                                    mBinder.ssidInput.text.toString(),
                                    wpaModesAdapter.getPosition(mBinder.wpaModeInput.text.toString()) + 1,
                                    mBinder.wifiPasswordInput.text.toString(),
                                    mBinder.ipAddressInput.text.toString(),
                                    mBinder.gatewayInput.text.toString(),
                                    mBinder.subnetMaskInput.text.toString(),
                                    mBinder.primaryDnsInput.text.toString()
                                )
                            }
                        }
                    )
                }
            })
    }

    private fun processWifiProfile(
        ssid: String,
        wpaMode: Int,
        wifiPassword: String,
        ipAddress: String,
        gateway: String,
        subnetMask: String,
        primaryDns: String
    ) {
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
                    EMDKUtility.processProfile(
                        ssid,
                        wpaMode,
                        wifiPassword,
                        ipAddress,
                        gateway,
                        subnetMask,
                        primaryDns,
                        object : EMDKUtility.ProcessProfileResult {
                            override fun onProcessed(isProcessed: Boolean) {
                                runOnUiThread {
                                    Toast.makeText(
                                        this@MainActivity,
                                        if (isProcessed) {
                                            "Profile has been successfully processed!"
                                        } else {
                                            "Unable to process the profile!"
                                        }, Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    )
                }
            })
    }
}