package com.example.timekeeper;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class NowTime {
    //現在時刻を表示

    private MainActivity mainactivity;

    int now_hour, now_minute, now_second, now_millosecond;
    String now_time;
    Calendar calendar;
    String countdown_minute = "00", countdown_second = "", countdown_millsecond = "";


    public NowTime(MainActivity mainActivity) {
        mainactivity = mainActivity;
    }

    public void getnowtime() {
        calendar = Calendar.getInstance();

        now_hour = calendar.get(Calendar.HOUR);
        now_minute = calendar.get(Calendar.MINUTE);
        now_second = calendar.get(Calendar.SECOND);
        now_millosecond = calendar.get(Calendar.MILLISECOND);
        // 秒を補正する
        now_second += mainactivity.dsecond;

        //２４時間表記に
        if (calendar.get(Calendar.AM_PM) == 1)
            now_hour += 12;

        if (now_hour < 10)
            now_time = "0" + now_hour + ":";
        else
            now_time = String.valueOf(now_hour) + ":";
        if (now_minute < 10)
            now_time = now_time + "0" + String.valueOf(now_minute) + ":";
        else
            now_time = now_time + String.valueOf(now_minute) + ":";
        if (now_second < 10)
            now_time = now_time + "0" + String.valueOf(now_second);
        else
            now_time = now_time + String.valueOf(now_second);


        ((TextView) mainactivity.findViewById(R.id.now_time)).setText(now_time);//文字の登録

        //点の登録
        if (now_millosecond < 500) {
            ((TextView) mainactivity.findViewById(R.id.clock2)).setText(" ");
        } else {
            ((TextView) mainactivity.findViewById(R.id.clock2)).setText(".");
        }

        countdown_millsecond = String.valueOf(10 - (now_millosecond / 100));

        switch (mainactivity.count_mode) {//残り時間の計算
            case 1://無音
                countdown_second = String.valueOf(55 - now_second - 1);
                break;
            case 2://残り時間
                if (mainactivity.lasttime_checkstts == false) {
                    countdown_second = String.valueOf(mainactivity.rectime_second - now_second - 1);
                } else {
                    if (now_millosecond < 500) {
                        countdown_millsecond = String.valueOf(5 - (now_millosecond / 100));
                        countdown_second = String.valueOf(mainactivity.rectime_second - now_second);
                    } else {
                        countdown_millsecond = String.valueOf(15 - (now_millosecond / 100));
                        countdown_second = String.valueOf(mainactivity.rectime_second - now_second - 1);
                    }
                }
                break;
            case 3://0秒
                countdown_second = String.valueOf(60 - now_second - 1);
                break;
            case 4://30秒
                countdown_second = String.valueOf(30 - now_second - 1);
                break;
            default:
                break;
        }

        if (Integer.parseInt(countdown_second) < 0)//0秒以下を表示しない
            countdown_second = String.valueOf(60 + Integer.parseInt(countdown_second));
        if (Integer.parseInt(countdown_second) < 10)//10秒以下は前に0を付ける
            countdown_second = "0" + countdown_second;
        if (Integer.parseInt(countdown_second) == 0)//0秒の時は00を表示
            countdown_second = "00";

        if (now_millosecond / 100 == 0)//ミリ秒が100以下の時は0
            countdown_millsecond = "0";

        //残り秒，ミリ秒を登録
        ((TextView) mainactivity.findViewById(R.id.LastTime)).setText("" + countdown_minute + ":" + countdown_second + ".");
        ((TextView) mainactivity.findViewById(R.id.LastTime_ss)).setText("" + countdown_millsecond + "");

    }
}
