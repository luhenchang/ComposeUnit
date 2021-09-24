package com.example.composeunit.project.model.local
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.composeunit.AppDatabase
import com.example.composeunit.RoomAsset
import com.example.composeunit.project.bean.Information

/**
 * Created by wangfei44 on 2021/9/16.
 */
object HomeRepository {
    interface InformationLocalInterface {
        fun onSuccess(information: List<Information>)
        fun onFail(e: String)
    }

    @Composable
    fun getListInformationFromLocalData(informationCallBack: InformationLocalInterface) {
        val db: AppDatabase = RoomAsset.databaseBuilder(LocalContext.current, AppDatabase::class.java, "chinook.db")
                .build()
        val list1 = db.chinookDao().users
        val list2 = db.chinookDao().composeDatas
        var information = ArrayList<Information>()
        information.add(Information(list1[0].name, list1[0].address))
        informationCallBack.onSuccess(information)
    }
}