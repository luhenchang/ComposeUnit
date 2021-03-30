---
theme : qklhk-chocolate
---
## 1、首页数据表
```js
//创建首页文章列表数据表
//key id
//item_title    itme标题
//item_subtitle item副标题
//item_content  item内容
//item_star     星星个数
CREATE TABLE compose_data(
id int id key  auto_increment,
item_title varchar(20),
item_subtitle varchar(40),
item_content TEXT,
item_star int
);
```
