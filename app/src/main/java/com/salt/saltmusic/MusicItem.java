package com.salt.saltmusic;

/**
 * Created by 咸味 on 2016/6/12.
 */
public class MusicItem {
    //标题
    private String title;
    //时常
    private String duration;
    //文件大小
    private Long size;
    //播放地址
    private String data;

    public void setTitle(String title){
        this.title=title;
    }
    public String getTitle(){
        return title;
    }
    public void setDuration(String duration){
        this.duration=duration;
    }
    public String getDuration(){
        return duration;
    }
    public void setSize(Long size){
        this.size=size;
    }
    public Long getSize(){
        return size;
    }
    public void setData(String data){
        this.data=data;
    }
    public String getData(){
        return data;
    }
    public String toString(){
        return" MusicItem[title"+title+",duration"+duration+",size"+size+",data"+data+"]";

    }
}
