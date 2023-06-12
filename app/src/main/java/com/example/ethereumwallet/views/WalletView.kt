package com.example.ethereumwallet.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ethereumwallet.R
import com.example.ethereumwallet.models.WalletModel
import com.example.ethereumwallet.ui.theme.Black
import com.example.ethereumwallet.ui.theme.Blue
import com.example.ethereumwallet.ui.theme.DarkGrey
import com.example.ethereumwallet.ui.theme.ShadeOfAlabaster
import com.example.ethereumwallet.ui.theme.ShadeOfOsloGrey
import com.example.ethereumwallet.ui.theme.White
import com.example.ethereumwallet.util.removeRippleClickEffect
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WalletView(
    wallets: List<WalletModel>,
    selectedWallet: WalletModel?,
    bottomSheetCollapse: Boolean,
    addWallet: (walletName: String, walletPassword: String) -> Unit,
    transferETH: (address: String, amount: String) -> Unit,
    onTabViewItemClick: () -> Unit,
    onWalletClick: (WalletModel) -> Unit
) {

    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val localFocus = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    var addOrTransfer by remember { mutableStateOf(0) }
    var address by remember { mutableStateOf("") }

    LaunchedEffect(bottomSheetCollapse) {
        if (bottomSheetCollapse) {
            bottomSheetState.collapse()
            address= String()
        }
    }

    BottomSheetScaffold(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = wallets.isNotEmpty(),
        sheetBackgroundColor = ShadeOfAlabaster,
        sheetElevation = 0.dp,
        sheetContent = {
            AddTransferWalletView(modifier = Modifier.removeRippleClickEffect { localFocus.clearFocus() },
                showArrow=wallets.isNotEmpty(),
                focusRequester = focusRequester,
                animation = if (addOrTransfer == 0) R.raw.create_wallet else R.raw.transfer,
                addOrTransfer = addOrTransfer,
                address = address,
                onClickAddWallet = { name, password ->
                    addWallet(name, password)
                },
                onClickTransfer = { address, amount ->
                    transferETH(address, amount)
                }, closeView = {
                    scope.launch {
                        bottomSheetState.collapse()
                    }
                })

        }) {
        WalletDetails(selectedWallet?.balance ?: "", selectedWallet?.address ?: "",
            wallets = wallets,
            onClickAddWallet = {
                addOrTransfer = 0
                scope.launch {
                    bottomSheetState.expand()
                }
                onTabViewItemClick()
            },
            onClickTransfer = {
                addOrTransfer = 1
                scope.launch {
                    bottomSheetState.expand()
                }
                onTabViewItemClick()
            },
            onWalletCLick = {
                onWalletClick(it)
            },
            onWalletLongCLick = {
                addOrTransfer = 1
                address=it
                scope.launch {
                    bottomSheetState.expand()
                }
            }
        )
    }
}

@Composable
private fun AddTransferWalletView(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    animation: Int,
    showArrow:Boolean,
    address: String,
    addOrTransfer: Int,
    onClickAddWallet: (String, String) -> Unit,
    onClickTransfer: (String, String) -> Unit,
    closeView:()->Unit
) {
    val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(animation))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ShadeOfAlabaster),
        verticalArrangement = Arrangement.Center) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.50f), contentAlignment = Alignment.Center
        ) {
          if (showArrow){
              Icon(
                  painter = painterResource(id = R.drawable.arrow_down_),
                  contentDescription = stringResource(id = R.string.cancel),
                  modifier = Modifier
                      .size(50.dp)
                      .align(Alignment.TopEnd)
                      .padding(10.dp)
                      .removeRippleClickEffect { closeView() }
              )
          }
            LottieAnimation(
                modifier = Modifier.size(300.dp),
                composition = lottieComposition,
                iterations = LottieConstants.IterateForever
            )
        }
        Box(
            Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
        ) {
            when (addOrTransfer) {
                0 -> {
                    AddWallet(
                        focusRequester = focusRequester,

                        ) { name, password ->
                        onClickAddWallet(name, password)
                    }
                }

                1 -> {
                    TransferEthereum(address=address,
                        focusRequester = focusRequester,
                    ) { address, amount ->
                        onClickTransfer(address, amount)
                    }
                }
            }

        }
    }

}


