package com.example.ethereumwallet

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ethereumwallet.models.WalletModel
import com.example.ethereumwallet.util.getCurrentDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.io.File
import java.math.BigDecimal

class MainViewModel : ViewModel() {

    private lateinit var web3: Web3j

    var isConnected = mutableStateOf(true)

    var wallets = mutableStateListOf<WalletModel>()

    var selectedWallet = mutableStateOf<WalletModel?>(null)

    var snackBarState = mutableStateOf(false)

    var snackBarMessage: String = String()

    var bottomSheetCollapse= mutableStateOf(false)


    fun connectToEthNetwork(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            web3 =
                Web3j.build(HttpService("https://mainnet.infura.io/v3/API Key"))
            isConnected.value = try {
                //if the client version has an error the user will not gain access if successful the user will get connected
                val clientVersion = web3.web3ClientVersion().sendAsync().get()
                if (!clientVersion.hasError()) {
                    showMessage(message = context.getString(R.string.connected))
                    true
                } else {
                    showMessage(message = clientVersion.error.message)
                    false
                }
            } catch (e: Exception) {
                showMessage(message = context.getString(R.string.error_message))
                false
            }
        }
    }

    fun createFileWalletDetails(name: String, password: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(context.filesDir.toString() + name) // the Ethereum wallet location

            //create the directory if it does not exist
            if (!file.exists()) {
                file.mkdirs()
                createWallet(password = password, file = file,context=context) { credentials ->
                    val walletCreated = WalletModel(
                        name = name,
                        password = password,
                        balance = retrieveBalance(credentials.address,context),
                        address = credentials.address,
                        credential = credentials,
                        date = getCurrentDate()
                    )
                    wallets.add(walletCreated)
                    selectedWallet.value = walletCreated
                    showMessage(context.getString(R.string.wallet_created))
                    bottomSheetCollapse.value=true
                }
            } else {
                showMessage(context.getString(R.string.directory_already_created))
            }
        }
    }

    private fun createWallet(
        password: String,
        file: File,
        context: Context,
        walletCreated: (Credentials) -> Unit
    ) {
            try {
                // generating the ethereum wallet
                val walletName = WalletUtils.generateLightNewWalletFile(password, file)
                val credentials = WalletUtils.loadCredentials(password, "$file/$walletName")
                walletCreated(credentials)
            } catch (e: Exception) {
                showMessage(context.getString(R.string.failed))
            }
    }

    fun makeTransaction(
        walletAddress: String,
        amount: Double,
        context: Context,
        credentials: Credentials?
    ) {
        // get the amount of eth value the user wants to send
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val receipt = Transfer.sendFunds(
                    web3,
                    credentials,
                    walletAddress,
                    BigDecimal.valueOf(amount),
                    Convert.Unit.ETHER
                ).send()
                showMessage(context.getString(R.string.transaction_successful)+ receipt.transactionHash)
                bottomSheetCollapse.value=true
            } catch (e: java.lang.Exception) {
                showMessage(context.getString(R.string.low_balance))
            }
        }
    }

    private fun retrieveBalance(walletAddress: String, context: Context): String {
        //get wallet's balance
        return try {
            val balanceWei = web3.ethGetBalance(
                walletAddress,
                DefaultBlockParameterName.LATEST
            ).sendAsync().get()
            balanceWei.balance.toString()
        } catch (e: Exception) {
            showMessage(context.getString(R.string.balance_failed))
            String()
        }
    }

    private fun showMessage(message: String,delay:Long=4000) {
       viewModelScope.launch {
           snackBarMessage = message
           snackBarState.value = true
           delay(delay)
           snackBarState.value = false
       }

    }
}