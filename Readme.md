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
            <version>1.0.0</version>
        </dependency>
    ```
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
   // 调用方法即可
   TextResponse createTextCompletion(TextRequest data);
   ImageResponse createImageGeneration(ImageRequest imageRequest);
```