package com.github.peco2282.myapplication;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

  private TextView scoreLabel, startLabel;
  private ImageView box, orange, pink, black;

  private int frameHeight, boxSize;
  private int screenWidth, screenHeight;

  private float boxY;
  private float orangeX, orangeY;
  private float pinkX, pinkY;
  private float blackX, blackY;

  private int boxSpeed, orangeSpeed, pinkSpeed, blackSpeed;

  private int score = 0;

  private Handler handler = new Handler();
  private Timer timer = new Timer();

  private boolean action_flag = false;
  private boolean start_flag = false;

  private SoundPlayer soundPlayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    soundPlayer = new SoundPlayer(this);

    scoreLabel = findViewById(R.id.scoreLabel);
    scoreLabel = findViewById(R.id.scoreLabel);
    startLabel = findViewById(R.id.startLabel);
    box = findViewById(R.id.box);
    orange = findViewById(R.id.orange);
    pink = findViewById(R.id.pink);
    black = findViewById(R.id.black);

    WindowManager wm = getWindowManager();
    Display display = wm.getDefaultDisplay();

    Point size = new Point();
    display.getSize(size);

    screenWidth = size.x;
    screenHeight = size.y;

    boxSpeed = Math.round(screenHeight / 60F);
    orangeSpeed = Math.round(screenWidth / 60F);
    pinkSpeed = Math.round(screenWidth / 36F);
    blackSpeed = Math.round(screenWidth / 45F);

    orange.setX(-80.0F);
    orange.setY(-80.0F);

    pink.setX(-80.0F);
    pink.setY(-80.0F);

    black.setX(-80.0F);
    black.setY(-80.0F);
  }

  public void changePos() {

    hitCheck();

    orangeX -= orangeSpeed;
    if (orangeX < 0) {
      orangeX = screenWidth + 20;
      orangeY = (float) Math.floor(Math.random() * (frameHeight - orange.getHeight()));
    }
    orange.setX(orangeX);
    orange.setY(orangeY);

    blackX -= blackSpeed;
    if (blackX < 0) {
      blackX = screenWidth + 10;
      blackY = (float) Math.floor(Math.random() * (frameHeight - black.getHeight()));
    }
    black.setX(blackX);
    black.setY(blackY);

    pinkX -= pinkSpeed;
    if (pinkX < 0) {
      pinkX = screenWidth + 5000;
      pinkY = (float) Math.floor(Math.random() * (frameHeight - pink.getHeight()));
    }
    pink.setX(pinkX);
    pink.setY(pinkY);

    if (action_flag) {
      boxY -= 20;
    } else {
      boxY += 20;
    }

    if (boxY < 0) { boxY = 0; }

    if (boxY > frameHeight - boxSize) { boxY = frameHeight - boxSize; }

    box.setY(boxY);
  }

  private void hitCheck() {
    float orangeCenterX = orangeX + orange.getWidth() / 2;
    float orangeCenterY = orangeY + orange.getHeight() / 2;

    if (hitStatus(orangeCenterX, orangeCenterY)) {
      orangeX = -10.0F;
      score += 10;
      soundPlayer.playHitSound();
    }

    scoreLabel.setText("Score : " + score);

    float pinkCenterX = pinkX + pink.getWidth() / 2;
    float pinkCenterY = pinkY + pink.getHeight() / 2;

    if (hitStatus(pinkCenterX, pinkCenterY)) {
      pinkX = -10.0f;
      score += 30;
      soundPlayer.playHitSound();
    }

    float blackCenterX = blackX + black.getWidth() / 2;
    float blackCenterY = blackY + black.getHeight() / 2;

    if (hitStatus(blackCenterX, blackCenterY)) {
      if (timer != null) {
        timer.cancel();
        timer = null;
        soundPlayer.playOverSound();
      }

      Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
      intent.putExtra("SCORE", score);
      startActivity(intent);
    }
  }

  public boolean hitStatus(float centerX, float centerY) {
    return 0 <= centerX && centerX <= boxSize &&
            boxY <= centerY && centerY <= boxY + boxSize;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (!start_flag) {
      start_flag = true;

      FrameLayout frame = findViewById(R.id.frame);
      frameHeight = frame.getHeight();

      boxY = box.getY();
      boxSize = box.getHeight();

      startLabel.setVisibility(View.GONE);
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          handler.post(new Runnable() {
            @Override
            public void run() {
              changePos();
            }
          });
        }
      }, 0, 20);
    } else {
      if (event.getAction() == MotionEvent.ACTION_DOWN) {
        action_flag = true;
      } else if (event.getAction() == MotionEvent.ACTION_UP) {
        action_flag = false;
      }
    }
    return true;
  }

  @Override
  public void onBackPressed() {
  }
}