package id.ac.umn.jukebox;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button playBtn;
    Button pauseBtn;
    Button aboutBtn;
    MediaPlayer mp;
    ImageView imageview;
    RotateAnimation rotate;
    ObjectAnimator objectAnimator;
    ObjectAnimator lyrics;
    ScrollView lyric;
    ObjectAnimator lyricAnimator;
    int totalTime;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lyric = (ScrollView) this.findViewById(R.id.lyric);
        playBtn = (Button) this.findViewById(R.id.playBtn);
        pauseBtn = (Button) this.findViewById(R.id.pauseBtn);
        aboutBtn = (Button) this.findViewById(R.id.aboutBtn);
        imageview = (ImageView)findViewById(R.id.discrotate);
        // Rotate Image
        objectAnimator = ObjectAnimator.ofFloat(imageview ,
                "rotation", 0f, 360f);
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.setInterpolator(new LinearInterpolator());

        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                imageview.animate()
                        .alpha(0.0f)
                        .setDuration(1000);
            }
        });

        lyric.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });



        // Media Player
        mp = MediaPlayer.create(this,R.raw.music);
//        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f,0.5f);
        totalTime = mp.getDuration();


        lyric.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                lyric.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                lyricAnimator = ObjectAnimator.ofInt(lyric, "scrollY", lyric.getChildAt(0).getHeight() - lyric.getHeight());
                lyricAnimator.setStartDelay(4000);
                lyricAnimator.setDuration(mp.getDuration());
                lyricAnimator.setInterpolator(new LinearInterpolator());
            }
        });
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mp.start();
                objectAnimator.start();
                if(lyricAnimator.isPaused()){
                    lyricAnimator.resume();
                }else if(lyricAnimator.isRunning()){
                    lyricAnimator.isRunning();
                }
                else{
                    lyricAnimator.start();
                }
            }
        });
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openAbout();
            }
        });
        pauseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mp.pause();
                objectAnimator.removeAllListeners();
                objectAnimator.end();
                objectAnimator.cancel();
                lyricAnimator.pause();
            }
        });
    }
    public void openAbout(){
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }


}
