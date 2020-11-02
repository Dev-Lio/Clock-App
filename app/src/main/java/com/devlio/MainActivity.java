package com.devlio;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 2020.11.03 Git 업로드
 *
 */
public class MainActivity extends AppCompatActivity {

    TextView dateTextView;
    TextView ampmTextView;
    TextView timeTextView;

    SimpleDateFormat dateFormat;
    SimpleDateFormat timeFormat;
    SimpleDateFormat ampmFormat;

    String dateStr;
    String ampmStr;
    String timeStr;

    Thread thread;

    private View decorView;
    private int	uiOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 가로 화면 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 상/하단바 제거 (몰입 모드)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 화면 자동 꺼짐 방지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 하단바 숨기기
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        dateTextView = findViewById(R.id.dateTextView);
        ampmTextView = findViewById(R.id.ampmTextView);
        timeTextView = findViewById(R.id.timeTextView);

        // 폰트 설정 (임의 고정)
        Typeface typeface = getResources().getFont(R.font.sky);
        dateTextView.setTypeface(typeface);
        ampmTextView.setTypeface(typeface);
        timeTextView.setTypeface(typeface);

        // 폰트 색상 설정 (임의 고정)
        int color = Color.parseColor("#FFE400");
        dateTextView.setTextColor(color);
        ampmTextView.setTextColor(color);
        timeTextView.setTextColor(color);

        // 날짜/시각 표기 방식 설정
        dateFormat = new SimpleDateFormat("yyyy. MM. dd (E)", Locale.getDefault());
        ampmFormat = new SimpleDateFormat("aa", Locale.getDefault());
        timeFormat = new SimpleDateFormat("hh : mm");

        // 시계 쓰레드
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                while (true){

                    // 현재시간을 msec 으로 구한다.
                    long now = System.currentTimeMillis();
                    // 현재시간을 date 변수에 저장한다.
                    Date nowDate = new Date(now);

                    dateStr = dateFormat.format(nowDate);
                    ampmStr = ampmFormat.format(nowDate);
                    timeStr = timeFormat.format(nowDate);

                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          if (!dateTextView.getText().equals(dateStr)){
                                              dateTextView.setText(dateStr);
                                          }
                                          if (!ampmTextView.getText().equals(ampmStr)){
                                              ampmTextView.setText(ampmStr);
                                          }
                                          if (!timeTextView.getText().equals(timeStr)){
                                              timeTextView.setText(timeStr);
                                          }
                                      }
                                  });

                    try {
                        // 쓰레드 실행 = 1초 간격
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);

        if( hasFocus ) {
            decorView.setSystemUiVisibility( uiOption );
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        thread.interrupt();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (thread.isInterrupted()){
            thread.start();
        }
    }
}
