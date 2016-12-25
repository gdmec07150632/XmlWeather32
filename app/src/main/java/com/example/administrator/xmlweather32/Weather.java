package com.example.administrator.xmlweather32;
/**
 * Created by dell on 2016/12/20.
 * 此类保存天气预报信息，一个对象为一天的天气信息
 * 另外，可以参考此对象，建立子对象，保存一天内白天和晚上的天气信息
 * 也可以考虑扩展程序功能，建立相应的对象，保存xml中的指数信息，--在<zhishus>标记中
 */
public class Weather {
    public String date;//日期
    public String high;//高温
    public String low;//低温
    //白天天气，请自行补充，可以是对象，也可以是字符串

    //晚上天气，请自行补充，可以是对象，也可以是字符串

    public Weather(){ //构造函数
        //当前日期，温度
        date="";
        high="";
        low="";



    }

    public String winf(){
        String inf="";
        inf = inf+"日期："+date;
        return  inf;
    };
    public String winftem(){
        String inf="";
        inf= inf+"温度："+high+"，"+low;
        return  inf;
    };

}

