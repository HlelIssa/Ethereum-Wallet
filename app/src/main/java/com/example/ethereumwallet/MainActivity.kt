package com.example.ethereumwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.ethereumwallet.util.widget.SnackBarView
import com.example.ethereumwallet.views.ConnectToEthereum
import com.example.ethereumwallet.views.WalletView
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (viewModel.isConnected.value) {
                WalletView(
                    wallets = viewModel.wallets,
                    selectedWallet = viewModel.selectedWallet.value,
                    bottomSheetCollapse = viewModel.bottomSheetCollapse.value,
                    onTabViewItemClick = {
                        viewModel.bottomSheetCollapse.value = false
                    },
                    addWallet = { name, password ->
                        viewModel.createFileWalletDetails(
                            name = name,
                            context = this,
                            password = password
                        )
                    },
                    transferETH = { address, amount ->
                        viewModel.makeTransaction(
                            walletAddress = address,
                            amount = amount.toDouble(),
                            context = this,
                            viewModel.selectedWallet.value?.credential
                        )
                    }
                ) { selectedWallet ->
                    viewModel.selectedWallet.value = selectedWallet
                }
            } else {
                ConnectToEthereum(viewModel.isConnected.value){
                    viewModel.connectToEthNetwork(this)
                }
            }
            SnackBarView(
                message = viewModel.snackBarMessage,
                visibility = viewModel.snackBarState.value
            ) {}
        }
        initializeValues()
        setupBouncyCastle()
        viewModel.connectToEthNetwork(this)

    }

    private fun initializeValues() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }


    //set up the security provider
    private fun setupBouncyCastle() {
        val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up a provider when it's used for the first time.
            return
        if (provider.contains(BouncyCastleProvider::class.java)) {
            return
        }
        //There is a possibility  the bouncy castle registered by android may not have all ciphers
        //so we  substitute with the one bundled in the app.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }

}