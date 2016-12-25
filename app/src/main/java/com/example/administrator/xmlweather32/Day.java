package com.example.administrator.xmlweather32;

/**
 * Created by Administrator on 2016/12/25.
 */
public class Day {
    public String type1;//天气情况
    public String fengxiang1;//风向
    public String fengli1;//风力


    public Day(){
        //白天天气
        type1="";
        fengxiang1="";
        fengli1="";
    }

    public String winfday(){
        String inf="";
        inf= inf+"白天："+type1+"，风向："+fengxiang1+"，风力："+fengli1;
        return  inf;
    };
}
