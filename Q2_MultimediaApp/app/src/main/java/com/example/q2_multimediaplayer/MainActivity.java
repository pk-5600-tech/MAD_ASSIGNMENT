package com.example.q2_multimediaplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    MediaPlayer mediaPlayer;
    boolean isVideoActive = false;
    private static final int PICK_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.myVideoView);

        // FIX: Ye line video ko black screen se bachaati hai
        videoView.setZOrderOnTop(true);

        // a) Open Audio File from Disk
        findViewById(R.id.btnOpenFile).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            startActivityForResult(intent, PICK_FILE);
        });

        // b) Stream Video from URL
        findViewById(R.id.btnOpenUrl).setOnClickListener(v -> {
            stopAll();
            isVideoActive = true;
            videoView.setVisibility(View.VISIBLE);

            // Stable URL for testing
            String videoUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4";
            videoView.setVideoURI(Uri.parse(videoUrl));

            // FIX: Video ready hone ka wait karein
            videoView.setOnPreparedListener(mp -> {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                videoView.start();
                Toast.makeText(MainActivity.this, "Streaming Video...", Toast.LENGTH_SHORT).show();
            });

            videoView.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(MainActivity.this, "Error loading video", Toast.LENGTH_SHORT).show();
                return true;
            });
        });

        // PLAY Button
        findViewById(R.id.btnPlay).setOnClickListener(v -> {
            if (isVideoActive) {
                videoView.start();
            } else if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        });

        // PAUSE Button
        findViewById(R.id.btnPause).setOnClickListener(v -> {
            if (isVideoActive) {
                videoView.pause();
            } else if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });

        // STOP Button
        findViewById(R.id.btnStop).setOnClickListener(v -> stopAll());

        // RESTART Button
        findViewById(R.id.btnRestart).setOnClickListener(v -> {
            if (isVideoActive) {
                videoView.seekTo(0);
                videoView.start();
            } else if (mediaPlayer != null) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE && resultCode == RESULT_OK && data != null) {
            stopAll();
            isVideoActive = false;
            videoView.setVisibility(View.GONE);

            Uri audioUri = data.getData();
            mediaPlayer = MediaPlayer.create(this, audioUri);
            if (mediaPlayer != null) {
                mediaPlayer.start();
                Toast.makeText(this, "Playing Audio", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void stopAll() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        videoView.stopPlayback();
        videoView.setVisibility(View.GONE);
    }
}