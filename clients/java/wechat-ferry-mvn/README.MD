# WeChatFerry Java 客户端 maven - plugin版——若小智

本项目基于 [WeChatFerry](https://github.com/lich0821/WeChatFerry) 项目，感谢原作者及所有贡献者。
<details><summary><font color="red" size="12">免责声明【必读】</font></summary>

本工具仅供学习和技术研究使用，不得用于任何商业或非法行为，否则后果自负。

本工具的作者不对本工具的安全性、完整性、可靠性、有效性、正确性或适用性做任何明示或暗示的保证，也不对本工具的使用或滥用造成的任何直接或间接的损失、责任、索赔、要求或诉讼承担任何责任。

本工具的作者保留随时修改、更新、删除或终止本工具的权利，无需事先通知或承担任何义务。

本工具的使用者应遵守相关法律法规，尊重微信的版权和隐私，不得侵犯微信或其他第三方的合法权益，不得从事任何违法或不道德的行为。

本工具的使用者在下载、安装、运行或使用本工具时，即表示已阅读并同意本免责声明。如有异议，请立即停止使用本工具，并删除所有相关文件。

</details>

⚠️ **只支持 Windows** ⚠️

`声明：` 本项目是基于 WeChatFerry clients/java/wcferry 项目改造，支持插件化实现自己想要的功能。
## 快速使用

### 环境准备
| 名称    | 版本        | 备注 |
|-------|-----------|----|
| JDK/JRE   | 1.8+      | √  |
| 微信    | 3.9.12.17 | √  |

### 版本命名规则
```xml
    <artifactId>ChatFerry-Java-Bot</artifactId>
    <packaging>jar</packaging>
    <name>wxbot-plugin</name>
    <version>0.3.0_39.3.3</version>
    <description>WeChatFerry客户端Java-Maven-plugin版</description>
```
版本命名规则：`该客户端版本_WeChatFerry版本`
### 对应微信版本一览

| 客户端版本 | 微信版本  |
|-------|-------|
| 0.3.x | [3.9.11.25](https://www.123865.com/s/QqyDVv-9XV0h) |

gui界面也设置对应微信版本下载链接

#### 下载文件

下载框架包 请在release中下载wx-bot-client.zip。

### 运行
下载后解压压缩包，运行根目录下的.jar文件

根据gui界面修改配置文件【dll文件位置、插件路径、[若智云Token](https://www.ruojy.top)】

点击“继续” 即可启动

### 配置
在框架运行的情况下，访问 http://localhost:9201/swagger-ui/index.html

进入swagger，微信消息处理-接口->找到并点击获取联系人

依次点击：
try it out ——> Execute

在返回结果中找到你要开启的微信组群的roomId复制

打开框架config/application.yaml文件

将如下所示 open-msg-groups 更换为你要开启的群聊
```yaml
# 本服务参数
wechat:
  ferry:
    # DLL文件位置
    dll-path: E:\WeChatFerry\clients\java\wechat-ferry-mvn\dll\sdk.dll
    # socket端口
    socket-port: 10086
    # 需要开启消息处理的群
    open-msg-groups:
      - 52155225442@chatroom
      - 45411596543@chatroom
```


## 插件
所有的插件我将会放在 插件中心[暂无地址](#)，大家需自行下载，后续将引入插件中心，允许开发者自行上传

没有插件也可以直接启动，就是没功能罢了
### 写给插件开发者
插件开发文档请见：[插件开发接口工具包](https://gitee.com/xiaoyudi_xyz/wx-bot-dev-plugin)

## 参与开发
🚫 非开发用户不需要往下看。

开发用户：可以根据文档和错误提示，自行解决编译错误的人员。
### 核心依赖

| 依赖            | 版本          | 说明       |
|---------------|-------------|----------|
| Spring Boot   | 2.7.18      | 基础框架     |
| protobuf-java | 3.22.2      | rpc      |
| jna           | 5.6.0       | 态访问系统本地库 |
| nng-java      | 1.4.0       | 本地包      |
| fastjson2     | 2.0.52      | 序列化      |
| dom4j         | 2.1.3       | XML解析包   |
| httpclient    | 4.5.13      | 客户端请求    |
| validation    | 2.0.1.Final | 参数校验     |
| springfox     | 3.0.0       | swagger3 |
### clone本仓库
git clone https://gitee.com/xiaoyudi_xyz/WeChatFerry-JavaBot.git

以/clients/java/wechat-ferry-mvn为根目录打开

### 模块结构

```
wechat-ferry-mvn
├─config                                    配置文件及插件
│  ├─plugins                                插件目录
│  └─application.yaml                       本程序主配置文件
├─dll                                       核心dll
│  ├─sdk.dll                                sdk文件
│  └─readme.txt                             本目录说明文件
│ 
├─src                                       源
│  ├─main                                   重启命令
│  │  ├─java(com.wechat.ferry)              java代码目录
│  │  │  ├─config                           配置
│  │  │  ├─constant                         常量
│  │  │  ├─controller                       控制层(API接口)
│  │  │  ├─entity                           聚合模型
│  │  │  │  ├─dto                           DTO模型
│  │  │  │  ├─po                            数据库实体(与表结构一一对应,否则请使用DTO)
│  │  │  │  ├─proto                         PB实体
│  │  │  │  └─vo                            视图层返回体目录
│  │  │  ├─enums                            枚举
│  │  │  ├─exception                        异常封装
│  │  │  ├─handle                           处理层
│  │  │  ├─service                          业务层
│  │  │  │  └─impl                          业务实现类
│  │  │  ├─strategy                         策略层
│  │  │  │  └─impl                          策略实现类(如接收到消息之后的事件处理可以放在这里)
│  │  │  ├─task                             定时任务
│  │  │  ├─utils                            工具层
│  │  │  └─WcferryApplication.java          启动类
│  │  │
│  │  │resources                            资源目录
│  │  │  ├─libs                             本程序内置依赖包
│  │  │  ├─proto                            proto文件(此目录打包将被排除)
│  │  │  ├─win32-x86-64                     依赖程序
│  │  │  └─logback-spring.xml               日志配置文件
│ 
├─pom.xml                                   POM文件
├─README.MD                                 说明文件
│   

```

### 配置说明

本程序主配置文件为 application.yml

#### 配置参数

本程序内置参数统一前缀：wechat.ferry 所有自定义本服务的参数请都放置在此前缀下，如：

```ymal
wechat:
  ferry:
    # DLL文件位置
    dll-path: /dll/sdk.dll
```

### 缩写含义

首先特别通用的缩写，这里就不在重复了，比如DTO,PO这类

请求入参：req

请求出参：resp

微信：wx

个人微信：pp

企业微信：cp

微信公众号：mp

WeChatFerry框架：wcf

.gitkeep：占位文件，比如我们定义一个文件夹之后，为了避免文件夹下无内容，导致git未提交上，所以会建议此文件，阅读时直接忽略此类文件即可

### 生成proto文件

本程序已经集成了生成proto文件的maven插件，直接install即可生成proto文件，且会在打包程序中去除 src/main/resources/proto
下面的内容，只保留实体类中的文件

默认install会重新根据.proto文件重新生成实体，如果不想被替换，请删除 src/main/resources/proto 下对应的.proto文件即可

#### 本项目自适配类型

有一些查询数据的自定义返回字段，定义了本项目自身的返回类型，所以 proto 文件不要从其他项目拷贝，不然缺失类型导致接口返回乱码自行负责

如：

```
message RoomData

```

### 提交规范

本模块希望大家使用统一提交格式，便于区分

格式：类型(任务号/缺陷号/没有使用0替代): [模块名称]-[子模块名称]-本次修改的说明

如：

```cmd
feat(0): [java]-[wechat-ferry-mvn]-基础类目录划分迁移及代码格式
```

| 名称   | 版本           |
|------|--------------|
| feat | 新功能          |
| fix  | 缺陷           |
| ...  | 其他等git规范中的均可 |

### 说明

下面主要是针对一些合作开发者的咨询做统一回复，可以直接忽略。

#### 目录层级拆分太细

其实这个服务是底层服务，目录分细确实会增加开发量，但是针对于后面的拓展和维护上来说，个人感觉利大于弊，
毕竟功能开发完之后，这个服务很少在变动，更多的是阅读，如果能让大家阅读、维护方便，开发时多花点时间能接受。

当然，如果觉得目录太多，可以自己删除点，这样可以少写点代码，目前本项目还是建议保必要的层级隔离

#### 文件名太长

出发点是为了名称进行统一规范化，便于阅读更加方便，只要了解了基础的缩写含义，其实跟自己平时命名一样，
只不过加上了固定的前缀和后缀而已，这样单从文件名就能分析出功能模块及其含义
# 交流群
微信：
添加小宇微信，发送：“WeChatFerry-JavaBot” 即可获得邀请链接加入群聊

![img.png](img.png)

QQ

![交流群](group.png)

