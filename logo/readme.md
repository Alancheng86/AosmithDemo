
**使用ADB进行调试Android环境，更改设定等操作。**

记录一下AS软件调用ADB进行调试荣耀4  

**问题一 ：**  
**cmd无法调用adb指令 ** 
解决方法：  
找到AS软件的SDK目录，D:\SOFT\Android_SDK\platform-tools  
去里面找到ADB.exe文件，加入到系统环境变量之中，以便调用adb指令。。  

在‘我的电脑’->'属性'->‘高级’->'环境变量'下，系统变量中找到path变量，编辑添加adb.exe的路径，格式为：“;路径名”（添加路径为“”里面部分），添加的环境变量一定要在之前添加“;”与前面的环境变量隔开，路径为adb.exe在platform-toos下的路径

加入ADB路径后，CMD中ADB指令可以使用了。。若AS软件一时无法使用，则重启AS软件即可。   
 

**问题二：  
如何root手机**  
解决方法：  
重新插拔usb线，在手机弹出的调试选项中，勾选信任此台电脑进行连接，以便进行adb指令使用。  
adb  root指令输入后，
即可使用as软件进行再真机上调试app程序了。。  
使用lcdapp的程序代码，在真机环境上调试验证。  


ADB进入删除launch.apk(系统默认桌面)，放入自生成的apk替代原生桌面。  
adb shell--->进入ADB命令行界面  
进入到system/app路径，查找launch的apk，并删除之。  
尝试用  刷机精灵  取得root权限，看似成功了，测试中。。

++此贴说的方法尤其接地气，可以一步步跟着做。++  
http://blog.csdn.net/lsmsrc/article/details/46989971  
虽然仍然没能成功，但是猜测是root不完全导致的不成功，继续找方法root尝试。。  

有网友说k开头的root软件成功率高，准备试试。。  
https://tieba.baidu.com/p/4896597612   
下载kingroot和supersume进行尝试  

多次试验后，在supersume pro界面上跑了几次后，root看似成功了，但是指令只能运行一次，修改了system文件夹属性，成功，再想进行删除apk，却又失败了，报错是read only file system；只能暂时先搁置删除HwLaunch6.apk（系统桌面），  下一步使用root权限进行修改开关机动画。。。

删除开关机动画操作。（开机，在短暂的root权限下进行操作）   
开机；    
迅速连接adb  shell   
su  
先用mount指令查看system以及cust的文件读写属性  
mount -o rw,remount /dev/block/bootdevice/by-name/cust /cust   
mount -o rw,remount /dev/block/bootdevice/by-name/system /system    
rm -f /system/set_immutable.list    
进入如下路径找到开机动画  
/data/cust/media/bootanimation.zip    
在此路径下找到关机动画   
shutdownanimation.zip    

先修改读写权限  
chmod 777 bootanimation.zip  
chmod 777 shutdownanimation.zip   

然后删除之   
rm -f bootanimation.zip  
rm -f shutdownanimation.zip   

找到开机logo文件删除之    
/data/cust/logo/oemlogo.mbn   

cat /storage/emulated/0/Download/oemlogo.mbn > /data/cust/logo/oemlogo.mbn

chmod 777 /data/cust/logo/oemlogo.mbn     

rm -f /data/cust/logo/oemlogo.mbn  


加入自制的开机动画，   
/storage/emulated/0/Download/bootanimation.zip

复制文件到系统目录下   
cat /storage/emulated/0/Download/bootanimation.zip > /data/cust/media/bootanimation.zip

然后赋予文件权限。  
chmod 1247 bootanimation.zip  

重新开机查看效果。。   


目标：   
开机进入系统设置中，将app设置为主屏幕，并在内部存储的根目录下放置一个1.MP4的小视频，  
**初步效果是**  
开机后进入app界面，温度调节旋钮可以随着触摸而改变，在非旋钮区域触摸，向上滑动或者向下滑动可以改变背景色，从右向左滑动可以拉出下一个界面，展示出小视频。


未完成选项，未能将系统的桌面app删除，HwLauncher6.apk，暂时方法是，通过  设置  选项中  主屏幕 设定为app，达成开机显示app效果。

备注一下：  
https://www.cnblogs.com/zl1991/p/6378033.html   
adb remount后仍旧不能对system进行读写。需要进行adb disable-verity

在Android6.0 （Android M）userdebug版本上(eng版本不存在该问题)，发现使用adb remount 系统之后，还是不能对system分区进行操作，提示没有写权限，为只读文件系统Read-only file system




****修改开关机动画参照文本文档**： **      
bootanimation开机动画制作方法.txt

logo修改方法，参照   
**制作logo六部曲：**  
