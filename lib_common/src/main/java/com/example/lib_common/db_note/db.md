---
theme : qklhk-chocolate
---
https://developer.android.google.cn/jetpack/compose/text?hl=en
## 1、首页数据表
```kotlin
//创建首页文章列表数据表
//key id
//item_title    itme标题
//item_subtitle item副标题
//item_content  item内容
//item_star     星星个数
CREATE TABLE compose_data(
    id INTEGER PRIMARY KEY  AUTOINCREMENT,
    item_title TEXT,
    item_subtitle TEXT,
    item_content TEXT,
    item_star INTEGER
);
INSERT INTO compose_data(item_title,item_subtitle,item_content,item_star) VALUES ("Text","Text","用于显示常用文字组件。拥有的属性非常多,足够满足你的使用需求,核心样式由style决定和控制",5)
```
