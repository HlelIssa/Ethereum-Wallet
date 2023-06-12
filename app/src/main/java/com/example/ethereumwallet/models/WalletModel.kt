package com.example.ethereumwallet.models

import org.web3j.crypto.Credentials

data class WalletModel(
    var name:String,
    var password:String,
    var address:String,
    var balance:String,
    var credential: Credentials,
    var date:String,
)