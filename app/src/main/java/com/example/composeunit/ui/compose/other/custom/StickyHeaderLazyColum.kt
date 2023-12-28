package com.example.composeunit.ui.compose.other.custom


import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeunit.R
import com.example.composeunit.ui.compose.other.custom.Row
import com.example.composeunit.ui.compose.other.custom.StickyHeaderBean
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.sourceforge.pinyin4j.PinyinHelper
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

@Preview
@Composable
fun StickyHeaderLazyColum() {
    val context = LocalContext.current
    val stickyHeaderState = rememberStickyHeaderState()
    val data = getLinkMap(context)
    stickyHeaderState.setData(data)
    Scaffold {
        Column(Modifier.padding(it)) {
            StickyHeaderSearch {
            }
            Box(contentAlignment = Alignment.TopEnd) {
                LazyColumLeftUI(data, stickyHeaderState)
                LazyColumRightUI(data, stickyHeaderState)
            }
        }
    }


}

@Composable
fun rememberStickyHeaderState(
    state: LazyListState = LazyListState(),
    hashMap: HashMap<String, MutableList<Row>> = HashMap()
): StickyHeaderState {
    return remember(state) {
        StickyHeaderState(
            state,
            hashMap
        )
    }
}

class StickyHeaderState(
    val state: LazyListState = LazyListState(),
    private var hashMap: HashMap<String, MutableList<Row>>
) {
    val selectedIndex = mutableIntStateOf(0)
    fun setData(data: HashMap<String, MutableList<Row>>) {
        this.hashMap = data
    }

    suspend fun scrollToItem(initial: String) {
        val (sum, indexOfSelf) = getLeftHeaderIndexByChar(hashMap, initial)
        state.scrollToItem(sum + indexOfSelf)
    }


    private fun getLeftHeaderIndexByChar(
        data: Map<String, MutableList<Row>>,
        initial: String
    ): Pair<Int, Int> {
        val keysBeforeList =
            data.keys.takeWhile { it != initial } // 获取输入字母之前的键
        val sum = keysBeforeList.sumOf { data[it]?.size ?: 0 } // 计算目标之前value数量的总和
        //0对应加 0、1加1、2加2。所以获取目标索引相加即可
        val indexOfSelf = data.keys.indexOf(initial) // 获取输入字母之前的键
        return Pair(sum, indexOfSelf)
    }

    fun setIndexSelected(findIndex: Int) {
        selectedIndex.intValue = findIndex
    }

    fun setIndexCallBack(callBack: (selectedIndex: Int) -> Unit) {
        callBack.invoke(selectedIndex.intValue)
    }

}

suspend fun PointerInputScope.detectTapAndMoveGestures(
    onDown: ((Offset) -> Unit)? = null,
    onMove: ((Offset) -> Unit)? = null,
) = coroutineScope {
    //不断等待获取屏幕事件
    while (true) {
        val downPointer = awaitPointerEventScope {
            awaitFirstDown()
        }
        onDown?.invoke(downPointer.position)
        val movePointer = awaitPointerEventScope {
            drag(downPointer.id, onDrag = { movePointer ->
                onMove?.invoke(movePointer.position)
            })
        }
    }
}

@Composable
private fun LazyColumRightUI(data: Map<String, MutableList<Row>>, stickyState: StickyHeaderState) {
    val scope = rememberCoroutineScope()
    val pxHeight = with(LocalDensity.current) {
        30.dp.roundToPx()
    }
    val indexTouch = remember { mutableIntStateOf(0) }
    stickyState.setIndexCallBack {
        indexTouch.intValue = it
    }
    LazyColumn(
        Modifier
            .padding(end = 10.dp)
            .width(40.dp)
            .pointerInput(Unit) {
                detectTapAndMoveGestures(onDown = {
                    val selectedIndex = (it.y / pxHeight).toInt()
                    if (selectedIndex in 0 until data.size) {
                        indexTouch.intValue = selectedIndex
                        scope.launch {
                            stickyState.scrollToItem(
                                data.toList()[selectedIndex].first
                            )
                        }
                    }
                }, onMove = {
                    val selectedMoveIndex = (it.y / pxHeight).toInt()
                    if (selectedMoveIndex in 0 until data.size) {
                        indexTouch.intValue = selectedMoveIndex
                        scope.launch {
                            stickyState.scrollToItem(
                                data.toList()[selectedMoveIndex].first
                            )
                        }
                    }
                })
            }, userScrollEnabled = false
    ) {
        data.onEachIndexed { index, initial ->
            item {
                Box(
                    Modifier
                        .height(30.dp)
                        .width(30.dp)
                        .padding(end = if (indexTouch.intValue == index && indexTouch.intValue in 1 until data.size - 1) 10.dp else 0.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = initial.key,
                        color = if (index == indexTouch.intValue) Color.Blue else Color.Black,
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }
            }
        }
    }
}


@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun LazyColumLeftUI(data: Map<String, MutableList<Row>>, stickyState: StickyHeaderState) {
    val uiUpdate = remember {
        derivedStateOf {
            if (stickyState.state.isScrollInProgress && stickyState.state.layoutInfo.visibleItemsInfo.isNotEmpty()) {
                val stickyIndex = stickyState.state.layoutInfo.visibleItemsInfo[0].index
                if (stickyIndex < maxSelectedIndex(data, data.size - 1)) {
                    val findIndex = rightTopSelectedIndex(data, stickyIndex)
                    stickyState.setIndexSelected(findIndex)
                    findIndex
                }else{
                    null
                }
            } else {
                null
            }
        }
    }
    uiUpdate.value?.toString()
    LazyColumn(state = stickyState.state) {
        data.forEach { (initial, contactsForInitial) ->
            stickyHeader {
                StickyHeaderTop(initial)
            }

            items(contactsForInitial.size) { contact ->
                StickyHeaderItem(contactsForInitial, contact)
            }
        }
    }
}


fun maxSelectedIndex(cities: Map<String, MutableList<Row>>, index: Int): Int {
    val keyAtIndex = cities.keys.elementAtOrNull(index) // 获取指定索引的键
    val keysBeforeInput = cities.keys.takeWhile { it != keyAtIndex } // 获取输入字母之前的键
    val sum = keysBeforeInput.sumOf { cities[it]?.size ?: 0 } // 计算元素数量的总和
    return sum + (keysBeforeInput.size + 1) // 返回总和加上指定键所在位置
}


fun rightTopSelectedIndex(
    cities: Map<String, MutableList<Row>>,
    stickyIndex: Int
): Int {
    var countSum = 0
    var currentCharIndex = 0

    cities.forEach { (_, rows) ->
        if (countSum == stickyIndex) {
            return currentCharIndex
        }

        countSum += 1 + rows.size
        currentCharIndex++
    }

    return currentCharIndex
}

@Composable
private fun StickyHeaderTop(initial: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color(0xFFE0E2E2))
            .padding(start = 10.dp).clickable {

            },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            initial,
            color = Color.Black
        )
    }
}

