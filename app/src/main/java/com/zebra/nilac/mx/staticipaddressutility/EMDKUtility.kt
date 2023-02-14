package com.zebra.nilac.mx.staticipaddressutility

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.symbol.emdk.EMDKResults
import com.zebra.nilac.emdkloader.EMDKLoader
import com.zebra.nilac.emdkloader.ProfileLoader
import com.zebra.nilac.emdkloader.interfaces.EMDKManagerInitCallBack
import com.zebra.nilac.emdkloader.interfaces.ProfileLoaderResultCallback

object EMDKUtility {

    const val TAG = "EMDKUtility"

    fun initEMDKManager(
        context: Context,
        emdkManagerInitCallBack: EMDKManagerInitCallBack
    ) {
        //Initialising EMDK First...
        Log.i(TAG, "Initialising EMDK Manager")
        EMDKLoader.getInstance().initEMDKManager(context, emdkManagerInitCallBack)
    }

    private fun processProfile(
        ssid: String,
        wpaMode: Int,
        wifiPassword: String,
        ipAddress: String,
        gateway: String,
        subnetMask: String,
        primaryDns: String,
        processProfileResult: ProcessProfileResult
    ) {
        val profile = """
            <characteristic type="ProfileInfo">
                <parm name="created_wizard_version" value="11.0.1" />
            </characteristic>
            <characteristic type="Profile">
                <parm name="ProfileName" value="IPStaticAddress" />
                <parm name="ModifiedDate" value="2023-02-14 16:29:46" />
                <parm name="TargetSystemVersion" value="11.1" />
            
                <characteristic type="Wi-Fi" version="11.1">
                    <parm name="UseRegulatory" value="0" />
                    <parm name="UseDiagnosticOptions" value="0" />
                    <parm name="UseAdvancedOptions" value="0" />
                    <parm name="NetworkAction" value="Add" />
                    <characteristic type="network-profile">
                        <parm name="SSID" value="$ssid" />
                        <parm name="SecurityMode" value="1" />
                        <parm name="WPAModePersonal" value="$wpaMode" />
                        <characteristic type="key-details">
                            <parm name="KeyType" value="Passphrase" />
                            <parm name="ProtectKey" value="0" />
                            <parm name="PassphraseWPAClear" value="$wifiPassword" />
                        </characteristic>
                        <parm name="UseDHCP" value="0" />
                        <characteristic type="ip-details">
                            <parm name="IpAddress" value="$ipAddress" />
                            <parm name="IpGateway1" value="$gateway" />
                            <parm name="IpMask" value="$subnetMask" />
                            <parm name="IpDns1" value="$primaryDns" />
                        </characteristic>
                        <parm name="UseProxy" value="0" />
                    </characteristic>
                    <parm name="UseHotspotOptions" value="0" />
                </characteristic>
            </characteristic>
        """

        ProfileLoader().processProfile(
            "IPStaticAddress",
            profile,
            object : ProfileLoaderResultCallback {
                override fun onProfileLoadFailed(errorObject: EMDKResults) {
                    //Nothing to see here..
                }

                override fun onProfileLoadFailed(message: String) {
                    Log.e(TAG, "Failed to process profile!\n$message")
                    processProfileResult.onProcessed(false)
                }

                override fun onProfileLoaded() {
                    processProfileResult.onProcessed(true)
                }
            })
    }

    private interface ProcessProfileResult {
        fun onProcessed(isProcessed: Boolean)
    }
}