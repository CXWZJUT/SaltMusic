package com.salt.saltmusic;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by 咸味 on 2016/6/10.
 */
public class Home extends Activity  {

    private TabHost tabhost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salt);

        //得到TabHost对象实例
        tabhost =(TabHost) findViewById(R.id.mytab);
        //调用 TabHost.setup()
        tabhost.setup();
        //创建Tab标签
        tabhost.addTab(tabhost.newTabSpec("one").setIndicator("音乐列表").setContent(R.id.widget_layout_list));
        tabhost.addTab(tabhost.newTabSpec("two").setIndicator("正在播放").setContent(R.id.widget_layout_playing));
        tabhost.addTab(tabhost.newTabSpec("three").setIndicator("用户收藏").setContent(R.id.widget_layout_user));

    }


}
