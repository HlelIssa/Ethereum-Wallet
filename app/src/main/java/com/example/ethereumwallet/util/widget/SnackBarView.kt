package com.example.ethereumwallet.util.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethereumwallet.R
import com.example.ethereumwallet.ui.theme.LightBlack


@Composable
fun SnackBarView(modifier: Modifier=Modifier,message: String, visibility: Boolean, function: () -> Unit) {

    AnimatedVisibility(
        visible = visibility,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Snackbar(
            shape = RoundedCornerShape(16.dp),
            backgroundColor = White,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
            action = {
                function()
            }) {
            Text(modifier = Modifier.padding(top = 8.dp,bottom =8.dp),
                text = message,
                style = TextStyle(
                    color = LightBlack,
                    fontFamily = FontFamily(
                        Font(R.font.inter_bold)
                    ),
                    fontSize = 20.sp,
                ))
        }
    }
}