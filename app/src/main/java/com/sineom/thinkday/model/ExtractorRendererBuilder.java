package com.sineom.thinkday.model;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.text.TextTrackRenderer;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;


/**
 * Created by ShuaiZhang on 2016/6/12.
 */
public class ExtractorRendererBuilder implements CustomPlayer.RendererBuilder {

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 256;

    private final Context context;
    private final String userAgent;
    private final Uri uri;

    public ExtractorRendererBuilder(Context context, String userAgent, Uri uri) {
        this.context = context;
        this.userAgent = userAgent;
        this.uri = uri;
    }

    @Override
    public void buildRenderers(CustomPlayer player) {
        //设置分配器大小
        Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
        Handler mainHandler = player.getMainHandler();
        //设置什么带宽
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(mainHandler,null);
        //提供媒体数据的组件
        DataSource dataSource = new DefaultUriDataSource(context,bandwidthMeter,userAgent);
        //样本资源器
        ExtractorSampleSource sampleSource = new ExtractorSampleSource(uri,dataSource,allocator,
                BUFFER_SEGMENT_SIZE * BUFFER_SEGMENT_COUNT , mainHandler,player,0);
        //多媒体解编码的视频渲染跟踪器
        MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(context,
                sampleSource, MediaCodecSelector.DEFAULT, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT,5000,
                mainHandler, player,50);

        player.onRenders(videoRenderer,bandwidthMeter);
    }

    @Override
    public void cancel() {
        //啥也不干
    }
}
