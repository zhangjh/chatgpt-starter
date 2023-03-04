#### ChatGpt JAVA API Starter
##### 简介
这是一个基于Java开发的ChatGpt API库，非常易于接入使用。
你只需生成一个自己的openAI apiKey，依赖本三方库，即可便利地使用ChatGpt。

当前接口功能主要有文本补全和图片生成两种，也会跟随官方更新进行升级。

##### 如何使用？
0. 到[这里](https://beta.openai.com/docs/quickstart/build-your-application)生成一个自己的API KEY
1. 工程中加入依赖：
   ```xml
        <dependency>
            <groupId>me.zhangjh</groupId>
            <artifactId>chatgpt-starter</artifactId>
            <version>${最新版本}</version>
        </dependency>
    ```
    最新版本查询：https://mvnrepository.com/artifact/me.zhangjh/chatgpt-starter
    
2. 将生成的apiKey加入配置文件application.properties
   ```properties
      openai.apikey=xxxxxxxxxxxxxxxxxxx
   ```
   或者将上述配置添加进环境变量
3. 代码中注入service

```java
   import org.springframework.beans.factory.annotation.Autowired;
   
   @Autowired
   private ChatGptService chatGptService;
   // 调用方法即可，其他方法不赘述
   TextResponse createTextCompletion(TextRequest data);
   ImageResponse createImageGeneration(ImageRequest imageRequest);
   
```


## 我使用这个starter制作了一个微信小程序：AI文图，欢迎交流~
![little-program](https://user-images.githubusercontent.com/3371714/219958080-f537f271-3d1b-41e1-86cf-1036d04ab6ba.jpeg)
