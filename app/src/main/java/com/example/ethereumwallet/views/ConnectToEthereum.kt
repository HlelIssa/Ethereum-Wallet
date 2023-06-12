package com.example.ethereumwallet.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ethereumwallet.R
import com.example.ethereumwallet.ui.theme.Blue
import com.example.ethereumwallet.ui.theme.DarkGrey
import com.example.ethereumwallet.ui.theme.White

@Composable
fun ConnectToEthereum(isConnected:Boolean, reset:()->Unit) {
    val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.ethereum_loading))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LottieAnimation(
            modifier = Modifier.size(300.dp),
            composition = lottieComposition,
            iterations = LottieConstants.IterateForever
        )

        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = stringResource(R.string.connect_to_ethereum_network),
            style = TextStyle(
                color = DarkGrey,
                fontFamily = FontFamily(
                    Font(R.font.inter_bold)
                ),
                fontSize = 20.sp
            )
        )
      if (!isConnected){
          Button(
              onClick = { reset() },
              modifier = Modifier
                  .wrapContentSize()
                  .padding(top = 30.dp, bottom = 20.dp),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(Blue)
          ) {
              Text(
                  text = stringResource(R.string.try_again), style = TextStyle(
                      color = White, fontFamily = FontFamily(
                          Font(R.font.inter_semi_bold)
                      ), fontSize = 22.sp
                  ), modifier = Modifier.padding(8.dp)
              )
          }
      }
    }
}


@Preview
@Composable
fun ConnectToEthereumPreview(){
    ConnectToEthereum(false){}
}


