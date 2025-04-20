# ViewfinderView

[![Download](https://img.shields.io/badge/download-App-blue.svg)](https://raw.githubusercontent.com/jenly1314/ViewfinderView/master/app/release/app-release.apk)
[![MavenCentral](https://img.shields.io/maven-central/v/com.github.jenly1314/viewfinderview)](https://repo1.maven.org/maven2/com/github/jenly1314/viewfinderview)
[![JitPack](https://jitpack.io/v/jenly1314/ViewfinderView.svg)](https://jitpack.io/#jenly1314/ViewfinderView)
[![CircleCI](https://circleci.com/gh/jenly1314/ViewfinderView.svg?style=svg)](https://circleci.com/gh/jenly1314/ViewfinderView)
[![API](https://img.shields.io/badge/API-21%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/license-Apche%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

ViewfinderView 是一个取景视图：主要用于渲染扫描相关的动画效果。其样式主要分为两大类：classic：经典样式（带扫描框）、popular：流行样式（不带扫描框）；可任意定制。

> 如果你之前有使用过 [ZXingLite](https://github.com/jenly1314/ZXingLite) 或 [MLKit](https://github.com/jenly1314/MLKit)，可能会用到其内部的 **ViewfinderView**。现已将 **ViewfinderView** 独立出来，便于后续统一维护。

> 以后[ZXingLite](https://github.com/jenly1314/ZXingLite)、[MLKit](https://github.com/jenly1314/MLKit)、[WeChatQRCode](https://github.com/jenly1314/WeChatQRCode) 发布的新版本都将直接使用此 **ViewfinderView** 作为通用的扫描渲染视图。

## Gif 效果展示
![Image](GIF.gif)

> 你也可以直接下载 [演示App](https://raw.githubusercontent.com/jenly1314/ViewfinderView/master/app/release/app-release.apk) 体验效果

## 引入

### Gradle:


1. 在Project的 **build.gradle** 或 **setting.gradle** 中添加远程仓库

    ```gradle
    repositories {
        //...
        mavenCentral()
    }
    ```

2. 在Module的 **build.gradle** 里面添加依赖项

    ```gradle
    implementation 'com.github.jenly1314:viewfinderview:1.3.0'

    ```

## 使用

### ViewfinderView属性说明

| 属性                       | 属性类型      | 默认值                                  | 属性说明                                            |
|:-------------------------|:----------|:-------------------------------------|:------------------------------------------------|
| vvViewfinderStyle        | enum      | classic                              | 取景框样式；支持：classic：经典样式（带扫描框）、popular：流行样式（不带扫描框） |
| vvMaskColor              | color     | <font color=#000000>#60000000</font> | 扫描区外遮罩的颜色                                       |
| vvFrameColor             | color     | <font color=#1FB3E2>#7F1FB3E2</font> | 扫描框边框的颜色                                        |
| vvFrameWidth             | dimension |                                      | 扫描框宽度                                           |
| vvFrameHeight            | dimension |                                      | 扫描框高度                                           |
| vvFrameRatio             | float     | 0.625f                               | 扫描框与屏幕占比,当未设置扫描框的宽高时，使用占比来计算宽高                  |
| vvFrameLineStrokeWidth   | dimension | 1dp                                  | 边框线宽度                                           |
| vvFramePaddingLeft       | dimension | 0                                    | 扫描框左边的内间距                                       |
| vvFramePaddingTop        | dimension | 0                                    | 扫描框上边的内间距                                       |
| vvFramePaddingRight      | dimension | 0                                    | 扫描框右边的内间距                                       |
| vvFramePaddingBottom     | dimension | 0                                    | 扫描框下边的内间距                                       |
| vvFrameGravity           | enum      | center                               | 扫描框对齐方式                                         |
| vvFrameCornerColor       | color     | <font color=#1FB3E2>#FF1FB3E2</font> | 扫描框边角的颜色                                        |
| vvFrameCornerSize        | dimension | 16dp                                 | 扫描框边角的大小                                        |
| vvFrameCornerStrokeWidth | dimension | 4dp                                  | 扫描框边角的描边宽度                                      |
| vvFrameCornerRadius      | dimension | 0dp                                  | 扫描框圆角半径                                         |
| vvFrameDrawable          | reference |                                      | 扫描框自定义图片                                        |
| vvLaserLineHeight        | dimension | 5dp                                  | 激光扫描线高度                                         |
| vvLaserMovementSpeed     | dimension | 2dp                                  | 激光扫描线的移动速度                                      |
| vvLaserAnimationInterval | integer   | 20                                   | 扫描动画延迟间隔时间，单位：毫秒                                |
| vvLaserGridColumn        | integer   | 20                                   | 网格激光扫描列数                                        |
| vvLaserGridHeight        | dimension | 40dp                                 | 网格激光扫描高度，为0dp时，表示动态铺满                           |
| vvLaserGridStrokeWidth   | dimension | 1dp                                  | 网格线条的宽                                          |
| vvLaserColor             | color     | <font color=#1FB3E2>#FF1FB3E2</font> | 扫描区激光线的颜色                                       |
| vvLaserStyle             | enum      | line                                 | 激光扫描的样式                                         |
| vvLaserDrawable          | reference |                                      | 激光扫描线自定义图片                                      |
| vvLaserDrawableRatio     | float     | 0.625f                               | 激光扫描图片与屏幕占比                                     |
| vvLabelText              | string    |                                      | 扫描提示文本信息                                        |
| vvLabelTextColor         | color     | <font color=#C0C0C0>#FFC0C0C0</font> | 提示文本字体颜色                                        |
| vvLabelTextSize          | dimension | 14sp                                 | 提示文本字体大小                                        |
| vvLabelTextPadding       | dimension | 24dp                                 | 提示文本距离扫描框的间距                                    |
| vvLabelTextWidth         | dimension |                                      | 提示文本的宽度，默认为View的宽度                              |
| vvLabelTextLocation      | enum      | bottom                               | 提示文本显示位置                                        |
| vvPointColor             | color     | <font color=#1FB3E2>#FF1FB3E2</font> | 结果点的颜色                                          |
| vvPointStrokeColor       | color     | <font color=#FFFFFF>#FFFFFFFF</font> | 结果点描边的颜色                                        |
| vvPointRadius            | dimension | 15dp                                 | 结果点的半径                                          |
| vvPointStrokeRatio       | float     | 1.2                                  | 结果点描边半径与结果点半径的比例                                |
| vvPointDrawable          | reference |                                      | 结果点自定义图片                                        |
| vvPointAnimation         | boolean   | true                                 | 是否显示结果点的动画                                      |
| vvPointAnimationInterval | integer   | 3000                                 | 结果点动画间隔时长；单位：毫秒                                 |
| vvFullRefresh            | boolean   | false                                | 是否完全刷新；适用于：ViewfinderStyle为：classic（经典样式）时      |

> ViewfinderView相关的自定义属性统一使用 **vv** 开头。

### 示例

#### 布局示例

ViewfinderView 相关的自定义属性可查看上面的 [ViewfinderView属性说明](#ViewfinderView属性说明)

```xml
 <com.king.view.viewfinderview.ViewfinderView
     android:id="@+id/viewfinderView"
     android:layout_width="match_parent"
     android:layout_height="match_parent"
     app:vvLaserStyle="line" />
```

更多使用详情，请查看[app](app)中的源码使用示例或直接查看 [API帮助文档](https://jenly1314.github.io/ViewfinderView/api/)

## 相关推荐

- [ZXingLite](https://github.com/jenly1314/ZXingLite) 基于zxing实现的扫码库，优化扫码和生成二维码/条形码功能。
- [MLKit](https://github.com/jenly1314/MLKit) 一个强大易用的工具包。通过ML Kit您可以很轻松的实现文字识别、条码识别、图像标记、人脸检测、对象检测等功能。
- [WeChatQRCode](https://github.com/jenly1314/WeChatQRCode) 基于OpenCV开源的微信二维码引擎移植的扫码识别库。
- [CameraScan](https://github.com/jenly1314/CameraScan) 一个简化扫描识别流程的通用基础库。
- [LibYuv](https://github.com/jenly1314/LibYuv) 基于Google的libyuv编译封装的YUV转换工具库，主要用途是在各种YUV与RGB之间进行相互转换、裁减、旋转、缩放、镜像等。

<!-- end -->

## 版本日志

#### v1.3.0：2025-4-20
* 新增属性：扫描框圆角半径`vvFrameCornerRadius`
* 新增属性：网格线条的宽`vvLaserGridStrokeWidth`
* 新增属性：是否完全刷新`vvFullRefresh`
* 优化一些细节

#### [查看更多日志](CHANGELOG.md)

## 赞赏
如果您喜欢ViewfinderView，或感觉ViewfinderView帮助到了您，可以点右上角“Star”支持一下，您的支持就是我的动力，谢谢 :smiley:<p>
您也可以扫描下面的二维码，请作者喝杯咖啡 :coffee:
<div>
   <img src="https://jenly1314.github.io/image/page/rewardcode.png">
</div>

## 关于我

| 我的博客                                                                                | GitHub                                                                                  | Gitee                                                                                  | CSDN                                                                                 | 博客园                                                                            |
|:------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------|
| <a title="我的博客" href="https://jenly1314.github.io" target="_blank">Jenly's Blog</a> | <a title="GitHub开源项目" href="https://github.com/jenly1314" target="_blank">jenly1314</a> | <a title="Gitee开源项目" href="https://gitee.com/jenly1314" target="_blank">jenly1314</a>  | <a title="CSDN博客" href="http://blog.csdn.net/jenly121" target="_blank">jenly121</a>  | <a title="博客园" href="https://www.cnblogs.com/jenly" target="_blank">jenly</a>  |

## 联系我

| 微信公众号        | Gmail邮箱                                                                          | QQ邮箱                                                                              | QQ群                                                                                                                       | QQ群                                                                                                                       |
|:-------------|:---------------------------------------------------------------------------------|:----------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------|
| [Jenly666](http://weixin.qq.com/r/wzpWTuPEQL4-ract92-R) | <a title="给我发邮件" href="mailto:jenly1314@gmail.com" target="_blank">jenly1314</a> | <a title="给我发邮件" href="mailto:jenly1314@vip.qq.com" target="_blank">jenly1314</a> | <a title="点击加入QQ群" href="https://qm.qq.com/cgi-bin/qm/qr?k=6_RukjAhwjAdDHEk2G7nph-o8fBFFzZz" target="_blank">20867961</a> | <a title="点击加入QQ群" href="https://qm.qq.com/cgi-bin/qm/qr?k=Z9pobM8bzAW7tM_8xC31W8IcbIl0A-zT" target="_blank">64020761</a> |

<div>
   <img src="https://jenly1314.github.io/image/page/footer.png">
</div>
