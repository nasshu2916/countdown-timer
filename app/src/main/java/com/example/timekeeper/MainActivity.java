package com.example.timekeeper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.view.WindowManager;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

	private NowTime nowtime;
	private TimeCount timecount;
	private Handler mHandler;
	private Timer mTimer;

	//補正する秒
	public int dsecond = 0;

	public int rectime_minute = 0;
	public int rectime_second = 0;
	public int rec_lasttime_minute = 0;
	public int rec_lasttime_second = 0;
	public int count_mode = 1;
	public int rec_counttime = 0;
	public int rec_countsecond = 0;

    public boolean lasttime_checkstts;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setContentViewの前でタイトル非表示
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//フルスクリーン化
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//バックライトを保持する（常時点灯）
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
		// 指定した ID のラジオボタンをチェックします

		// ラジオグループのチェック状態が変更された時に呼び出されるコールバックリスナーを登録します
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			// ラジオグループのチェック状態が変更された時に呼び出されます
			// チェック状態が変更されたラジオボタンのIDが渡されます
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton radioButton = (RadioButton) findViewById(checkedId);

				switch (checkedId){
					case 2131230738:
						count_mode = 1;
						Log.d("ラジオボタン", "無音");
						break;
					case 2131230739:
						count_mode = 2;
						Log.d("ラジオボタン", "残り時間");
						break;
					case 2131230740:
						count_mode = 3;
						Log.d("ラジオボタン", "0秒スタート");
						break;
					case 2131230741:
						count_mode = 4;
						Log.d("ラジオボタン", "30秒スタート");
						break;
					default:
						count_mode++;
						break;
				}
			}
		});



		nowtime = new NowTime(this);
		timecount = new TimeCount(this);
		mHandler = new Handler(getMainLooper());
		mTimer = new Timer();

		// 一秒ごとに定期的に実行します。
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				mHandler.post(new Runnable() {
					public void run() {
						nowtime.getnowtime();
						timecount.last_time();
					}
				});}
		},0,100);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick_set(View view) {
		//チェック状態を取得
		CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
		lasttime_checkstts = checkBox.isChecked();
		// EditTextオブジェクトを取得
		EditText editText = (EditText)findViewById(R.id.editText1);

		// 入力された文字を取得
		// 分を入力
		String text = editText.getText().toString();
		if (text.length() == 0) {
			text = "0";
		}
		//入力された文字列を数字に変換
		rectime_minute = Integer.parseInt(text);
		//秒を取得
		editText = (EditText)findViewById(R.id.editText2);
		text = editText.getText().toString();
		if (text.length() == 0) {
			text = "0";
		}
		rectime_second = Integer.parseInt(text);

		rec_lasttime_second = 30 - rectime_second;
		rec_lasttime_minute = 4 - rectime_minute;
		if(rec_lasttime_second < 0){
			rec_lasttime_second = 60 + rec_lasttime_second;
			rec_lasttime_minute--;
		}

	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 定期実行をcancelする
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}


}