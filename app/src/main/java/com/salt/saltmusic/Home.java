package com.salt.saltmusic;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import java.util.ArrayList;
import android.text.format.Formatter;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import utils.Utils;

/**
 * Created by 咸味 on 2016/6/10.
 */
public class Home extends Activity {
    private ListView musiclist;
    private TextView nomusic;
    private TabHost tabhost;
    private ArrayList<MusicItem> musicItems;
    private Utils utils;
    private Handler handler = new Handler() {

        public void close() {
            if (musicItems != null && musicItems.size() > 0) {
                nomusic.setVisibility(View.GONE);
                musiclist.setAdapter(new MusicListAdapter());
            } else {
                //没有发现视频
                nomusic.setVisibility(View.VISIBLE);
            }
        }

        public void handleMessage(android.os.Message msg) {
            musiclist.setAdapter(new MusicListAdapter());


        }

        ;


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salt);

        //得到TabHost对象实例
        tabhost = (TabHost) findViewById(R.id.mytab);
        //调用 TabHost.setup()
        tabhost.setup();
        //创建Tab标签
        tabhost.addTab(tabhost.newTabSpec("one").setIndicator("音乐列表").setContent(R.id.widget_layout_list));
        tabhost.addTab(tabhost.newTabSpec("two").setIndicator("正在播放").setContent(R.id.widget_layout_playing));
        tabhost.addTab(tabhost.newTabSpec("three").setIndicator("用户收藏").setContent(R.id.widget_layout_user));
        musiclist = (ListView) findViewById(R.id.musiclist);
        nomusic = (TextView) findViewById(R.id.nomusic);
        musiclist.setOnItemClickListener(new MusicListItemClickListener());
        utils = new Utils();
        //加载音乐
        getAllMusic();


    }

    private class MusicListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return musicItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class MusicHolder {
            TextView name;
            TextView duration;
            TextView size;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            MusicHolder holder;
            if (convertView != null) {
                view = convertView;
                holder = (MusicHolder) view.getTag();
            } else {
                view = View.inflate(Home.this, R.layout.musiclist_item, null);
                holder = new MusicHolder();
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.duration = (TextView) view.findViewById(R.id.duration);
                holder.size = (TextView) view.findViewById(R.id.size);
                view.setTag(holder);
            }
            MusicItem audioItem = musicItems.get(position);
            holder.name.setText(audioItem.getTitle());
            holder.duration.setText(utils.stringForTime(Integer.valueOf(audioItem.getDuration())));
            holder.size.setText(Formatter.formatFileSize(Home.this, audioItem.getSize()));
            return view;

        }
    }

    //子线程
    private void getAllMusic() {
        new Thread() {
            public void run() {
                //把手机里的信息读取出来
                musicItems = new ArrayList<MusicItem>();
                ContentResolver resolver = getContentResolver();

                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] projection = {
                        MediaStore.Audio.Media.TITLE,//标题
                        MediaStore.Audio.Media.DURATION,//时间长度
                        MediaStore.Audio.Media.SIZE,//大小
                        MediaStore.Audio.Media.DATA//绝对播放地址
                };

                Cursor cursor = resolver.query(uri, projection, null, null, null);
                while (cursor.moveToNext()) {
                    //具体的音乐信息
                    MusicItem item = new MusicItem();
                    String title = cursor.getString(0);
                    item.setTitle(title);
                    String duration = cursor.getString(1);
                    item.setDuration(duration);
                    Long size = cursor.getLong(2);
                    item.setSize(size);
                    String data = cursor.getString(3);
                    item.setData(data);
                    musicItems.add(item);
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();


    }

    private class MusicListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if(mp3Infos != null) {
                Mp3Info mp3Info = mp3Infos.get(position);
                Log.d("mp3Info-->", mp3Info.toString());
                Intent intent = new Intent();
                intent.putExtra("url", mp3Info.getUrl());
                intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
                intent.setClass(Home.this, PlayerService.class);
                startService(intent);       //启动服务
            }
        }
    }
}





