package com.example.administrator.xmlweather32;

/**
 * Created by Administrator on 2016/12/25.
 */
public class Night {
    public String type2;//天气情况
    public String fengxiang2;//风向
    public String fengli2;//风力
    public Night(){
        //夜间天气
        type2="";
        fengxiang2="";
        fengli2="";
    }
    public String winfnight(){
        String inf="";
        inf= inf+"夜间："+type2+"，风向："+fengxiang2+"，风力："+fengli2;
        return  inf;
    };
}
