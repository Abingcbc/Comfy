# Comfy
<img src="./screenshots/icon.jpg" height="100px"/>



一个提升工作效率的app



## 目录

* [功能](#功能)
  * [账户管理与注册登录](#账户管理与注册登录)
  * [个人](#个人)
  * [团队](#团队)
  * [使用情况](#使用情况)
  * [设置](#设置)
  * [美观](#美观)
* [第三方库](#第三方库)
* [数据库](#数据库)



## 功能

### 账户管理与注册登录

- 注册与登录主界面

  <img src="./screenshots/Login.gif" height="600px">

- 用户管理侧栏

  <img src="./screenshots/navigation.gif" height="600px">

- 设置界面

  <img src="./screenshots/setting_page.jpg" height="600px">

- 用户信息和登出界面

  <img src="./screenshots/user_info.jpg" height="600px">

- 修改用户名和密码

  <img src="./screenshots/change_username.jpg" height="600px"><img src="./screenshots/change_password.jpg" height="600px">

- 修改头像

  <img src="./screenshots/change_avatar.jpg" height="600px">

### 个人

<img src="./screenshots/personal.png" height="400px"/>

* 添加个人任务列表

<img src="./screenshots/personal_plus.png" height="400px"/><img src="./screenshots/personal_task_list.png" height="400px"/>

* 添加个人任务

  <img src="./screenshots/personal_task_add.png" height="400px"/><img src="./screenshots/personal_add_activity.png" height="400px"/><img src="./screenshots/personal_task_without_notify.png" height="400px"/>

* 修改 删除 完成项目

  <img src="./screenshots/personal_task_edit.png" height="400px"/>

* 设置提醒

<img src="./screenshots/reminder_date.png" height="400px"/><img src="./screenshots/reminder_time.png" height="400px"/><img src="./screenshots/reminder.png" height="400px"/><img src="./screenshots/reminder_effect.png" height="400px"/>

* 编辑 删除 完成任务列表

<img src="./screenshots/personal_task_list_edit.png" height="400px"/><img src="./screenshots/personal_task_list_edit_input.png" height="400px"/><img src="./screenshots/personal_task_list_confirm.png" height="400px"/>

- 个人任务分享

  <img src="./screenshots/share.gif" height="600px">

### 团队

<img src="./screenshots/team.png" height="400px"/>

* 创建项目

<img src="./screenshots/team_project_add.png" height="400px"/><img src="./screenshots/team_project_added.png" height="400px"/>

* 添加任务列表

<img src="./screenshots/stage_plus.png" height="400px"/><img src="./screenshots/stage1.png" height="400px"/><img src="./screenshots/stage1_added.png" height="400px"/>

* 添加任务

<img src="./screenshots/team_task_add.png" height="400px"/><img src="./screenshots/team_task_added.png" height="400px"/>

* 为成员分配任务

<img src="./screenshots/assign_member.png" height="400px"/><img src="./screenshots/assign_finish.png" height="400px"/><img src="./screenshots/add_member_task.png" height="400px"/>

* 修改 删除 完成任务

<img src="./screenshots/edit_team_task.png" height="400px"/>



* 修改 删除 完成任务列表

<img src="./screenshots/edit_team_stage.png" height="400px"/>

* 修改 删除 完成项目

<img src="./screenshots/edit_team_project.png" height="400px"/>

* 项目变更通知

<img src="./screenshots/team_notify.png" height="400px"/><img src="./screenshots/team_notify_complete.png" height="400px"/>

- 团队定位

  <img src="./screenshots/location.gif" height="800px">

- 聊天室

  <img src="./screenshots/chat.gif" height="600px">

### 使用情况

### 美观

- 更换皮肤



## 第三方库

```groovy
implementation 'cn.leancloud.android:avoscloud-sdk:v4.7.3'
implementation('cn.leancloud.android:avoscloud-push:v4.7.3@aar') { transitive = true }
implementation 'cn.leancloud.android:avoscloud-statistics:v4.7.3'
implementation 'cn.leancloud.android:avoscloud-feedback:v4.7.3@aar'
implementation 'cn.leancloud.android:avoscloud-sns:v4.7.3@aar'
implementation 'cn.leancloud.android:qq-sdk:1.6.1-leancloud'
implementation 'cn.leancloud.android:avoscloud-search:v4.7.3@aar'
implementation 'androidx.appcompat:appcompat:1.0.2'
implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
testImplementation 'junit:junit:4.12'
implementation files('./jar/weibo-sdk-android-sso-3.0.1-leancloud.jar')
implementation files(
 './jar/AMap3DMap_6.5.0_AMapNavi_6.5.0_AMapSearch_6.5.0_AMapTrack_1.0.0_AMapLocation_4.4.0_20181122.jar'
)
implementation 'com.google.android.material:material:1.0.0'
implementation 'de.hdodenhof:circleimageview:2.2.0'
implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0-alpha'
implementation 'com.squareup.retrofit2:retrofit:2.4.0'
implementation 'com.squareup.retrofit2:converter-gson:2.4.0'
implementation 'com.squareup.okhttp3:okhttp:3.11.0'
implementation 'io.reactivex.rxjava2:rxjava:2.2.3'
implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
implementation 'com.squareup.retrofit2:adapter-rxjava2:2.4.0'
implementation 'com.squareup.okhttp3:logging-interceptor:3.11.0'
implementation 'com.github.shem8:material-login:2.1.1'
implementation 'com.google.code.gson:gson:2.8.5'
implementation 'com.loopeer.library:cardstack:1.0.2'
implementation 'org.greenrobot:greendao:3.2.2'
implementation 'com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar'
implementation 'com.leon:lsettingviewlibrary:1.7.0'
implementation 'com.github.yalantis:ucrop:2.2.2'
kapt 'com.github.bumptech.glide:compiler:4.8.0'
implementation 'com.github.bumptech.glide:glide:4.8.0'
implementation 'com.afollestad.material-dialogs:core:2.0.0-rc1'
implementation 'com.afollestad.material-dialogs:input:2.0.0-rc1'
implementation 'jp.wasabeef:recyclerview-animators:3.0.0'
implementation('com.alibaba.android:ultraviewpager:1.0.7.7@aar') {
    transitive = true
}
implementation 'pub.devrel:easypermissions:2.0.0'
implementation 'com.daimajia.swipelayout:library:1.2.0@aar'
implementation 'com.wdullaer:materialdatetimepicker:4.1.0'
implementation 'cn.leancloud.android:chatkit:1.0.6'
implementation 'com.ldoublem.loadingview:loadingviewlib:1.0'
implementation 'com.github.Mindinventory:OverlapImageGalleryView:1.0'
implementation 'com.bilibili:magicasakura:0.1.8@aar'
implementation 'com.nightonke:boommenu:2.1.1'
implementation 'com.github.YiiGuxing:CompositionAvatar:v1.0.4'
implementation 'com.pnikosis:materialish-progress:1.7'
implementation 'com.github.Kennyc1012:BottomSheet:2.4.1'
implementation 'com.ramotion.directselect:direct-select:0.1.0'
implementation 'com.github.YiiGuxing:CompositionAvatar:v1.0.4'
implementation 'com.github.paolorotolo:appintro:v5.1.0'
```



## 数据库

后端数据库使用第三方服务 LeanCloud

<img src="./screenshots/database.png" height="400px"/>