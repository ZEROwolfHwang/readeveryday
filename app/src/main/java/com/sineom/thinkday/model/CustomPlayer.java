package com.sineom.thinkday.model;

import android.media.MediaCodec;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;

import com.google.android.exoplayer.DummyTrackRenderer;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.util.PlayerControl;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ShuaiZhang on 2016/6/12.
 */
public class CustomPlayer implements ExoPlayer.Listener,//ExoPlayer播放器的回调
        ExtractorSampleSource.EventListener,//其他资源块处理接口
        MediaCodecVideoTrackRenderer.EventListener//视频解码
{


    public interface RendererBuilder {
        /**
         * 为播放器构建渲染
         *
         * @param player
         */
        void buildRenderers(CustomPlayer player);

        /**
         * 取消当前操作
         */
        void cancel();
    }

    /**
     * 核心事件监听器
     */
    public interface Listener {
        void onStateChanged(boolean playWhenReady, int playbackState);

        void onError(Exception e);

        void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                                float pixelWidthHeightRatio);
    }

    /**
     * 事件集合器，写在了这里，使用时会方便很多，不用再去一一实现那么多借口了
     * 未来使用时，一个监听器器顶N个
     * 当然了，为了方便管理，在自己理解的基础上可以对接口的回调进行分类
     */
    public interface InternalErrorListener {
        //初始化异常
        void onRendererInitializationError(Exception e);

        //解码异常
        void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException e);

        //加密异常
        void onCryptoError(MediaCodec.CryptoException e);

        //加载异常
        void onLoadError(int sourceId, IOException e);

        //降帧
        void onDroppedFrames(int count, long elapsed);

        void onDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs);

        ;
    }


    //ExoPlayer的一堆状态值
    public static final int STATE_IDLE = ExoPlayer.STATE_IDLE;//空闲
    public static final int STATE_PREPARING = ExoPlayer.STATE_PREPARING;//准备
    public static final int STATE_BUFFERING = ExoPlayer.STATE_BUFFERING;//拼接加载
    public static final int STATE_READY = ExoPlayer.STATE_READY;//准备好
    public static final int STATE_ENDED = ExoPlayer.STATE_ENDED;//结束
    public static final int TRACK_DISABLED = ExoPlayer.TRACK_DISABLED;//禁用
    public static final int TRACK_DEFAULT = ExoPlayer.TRACK_DEFAULT;//默认

    private final RendererBuilder rendererBuilder;
    private final ExoPlayer player;
    private final PlayerControl playerControl;
    private final Handler mainHandler;
    private final CopyOnWriteArrayList<Listener> listeners;


    private int rendererBuildingState;
    private int lastReportedPlaybackState;
    private boolean lastReportedPlayWhenReady;

    private static final int RENDERER_BUILDING_STATE_IDLE = 1;
    private static final int RENDERER_BUILDING_STATE_BUILDING = 2;
    private static final int RENDERER_BUILDING_STATE_BUILT = 3;

    private Surface surface;
    private TrackRenderer videoRenderer;
    private int videoTrackToRestore;

    private BandwidthMeter bandwidthMeter;
    private boolean backgrounded;


    public void setInternalErrorListener(InternalErrorListener internalErrorListener) {
        this.internalErrorListener = internalErrorListener;
    }

    private InternalErrorListener internalErrorListener;

    public CustomPlayer(RendererBuilder rendererBuilder) {
        this.rendererBuilder = rendererBuilder;
        player = ExoPlayer.Factory.newInstance(1, 1000, 5000);
        player.addListener(this);
        playerControl = new PlayerControl(player);
        mainHandler = new Handler();
        listeners = new CopyOnWriteArrayList<>();
    }


    public PlayerControl getPlayerControl() {
        return playerControl;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
        pushSurface(false);
    }

    public Surface getSurface() {
        return surface;
    }

    public void blockingClearSurface() {
        surface = null;
        pushSurface(true);
    }

    public int getTrackCount(int type) {
        return player.getTrackCount(type);
    }

    public com.google.android.exoplayer.MediaFormat getTrackFormat(int type, int index) {
        return player.getTrackFormat(type, index);
    }

    public int getSelectedTrack(int type) {
        return player.getSelectedTrack(type);
    }

    public void setSelectedTrack(int type, int index) {
        player.setSelectedTrack(type, index);
    }

    public boolean getBackgrounded() {
        return backgrounded;
    }

    public void setBackgrounded(boolean backgrounded) {
        if (this.backgrounded == backgrounded)
            return;
        this.backgrounded = backgrounded;
        if (backgrounded) {
            videoTrackToRestore = getSelectedTrack(0);
            setSelectedTrack(0, TRACK_DISABLED);
            blockingClearSurface();
        } else {
            setSelectedTrack(0, videoTrackToRestore);
        }
    }

    public void prepare() {
        if (rendererBuildingState == RENDERER_BUILDING_STATE_BUILT) {
            player.stop();
        }
        rendererBuilder.cancel();
//        videoFormat = null;
        videoRenderer = null;
        rendererBuildingState = RENDERER_BUILDING_STATE_BUILDING;
        maybeReportPlayerState();
        rendererBuilder.buildRenderers(this);
    }


    void onRenders(TrackRenderer renderers, BandwidthMeter bandwidthMeter) {
        if (renderers == null)
            renderers = new DummyTrackRenderer();
        this.videoRenderer = renderers;
//        this.codecCounters = videoRenderer instanceof MediaCodecTrackRenderer
//                ? ((MediaCodecTrackRenderer)videoRenderer).codecCounters : null;
        this.bandwidthMeter = bandwidthMeter;
        pushSurface(false);
        player.prepare(renderers);
        rendererBuildingState = RENDERER_BUILDING_STATE_BUILT;
    }

    void onRenderersError(Exception e) {
        if (internalErrorListener != null)
            internalErrorListener.onRendererInitializationError(e);
        for (Listener listener : listeners) {
            listener.onError(e);
        }
        rendererBuildingState = RENDERER_BUILDING_STATE_IDLE;
        maybeReportPlayerState();
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        player.setPlayWhenReady(playWhenReady);
    }

    public void seekTo(long position) {
        player.seekTo(position);
    }

    public void release() {
        rendererBuilder.cancel();
        rendererBuildingState = RENDERER_BUILDING_STATE_IDLE;
        surface = null;
        player.release();
    }


    public int getPlaybackState() {
        if (rendererBuildingState == RENDERER_BUILDING_STATE_BUILT)
            return STATE_PREPARING;
        int playerState = player.getPlaybackState();
        if (rendererBuildingState == RENDERER_BUILDING_STATE_BUILT && playerState == STATE_IDLE)
            return STATE_PREPARING;
        return playerState;
    }

    public long getDuration() {
        return player.getDuration();
    }

    public int getBufferedPercentage() {
        return player.getBufferedPercentage();
    }

    public boolean getPlayWhenReady() {
        return player.getPlayWhenReady();
    }

    Looper getPlaybackLooper() {
        return player.getPlaybackLooper();
    }

    Handler getMainHandler() {
        return mainHandler;
    }

    //    @Override
    public long getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void onLoadError(int sourceId, IOException e) {
        if (internalErrorListener != null)
            internalErrorListener.onLoadError(sourceId, e);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        maybeReportPlayerState();
    }

    @Override
    public void onPlayWhenReadyCommitted() {
        //Do nothing
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        rendererBuildingState = RENDERER_BUILDING_STATE_IDLE;
        for (Listener listener : listeners) {
            listener.onError(error);
        }
    }


    @Override
    public void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException e) {
        if (internalErrorListener != null)
            internalErrorListener.onDecoderInitializationError(e);
    }

    @Override
    public void onCryptoError(MediaCodec.CryptoException e) {
        if (internalErrorListener != null)
            internalErrorListener.onCryptoError(e);
    }

    @Override
    public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
        if (internalErrorListener != null)
            internalErrorListener.onDecoderInitialized(decoderName, elapsedRealtimeMs, initializationDurationMs);
    }

    @Override
    public void onDroppedFrames(int count, long elapsed) {
        if (internalErrorListener != null) {
            internalErrorListener.onDroppedFrames(count, elapsed);
        }
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        for (Listener listener : listeners) {
            listener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    @Override
    public void onDrawnToSurface(Surface surface) {
        // do nothing
    }

    private void maybeReportPlayerState() {
        boolean playWhenReady = player.getPlayWhenReady();
        int playBackState = getPlaybackState();
        if (lastReportedPlayWhenReady != playWhenReady || lastReportedPlaybackState != playBackState) {
            for (Listener listener : listeners) {
                listener.onStateChanged(playWhenReady, playBackState);
            }
            lastReportedPlayWhenReady = playWhenReady;
            lastReportedPlaybackState = playBackState;
        }
    }

    private void pushSurface(boolean blockForSurfacePush) {
        if (videoRenderer == null) {
            return;
        }

        if (blockForSurfacePush) {
            player.blockingSendMessage(videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);
        } else {
            player.sendMessage(videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);
        }
    }


}
