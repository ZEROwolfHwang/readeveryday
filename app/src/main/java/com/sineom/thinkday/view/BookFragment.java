package com.sineom.thinkday.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.Toast;

import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.util.Util;
import com.sineom.thinkday.R;
import com.sineom.thinkday.model.CustomPlayer;
import com.sineom.thinkday.model.ExtractorRendererBuilder;
import com.sineom.thinkday.model.KeyCompatibleMediaController;
import com.sineom.thinkday.present.PresentIml;
import com.sineom.thinkday.present.SingleFragment;

import butterknife.BindView;

/**
 * User: sineom(sineom@126.com)
 * Date: 2016-10-29
 * Time: 15:40
 * DESIC
 */
public class BookFragment extends SingleFragment implements SurfaceHolder.Callback,
        CustomPlayer.Listener {
    private MediaController mediaController;
    @BindView(R.id.sv_video)
    SurfaceView surfaceView;
    @BindView(R.id.video_frame)
    AspectRatioFrameLayout videoFrame;
    private CustomPlayer player;
    private long playerPosition;
    private boolean playerNeedsPrepare;
    private Uri contentUri;
    private PresentIml mPresentIml;
    @BindView(R.id.fl_root)
    FrameLayout root;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_book, container, false);
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresentIml = new PresentIml();
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    toggleControlsVisibility();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                }
                return true;
            }
        });

        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE
                        || keyCode == KeyEvent.KEYCODE_MENU)
                    return false;
                return mediaController.dispatchKeyEvent(event);
            }
        });


        surfaceView.getHolder().addCallback(this);
        mediaController = new KeyCompatibleMediaController(getActivity());
        mediaController.setAnchorView(root);
    }

    private void toggleControlsVisibility() {
        if (mediaController.isShowing()) {
            hideControls();
        } else {
            showControls();
        }
    }

    private CustomPlayer.RendererBuilder getRendererBuilder() {
        String userAgent = Util.getUserAgent(getActivity(), "ExoPlayerTest");
        return new ExtractorRendererBuilder(getActivity(), userAgent, contentUri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23)
            onShown();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null)
            onShown();
    }

//    @Override
//    public void onNewIntent(Intent intent) {
//        releasePlayer();
//    }

    private void releasePlayer() {
        if (player != null) {
            playerPosition = player.getCurrentPosition();
            player.release();
        }
    }


    private void onShown() {

        contentUri = Uri.parse("http://static.meiriyiwen.com/20161006.mp3");

        if (player == null) {
            if (!maybeRequestPermission()) {
                preparePlayer(true);
            }
        } else {
            player.setBackgrounded(false);
        }
    }

    private void preparePlayer(boolean playWhenReady) {
        if (player == null) {
            player = new CustomPlayer(getRendererBuilder());
            player.addListener(this);
            player.seekTo(playerPosition);
            playerNeedsPrepare = true;
            mediaController.setMediaPlayer(player.getPlayerControl());
            mediaController.setEnabled(true);

        }
        if (playerNeedsPrepare) {
            player.prepare();
            playerNeedsPrepare = false;
        }
        player.setSurface(surfaceView.getHolder().getSurface());
        player.setPlayWhenReady(playWhenReady);
    }


    @TargetApi(23)
    private boolean maybeRequestPermission() {
        if (requiresPermission(contentUri)) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return true;
        }

        return false;
    }

    private boolean requiresPermission(Uri contentUri) {
//        return Util.SDK_INT > 23 &&
//                Util.isLocalFileUri(contentUri) &&
//                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        return true;
    }


    private void showControls() {
        mediaController.show(0);
    }

    private void hideControls() {
        mediaController.hide();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (player != null)
            player.setSurface(holder.getSurface());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //啥也不干
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player != null)
            player.blockingClearSurface();
    }


    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onError(Exception e) {
        String errorString = null;
        if (e instanceof ExoPlaybackException && e.getCause() instanceof MediaCodecTrackRenderer.DecoderInitializationException) {
            MediaCodecTrackRenderer.DecoderInitializationException initializationException = (MediaCodecTrackRenderer.DecoderInitializationException) e.getCause();
            if (initializationException.decoderName == null) {
                if (initializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                    errorString = "找不到相关解码设备";
                } else if (initializationException.secureDecoderRequired) {
                    errorString = "当前设备未提供稳定解码格式：" + initializationException.mimeType;
                } else {
                    errorString = "当前设备未提供解码格式：" + initializationException.mimeType;
                }
            } else {
                errorString = "无法初始化编译器：" + initializationException.decoderName;
            }
        }
        if (errorString != null) {
            Toast.makeText(getActivity(), errorString, Toast.LENGTH_LONG).show();
        }
        playerNeedsPrepare = true;
        showControls();
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        videoFrame.setAspectRatio(height == 0 ? 1 : width * pixelWidthHeightRatio / height);
    }


}
