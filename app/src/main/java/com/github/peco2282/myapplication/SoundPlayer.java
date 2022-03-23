package com.github.peco2282.myapplication;

import android.content.Context;
import android.icu.util.RangeValueIterator;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayer {

  private static SoundPool soundPool;
  private static int hitSound, overSound;

  public SoundPlayer(Context context) {
    soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
    hitSound = soundPool.load(context, R.raw.hit, 1);
    overSound = soundPool.load(context, R.raw.over, 1);
  }

  public static void playHitSound() {
    soundPool.play(hitSound, 1.0F, 1.0F, 1, 0, 1.0F);
  }

  public static void playOverSound() {
    soundPool.play(overSound, 1.0F, 1.0F, 1, 0, 1.0F);
  }
}
