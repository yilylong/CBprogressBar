# CBprogressBar
一个简单的自定义进度条，支持三种样式
===========

##ImageProgressBar

<iframe height=498 width=510 src='/imageprogressbar.mp4' frameborder=0 'allowfullscreen'></iframe>


![](https://raw.githubusercontent.com/yilylong/ImageResource/master/cbprogressbar.gif)  

useage 
----
    <com.zhl.cbprogressbar.view.CBProgressBar
        android:id="@+id/my_progress2"
        android:layout_width="match_parent"
        android:layout_height="15dp"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="20dp"
        cb:isHorizonStroke="true"
        cb:orientation="horizontal"
        cb:percent_text_color="@color/percent_text_color_2"
        cb:percent_text_size="@dimen/percent_text_size_large"
        cb:progressBarBgColor="@color/progressbar_bg_color_1"
        cb:progressColor="@color/progress_color_1"
        cb:rect_round="@dimen/horizontal_corner" />
        
        
        <com.zhl.rx.views.ImageProgressBar
        android:id="@+id/progressBar"
        android:layout_width="240dp"
        android:layout_height="160dp"
        app:coverColor="@color/progress_cover_color"
        app:imagebg="@mipmap/progressbar_bg"
        app:roundConer="5dp"
        app:styleMode="mode_image_show" />

具体属性功能下载demo后调试便知
