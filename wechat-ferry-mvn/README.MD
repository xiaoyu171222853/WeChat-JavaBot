# WeChatFerry Java 客户端 maven版

⚠️ **只支持 Windows** ⚠️

`声明：` 本项目是基于 clients/java/wcferry 项目改造，随着时间推进，项目结构和代码规范逐渐产生分离，使用此项目的人员可参考之前的项目
我们在开发时请尽量保持注释的完整性，便于阅读维护

## 快速使用

### 环境准备

| 名称    | 版本        | 备注 |
|-------|-----------|----|
| JDK   | 1.8+      | √  |
| Maven | 3.8+      | √  |
| 微信    | 3.9.10.27 | √  |
| MySQL | 8.0+      | 备用 |

### 下载文件

* 下载 [最新发布的文件](https://github.com/lich0821/WeChatFerry/releases/latest)

### 使用惯用 IDE，打开工程

可以直接以WeChatFerry为根目录打开

或者以WeChatFerry/clients/java/wechat-ferry-mvn为根目录打开

### 添加Maven

找到 WeChatFerry/clients/java/wechat-ferry-mvn/pom.xml 文件，右键添加到Maven中，会自动下载依赖

### 替换对应版本的dll

把刚下载的最新发布文件解压到本项目中的 dll 文件目录下，直接替换原因文件即可

### 修改配置文件

配置文件：src/main/resources/application.yml

根据自己的dll目录位置修改配置文件

```yaml
# 本服务参数
wechat:
  ferry:
    # DLL文件位置
    dll-path: E:\WeChatFerry\clients\java\wechat-ferry-mvn\dll\sdk.dll
    # socket端口
    socket-port: 10086
```

### 数据库修改-目前未使用

为了更好的拓展应用，本客户端后续将采用mysql数据库，请自行安装mysql数据库

### 编译运行

找到 src/main/java/com/wechat/ferry/WeChatFerryApplication.java 类

直接启动即可

### 访问

启动后springboot自身的端口为 9201 socket的端口为 10086

swagger地址：http://localhost:9201/swagger-ui/index.html

## 参与开发

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
│  │  │  │  ├─dto                           DTO模型
│  │  │  │  ├─po                            数据库实体(与表结构一一对应,否则请使用DTO)
│  │  │  │  ├─proto                         PB实体
│  │  │  │  └─vo                            视图层返回体目录
│  │  │  ├─enums                            枚举
│  │  │  ├─handle                           处理层
│  │  │  ├─service                          业务接口
│  │  │  │  └─impl                          业务实现类
│  │  │  ├─utils                            工具类
│  │  │  └─WcferryApplication.java          启动类
│  │  │
│  │  │resources                            资源目录
│  │  │  ├─libs                             本程序内置依赖包
│  │  │  ├─proto                            proto文件(此目录打包将被排除)
│  │  │  ├─win32-x86-64                     依赖程序
│  │  │  ├─application.yml                  本程序主配置文件
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


# 接收消息梳理
个人
文本消息
WxPpMsgDTO(
isSelf=false, isGroup=false, id=8400857939565628747,
type=1, 消息类型：WcfMsgTypeEnum
ts=1734257241, 
roomId=wxid_np0svihvzw3o22,
content=[旺柴][旺柴][旺柴],
jsonContent=null, 
quoteContent=null, 
sender=wxid_np0svihvzw3o22

图片消息
WxPpMsgDTO(isSelf=false, isGroup=false, id=5753869847200309604, 
type=3, 
ts=1734258596, 
roomId=wxid_np0svihvzw3o22, 
content=<xml/>
, jsonContent=null, quoteContent=null, 
sender=wxid_np0svihvzw3o22,
sign=592cec710f83ea928d7e4dfce77ec35b, thumb=D:/wx-file/WeChat Files/wxid_hufez2ei3p6t22/FileStorage/MsgAttach/4d640de2c12fa788ff4345930cea1b49/Thumb/2024-12/28a23cba62cefe167ca14f01d7fee040_t.dat, extra=D:/wx-file/WeChat Files/wxid_hufez2ei3p6t22/FileStorage/MsgAttach/4d640de2c12fa788ff4345930cea1b49/Image/2024-12/77552f26def1a6dfc270afa5647190c2.dat, xml=<msgsource><bizflag>0</bizflag><tmp_node><publisher-id /></tmp_node><alnode><fr>4</fr></alnode><sec_msg_node><uuid>0f35d9e2951d5f4ec707c863436abb17_</uuid><risk-file-flag /><risk-file-md5-list /><alnode><fr>1</fr></alnode></sec_msg_node><imgmsg_pd cdnmidimgurl_size="175382" cdnmidimgurl_pd_pri="30" cdnmidimgurl_pd="0" /><signature>V1_tQjk/Z6y|v1_8ktAtWKx</signature></msgsource>
)

组群
WxPpMsgDTO(isSelf=false, isGroup=true, id=5442343913988420497,
type=1, 
ts=1734257495, 
roomId=19261157689@chatroom, 
content=八万坐水库是前人给后人留下的一笔政治利益和精神财富，世人是取之不尽，用之不竭的。, 
jsonContent=null, 
quoteContent=null, 
sender=wxid_qymz3zwvaqvp22)

组群引用文本消息
WxPpMsgDTO(isSelf=false, isGroup=true, id=5514944146084128728, 
type=49, 
ts=1734258098, 
roomId=22671412275@chatroom, 
content=？, 
jsonContent={"msg":{"appmsg":{"_sdkver":"0","title":"？","action":"view","type":"57","showtype":"0","forwardflag":"0","contentattr":"0","streamvideo":{"streamvideototaltime":"0"},"refermsg":{"type":"1","svrid":"1360075919322890042","fromusr":"22671412275@chatroom","chatusr":"wxid_hufez2ei3p6t22","displayname":"小宇在折腾","msgsource":"<msgsource>\n\t<sec_msg_node>\n\t\t<alnode>\n\t\t\t<fr>1</fr>\n\t\t</alnode>\n\t</sec_msg_node>\n\t<pua>1</pua>\n\t<silence>1</silence>\n\t<membercount>5</membercount>\n\t<signature>V1_yRhmYxEN|v1_yRhmYxEN</signature>\n\t<tmp_node>\n\t\t<publisher-id></publisher-id>\n\t</tmp_node>\n</msgsource>\n","content":"\n发张图片我瞅瞅","createtime":"1734258032"},"appattach":{"totallen":"0","islargefilemsg":"0"},"androidsource":"0","emoticongift":{"packageflag":"0"},"emoticonshared":{"packageflag":"0"},"designershared":{"designeruin":"0","designername":"null","designerrediretcturl":"null"},"emotionpageshared":{"tid":"0","title":"null","desc":"null","iconUrl":"null","secondUrl":"null","pageType":"0","setKey":"null"},"websearch":{"rec_category":"0","channelId":"0"},"weappinfo":{"appservicetype":"0","secflagforsinglepagemode":"0","videopageinfo":{"thumbwidth":"0","thumbheight":"0","fromopensdk":"0"}},"musicShareItem":{"musicDuration":"0"},"finderLiveProductShare":{"marketPrice":"0","sellingPrice":"0","flashSalePrice":"0","flashSaleEndTime":"0","firstProductTagAspectRatioString":"0.0","secondProductTagAspectRatioString":"0.0","isPriceBeginShow":"false"},"finderOrder":{"productsCount":"0"},"finderShopWindowShare":{"reputationInfo":{"hasReputationInfo":"0","reputationScore":"0"}},"findernamecard":{"auth_icon":"0"},"finderGuarantee":{"scene":"0"},"directshare":"0","patMsg":{"records":{"recordNum":"0"}},"secretmsg":{"issecretmsg":"0"},"referfromscene":"0","gameshare":{"liteappext":{"priority":"0"},"appbrandext":{"priority":"-1"},"isvideo":"0","duration":"-1","isexposed":"0"},"mpsharetrace":{"hasfinderelement":"0"},"wxgamecard":{"mbcardheight":"0","isoldversion":"0"},"liteapp":{"id":"null"}},"fromusername":"wxid_06lytxewf5bm22","scene":"0","appinfo":{"version":"1"}}}, quoteContent=null, sender=wxid_06lytxewf5bm22, sign=8e30010f3bb42211366383523a9e105d, thumb=, extra=D:/wx-file/WeChat Files/wxid_hufez2ei3p6t22/FileStorage/Cache/2024-12/9211b49a34a1b353ae5412bc69423b19, 

组群引用图片消息
WxPpMsgDTO(isSelf=false, isGroup=true, id=2955321972363926387, 
type=49, 
ts=1734258303, 
roomId=22671412275@chatroom, 
content=哪一年的, 
jsonContent={"msg":{"appmsg":{"_sdkver":"0","title":"哪一年的","action":"view","type":"57","showtype":"0","forwardflag":"0","contentattr":"0","streamvideo":{"streamvideototaltime":"0"},"refermsg":{"type":"3","svrid":"1684938875197651826","fromusr":"22671412275@chatroom","chatusr":"wxid_06lytxewf5bm22","displayname":"一起数星星～.～","msgsource":"<msgsource>\n\t<alnode>\n\t\t<fr>4</fr>\n\t</alnode>\n\t<sec_msg_node>\n\t\t<uuid>56e7defe0dd9001757687c651a066b6d_</uuid>\n\t\t<risk-file-flag />\n\t\t<risk-file-md5-list />\n\t</sec_msg_node>\n\t<imgmsg_pd cdnmidimgurl_size=\"84352\" cdnmidimgurl_pd_pri=\"50\" cdnmidimgurl_pd=\"1\" />\n\t<silence>1</silence>\n\t<membercount>5</membercount>\n\t<NotAutoDownloadRange>20:00-22:00;00:00-01:00</NotAutoDownloadRange>\n\t<signature>V1_QurX9rog|v1_yCLCxloW</signature>\n\t<tmp_node>\n\t\t<publisher-id></publisher-id>\n\t</tmp_node>\n</msgsource>\n","content":"<?xml version=\"1.0\"?>\n<msg>\n\t<img aeskey=\"d7004d1816e22c15ac434c955c068153\" encryver=\"1\" cdnthumbaeskey=\"d7004d1816e22c15ac434c955c068153\" cdnthumburl=\"3057020100044b30490201000204fb4b784902032fc67a02048f91c4740204675ea7b9042435363631393264312d626639352d343965612d383365322d356134376565633834343238020405290a020201000405004c518200\" cdnthumblength=\"3851\" cdnthumbheight=\"120\" cdnthumbwidth=\"68\" cdnmidheight=\"0\" cdnmidwidth=\"0\" cdnhdheight=\"0\" cdnhdwidth=\"0\" cdnmidimgurl=\"3057020100044b30490201000204fb4b784902032fc67a02048f91c4740204675ea7b9042435363631393264312d626639352d343965612d383365322d356134376565633834343238020405290a020201000405004c518200\" length=\"84352\" md5=\"f0bcff4efd03d11e152c9ec5b7661106\" hevc_mid_size=\"84352\" originsourcemd5=\"c4bbdca464deffd1089f03d9a6c78eea\">\n\t\t<secHashInfoBase64>eyJwaGFzaCI6IjMwMTAzMDIwMzAwMDEwMjAiLCJwZHFIYXNoIjoiYjM5MWIzOWIwNzcxNzkxYjc3\nMTljMTNiYmIzMzkzYjEzOWI5M2EzMzkzMzMxMzExZmIzM2EzMTFiMTEzMTUxMSJ9\n</secHashInfoBase64>\n\t</img>\n\t<platform_signature />\n\t<imgdatahash />\n\t<ImgSourceInfo>\n\t\t<ImgSourceUrl />\n\t\t<BizType>0</BizType>\n\t</ImgSourceInfo>\n</msg>\n","createtime":"1734256585"},"appattach":{"totallen":"0","islargefilemsg":"0"},"androidsource":"0","emoticongift":{"packageflag":"0"},"emoticonshared":{"packageflag":"0"},"designershared":{"designeruin":"0","designername":"null","designerrediretcturl":"null"},"emotionpageshared":{"tid":"0","title":"null","desc":"null","iconUrl":"null","secondUrl":"null","pageType":"0","setKey":"null"},"websearch":{"rec_category":"0","channelId":"0"},"weappinfo":{"appservicetype":"0","secflagforsinglepagemode":"0","videopageinfo":{"thumbwidth":"0","thumbheight":"0","fromopensdk":"0"}},"musicShareItem":{"musicDuration":"0"},"finderLiveProductShare":{"marketPrice":"0","sellingPrice":"0","flashSalePrice":"0","flashSaleEndTime":"0","firstProductTagAspectRatioString":"0.0","secondProductTagAspectRatioString":"0.0","isPriceBeginShow":"false"},"finderOrder":{"productsCount":"0"},"finderShopWindowShare":{"reputationInfo":{"hasReputationInfo":"0","reputationScore":"0"}},"findernamecard":{"auth_icon":"0"},"finderGuarantee":{"scene":"0"},"directshare":"0","patMsg":{"records":{"recordNum":"0"}},"secretmsg":{"issecretmsg":"0"},"referfromscene":"0","gameshare":{"liteappext":{"priority":"0"},"appbrandext":{"priority":"-1"},"isvideo":"0","duration":"-1","isexposed":"0"},"mpsharetrace":{"hasfinderelement":"0"},"wxgamecard":{"mbcardheight":"0","isoldversion":"0"},"liteapp":{"id":"null"}},"fromusername":"wxid_qy9tpxmbrta322","scene":"0","appinfo":{"version":"1"}}}, quoteContent=null, sender=wxid_qy9tpxmbrta322, sign=87d2bf38ef51cafc4f1916c10e7c32c5, thumb=, extra=D:/wx-file/WeChat Files/wxid_hufez2ei3p6t22/FileStorage/Cache/2024-12/0b65e53ab2d0fafcd3a14b97ea7c9230, 

组群表情
WxPpMsgDTO(isSelf=false, isGroup=true, id=8416935858064495465, 
type=47, 
ts=1734258149, 
roomId=22671412275@chatroom, 
content=<msg><emoji fromusername = "wxid_u4jgsydxzpok22" tousername = "22671412275@chatroom" type="2" idbuffer="media:0_0" md5="845960c4c43e87e125b84e0f227232f9" len = "231614" productid="" androidmd5="845960c4c43e87e125b84e0f227232f9" androidlen="231614" s60v3md5 = "845960c4c43e87e125b84e0f227232f9" s60v3len="231614" s60v5md5 = "845960c4c43e87e125b84e0f227232f9" s60v5len="231614" cdnurl = "http://vweixinf.tc.qq.com/110/20401/stodownload?m=845960c4c43e87e125b84e0f227232f9&amp;filekey=30440201010430302e02016e040253480420383435393630633463343365383765313235623834653066323237323332663902030388be040d00000004627466730000000132&amp;hy=SH&amp;storeid=26507a3be000aaf1e42172cc30000006e01004fb1534803c82bc1e65564508&amp;ef=1&amp;bizid=1022" designerid = "" thumburl = "" encrypturl = "http://vweixinf.tc.qq.com/110/20402/stodownload?m=b5d66646c84a4ea36f9988fbd73292c7&amp;filekey=30440201010430302e02016e040253480420623564363636343663383461346561333666393938386662643733323932633702030388c0040d00000004627466730000000132&amp;hy=SH&amp;storeid=26507a3be000b5e0542172cc30000006e02004fb2534803c82bc1e65564517&amp;ef=2&amp;bizid=1022" aeskey= "3c6e7cb7fedf47baaf43e0aea3e5c285" externurl = "http://vweixinf.tc.qq.com/110/20403/stodownload?m=1eb70dda1d229ecaae62fd6e4f002aee&amp;filekey=30440201010430302e02016e04025348042031656237306464613164323239656361616536326664366534663030326165650203008ee0040d00000004627466730000000132&amp;hy=SH&amp;storeid=26507a3be000c5ad642172cc30000006e03004fb3534803c82bc1e6556452a&amp;ef=3&amp;bizid=1022" externmd5 = "8c84ec3147029912910a0a6a48f2988f" width= "278" height= "278" tpurl= "" tpauthkey= "" attachedtext= "" attachedtextcolor= "" lensid= "" emojiattr= "" linkid= "" desc= "" ></emoji></msg>, jsonContent=null, quoteContent=null, sender=wxid_u4jgsydxzpok22, sign=9e67690682bd6499f8ddf0b5eb30c05e, thumb=, extra=D:/wx-file/WeChat Files/845960C4C43E87E125B84E0F227232F9
)

组群图片
WxPpMsgDTO(isSelf=false, isGroup=true, id=4882275502416840897, 
type=3, 
ts=1734258226,
roomId=22671412275@chatroom, 
content=<?xml version="1.0"?>
, jsonContent=null, quoteContent=null, sender=wxid_06lytxewf5bm22, sign=d7e9b2839a300e9c62294c234dd65b99, thumb=D:/wx-file/WeChat Files/wxid_hufez2ei3p6t22/FileStorage/MsgAttach/0abc37a7f8125f860e74ebe682f75f55/Thumb/2024-12/670007d460ced0d96feed023aee39854_t.dat, extra=D:/wx-file/WeChat Files/wxid_hufez2ei3p6t22/FileStorage/MsgAttach/0abc37a7f8125f860e74ebe682f75f55/Image/2024-12/4d5d56c2af04ef01fc40ba69b114c6d1.dat, xml=<msgsource><alnode><fr>5</fr></alnode><tmp_node><publisher-id /></tmp_node><sec_msg_node><uuid>0f35d9e2951d5f4ec707c863436abb17_</uuid><risk-file-flag /><risk-file-md5-list /><alnode><fr>1</fr></alnode></sec_msg_node><imgmsg_pd cdnmidimgurl_size="175745" cdnmidimgurl_pd_pri="30" cdnmidimgurl_pd="0" /><silence>0</silence><membercount>5</membercount><signature>V1_6kAjurG+|v1_qSXD7Whh</signature></msgsource>
)


公众号推送
WxPpMsgDTO(isSelf=false, isGroup=false, id=5333227579403642762, 
type=49, 
ts=1734257323, 
roomId=gh_f22ca81d71ff, 
content=后怕！5岁男童瞬间晕厥，筋都断了！街上常见，看见尽量远离, 
jsonContent={"msg":{"appmsg":{"_sdkver":"0","title":"后怕！5岁男童瞬间晕厥，筋都断了！街上常见，看见尽量远离","type":"5","showtype":"1","contentattr":"0","url":"http://mp.weixin.qq.com/s?__biz=MzI0Mjg4NjYzOA==&mid=2247815276&idx=1&sn=e9252769d2aa398d259b4f83efa1a152&chksm=e8b3b6fdad4975e66505d38f03c2881e7e58147bc1eb2fe6241d07d0c7549a8bce310a53a15d&scene=0&xtrack=1#rd","appattach":{"totallen":"0"},"mmreader":{"category":{"_type":"20","_count":"1","name":"中国反邪教","topnew":{"cover":"https://mmbiz.qpic.cn/mmbiz_jpg/YueB5DeNoGs25iaFLP0fVOZSVoOLicZ1B2kMYLdib1w8wxHibUNUvnKM1yIZYiaLKibGMcMU7F8X7LxoSKFiclNUGzXSg/640?wxtype=jpeg&wxfrom=0","width":"0","height":"0"},"item":{"has_redpacket_cover":"0","itemshowtype":"0","title":"后怕！5岁男童瞬间晕厥，筋都断了！街上常见，看见尽量远离","title_v2":"后怕！5岁男童瞬间晕厥，筋都断了！街上常见，看见尽量远离","url":"http://mp.weixin.qq.com/s?__biz=MzI0Mjg4NjYzOA==&mid=2247815276&idx=1&sn=e9252769d2aa398d259b4f83efa1a152&chksm=e8d9db4a0fa8380db2bf43a6f3a69d0ed777317d1eb54f1680d80e6b06d0228c87dfe451f67d&scene=0&xtrack=1#rd","pub_time":"1734256800","summary":"近日，在浙江嘉兴桐乡工作的解先生反映，自己5岁的孩子在一家店铺外玩耍时突遭意外受伤。据解先生描述，当时孩子在跟其他小朋友玩耍的过程中，有东西掉在商铺外的空调外机附近，捡东西时不知道触碰到了什么，被电击","cover":"https://mmbiz.qpic.cn/mmbiz_jpg/YueB5DeNoGs25iaFLP0fVOZSVoOLicZ1B2kMYLdib1w8wxHibUNUvnKM1yIZYiaLKibGMcMU7F8X7LxoSKFiclNUGzXSg/640?wxtype=jpeg&wxfrom=0","fileid":"0","sources":{"source":{"name":"中国反邪教"}},"del_flag":"0","contentattr":"0","play_length":"0","voice_type":"0","music_source":"0","pic_num":"0","comment_topic_id":"3768786484719796226","cover_235_1":"https://mmbiz.qpic.cn/mmbiz_jpg/YueB5DeNoGs25iaFLP0fVOZSVoOLicZ1B2kMYLdib1w8wxHibUNUvnKM1yIZYiaLKibGMcMU7F8X7LxoSKFiclNUGzXSg/640?wxtype=jpeg&wxfrom=0","cover_1_1":"https://mmbiz.qpic.cn/mmbiz_jpg/YueB5DeNoGs25iaFLP0fVOZSVoOLicZ1B2CCgwoxrVPyqIK5umwybtWpywofCzGS1cibHibETDicgLbv5w0AyfUIPjw/300?wxtype=jpeg&wxfrom=0","appmsg_like_type":"2","video_width":"0","video_height":"0","is_pay_subscribe":"0","finder_feed":{"feed_type":"0","media_count":"0"}}},"publisher":{"username":"gh_f22ca81d71ff","nickname":"中国反邪教"},"forbid_forward":"0"},"thumburl":"https://mmbiz.qpic.cn/mmbiz_jpg/YueB5DeNoGs25iaFLP0fVOZSVoOLicZ1B2kMYLdib1w8wxHibUNUvnKM1yIZYiaLKibGMcMU7F8X7LxoSKFiclNUGzXSg/640?wxtype=jpeg&wxfrom=0"},"fromusername":"gh_f22ca81d71ff","appinfo":{"appname":"中国反邪教","isforceupdate":"1"}}}, quoteContent=null, sender=gh_f22ca81d71ff,