@Composable
private fun AddWallet(
    focusRequester: FocusRequester, onClick: (String, String) -> Unit
) {
    var walletName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WalletTextFields(
            modifier = Modifier.padding(start = 8.dp, top = 20.dp, bottom = 8.dp),
            title = R.string.wallet_name,
            value = walletName,
            hint = R.string.enter_wallet_name,
            keyBoardType = KeyboardType.Text,
            showInput = true,
            focusRequester = focusRequester
        ) { name ->
            walletName = name
        }

        WalletTextFields(
            modifier = Modifier.padding(start = 8.dp, top = 20.dp, bottom = 8.dp),
            title = R.string.password,
            value = password,
            hint = R.string.please_enter_password,
            keyBoardType = KeyboardType.Password,
            showInput = false,
            focusRequester = focusRequester
        ) { passwordValue ->
            password = passwordValue
        }

        Button(
            onClick = { onClick(walletName, password) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 30.dp, bottom = 20.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Blue)
        ) {
            Text(
                text = stringResource(R.string.create_wallet), style = TextStyle(
                    color = White, fontFamily = FontFamily(
                        Font(R.font.inter_semi_bold)
                    ), fontSize = 22.sp
                ), modifier = Modifier.padding(8.dp)
            )
        }
    }

}


@Composable
private fun TransferEthereum(address: String,
    focusRequester: FocusRequester, onClick: (String, String) -> Unit
) {
    var addressValue by remember { mutableStateOf(address) }
    var amount by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WalletTextFields(
            modifier = Modifier.padding(start = 8.dp, top = 20.dp, bottom = 8.dp),
            title = R.string.address,
            value = addressValue,
            hint = R.string.enter_wallet_address,
            keyBoardType = KeyboardType.Text,
            showInput = true,
            focusRequester = focusRequester
        ) { name ->
            addressValue = name
        }

        WalletTextFields(
            modifier = Modifier.padding(start = 8.dp, top = 20.dp, bottom = 8.dp),
            title = R.string.send_ethereum,
            value = amount,
            hint = R.string.enter_eth_value_to_send,
            keyBoardType = KeyboardType.Number,
            showInput = true,
            focusRequester = focusRequester
        ) { passwordValue ->
            amount = passwordValue
        }

        Button(
            onClick = { onClick(addressValue, amount) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 30.dp, bottom = 20.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Blue)
        ) {
            Text(
                text = stringResource(R.string.send), style = TextStyle(
                    color = White, fontFamily = FontFamily(
                        Font(R.font.inter_semi_bold)
                    ), fontSize = 22.sp
                ), modifier = Modifier.padding(8.dp)
            )
        }
    }

}

@Composable
private fun WalletTextFields(
    modifier: Modifier = Modifier,
    title: Int,
    value: String,
    hint: Int,
    keyBoardType: KeyboardType,
    showInput: Boolean,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit
) {
    var showPassword by remember { mutableStateOf(showInput) }
    Text(
        text = stringResource(title), style = TextStyle(
            color = ShadeOfOsloGrey, fontFamily = FontFamily(
                Font(R.font.inter_semi_bold)
            ), fontSize = 16.sp, textAlign = TextAlign.Start
        ), modifier = modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = value,
        onValueChange = { name ->
            onValueChange(name)
        },
        singleLine = true,
        textStyle = TextStyle(
            color = DarkGrey,
            fontFamily = FontFamily(
                Font(R.font.inter_medium)
            ),
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = White, unfocusedBorderColor = White
        ),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            if (!showInput) {
                IconButton(onClick = {
                    showPassword = !showPassword
                }) {
                    if (showPassword) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.eye_open //Open
                            ),
                            contentDescription = stringResource(R.string.show_password),
                            tint = Black
                        )
                    } else {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.eye_closed
                            ),
                            contentDescription = stringResource(R.string.hide_password),
                            tint = Black
                        )
                    }
                }
            }
        },
        placeholder = {
            Text(text = stringResource(hint))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyBoardType
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .focusRequester(focusRequester = focusRequester)
    )
}
