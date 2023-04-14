#                       一、ComposeUnit

> 第一款炫酷的Compose应用

#### 一、配置[Github上的SSH](https://github.com/settings/keys)

```
1、验证是否有ssh keys
   ls -al ~/.ssh
   
2、如果有.pub结尾的文件直接打开
   cat ~/.ssh/id_rsa.pub
   
3、如果没有新建ssh keys 回车生成公私钥
   ssh-keygen -t rsa -b 4096 -C "自己邮箱号"
   
4、github进行配置ssh   

5、验证是否和github链接
   ssh -T git@github.com   
   
```

#### 二、目的

#### 三、架构

#### 四、模块

#### 五、代码和README不断更新







#                           二、OpenAI API



![](https://gitee.com/LuHenChang/blog_pic/raw/master/open_ai_01.png)

镇楼图

![](https://gitee.com/LuHenChang/blog_pic/raw/master/open_ai_0007.gif)



> You can interact with the API through HTTP。

[**OpenAI**](https://platform.openai.com/docs/api-reference/introduction) 提供了开放**API**、各平台都可以通过HTTP与其建立连接进行调用。开发者可以尝试在不同平台上对接。这篇文章尝试将开放API对接到**ComposeUnit**。

**免费额度：** **API方式调用**官方明确是需要**收费**的。好在注册之后默认有[**$18.00**](https://platform.openai.com/account/usage)的免费使用额度。对于调试API这些应该足够了。

![](https://gitee.com/LuHenChang/blog_pic/raw/master/open_ai_009.png)

**充值相关：**国内充值比较麻烦。当然可以通过下面方式进行充值。

通过 [**欧易平台购买USDT**](https://www.cnouyi.care/cn/join/18639032)->[**提现到Depay钱包**](https://depay.depay.one/web-app/register-h5?invitCode=920750&lang=zh-cn) ->**兑换成美刀Depay信用卡**->[**充值**](https://platform.openai.com/account/billing/overview)或[**升级到ChatGPT Plus**](https://pay.openai.com/c/pay/cs_live_a11dxrOIyW9wy8boj6BoiaqV1omgDJHYyTfKOLpM9IUUuOTAAgdcjfxU3Y#fidkdWxOYHwnPyd1blppbHNgWjA0TUp3VnJGM200a31Cakw2aVFEYldvXFN3fzFhUDZjU0pkZ3xGZk5XNnVnQE9icEZTRGl0Rn1hfUZQc2pXbTRdUnJXZGZTbGpzUDZuSU5zdW5vbTJMdG5SNTVsXVR2b2o2aycpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYCkndXdgaWpkYUNqa3EnPydMa3Fgdyd4JSUl)。（开发者根据实际情况，选择是否**升级**开通[**ChatGPT Plus**](https://pay.openai.com/c/pay/cs_live_a11dxrOIyW9wy8boj6BoiaqV1omgDJHYyTfKOLpM9IUUuOTAAgdcjfxU3Y#fidkdWxOYHwnPyd1blppbHNgWjA0TUp3VnJGM200a31Cakw2aVFEYldvXFN3fzFhUDZjU0pkZ3xGZk5XNnVnQE9icEZTRGl0Rn1hfUZQc2pXbTRdUnJXZGZTbGpzUDZuSU5zdW5vbTJMdG5SNTVsXVR2b2o2aycpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYCkndXdgaWpkYUNqa3EnPydMa3Fgdyd4JSUl)）

#### 一、官方UI

[**官方布局UI**](https://chat.openai.com/chat) 想必大家都看到过，如下所示。ComposeUnit OpenAI模块的UI样式也沿袭官方进行，接下来对官网布局分析，好进行设计我们APP中的UI布局。

![](https://gitee.com/LuHenChang/blog_pic/raw/master/open_ai_02.png)

##### **1、布局模块**

**官方布局**，右侧UI大概包含，**对话列表**、**输入框**、**重定向请求**这三大模块。如下所示：

###### **消息列表**

<img src="https://gitee.com/LuHenChang/blog_pic/raw/master/image-20230410133543488.png" alt="image-20230410133543488" style="zoom:80%;" />

###### **输入框**

![image-20230410133635110](https://gitee.com/LuHenChang/blog_pic/raw/master/image-20230410133635110.png)

###### **重定向请求**

![image-20230410133715119](https://gitee.com/LuHenChang/blog_pic/raw/master/image-20230410133715119.png)

##### **2、模块分析**

###### **消息列表**

可以看到消息部分，是常见的问答列表样式。文章由于时间问题，不做本地数据库文件缓存等，数据操作都基于viewModel中可观察集合进行。此案例使用**compose+viewModel+rerofit+协程**方式进行代码编写。列表部分，使用Compose库提供的**LazyColumn**进行布局。

在对话中可以观察到对应的文字有输入效果。应该不是印度小哥后台的一顿疯狂操作。开个玩笑，先看看官方输出文字效果。这个是需要安排的小动画。

<img src="https://gitee.com/LuHenChang/blog_pic/raw/master/open_ai_0018.gif" alt="open_ai_0018" style="zoom:150%;" />

###### **输入框**

输入框内不也有很多细节，未输入之前的提示Send a message和文字颜色，以及输入之后的文字颜色等都是不同。

1、文字颜色变化

2、输入框右侧发送按钮和加载动画由网络是否请求而决定。

3、加载过程中右侧是三个小点加载动画。

4、加载过程中输入框是可以输入的，但是无法进行发送请求。

###### **重定向请求**

在输入框上部有个Regenerate response和Stop generating可以切换的按钮。当请求加载数据过程可以停止，而数据停止之后，可以点击Regenerate response再次请求。

#### 二、设计页面UI

##### **1、UI整体布局**

同官方UI，进行移动端页面设计，整体UI色调只用灰色系深浅两种：

```
val openAiDark = Color(52, 54, 65, 255)
val openAiLight = Color(68, 70, 84, 255)
```

对比如下：

![image-20230410180613811](https://gitee.com/LuHenChang/blog_pic/raw/master/image-20230410180613811.png)

通过**Scaffold**构建基本的页面框架。框架代码如下：

```kotlin
@Composable
fun OpenAIPage(viewModel: OpenAiViewModel) {
    Scaffold(
        backgroundColor = openAiDark,
        topBar = {//1、顶部TopBar
            OpenAITopBar()
        }) {
        OpenAIUI(//2、列表和底部输入框
        )
    }
}
```

##### **2、TopBar**

**TopBar**背景色取主题色的亮色，整个UI布局只用用灰色的深亮**两**种色，我是比较喜欢简约风。文字部分加粗且增加阴影。

```kotlin
//topBar部分，标题文字加粗增阴影
@Composable
fun OpenAITopBar() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(
                openAiLight
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "OpenAI",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = Shadow(
                    Color(43, 43, 43, 255),
                    offset = Offset(2f, 6f),
                    blurRadius = 11f
                )
            )
        )
    }
}
```

效果如下：

![image-20230410184115878](https://gitee.com/LuHenChang/blog_pic/raw/master/image-20230410184115878.png)



##### **3、列表**

对话列表和输入框部分，我们可以用Box进行层次布局。

```kotlin
 @Composable
fun OpenAIUI(
    modifier: Modifier,
    textFieldAlignment: Alignment = Alignment.BottomCenter,
 ) {
    Box(modifier = modifier,
        contentAlignment = textFieldAlignmen) {
        //列表布局
        OpenAIListView()
        //输入和重定向布局
        Column(
            Modifier
                .fillMaxWidth()
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OpenAIReRequestUI()
            Box(Modifier.height(5.dp))
            OpenAIBottomInputUI()
        }
    }
}
```

列表OpenAIListView部分，假数据进行填充，增加背景配色。

```kotlin
@Composable
fun OpenAIListView1() {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        items(2, key = { index ->
            index
        }) { index ->
            if (index % 2 == 0)
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(52, 54, 65, 255))
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = "我是假数据，用户数据",
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 20.dp, bottom = 20.dp, end = 20.dp)
                    )
                }
            else
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(openAiLight)
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = "我是假数据，OpenAI返回数据，如果回答有问题，我希望你可以给我一些建议，我继续生成，文字不够两行。",
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 20.dp, bottom = 20.dp, end = 20.dp)
                    )
                }
        }
    }
}
```

![image-20230410191832626](https://gitee.com/LuHenChang/blog_pic/raw/master/image-20230410191832626.png)

用户和AI头像我们不难用Row之类的容器进行摆放。代码如下：

```kotlin
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        items(2, key = { index ->
            index
        }) { index ->
            if (index % 2 == 0)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(52, 54, 65, 255))
                        .padding(top = 20.dp)
                ) {
                    Box(Modifier.width(20.dp))
                    Box(
                        Modifier
                            .size(30.dp)
                            .background(
                                color = Color(
                                    3,
                                    149,
                                    135,
                                    255
                                ),
                                shape = RoundedCornerShape(5.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "11",
                            color = Color.White,
                        )
                    }
                    Text(
                        text = "我是假数据，用户数据",
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 20.dp, bottom = 20.dp, end = 20.dp)
                    )
                }
            else
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(openAiLight)
                        .padding(top = 20.dp)
                ) {
                    Box(Modifier.width(20.dp))
                    Image(
                        painter = painterResource(id = R.mipmap.open_ai_head),
                        contentDescription = "head",
                        contentScale = ContentScale.Fit,
                            modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = "我是假数据，OpenAI返回数据，如果回答有问题，我希望你可以给我一些建议，我继续生成，文字不够两行。",
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 20.dp, bottom = 20.dp, end = 20.dp)
                    )
                }
        }
    }
```

代码别学上面堆屎山。

```kotlin
@Composable
fun OpenAIListView() {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        items(2, key = { index ->
            index
        }) { index ->
            if (index % 2 == 0)
                UserMessagesUI()
            else
                OpenAIMessageUI()
    }
}

```

<img src="https://gitee.com/LuHenChang/blog_pic/raw/master/image-20230410192755346.png" alt="image-20230410192755346" style="zoom:50%;" />

在网络请求错误或者异常请求，头像以及文字颜色显示效果等。

![image-20230410194435594](https://gitee.com/LuHenChang/blog_pic/raw/master/image-20230410194435594.png)

约束布局进行设置一个网络请求异常的状态布局。

```kotlin
ConstraintLayout {
         val (imageView, errorView) = createRefs()
         Image(painter = painterResource(id = R.mipmap.open_ai_head),
               contentDescription = "head",
               contentScale = ContentScale.Fit,
               modifier = Modifier.constrainAs(imageView) {
                          top.linkTo(parent.top)
                          start.linkTo(parent.start)}.size(30.dp))
        if (netErro)
              Image(
                  painter = painterResource(id = R.mipmap.open_ai_error),
                  colorFilter = ColorFilter.tint(Color.Red),
                  contentDescription = "error",
                  contentScale = ContentScale.Fit,
                  modifier = Modifier
                      .constrainAs(errorView) {
                          bottom.linkTo(imageView.bottom)
                          end.linkTo(imageView.end)
                      }
                      .offset(x = 6.dp, y = 8.dp)
                      .size(16.dp)
              )
         }
```

![image-20230410195811203](https://gitee.com/LuHenChang/blog_pic/raw/master/image-20230410195811203.png)

##### **4、输入框**

官方效果如下：

![open_ai_2023_4_10_20_20](https://gitee.com/LuHenChang/blog_pic/raw/master/open_ai_2023_4_10_20_20.gif)

输入框部分，使用**TextField**，通过**Modifier**设置**background**设置背景色和裁剪背景有四角弧度，**border**设置增加边框。TexField的placeholder属性进行设置**Send a message...**提示语，保证在无输入内容以及无焦点是显示提示语。通过**TextField的shape**进行设置输入内容部分的形状保持和背景一致。**trailingIcon**可用于设置最后边部分的发送按钮和加载中动画部分。

```kotlin
OpenAIBottomInputUI(){
 TextField(
        textStyle = TextStyle(
            color = (inputColor(isFocused, textFieldValue.value.text, loading))
        ), colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            backgroundColor = Color(64, 65, 79, 255)
        ),
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(50.dp)
            .focusRequester(focusRequester)
            .background(
                color = Color(64, 65, 79, 255), RoundedCornerShape(10)
            )
            .border(
                0.5.dp, Color(47, 49, 56, 166), RoundedCornerShape(10)
            ),
        placeholder = {
            Text(
                text = "Send a message...",
                color = (inputColor(isFocused, textFieldValue.value.text))
            )
        },
        shape = RoundedCornerShape(10),
        value = textFieldValue.value,
        onValueChange = {
            if (!isFocused && it.text.isNotEmpty()) {
                textFieldValue.value = TextFieldValue(
                    text = it.text, selection = TextRange(0, it.text.lastIndex + 1)
                )
            } else {
                textFieldValue.value = TextFieldValue(
                    text = it.text,
                    selection = TextRange(it.text.lastIndex + 1)
                )
            }
        }, trailingIcon = {
            Icon(
                    modifier = Modifier.clickable {
                        if (textFieldValue.value.text.isNotEmpty()) {
                            focusManager.clearFocus()
                        }
                    },
                    painter = painterResource(R.mipmap.send_icon),
                    contentDescription = "sendIcon",
                    tint = submitColor(isFocused, textFieldValue.value.text)
                )
        })
}

```

重定向请求部分比较简单，不在阐述

```kotlin
@Composable
private fun OpenAIReRequestUI(
    pageList: ArrayList<ChatGTPModel>,
) {
        Row(
            Modifier
                .background(color = Color(52, 54, 65, 255))
                .border(
                    0.6.dp,
                    Color(85, 87, 104, 255),
                    RoundedCornerShape(5.dp),
                )
                .clickable {
                    //点击网络操作
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .height(18.dp)
                    .width(18.dp)
                    .padding(start = 5.dp),
                painter = painterResource(
                    id = if (loading)
                        R.mipmap.open_ai_stop else
                        R.mipmap.open_ai_reload
                ),
                contentDescription = "reload",
                tint = Color(216, 216, 226, 255)
            )
            Text(
                text = "Regenerate response",
                Modifier.padding(5.dp),
                color = Color(216, 216, 226, 255)
            )
        }
}
```

![image-20230410203758526](https://gitee.com/LuHenChang/blog_pic/raw/master/image-20230410203758526.png)

至此基本布局完成，接下来我们看看对应官方的API，逐步接入数据部分。

![image-20230410204632368](https://gitee.com/LuHenChang/blog_pic/raw/master/image-20230410204632368.png)

#### 三、Completinos和Chat API

**[官方API](https://platform.openai.com/docs/api-reference)**在文字信息获取方面提供了**[Completinos](https://platform.openai.com/docs/api-reference/completions)**和**[Chat](https://platform.openai.com/docs/api-reference/chat)**两个种。两者是有一些区别：

- **功能不同：** [**Completinos**](https://platform.openai.com/docs/api-reference/completions)是一个用于自动生成API端点代码的工具，它基于OpenAPI规范，可以帮助开发人员自动创建API端点的代码。而[**Chat**](https://platform.openai.com/docs/api-reference/chat)是一个自然语言处理模型，可以用于生成语言文本、回答问题等。
- **使用场景不同：**  [**Completinos**](https://platform.openai.com/docs/api-reference/completions)适用于开发人员，可以帮助他们更快速、更准确地创建API端点的代码，提高开发效率。而[**Chat**](https://platform.openai.com/docs/api-reference/chat)则可以用于各种场景，例如客户服务、智能助手等。
- **技术实现不同：** [**Completinos**](https://platform.openai.com/docs/api-reference/completions)是基于机器学习技术实现的，它可以通过学习OpenAPI规范中定义的API接口信息，自动生成相应的代码。而[**Chat**](https://platform.openai.com/docs/api-reference/chat)则是基于自然语言处理技术实现的，它可以理解人类语言，并生成有意义的回答或文本。
- **输出结果不同：** [**Completinos**](https://platform.openai.com/docs/api-reference/completions)的输出结果是一段API端点的代码，可以直接用于开发项目。而[**Chat**](https://platform.openai.com/docs/api-reference/chat)的输出结果是一段文本，可以用于回答问题、提供信息等。

案例中，我演示拿[**Chat API**](https://platform.openai.com/docs/api-reference/chat)进行网络请求数据。

###### **1、Request url**

> Chat  POST https://api.openai.com/v1/chat/completions

###### **2、Request body**

请求体参数有很多，每个参数都有其重要的意义。

**model** string Required

在 OpenAI API使用时指定的GPT模型。不同模型具有不同的特点和性能，在使用时需要根据具体的任务和应用场景进行选择。常见[**模型**](https://platform.openai.com/docs/models/model-endpoint-compatibility)【gpt-4, gpt-4-0314, gpt-4-32k, gpt-4-32k-0314, gpt-3.5-turbo, gpt-3.5-turbo-0301】，此参数是必选参数。

> **model** string Required
>
> ID of the model to use. See the [model endpoint compatibility](https://platform.openai.com/docs/models/model-endpoint-compatibility) table for details on which models work with the Chat API.

**messages** array Required

OpenAI GPT 系列模型中的 messages 是指模型接收到的一系列文本信息，可以是单个字符串或者字符串数组，用于生成后续的文本输出。messages 数组的长度和内容可以对生成的文本质量和多样性产生重要影响。

> **messages** array Required
>
> The messages to generate chat completions for, in the [chat format](https://platform.openai.com/docs/guides/chat/introduction).

**temperature** number Optional  Defaults to 1

在自然语言处理中，temperature是一种用于控制文本生成多样性的参数。它通常用于基于概率的文本生成模型，如语言模型和机器翻译模型，可以影响模型生成文本的随机性和多样性。具体来说，temperature用于在模型的预测分布中引入一定的随机性。在生成每个单词时，模型会计算每个单词出现的概率，并按照这些概率进行采样。

> What sampling temperature to use, between 0 and 2. Higher values like 0.8 will make the output more random, while lower values like 0.2 will make it more focused and deterministic.
>
> We generally recommend altering this or `top_p` but not both.

**top_p** number Optional Defaults to 1

top_p 是 OpenAI GPT 系列模型中的一个参数，也称为 nucleus sampling，用于控制文本生成的多样性。它是一种基于概率的采样方法，通过指定一个概率阈值来限制模型输出的词汇表，从而使生成的文本更加多样化。

> An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of the tokens with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are considered.
>
> We generally recommend altering this or `temperature` but not both.

**n** integer Optional Defaults to 1

指定为每条输入消息生成多少聊天结果，默认是一条。

> How many chat completion choices to generate for each input message.

**stream** boolean Optional Defaults to false

Stream 是一种用于生成持续流式输出的技术。在某些文本生成任务中，我们需要将生成的文本作为持续流式输出呈现给用户，在Python请求中，可以通过遍历请求事件进行轮循获取消息。当然在Android中也有相关的三方库。

> If set, partial message deltas will be sent, like in ChatGPT. Tokens will be sent as data-only [server-sent events](https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events#Event_stream_format) as they become available, with the stream terminated by a `data: [DONE]` message. See the OpenAI Cookbook for [example code](https://github.com/openai/openai-cookbook/blob/main/examples/How_to_stream_completions.ipynb).

**stop** string or array Optional Defaults to null

Stop 是用于生成文本时控制停止生成的技术。在文本生成任务中，有时候模型会生成无意义的文本，或者需要生成特定的文本内容，这时候我们需要控制模型停止生成。例如，如果需要生成包含某些关键词的文本，模型可以检查已生成的文本中是否包含这些关键词，如果是，模型将停止生成；如果不是，模型将继续生成，直到满足停止条件为止。

> Up to 4 sequences where the API will stop generating further tokens.

**max_tokens** integer Optional Defaults to inf

Max Tokens 是用于生成文本时控制生成文本长度的技术。在文本生成任务中，有时候模型会生成过长或过短的文本，这会导致生成的文本质量不佳。tokens对于套壳人员来说都是money，对于tokens约束可能有所帮助。

> The maximum number of [tokens](https://platform.openai.com/tokenizer) to generate in the chat completion.
>
> The total length of input tokens and generated tokens is limited by the model's context length.

**presence_penalty** number Optional Defaults to 0

Presence Penalty 是用于生成文本时控制单词出现频率的技术。在文本生成任务中，有时候模型会倾向于生成某些高频词汇，这会导致生成的文本缺乏多样性和新颖性。具体来说，当模型生成一个新单词时，OpenAI Presence Penalty 会将这个单词在之前生成的文本中的存在情况作为惩罚项添加到损失函数中。如果这个单词在之前的文本中出现过，惩罚项就会更大，从而鼓励模型生成之前未出现的单词，增加文本的多样性和新颖性。

> Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far, increasing the model's likelihood to talk about new topics.
>
> [See more information about frequency and presence penalties.](https://platform.openai.com/docs/api-reference/parameter-details)

**frequency_penalty** number Optional Defaults to 0

Frequency Penalty 是用于生成文本时控制单词重复使用的技术。在文本生成任务中，有时候模型会重复使用某些词汇，这会导致生成的文本缺乏多样性和新颖性。

> Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far, decreasing the model's likelihood to repeat the same line verbatim.
>
> [See more information about frequency and presence penalties.](https://platform.openai.com/docs/api-reference/parameter-details)

**logit_bias** map Optional Defaults to null

Logit Bias 是用于降低神经网络模型中的偏见的技术。在自然语言处理任务中，神经网络通常使用softmax函数将输入转换为概率分布，以便进行分类或生成文本。但是，这种方法可能会导致模型对某些输入数据具有偏见，例如对于某些性别、种族或文化背景的输入，模型可能会产生不公平的结果。

> Modify the likelihood of specified tokens appearing in the completion.
>
> Accepts a json object that maps tokens (specified by their token ID in the tokenizer) to an associated bias value from -100 to 100. Mathematically, the bias is added to the logits generated by the model prior to sampling. The exact effect will vary per model, but values between -1 and 1 should decrease or increase likelihood of selection; values like -100 or 100 should result in a ban or exclusive selection of the relevant token.

**user** string Optional

在请求中发送最终用户 ID 可以是 OpenAI 监控和检测滥用的有用工具。我们可以将用户的userId进行对应设置，有助于后期对于接口安全的跟踪和处理。

> A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse. [Learn more](https://platform.openai.com/docs/guides/safety-best-practices/end-user-ids).

###### **3、请求测试**

对于请求参数大家可以单独测试，案例中主要以必要参数进行开发，采用了Retrofit进行封装网络请求。

**创建Model**

根据官方响应数据创建数据模型，由于请求过程可能的异常中断等创建**ChatGTPFailModel**。由于案例中采用比较简单的数据处理，只通过集合控制，所以在官方响应数据模型上增加了**isAI**和**errorNet**两个参数。

```
interface ChatGTPModel
sealed class ChatGTPFailModel(val errCode: Int, val message: String?) : ChatGTPModel {
    object NETTER : ChatGTPFailModel(404,"网络错误")
}
data class ModelData(
    val choices: List<Choice>? = null,
    val created: Int? = null,
    val id: String? = null,
    val model: String? = null,
    val `object`: String? = null,
    val usage: Usage? = null,
    val isAI: Boolean = true,//用来判断是用户请求数据还是AI数据模型
    val errorNet: Boolean = false//用来判断是异常数据还是正常数据
) : ChatGTPModel

data class Choice(
    val finish_reason: Any? = null,
    val index: Int? = null,
    val message: Message? = null
)

data class Usage(
    val completion_tokens: Int? = null,
    val prompt_tokens: Int? = null,
    val total_tokens: Int? = null
)

data class Message(
    val content: String? = null,
    val role: String? = null
)
```

**网络请求接口：**

```kotlin
interface ApiService {
    @POST("chat/completions")
    fun getMessage(
        @Header("Content-Type") type: String,
        @Header("Authorization") authorization: String,
        @Body body: ClientSendBody
    ): Call<ModelData>
 }
```

**Retrofit请求**

```kotlin
object RetrofitManger {
    private const val BASE_URL = "https://api.openai.com/v1/"
    private var retrofit: Retrofit? = null
    private const val READ_TIMEOUT = 50L
    private const val WRITE_TIMEOUT = 50L
    private const val CONNECT_TIMEOUT = 50L

    val service: ApiService by lazy {
        getRetrofitInstance().create(ApiService::class.java)
    }

    private fun getRetrofitInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = getHttpClient()?.let {
                Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(it)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
        }
        return retrofit!!
    }
   } 
```

**固定参数**

[**API keys**](https://platform.openai.com/account/api-keys) 每个开发者都可以在官方进行创建Key。

![image-20230412103905532](https://gitee.com/LuHenChang/blog_pic/raw/master/image-20230412103905532.png)

官方请求参数如下，可以看到请求头有**Content-Type**和**Authorization**，请求体比较多，目前传入必要参数进行获取数据：

```kotlin
//官方请求参数
curl https://api.openai.com/v1/chat/completions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $OPENAI_API_KEY" \
  -d '{
    "model": "gpt-3.5-turbo",
    "messages": [{"role": "user", "content": "Hello!"}]
  }'
```

**请求基本数据**

**Authorization**需要注意，Bearer $API_KEY  是拼接起来的，别忘记前缀Bearer。**model**需要自行选用。

```
class HttpConst {
  companion object {
     private const val CHAT_GTP_KEY = "sk-Mkdrcl......."
        const val CHAT_AUTHORIZATION = "Bearer $CHAT_GTP_KEY"//Bearer 拼接key别写错了。
        const val CHAT_GTP_CONTENT_TYPE = "application/json"
        const val CHAT_GTP_ROLE = "user"
        const val CHAT_GTP_MODEL = "gpt-3.5-turbo"//自行选用
    }
}
```

**请求部分**

```
object ChatGTPRepository {
    suspend fun getMessage(
        type: String, authorization: String, body: ClientSendBody
    ): ChatGTPResult<ModelData> = withContext(Dispatchers.IO) {
        val response = RetrofitManger.service.getMessage(type, authorization, body).awaitResponse()
        Log.e(
            "response msg=",
            "body=${response.body().toString()}" + ":message=${response.message()}"
        )
        if (response.isSuccessful) {
            ChatGTPResult.Success(response.body())
        } else {
            ChatGTPResult.Fail(response.code(), response.message().messageResult())
        }
    }
} 
```

接下来，我们在ViewModel进行网络请求。

**第一步：**输入框输入数据之后点击submit按钮进行调用 **viewModle.getChatGTPMessage(info: String)**。

**第二步：**通过用户输入的数据组装为UI数据，然后更新到界面。**_pageList.emit(newList)**

**第三步：**调用网络数据，然后设置数据且更新到UI界面且**emit(getMessage(info))**、

**第四部：**更新UI界面状态例如：加载小点动画结束变为提交按钮等。**updateLoadingState(it)**

```kotlin
class OpenAiViewModel : ViewModel() {
    private var _pageList = MutableStateFlow<ArrayList<ChatGTPModel>>(arrayListOf())
    var pageList: StateFlow<ArrayList<ChatGTPModel>> = _pageList
    //获取消息
    fun getChatGTPMessage(info: String) {
       viewModelScope.launch(handler) {
            flow {
                val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                newList.add(
                    ModelData(
                        choices = arrayListOf(Choice(message = Message(content = info))),
                        isAI = false
                    )
                )
                //2、我们先将用户输入的数据组装为UI数据然后更新到界面
                _pageList.emit(newList)
                emit(getMessage(info))
            }.flowOn(Dispatchers.IO).collect { result ->
                when (result) {
                    is ChatGTPResult.Success -> {
                        //新数据来了增加到集合
                        val netList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                        result.data?.let {
                            netList.add(it)
                            //去刷新UI
                            _pageList.emit(netList)
                            updateLoadingState(it)
                        }
                    }
                    is ChatGTPResult.Fail -> {
                        val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                        val mode = ModelData(
                            choices = arrayListOf(
                              Choice(message = Message(content = result.message))
                            ),
                            isAI = true,
                            errorNet = true
                        )
                        newList.add(mode)
                        _pageList.emit(newList)
                        updateLoadingState(mode)
                    }
                }
            }
        }
    }
  
    //更新UI状态，关闭加载按钮小点。
    private fun updateLoadingState(info: ChatGTPModel) {
        when (info) {
            is ModelData -> {
                if (info.isAI) {
                    setLoadValue(false)
                }
            }
            is ImageData -> {
                if (info.isAI) {
                    setLoadValue(false)
                }
            }
        }
    }
  
     private suspend fun getMessage(info: String): ChatGTPResult<ChatGTPModel> {
        return ChatGTPRepository.getMessage(
            HttpConst.CHAT_GTP_CONTENT_TYPE, HttpConst.CHAT_AUTHORIZATION,
            ClientSendBody(
                listOf(ClientMessage(info, HttpConst.CHAT_GTP_ROLE)),
                HttpConst.CHAT_GTP_MODEL
            )
        )
    }
 }
```

接下来我们看看效果。

![open_ai_20234121149](https://gitee.com/LuHenChang/blog_pic/raw/master/open_ai_20234121149.gif)

在请求过程，我们输入框有加载小点，而加请求完毕输入框请求焦点变为提交按钮图标。这一块我们可以通过请求过程的状态和动画进行相关的处理。

![open_ai_2023-04-12 at 11.52.54](https://gitee.com/LuHenChang/blog_pic/raw/master/open_ai_2023-04-12%20at%2011.52.54.gif)

**动画**部分相关代码：

```kotlin
TextField(
        textStyle = TextStyle(
            color = (inputColor(isFocused, textFieldValue.value.text, loading))
        )
        trailingIcon = {
            if (loading) {
                trailingAnimalIcon()
            } else
                trailingSubmitIcon(textFieldValue, viewModel, focusManager, isFocused)
        })
//提交请求按钮
@Composable
private fun trailingSubmitIcon(
    textFieldValue: MutableState<TextFieldValue>,
    viewModel: OpenAiViewModel,
    focusManager: FocusManager,
    isFocused: Boolean
) {
    Icon(
        modifier = Modifier.clickable {
            if (textFieldValue.value.text.isNotEmpty()) {
                //执行加载小点动画
                viewModel.setLoadValue(true)
                //网络请求
                viewModel.getChatGTPMessage(textFieldValue.value.text)
                //储存当前请求的消息，如果请求失败或者中途stop请求，我们可以再次请求从而拿到请求内容。
                viewModel.setRegenerateInfo(textFieldValue.value.text)
                //更新当前输入内容
                textFieldValue.value = TextFieldValue(
                    text = ""
                )
                //清除焦点
                focusManager.clearFocus()
            }
        },
        painter = painterResource(R.mipmap.send_icon),
        contentDescription = "sendIcon",
        tint = submitColor(isFocused, textFieldValue.value.text)
    )
}
//单独Composeable最小颗粒局部执行动画。
@Composable
private fun trailingAnimalIcon() {
    val infiniteTransition = rememberInfiniteTransition()
    val animation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .width(15.dp)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .size(3.dp)
                .background(Color(141, 141, 159, 255), shape = CircleShape)
        )
        Box(Modifier.width(3.dp))
        AnimatedVisibility(visible = (animation >= 1f)) {
            Box(
                modifier = Modifier
                    .size(3.dp)
                    .background(Color(141, 141, 159, 255), shape = CircleShape)
            )
        }
        Box(Modifier.width(3.dp))
        AnimatedVisibility(visible = (animation >= 2f)) {
            Box(
                modifier = Modifier
                    .size(3.dp)
                    .background(Color(141, 141, 159, 255), shape = CircleShape)
            )
        }

    }
}
```

#### 四、Images API

**[官方API](https://platform.openai.com/docs/api-reference)**在图片获取提供了对于API [**Images**](https://platform.openai.com/docs/api-reference/images)。图片 API 提供了三种与图片交互的方法：

1. 根据文本提示从头开始创建图像
2. 根据新文本提示创建现有图像的编辑
3. 创建现有图像的变体

接下来，我们基于文本提示获取图片来进行集成到我们的Demo中，首先文字混排已经搞定，我们只是在此基础上增加图片请求接口和图片展示部分。如何让文字描述可以分别对应请求图片还是文字，最简单的莫过于if else。当然了，我们可以定义匹配规则。

```kotlin
if (info.contains("生成图片")) {
    emit(generateImage(info))
} else {
    emit(getMessage(info))
}
```

###### **1、官方示例请求**

```kotlin
curl https://api.openai.com/v1/images/generations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $OPENAI_API_KEY" \
  -d '{
    "prompt": "A cute baby sea otter",
    "n": 2,
    "size": "1024x1024"
  }'
//参数
{
  "prompt": "A cute baby sea otter",
  "n": 2,
  "size": "1024x1024"
}

//回复
{
  "created": 1589478378,
  "data": [
    {
      "url": "https://..."
    },
    {
      "url": "https://..."
    }
  ]
}
```

###### **2、数据模型：**

```
data class ImageData(
    val created: Int = 0,
    val `data`: List<Data>? = null,
    val userData: String? = "",
    var isAI: Boolean = true,
    var errorNet: Boolean = false,
) : ChatGTPModel

data class Data(
    val url: String? = null
)
```

###### **3、定义请求接口**

```kotlin
interface ApiService {
    @POST("chat/completions")
    fun getMessage(
        @Header("Content-Type") type: String,
        @Header("Authorization") authorization: String,
        @Body body: ClientSendBody
    ): Call<ModelData>

    @POST("images/generations")
    fun generateImage(
        @Header("Content-Type") type: String,
        @Header("Authorization") authorization: String,
        @Body body: ImageBody
    ): Call<ImageData>
}


object ChatGTPRepository {
    suspend fun generateImage(
        type: String, authorization: String, imageBody: ImageBody
    ): ChatGTPResult<ChatGTPModel> = withContext(Dispatchers.IO) {
        val response =
            RetrofitManger.service.generateImage(type, authorization, imageBody).awaitResponse(
        if (response.isSuccessful) {
            ChatGTPResult.Success(response.body())
        } else {
            ChatGTPResult.Fail(response.code(), response.message().messageResult())
        }
    }
}
              
fun String.messageResult(): String = if (isNullOrEmpty()) 
    "unknown error may need to be created or replaced API keys"
    else this
```

ViewModel中进行请求组装数据，我们只需要在上述**getChatGTPMessage(info: String)**中加入图片和消息判断分别请求图片接口和消息接口即可。采用多态进行数据整合和分离。

```kotlin
fun getChatGTPMessage(info: String) {
        job = viewModelScope.launch(handler) {
            flow {
                val newList = _pageList.value.clone() as ArrayList<ChatGTPModel>
                    .................
                _pageList.emit(newList)
                if (info.contains("生成图片")) {
                    emit(generateImage(info))
                } else {
                    emit(getMessage(info))
                }
            }.flowOn(Dispatchers.IO).collect { result ->
                when (result) {
                    .................
                }
            }
        }
    }

private suspend fun generateImage(
        prompt: String = "A cute baby sea otter",
        n: Int = 1,
        size: String = "256x256"
    ) = ChatGTPRepository.generateImage(
        HttpConst.CHAT_GTP_CONTENT_TYPE,
        HttpConst.CHAT_AUTHORIZATION,
        ImageBody(prompt, n, size)
    )
```

###### **4、UI展示**

UI部分代码同样只需要判断请求结果是图片还是消息进行显示不同的内容。图片库使用的**io.coil-kt:coil-compose**

```kotlin
@Composable
private fun OpenAIListView(pageList: ArrayList<ChatGTPModel>, viewModel: OpenAiViewModel) {
    Log.e("OpenAIListView=", "OpenAIListView")
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        items(pageList.size, key = { index ->
            index
        }) { index ->
            when (val data = pageList[index]) {
                is ModelData -> {
                    data.choices?.get(0)?.message?.content?.let { content ->
                        if (index % 2 == 0)
                            UserMessagesUI(content)
                        else
                            OpenAIMessageUI(content, data.errorNet, viewModel,index)
                    }
                }
                is ImageData -> {
                    if (index % 2 == 0)
                        UserMessagesUI(data.userData)
                    else
                        OpenAIImageUI(data.data?.get(0), data.errorNet)
                }
            }
        }
    }
}

@Composable
private fun OpenAIImageUI(content: Data?, errorNet: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(68, 70, 84, 255))
            .padding(top = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(Modifier.width(20.dp))
        ConstraintLayout {
            val (imageView, errorView) = createRefs()
            Image(
                painter = painterResource(id = R.mipmap.open_ai_head),
                contentDescription = "head",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .constrainAs(imageView) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(30.dp)
            )
            if (errorNet)
                Image(
                    painter = painterResource(id = R.mipmap.open_ai_error),
                    colorFilter = ColorFilter.tint(Color(95, 1, 1, 255)),
                    contentDescription = "error",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .constrainAs(errorView) {
                            bottom.linkTo(imageView.bottom)
                            end.linkTo(imageView.end)
                        }
                        .size(10.dp)
                )
        }
        AsyncImage(
            modifier = Modifier
                .size(125.dp)
                .padding(start = 20.dp, bottom = 20.dp, end = 20.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(content?.url)
                .crossfade(true)
                .build(),
            onLoading = {

            },
            onSuccess = {

            },
            onError = {

            },
            placeholder = painterResource(R.drawable.jetpack),
            contentDescription = ""
        )
    }
}
```

![](https://gitee.com/LuHenChang/blog_pic/raw/master/open_ai_05.gif)![](https://gitee.com/LuHenChang/blog_pic/raw/master/open_ai_006.gif)![](https://gitee.com/LuHenChang/blog_pic/raw/master/open_ai_005.gif)

#### 五、最终效果

![open_ai_0oo 2023-04-04 12.39.58](https://gitee.com/LuHenChang/blog_pic/raw/master/open_ai_0oo%202023-04-04%2012.39.58.gif)

#### **六、总结**

**OpenAI**提供了足够多的API，包括[**Completions**](https://link.juejin.cn?target=https%3A%2F%2Fplatform.openai.com%2Fdocs%2Fapi-reference%2Fcompletions)、[**Chat**](https://link.juejin.cn?target=https%3A%2F%2Fplatform.openai.com%2Fdocs%2Fapi-reference%2Fchat)、[**Edits**](https://link.juejin.cn?target=https%3A%2F%2Fplatform.openai.com%2Fdocs%2Fapi-reference%2Fedits)、[**Images**](https://link.juejin.cn?target=https%3A%2F%2Fplatform.openai.com%2Fdocs%2Fapi-reference%2Fimages)、[**Audio**](https://link.juejin.cn?target=https%3A%2F%2Fplatform.openai.com%2Fdocs%2Fapi-reference%2Faudio)、[**Files**](https://link.juejin.cn?target=https%3A%2F%2Fplatform.openai.com%2Fdocs%2Fapi-reference%2Ffiles)...等。这篇文章也就是简单的使用了基础API，由于时间问题其他模块以及各个参数可自行测试。

**OpenAI API** 可以帮助开发人员在他们的应用程序中集成自然语言处理功能。提供了各种语言任务的功能，包括文本生成、文本分类、语言翻译和对话生成等。OpenAI API的应用前景非常广泛。例如，它可以被用于开发智能聊天机器人、自动化的文本摘要生成、自动翻译、搜索引擎优化等方面。此外，OpenAI API可以帮助企业更好地理解和分析大量的自然语言数据，从而帮助他们做出更好的商业决策。随着人工智能技术的不断发展和应用，OpenAI API的作用将会越来越重要。

**对于企业：** OpenAI根据产品的需要可以进行深度挖掘，带给用户更好的体验工具。

**对于个人：** 想赚点外快的开发者，可以试一试马甲包，应该是空前绝后的机会。
