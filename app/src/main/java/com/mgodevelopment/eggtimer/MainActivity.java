package com.mgodevelopment.eggtimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SeekBar sbTimeSlider;
    private Button btnGo;
    private TextView tvCountDown;
    private MediaPlayer mMediaPlayer;
    private boolean isRunning;
    private CountDownTimer timer;
    private int selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isRunning = false;

        sbTimeSlider = (SeekBar) findViewById(R.id.sbTimeLeft);
        btnGo = (Button) findViewById(R.id.btnGo);
        tvCountDown = (TextView) findViewById(R.id.tvCountdown);
        mMediaPlayer = MediaPlayer.create(this, R.raw.buzzer);

        sbTimeSlider.setMax(60 * 10); // 10 minutes in seconds

        sbTimeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isUser) {

                String timeLeft = getCountdown(progress);
                tvCountDown.setText(timeLeft);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                selectedTime = sbTimeSlider.getProgress();
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int sbTimerProgress = sbTimeSlider.getProgress();

                if (sbTimerProgress > 0) {
                    isRunning = !isRunning;

                    int countdownTime = sbTimerProgress * 1000;
                    if (isRunning) {
                        btnGo.setText("Stop");
                        timer = new CountDownTimer(countdownTime, 1000) {

                            public void onTick(long millisecondsUntilDone) {
                                String timeLeft = getCountdown(millisecondsUntilDone / 1000);
                                tvCountDown.setText(timeLeft);
                                sbTimeSlider.setVisibility(View.INVISIBLE);
                            }

                            public void onFinish() {
                                isRunning = false;
                                mMediaPlayer.start();
                                resetTimer();
                            }

                        }.start();
                    } else {
                        if (timer != null) {
                            Toast.makeText(MainActivity.this, "Timer cancelled", Toast.LENGTH_SHORT).show();
                            timer.cancel();
                            resetTimer();
                        }

                    }


                } else {
                    Toast.makeText(MainActivity.this, "Please add time to the timer before starting", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void resetTimer() {
        sbTimeSlider.setVisibility(View.VISIBLE);
        btnGo.setText("Go!");
        sbTimeSlider.setProgress(selectedTime);
        String timeLeft = getCountdown(selectedTime);
        tvCountDown.setText(timeLeft);
    }

    private String getCountdown(long progress) {
        long minutes = progress / 60;
        long seconds = progress - (minutes * 60);
        String sSeconds = Long.toString(seconds);
        if (sSeconds.length() < 2)
            sSeconds = "0" + sSeconds;
        return Long.toString(minutes) + ":" + sSeconds;
    }
}
