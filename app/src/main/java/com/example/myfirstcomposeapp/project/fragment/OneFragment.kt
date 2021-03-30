package com.example.myfirstcomposeapp.project.fragment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.canvas_ui.BoxBorderClipShape
import com.example.myfirstcomposeapp.canvas_ui.BoxClipShapes

@Composable
fun OneFragment(modifier: Modifier) {
    //设置滑动
    val scrollLazyState = rememberLazyListState()
    LazyColumn(state = scrollLazyState) {
        //遍历循环内部Item部件
        items(100) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StudyLayoutViews()
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                StudyLayoutViews()
            }
        }
    }
}

@Composable
fun StudyLayoutViews() {
    val imageBitmap: ImageBitmap = ImageBitmap.imageResource(R.drawable.hean_lhc)
    val delectedIcon: ImageBitmap = ImageBitmap.imageResource(R.drawable.delected_icon)
    Box(
        modifier = Modifier
            .clip(BoxBorderClipShape)
            .background(Color(0XFF0DBEBF))
            .clickable(onClick = {})
    ) {
        Box(
            modifier = Modifier
                .padding(0.dp)
                .clip(BoxClipShapes)
                .background(Color(206, 236, 250))
                .border(width = 1.dp, color = Color(0XFF0DBEBF), shape = BoxClipShapes)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(3.dp)
            ) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "w",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(55.dp)
                        .width(55.dp)
                        .background(Color.White, shape = CircleShape)
                        .padding(3.dp)
                        .clip(
                            CircleShape
                        )
                        .shadow(elevation = 150.dp, clip = true)
                )
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(
                        "Container",
                        fontSize = 14.sp,
                        color = Color.Black,
                    )
                    Text(
                        "容器组件",
                        modifier = Modifier.padding(top = 3.dp, bottom = 3.dp),
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                    Text(
                        "123万阅读量",
                        fontSize = 8.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(start = 3.dp, end = 2.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Image(
                        bitmap = delectedIcon,
                        contentDescription = "w",
                        modifier = Modifier
                            .height(16.dp)
                            .shadow(elevation = 150.dp, clip = true)

                    )
                }
            }
        }
    }

}