package com.example.composeunit.ui.compose.home;

/**
 * Created by wangfei44 on 2023/3/24.
 */


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeunit.data.repository.dao.table.ComposeData
import com.example.composeunit.ui.compose.other.canvas.BoxBorderClipShape
import com.example.composeunit.ui.compose.other.canvas.BoxClipShapes
import com.example.lib_common.utils.notNull
import com.example.lib_common.utils.splitEndContent

@Composable
fun HomeItemView(composeData: ComposeData, index: Int, viewModel: HomeViewModel) {
    Box(
        modifier = Modifier
            .clip(BoxBorderClipShape)
            .background(MaterialTheme.colors.primary)
            .shadow(elevation = 33.dp, spotColor = MaterialTheme.colors.primary, ambientColor = Color.Yellow)
            .clickable(onClick = {})
    ) {
        Box(
            modifier = Modifier
                .padding(0.dp)
                .clip(BoxClipShapes)
                .background(MaterialTheme.colors.background)
                .border(width = 1.dp, color = MaterialTheme.colors.primary, shape = BoxBorderClipShape)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(55.dp)
                        .width(55.dp)
                        .align(Alignment.CenterVertically)
                        .border(2.dp, color = Color.White, shape = CircleShape)
                        .shadow(
                            elevation = 1.dp, shape = CircleShape
                        )
                        .background(
                            Color(161, 202, 221, 255), shape = CircleShape
                        )
                ) {
                    Text(
                        composeData.item_title.notNull().splitEndContent(),
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .align(Alignment.Center),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold, fontSize = 15.sp, shadow = Shadow(
                                color = Color(32, 3, 37, 100),
                                offset = Offset(2f, 3f),
                                blurRadius = 3f
                            )
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f)
                ) {
                    Text(
                        composeData.item_title.notNull(),
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 3.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            fontSize = 15.sp,
                            shadow = Shadow(
                                color = Color(42, 7, 48, 100),
                                offset = Offset(2f, 3f),
                                blurRadius = 3f
                            )
                        )
                    )
                    Text(
                        composeData.item_content.notNull(),
                        color = Color(42, 7, 48, 255),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        modifier = Modifier.padding(bottom = 20.dp, end = 20.dp),
                        fontStyle = FontStyle.Italic,
                        style = TextStyle(
                            fontSize = 15.sp, shadow = Shadow(
                                color = Color(42, 7, 48, 100),
                                offset = Offset(2f, 3f),
                                blurRadius = 3f
                            )
                        )
                    )
                }
            }
        }
    }

}
