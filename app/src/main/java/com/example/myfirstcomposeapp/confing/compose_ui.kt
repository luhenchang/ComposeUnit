package com.example.myfirstcomposeapp.confing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.utils.getBitmap

@Composable
fun MyTopAppBar(mainActions: MainActions, position: Int?) {
    if (position == 0) TopAppBar(Modifier.height(70.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(
                text = "Text",
                Modifier
                    .padding(start = 15.dp)
                    .clickable {
                        mainActions.popCurrenPage()
                    }
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                bitmap = getBitmap(resource = R.drawable.home),
                contentDescription = null,
                Modifier
                    .width(25.dp)
                    .height(25.dp),
            )
            Icon(
                bitmap = getBitmap(resource = R.drawable.android_icon),
                contentDescription = null,
                Modifier
                    .padding(start = 20.dp, end = 15.dp)
                    .width(25.dp)
                    .height(25.dp),
            )
        }
    }
}

/**
 * 画分割线
 */
@Composable
fun AppDivider(top: Dp = 30.dp) {
    Divider(
        Modifier.padding(top = top),
        color = Color(11, 11, 11, 11)
    )
}
