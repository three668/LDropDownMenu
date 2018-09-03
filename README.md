# LDropDownMenu
一个更简单易用、定制型更好的筛选菜单
一、功能简介：
1.可以自己设置背景颜色；
2.tab的文本颜色、字体大小等可自定义；
3.tab底部的线条大小、颜色等可自定义；
4.tab之间的分割线大小、颜色等可自定义；
5.tab右边的选中图标也可自定义等。

二、使用
在项目的build.gradle文件添加：
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
在Module的build.gradle文件添加：
dependencies {
	        implementation 'com.github.three668:LDropDownMenu:v1.0'
	}
想了解更多请参考demo.
