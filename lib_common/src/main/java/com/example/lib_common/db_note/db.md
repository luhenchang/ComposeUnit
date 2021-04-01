---
theme : qklhk-chocolate
---
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


Expected:
 StudentId=Column{name='StudentId', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='null'}, Name=Column{name='Name', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}}, foreignKeys=[], indices=[]}
Found:
 StudentId=Column{name='StudentId', type='INTEGER', affinity='3', notNull=false, primaryKeyPosition=1, defaultValue='null'}, Name=Column{name='Name', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}}, foreignKeys=[], indices=[]}

Expected:
TableInfo{name='compose_data', columns={id=Column{name='id', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='null'}, item_title=Column{name='item_title', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}, item_subtitle=Column{name='item_subtitle', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}, item_content=Column{name='item_content', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}, item_star=Column{name='item_star', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'}}, foreignKeys=[], indices=[]}
Found:
TableInfo{name='compose_data', columns={item_subtitle=Column{name='item_subtitle', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}, id=Column{name='id', type='INTEGER', affinity='3', notNull=false, primaryKeyPosition=1, defaultValue='null'}, item_title=Column{name='item_title', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}, item_content=Column{name='item_content', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}, item_star=Column{name='item_star', type='INTEGER', affinity='3', notNull=false, primaryKeyPosition=0, defaultValue='null'}}, foreignKeys=[], indices=[]}


Country=Column{name='Country', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'}
Country=Column{name='Country', type='INTEGER', affinity='3', notNull=false, primaryKeyPosition=0, defaultValue='null'}

{name='Address', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}
{name='Address', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}

Age=Column{name='Age', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'},
Age=Column{name='Age', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='null'}, 