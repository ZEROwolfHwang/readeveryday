package com.sineom.thinkday.model;


import android.content.Context;
import android.view.KeyEvent;
import android.widget.MediaController;

/**
 * Created by ShuaiZhang on 2016/6/14.
 */
public class KeyCompatibleMediaController extends MediaController {
    private MediaPlayerControl control;
    public KeyCompatibleMediaController(Context context) {
        super(context);
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        super.setMediaPlayer(player);
        control = player;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (control.canSeekForward() && (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)){
            //点击前进5秒，这个可以自由设置
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                control.seekTo(control.getCurrentPosition() + 5000); //unit:millsecond
                show();
            }
            return true;
        }else if (control.canSeekBackward() && (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD || keyCode == KeyEvent.KEYCODE_DPAD_LEFT)){
            //点击后退5秒
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                control.seekTo(control.getCurrentPosition() - 5000);
                show();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
