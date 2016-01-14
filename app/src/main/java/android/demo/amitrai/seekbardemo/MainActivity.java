package android.demo.amitrai.seekbardemo;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends Activity implements OnPreparedListener {
    /**
     * Called when the activity is first created.
     */
    private Button btnPlay;
    private Button btnPouse;
    private int current = 0;
    private boolean running = true;
    private int duration = 0;
    private MediaPlayer mPlayer;
    private SeekBar mSeekBarPlayer;
    private TextView mMediaTime;
    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run() {
            if (true == running) {
                if (mSeekBarPlayer != null) {
                    mSeekBarPlayer.setProgress(mPlayer.getCurrentPosition());
                }

                if (mPlayer.isPlaying()) {
                    mSeekBarPlayer.postDelayed(onEverySecond, 1000);
                    updateTime();
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music);
        btnPlay = (Button) findViewById(R.id.button1);
        btnPouse = (Button) findViewById(R.id.button2);
        mMediaTime = (TextView) findViewById(R.id.mediaTime);
        mSeekBarPlayer = (SeekBar) findViewById(R.id.progress_bar);
        mPlayer.setOnPreparedListener(this);

        mSeekBarPlayer.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPlayer.seekTo(progress);
                    updateTime();
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    mPlayer.prepare();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mPlayer.start();
                mSeekBarPlayer.postDelayed(onEverySecond, 1000);
            }
        });


        btnPouse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPlayer.pause();
            }
        });
    }

    private void updateTime() {
        do {
            current = mPlayer.getCurrentPosition();
            System.out.println("duration - " + duration + " current- "
                    + current);
            int dSeconds = (int) (duration / 1000) % 60;
            int dMinutes = (int) ((duration / (1000 * 60)) % 60);
            int dHours = (int) ((duration / (1000 * 60 * 60)) % 24);

            int cSeconds = (int) (current / 1000) % 60;
            int cMinutes = (int) ((current / (1000 * 60)) % 60);
            int cHours = (int) ((current / (1000 * 60 * 60)) % 24);

            if (dHours == 0) {
                mMediaTime.setText(String.format("%02d:%02d / %02d:%02d", cMinutes, cSeconds, dMinutes, dSeconds));
            } else {
                mMediaTime.setText(String.format("%02d:%02d:%02d / %02d:%02d:%02d", cHours, cMinutes, cSeconds, dHours, dMinutes, dSeconds));
            }

            try {
                Log.d("Value: ", String.valueOf((int) (current * 100 / duration)));
                if (mSeekBarPlayer.getProgress() >= 100) {
                    break;
                }
            } catch (Exception e) {
            }
        } while (mSeekBarPlayer.getProgress() <= 100);
    }

    @Override
    public void onPrepared(MediaPlayer arg0) {
        // TODO Auto-generated method stub
        duration = mPlayer.getDuration();
        mSeekBarPlayer.setMax(duration);
        mSeekBarPlayer.postDelayed(onEverySecond, 1000);
    }
}