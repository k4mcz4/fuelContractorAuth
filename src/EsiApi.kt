package com.fuelContractorAuth

import com.fuelContractorAuth.auth.OAuth2
import com.fuelContractorAuth.dataClasses.esi.WalletBalance
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class EsiApi {

    fun setupRequestUrl(){

    }

    fun getWalletData(): WalletBalance {
        val balance = 1.1



        return WalletBalance(balance)
    }

}