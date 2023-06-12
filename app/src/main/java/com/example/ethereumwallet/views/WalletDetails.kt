package com.example.ethereumwallet.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethereumwallet.R
import com.example.ethereumwallet.models.WalletModel
import com.example.ethereumwallet.ui.theme.Black
import com.example.ethereumwallet.ui.theme.DodgerBlue
import com.example.ethereumwallet.ui.theme.ShadeOfAlabaster
import com.example.ethereumwallet.ui.theme.ShadeOfOsloGrey
import com.example.ethereumwallet.util.removeRippleClickEffect


@Composable
fun WalletDetails(
    balance: String,
    address: String,
    wallets: List<WalletModel>,
    onClickTransfer: () -> Unit,
    onClickAddWallet: () -> Unit,
    onWalletCLick: (WalletModel) -> Unit,
    onWalletLongCLick: (String) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(White)
    ) {
        WalletCartView(balance, address)
        WalletTabView(modifier = Modifier.padding(top = 10.dp),
            onClickTransfer = {
                onClickTransfer()
            }, onClickAddWallet = {
                onClickAddWallet()
            })
        WalletCreated(wallets, onCLick = { wallet ->
            onWalletCLick(wallet)
        }, onLongCLick = {address->
            onWalletLongCLick(address)
        })
    }

}


@Composable
private fun WalletCartView(balance: String, address: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 10.dp, end = 10.dp, top = 20.dp),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = DodgerBlue
    ) {
        Column(
            Modifier.padding(20.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.ethereum),
                    style = TextStyle(
                        color = White,
                        fontFamily = FontFamily(
                            Font(R.font.inter_extra_bold)
                        ),
                        fontSize = 30.sp
                    )
                )

                Image(
                    modifier = Modifier
                        .size(60.dp),
                    painter = painterResource(id = R.drawable.ethereum),
                    contentDescription = stringResource(
                        id = R.string.ethereum
                    )
                )
            }

            Text(
                text = stringResource(R.string.current_balance),
                style = TextStyle(
                    color = White,
                    fontFamily = FontFamily(
                        Font(R.font.inter_light)
                    ),
                    fontSize = 20.sp, textAlign = TextAlign.Start
                )
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "$balance ETH",
                style = TextStyle(
                    color = White,
                    fontFamily = FontFamily(
                        Font(R.font.inter_bold)
                    ),
                    fontSize = 30.sp, textAlign = TextAlign.Start
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.BottomStart),
                    text = address,
                    style = TextStyle(
                        color = White,
                        fontFamily = FontFamily(
                            Font(R.font.inter_medium)
                        ),
                        fontSize = 20.sp, textAlign = TextAlign.End
                    )
                )
            }

        }

    }
}

@Composable
private fun WalletTabView(
    modifier: Modifier,
    onClickTransfer: () -> Unit,
    onClickAddWallet: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topEnd = 40.dp, topStart = 40.dp),
        backgroundColor = ShadeOfAlabaster
    ) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabViewItem(icon = R.drawable.wallet, title = R.string.add_wallet) {
                    onClickAddWallet()
                }
                TabViewItem(icon = R.drawable.send, title = R.string.send) {
                    onClickTransfer()
                }
            }
        }
    }
}

@Composable
private fun TabViewItem(icon: Int, title: Int, onCLick: () -> Unit) {
    Column(
        modifier = Modifier.removeRippleClickEffect { onCLick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .width(80.dp)
                .height(70.dp), shape = RoundedCornerShape(20.dp)
        ) {
            Box(Modifier.fillMaxSize()) {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp),
                    painter = painterResource(id = icon),
                    contentDescription = stringResource(id = title)
                )
            }
        }

        Text(
            text = stringResource(title),
            style = TextStyle(
                color = ShadeOfOsloGrey,
                fontFamily = FontFamily(
                    Font(R.font.inter_semi_bold)
                ),
                fontSize = 16.sp, textAlign = TextAlign.Start
            ),
            modifier = Modifier.padding(top = 10.dp)
        )
    }


}

@Composable
private fun WalletCreated(
    wallets: List<WalletModel>,
    onCLick: (WalletModel) -> Unit,
    onLongCLick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(ShadeOfAlabaster),
        shape = RoundedCornerShape(topEnd = 40.dp, topStart = 40.dp),

        ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {
            items(wallets.size) {
                WalletCreatedItem(wallets[it],
                    onCLick = { wallet ->
                        onCLick(wallet)
                    }, onLongCLick = {address->
                        onLongCLick(address)
                    })
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WalletCreatedItem(
    walletModel: WalletModel,
    onCLick: (WalletModel) -> Unit,
    onLongCLick: (String) -> Unit
) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 10.dp, bottom = 10.dp)
            .combinedClickable(onClick = {
                onCLick(walletModel)
            }, onLongClick = {
                onLongCLick(walletModel.address)
            },    interactionSource = remember { MutableInteractionSource() },
                indication = null),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card(
            modifier = Modifier
                .width(90.dp)
                .height(70.dp)
                .padding(start = 10.dp, end = 10.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(Modifier.fillMaxSize()) {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp),
                    painter = painterResource(id = R.drawable.digital_wallet),
                    contentDescription = stringResource(id = R.string.wallet_name)
                )
            }
        }

        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = walletModel.name,
                style = TextStyle(
                    color = ShadeOfOsloGrey,
                    fontFamily = FontFamily(
                        Font(R.font.inter_bold)
                    ),
                    fontSize = 16.sp, textAlign = TextAlign.Start
                ),
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = walletModel.date,
                style = TextStyle(
                    color = ShadeOfOsloGrey,
                    fontFamily = FontFamily(
                        Font(R.font.inter_semi_bold)
                    ),
                    fontSize = 14.sp, textAlign = TextAlign.Start
                ),
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Text(
            modifier = Modifier
                .padding(top = 4.dp, end = 20.dp)
                .fillMaxWidth(),
            text = "${walletModel.balance} ETH",
            style = TextStyle(
                color = Black,
                fontFamily = FontFamily(
                    Font(R.font.inter_bold)
                ),
                fontSize = 18.sp, textAlign = TextAlign.End
            )
        )

    }
}