@Composable
private fun StickyHeaderItem(
    contactsForInitial: MutableList<Row>,
    contact: Int
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { }
    )
    Column(
        modifier = Modifier
            .background(Color.White)
            .clickable {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(vertical = 10.dp, horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StickyHeaderLeftItem(contactsForInitial, contact)
            StickyHeaderRightItem(contactsForInitial, contact)
        }
        Divider(
            Modifier
                .fillMaxWidth()
                .background(Color.White.copy(0.5f))
                .height(3.dp)
        )
    }
}

@Composable
private fun StickyHeaderRightItem(
    contactsForInitial: MutableList<Row>,
    contact: Int
) {
    Column(
        Modifier
            .height(80.dp)
            .padding(start = 10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Text(contactsForInitial[contact].name)
            Text(
                contactsForInitial[contact].phoneNumber ?: "123232312",
                color = Color.Gray
            )
        }
        Text(
            contactsForInitial[contact].place ?: "北京市昌平区医院",
            color = Color.Gray
        )
    }
}

@Composable
private fun StickyHeaderLeftItem(
    contactsForInitial: MutableList<Row>,
    contact: Int
) {
    Box(
        Modifier
            .size(60.dp)
            .background(Color(0xFF05A045), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            contactsForInitial[contact].name[0].toString(),
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

fun getLinkMap(context: Context): HashMap<String, MutableList<Row>> {
    val jsonResult = loadJSONFromAsset(context, "json_file.json")
    val stickBean = Gson().fromJson(jsonResult, StickyHeaderBean::class.java)
    val originalHashMap = HashMap<String, MutableList<Row>>()
    stickBean.rows.forEach { row ->
        val char = PinyinHelper.toHanyuPinyinStringArray(row.name.first())
        //获取首字母例如：luhenchang 结果为l
        val key = char[0][0].uppercase()
        //进行添加到Map中，如果存在，存储到当前key对应的value集合中。否则新建key进行存储
        //使用 getOrPut 函数简化添加到 Map 中的逻辑
        originalHashMap.getOrPut(key) { ArrayList() }.add(row)
    }
    //根据字母进行排序
    val sortedLinkedHashMap = originalHashMap
        .toSortedMap(compareBy { it })
        .toMap()
    // 打印结果
    sortedLinkedHashMap.forEach { (key, value) ->
        println("$key: $value")
    }
    return sortedLinkedHashMap as HashMap<String, MutableList<Row>>
}

@Composable
fun StickyHeaderSearch(callBack: () -> Unit) {
    Box(
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 15.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            Modifier
                .background(
                    Color(0xFFE0E2E2),
                    shape = RoundedCornerShape(10.dp)
                )
                .shadow(0.dp, shape = RoundedCornerShape(10.dp))
                .height(35.dp)
                .padding(start = 5.dp)
                .fillMaxWidth()
                .clickable {
                    callBack.invoke()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .height(16.dp),
                painter = painterResource(id = R.drawable.search),
                contentDescription = "",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "搜索",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }

}

fun loadJSONFromAsset(context: Context, fileName: String): String? {
    return try {
        val assetManager = context.assets
        val inputStream: InputStream = assetManager.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, StandardCharsets.UTF_8)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

