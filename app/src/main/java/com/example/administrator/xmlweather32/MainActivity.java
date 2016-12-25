package com.example.administrator.xmlweather32;

import android.graphics.Color;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity{
    EditText value; //输入的城市名
    Button bt_sh;    //查询按钮
    LinearLayout tv_show;//显示查询结果
    String city = "广州";
    ScrollView scrollView;

    Vector<String> datev = new Vector<String>();
    Vector<String> temv = new Vector<String>();
    Vector<String> dayv = new Vector<String>();
    Vector<String> nightv = new Vector<String>();

    List<Weather> weathers = new ArrayList<Weather>();//建立天气对象列表，Weather为自行创建的保存天气信息的类
    Weather perday = null;//临时存储天气信息对象变量
    //Weather tem1 = null;//临时存储当前温度信息对象变量
    //Weather day1 = null;//临时存储白天天气信息对象变量
    //Weather night1 = null;//临时存储夜间天气信息对象变量
    List<Day> days = new ArrayList<Day>();//建立天气对象列表
    Day day1 = null;//临时存储白天天气信息对象变量
    List<Night> nights = new ArrayList<Night>();//建立天气对象列表
    Night night1 = null;//临时存储夜间天气信息对象变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("天气查询多层XML");
        value = (EditText)findViewById(R.id.et_city);
        bt_sh = (Button)findViewById(R.id.bt_sh);
        scrollView = (ScrollView)findViewById(R.id.sview);
        tv_show = (LinearLayout)findViewById(R.id.tv_show);
        value.setText("广州");//初值，方便测试
        bt_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city=value.getText().toString();
                GetWeather gd = new GetWeather(city);
                gd.start();
            }
        });
    }
    class GetWeather extends Thread{
        //本线程返回天气预报数据。
        private String urlstr= null; //保持天气预报接口网址信息
        public GetWeather(String cityname) {
            try {
                this.urlstr = "http://wthrcdn.etouch.cn/WeatherApi?city=" + URLEncoder.encode(cityname, "UTF-8");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    showData();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public void parseData(InputStream din) {

        //String[] str;//返回所有值
        //str = new String[4];
        XmlPullParser xmlPullParser = Xml.newPullParser();
        try {
            xmlPullParser.setInput(din, "UTF-8");
            int evtType = xmlPullParser.getEventType();
            while (evtType != XmlPullParser.END_DOCUMENT) { //如果没有到文档末尾的标记，继续读
                switch (evtType) {
                    case XmlPullParser.START_TAG:  //如果是xml标记的开始标签
                        String tag = xmlPullParser.getName();
                        if (tag.equalsIgnoreCase("weather")) {
                            perday = new Weather(); //读到一个记录一天天气信息的开始标记，新建一个对象。
                            //tem1 = new Weather();
                        } else if (perday != null) { //如果天气对象已经不为空，表示对象已经建立，但还没有保存完所有的天气信息细项，继续保存
                            if (tag.equalsIgnoreCase("date")) {
                                perday.date = xmlPullParser.nextText();
                                //datev.addElement(xmlPullParser.getText());
                                //读取标记的值，注意，不是读取属性，和教材的例子有区别
                                //tv_show.setBackgroundColor(Color.rgb(0,0,0));
                            }
                            if (tag.equalsIgnoreCase("high")) {
                                perday.high = xmlPullParser.nextText();//最高温

                            }
                            if (tag.equalsIgnoreCase("low")) {
                                perday.low = xmlPullParser.nextText();//最低温

                            }
                        }
                            if (tag.equalsIgnoreCase("day")) {
                                day1 = new Day();
                            }else if (day1!=null){
                                if (tag.equalsIgnoreCase("type")) {
                                    day1.type1 = xmlPullParser.nextText();//天气情况
                                }
                                if (tag.equalsIgnoreCase("fengxiang")) {
                                    day1.fengxiang1 = xmlPullParser.nextText();//风向

                                }
                                if (tag.equalsIgnoreCase("fengli")) {
                                    day1.fengli1 = xmlPullParser.nextText();//风力
                                }

                            } else if (tag.equalsIgnoreCase("night")) {
                                night1 = new Night();
                            }else if (night1!=null){
                                if (tag.equalsIgnoreCase("type")) {
                                    night1.type2 = xmlPullParser.nextText();//天气情况
                                }
                                if (tag.equalsIgnoreCase("fengxiang")) {
                                    night1.fengxiang2 = xmlPullParser.nextText();//风向
                                }
                                if (tag.equalsIgnoreCase("fengli")) {
                                    night1.fengli2 = xmlPullParser.nextText();//风力
                                }

                            }


                        break;
                    case XmlPullParser.END_TAG://读到结束标记，如</test>
                        tag = xmlPullParser.getName();
                        if (tag.equalsIgnoreCase("weather")) { //如果读到</weather>表示已经读完一天的天气信息
                            if (perday != null) {
                                weathers.add(perday);//加入到天气信息
                                perday = null;
                                //tem1 = null;
                            }
                        }
                        if(tag.equalsIgnoreCase("day")){ //如果读到</weather>表示已经读完一天的天气信息
                            if(day1!=null){
                                days.add(day1);//加入到天气信息列表
                                day1 = null;}
                        }
                        if(tag.equalsIgnoreCase("night")){ //如果读到</weather>表示已经读完一天的天气信息
                            if(night1!=null){
                                nights.add(night1);//加入到天气信息列表
                                night1 = null;}
                        }

                }
                evtType = xmlPullParser.next();

            }

            for(int i=0;i<weathers.size();i++){
                datev.addElement(weathers.get(i).winf());//输入日期信息
                temv.addElement(weathers.get(i).winftem());//输入当前天气
                }
            for (int i=0;i<days.size();i++){
                dayv.addElement(days.get(i).winfday());//输入每个对象白天的天气信息
            }
            for (int i=0;i<nights.size();i++){
                nightv.addElement(nights.get(i).winfnight());//输入每个对象夜间的天气信息
            }

        } catch (Exception ee) {
            ee.printStackTrace();
        }finally {
            //释放连接
            try {
                din.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //return str;
        }
        //return str;
    }
    @Override
    public void run(){
        datev.removeAllElements();
        temv.removeAllElements();
        dayv.removeAllElements();
        nightv.removeAllElements();
        try {
                URL url = new URL(urlstr);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(5000);//等待链接返回的时间为5秒

                int code = httpURLConnection.getResponseCode();
                if(code ==200){
                    InputStream in = httpURLConnection.getInputStream();
                    Message msg = new Message();
                    msg.what=1;
                    //msg.obj = parseData(in);
                    parseData(in);
                    handler.sendMessage(msg);
                }else{
                    //如果网址返回的数据不是200，表示没有正确获取信息，给出提示
                    Looper.prepare();
                    Toast.makeText(MainActivity.this,"获取数据失败，网络错误或输入有误！",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
                httpURLConnection.disconnect();
            }catch (Exception ee){
            }
        }
    }
public void showData(){
    tv_show.removeAllViews();//清除所存储旧的查询结果的组件
    tv_show.setOrientation(LinearLayout.VERTICAL);
    LayoutParams params = new LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    //params.width = 400;
    params.height = 30;
    for(int i=0;i<weathers.size();i++){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //日期
        TextView dateView = new TextView(this);
        dateView.setLayoutParams(params);
        dateView.setGravity(Gravity.CENTER_HORIZONTAL);
        dateView.setText(datev.elementAt(i));
        //dateView.setMovementMethod(ScrollingMovementMethod.getInstance());
        dateView.setBackgroundColor(Color.rgb(208, 208, 208));
        linearLayout.addView(dateView);
        //当前温度
        TextView temView = new TextView(this);
        temView.setLayoutParams(params);
        //temView.setMovementMethod(ScrollingMovementMethod.getInstance());
        temView.setText(temv.elementAt(i));
        linearLayout.addView(temView);
        //白天天气
        TextView dayView = new TextView(this);
        dayView.setLayoutParams(params);
        //dateView.setMovementMethod(ScrollingMovementMethod.getInstance());
        dayView.setText(dayv.elementAt(i));
        linearLayout.addView(dayView);
        //夜间天气
        TextView nightView = new TextView(this);
        nightView.setLayoutParams(params);
        //nightView.setMovementMethod(ScrollingMovementMethod.getInstance());
        nightView.setText(nightv.elementAt(i));
        linearLayout.addView(nightView);
        //添加到界面
        tv_show.addView(linearLayout);

    }
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
