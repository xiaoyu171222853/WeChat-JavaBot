# WeChatFerry Java 客户端 maven - plugin版

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
## 快速使用1
### 环境准备
| 名称    | 版本        | 备注 |
|-------|-----------|----|
| JDK/JRE   | 1.8+      | √  |
| 微信    | 3.9.11.25 | √  |

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

| 客户端版本         | 微信版本  |
|---------------|-------|
| 0.3.0_39.3.3  | [3.9.11.25](https://github.com/lich0821/WeChatFerry/releases/download/v39.3.3/WeChatSetup-3.9.11.25.exe) |

gui界面也设置对应微信版本下载链接

#### 下载文件

下载jar包 请在release中下载并运行jar包。

下载dll [发布的文件](https://github.com/lich0821/WeChatFerry/releases)

dll版本，请与本框架版本进行对应

如本框架为 0.3.0_39.3.3 根据版本命名规则：`该客户端版本_WeChatFerry版本` 需要下载v39.3.3下的v39.3.3.zip

### 运行

复制 config文件夹到jar包同级目录

根据gui界面修改配置文件【dll文件位置、[若智云Token](https://www.ruojy.top)】，启动即可

## 快速使用2

### 环境准备

| 名称    | 版本        | 备注 |
|-------|-----------|----|
| JDK/JRE   | 1.8+      | √  |
| Maven | 3.6+      | √  |
| 微信    | 3.9.11.25 | √  |

#### 下载文件

下载dll [发布的文件](https://github.com/lich0821/WeChatFerry/releases)

### 使用惯用 IDE，打开工程
以wechat-ferry-mvn为根目录打开

### 添加Maven

找到 wechat-ferry-mvn/pom.xml 文件，右键添加到Maven中，会自动下载依赖

### 替换对应版本的dll

把刚下载的最新发布文件解压到本项目中的 dll 文件目录下，直接替换原因文件即可

### 修改配置文件

配置文件：config/application.yaml

根据自己的dll目录位置修改配置文件

根据自己的[若智云](https://www.ruojy.top)Token修改token
```yaml
# 本服务参数
wechat:
  ferry:
    # DLL文件位置
    dll-path: E:\WeChatFerry\clients\java\wechat-ferry-mvn\dll\sdk.dll
    # socket端口
    socket-port: 10086

ruojy:
  token: YourToken
```
### 寻找插件
所有的插件我将会放在 插件中心[暂无地址](#)，大家需自行下载，后续将引入插件中心，允许开发者自行上传

没有插件也可以直接启动，就是没功能罢了
#### 写给插件开发者
插件开发文档请见：[插件开发接口工具包](https://gitee.com/xiaoyudi_xyz/wx-bot-dev-plugin)

### 编译运行

找到 src/main/java/com/wechat/ferry/WeChatFerryApplication.java 类

直接启动即可

### 访问

启动后springboot自身的端口为 9201 微信socket的端口为 10086

swagger地址：http://localhost:9201/swagger-ui/index.html

## 参与开发
🚫 非开发用户不需要往下看。

开发用户：可以根据文档和错误提示，自行解决编译错误的人员。
### 核心依赖

| 依赖            | 版本     | 说明       |
|---------------|--------|----------|
| Spring Boot   | 2.7.18 | 基础框架     |
| protobuf-java | 3.22.2 | rpc      |
| jna           | 5.6.0  | 态访问系统本地库 |
| nng-java      | 1.4.0  | 本地包      |
| fastjson2     | 2.0.52 | 序列化      |
| dom4j         | 2.1.3  | XML解析包   |
| httpclient    | 4.5.13 | 客户端请求    |

### 模块结构

```
wechat-ferry-mvn
├─dll                                       核心dll
│  ├─sdk.dll                                sdk文件
│  └─readme.txt                             本目录说明文件
│ 
├─src                                       源
│  ├─main                                   重启命令
│  │  ├─java(com.wechat.ferry)              java代码目录
│  │  │  ├─config                           配置
│  │  │  ├─entity                           聚合模型
│  │  │  │  ├─po                            数据库实体(与表结构一一对应,否则请使用DTO)
│  │  │  │  └─proto                         PB实体
│  │  │  ├─enums                            枚举
│  │  │  ├─handle                           处理层
│  │  │  ├─plugin                           插件管理
│  │  │  ├─service                          业务接口
│  │  │  │  └─impl                          业务实现类
│  │  │  ├─socket                           与若智云通信层交互
│  │  │  ├─swing                            swing/gui界面类
│  │  │  ├─utils                            工具类
│  │  │  └─WcferryApplication.java          启动类
│  │  │
│  │  │resources                            资源目录
│  │  │  ├─libs                             本程序内置依赖包
│  │  │  ├─plugins                          插件
│  │  │  ├─proto                            proto文件(此目录打包将被排除)
│  │  │  ├─win32-x86-64                     依赖程序
│  │  │  ├─application.yml                  本程序主配置文件
│  │  │  └─logback-spring.xml               日志配置文件
│ 
├─pom.xml                                   POM文件
└─README.MD                                 说明文件

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

## 客户端功能清单
- 插件化扩展功能
- 获取登录二维码
- 查询登录状态
- 获取登录账号信息
- 获取消息类型
- 获取联系人
- 获取可查询数据库
- 获取数据库所有表
- 获取语音消息
- 发送文本消息（可 @）
- 发送图片消息
- 发送文件消息
- 发送卡片消息
- 发送 XML 消息
- 发送 GIF 消息
- 拍一拍群友
- 转发消息
- 开启接收消息
- 关闭接收消息
- 查询数据库
- 获取朋友圈消息
- 下载图片、视频、文件
- 解密图片
- 添加群成员
- 删除群成员
- 邀请群成员

## 待做
- [ ] 插件中心

# 交流群
![交流群](group.png)

