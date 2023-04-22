package com.example.composeunit.composeble_ui.home;

/**
 * Created by wangfei44 on 2023/3/24.
 */


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.example.composeunit.repository.dao.table.ComposeData
import com.example.composeunit.canvas_ui.BoxBorderClipShape
import com.example.composeunit.canvas_ui.BoxClipShapes
import com.example.composeunit.project.view_model.home.HomeViewModel
import com.example.lib_common.utils.notNull
import com.example.lib_common.utils.splitEndContent

@Composable
fun HomeItemView(composeData: ComposeData, index: Int, viewModel: HomeViewModel) {
    Box(
        modifier = Modifier
            .clip(BoxBorderClipShape)
            .background(Color(0XFF0DBEBF))
            .shadow(elevation = 33.dp, spotColor = Color.Red, ambientColor = Color.Yellow)
            .clickable(onClick = {})
    ) {
        Box(
            modifier = Modifier
                .padding(0.dp)
                .clip(BoxClipShapes)
                .background(Color(206, 236, 250))
                .border(width = 1.dp, color = Color(0XFF0DBEBF), shape = BoxBorderClipShape)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(55.dp)
                        .width(55.dp)
                        .align(Alignment.CenterVertically)
                        .border(2.dp, color = Color(238, 204, 203, 255), shape = CircleShape)
                        .shadow(
                            elevation = 1.dp, shape = CircleShape
                        )
                        .background(
                            Color(13, 189, 190, 193), shape = CircleShape
                        )
                ) {
                    Text(
                        composeData.item_title.notNull().splitEndContent(),
                        fontSize = 18.sp,
                        color = Color.White,
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
                        composeData.item_title.notNull() + index,
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
