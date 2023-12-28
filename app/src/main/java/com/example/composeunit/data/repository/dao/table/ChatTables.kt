package com.example.composeunit.data.repository.dao.table

import androidx.room.*

/**
 * Created by wangfei44 on 2023/4/20.
 */

/**
 * 1、消息内容表 chat_content：
 * content_id           program_id   content   content_type   content_is_ai        content_fabulous
 * 一条消息内容id唯一      所属项目id    内容       内容类型        内容是否是ai返回消息    有人点赞
 *
 * 设置外键，当项目被删除，即项目中所有的聊天应该被删除。
 */
@Entity(
    tableName = "chat_content"
)
data class ChatContent(
    @PrimaryKey
    @ColumnInfo(name = "content_id")
    var content_id: String,
    @ColumnInfo(name = "program_id")
    var program_id: String,
    @ColumnInfo(name = "content")
    var content: String = "",
    @ColumnInfo(name = "content_type")
    var content_type: Int = 0,
    @ColumnInfo(name = "content_is_ai")
    var content_is_ai: Int = 0,
    @ColumnInfo(name = "content_fabulous")
    var content_fabulous: Int = 0,
    val errorNet: Boolean = false
) {
    override fun toString(): String {
        return "content_id=$content_id,content=$content,content_type=$content_type"
    }
}

/**
 * 2、消息项目管理表 chat_program：
 * id                                                                 title
 * 消息项目id，对应ChatContent中的program_id,项目被删除内容应该被删除。       项目名称
 * 设置外键，当项目被删除，即项目中所有的聊天应该被删除。
 */
@Entity(
    tableName = "chat_program"
)
data class ChatProgram(
    @PrimaryKey
    @ColumnInfo(name = "program_id")
    var program_id: String,
    @ColumnInfo(name = "title")
    var content: String
)


/**
 * 2、每条消息可以获取有多个答案 chat_contents：
 * id                                                                                                                     content
 * 每条消息id，对应ChatContent中的content_id,ChatContent中content_id被删除，那么同id也需要被全部删除。项目被删除内容应该被删除。       消息内容
 * 设置外键，当项目被删除，即项目中所有的聊天应该被删除。
 */
@Entity(
    tableName = "chat_contents"
)
data class ChatContents(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "content_id")
    var content_id: String,
    @ColumnInfo(name = "content")
    var content: String
)
