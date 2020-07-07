package com.example.mymediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    public final MediaPlayer mediaPlayer = new MediaPlayer();

    public static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        checkPermission();
//        initButtons();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    initMediaPlayer();
                }else {


                    Log.e(TAG,"onRequestPermissionsResult 拒绝权限将无法使用程序");
                    Toast.makeText(this,"拒绝权限将无法使用程序",Toast.LENGTH_SHORT).show();
                    finish();

                }
        }
    }

    private void checkPermission() {
//        if (ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED){
//            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//            },1);
//        }else{
//            initMediaPlayer();
//        }

        initMediaPlayer();
    }

    private void initMediaPlayer() {

        try {

            Log.e(TAG,"initMediaPlayer !");

//            File file = new File("/data/music2.mp3");

            //   音乐全路径： /storage/emulated/0/Music/music.mp3
            File file = new File(Environment.getExternalStorageDirectory(), "music/music.mp3");
            String canonicalPath = file.getCanonicalPath();
            String path = file.getPath();
            Log.e(TAG," canonicalPath == " + canonicalPath );
            Log.e(TAG," path == " + path );

            boolean exists = file.exists();
            if (exists){

                mediaPlayer.setDataSource(file.getPath());
                mediaPlayer.prepare();
            }else {
                Log.e(TAG,"initMediaPlayer error! no music2.mp3 file!");
            }


            //String url = "http://audio04.dmhmusic.com/71_53_T10038887855_128_4_1_0_sdk-cpm/cn/0209/M00/B5/88/ChR4611KQRGARQt4ADk5_kURqfg289.mp3";
//            String url = "http://file.kuyinyun.com/group1/M00/90/B7/rBBGdFPXJNeAM-nhABeMElAM6bY151.mp3";
//            String url = "http://audio04.dmhmusic.com/198_168_T10038999067_320_4_1_0_sdk-cpm/cn/0208/M00/E1/E5/ChR47F18WreACCJaAJ4uDBvzHM0269.mp3";
//            String url = "http://audio04.dmhmusic.com/198_168_T10049785972_128_4_1_0_sdk-cpm/cn/0311/M00/68/CC/ChAKC115Xl-AQpiXAEmqU1ASc_M142.mp3";
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
//            mediaPlayer.setOnBufferingUpdateListener(this);
//            mediaPlayer.setOnPreparedListener(this);
//            mediaPlayer.setOnCompletionListener(this);
//            mediaPlayer.setDataSource(url); // 设置数据源
//            mediaPlayer.prepare(); // prepare自动播放


        } catch (Exception e) {
            Log.e(TAG,"initMediaPlayer error!");
            e.printStackTrace();
        }

    }

    private void initButtons() {
        Button play = (Button) findViewById(R.id.play);
        Button pause = (Button) findViewById(R.id.pause);
        Button stop = (Button) findViewById(R.id.stop);
        MyOnClickListener listener = new  MyOnClickListener();
        play.setOnClickListener(listener);
        pause.setOnClickListener(listener);
        stop.setOnClickListener(listener);

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        Log.e(TAG, "onBufferingUpdate");
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.e(TAG, "onCompletion");
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        Log.e(TAG, "onPrepared");
        mediaPlayer.start();
    }


    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.play:
                    if(!mediaPlayer.isPlaying()){
                        Log.e(TAG, "start.......................");
                        mediaPlayer.start();
                    }
                    break;
                case R.id.pause:
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }
                    break;

                case R.id.stop:
                    if(mediaPlayer.isPlaying()){
//                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        initMediaPlayer();
                    }
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mediaPlayer){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
