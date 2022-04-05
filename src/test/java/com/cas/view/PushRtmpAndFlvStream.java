package com.cas.view;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameRecorder.Exception;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import javax.swing.*;

/**
 * flv录制或者http-flv/rtmp推流
 *
 * @author eguid
 */
public class PushRtmpAndFlvStream {

    /**
     * flv-rtmp通用推流
     *
     * @param input     可以是动态图片(apng,gif等等)，视频文件（mp4,flv,avi等等）,流媒体地址（http-flv,rtmp，rtsp等等）
     * @param output    可以是flv,http-flv,rtmp
     * @param width     录制/推流的视频图像宽度
     * @param height    录制/推流的视频图像高度
     * @param frameRate 录制/推流的视频帧率
     */
    public static void pushFlvOrRtmp(String input, String output, Integer width, Integer height, Integer frameRate) throws Exception, org.bytedeco.javacv.FrameGrabber.Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input);
        grabber.start();

        if (width == null || height == null) {
            width = grabber.getImageWidth();
            height = grabber.getImageHeight();
        }

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, width, height, 2);

        //设置flv格式
        recorder.setFormat("flv");

        if (frameRate == null) {
            frameRate = 30;
        }
        recorder.setFrameRate(frameRate);//设置帧率
        //因为我们是直播，如果需要保证最小延迟，gop最好设置成帧率相同或者帧率*2
        //一个gop表示关键帧间隔，假设25帧/秒视频，gop是50，则每隔两秒有一个关键帧，播放器必须加载到关键帧才能够开始解码播放，也就是说这个直播流最多有2秒延迟
        recorder.setGopSize(frameRate * 2);//设置gop
        recorder.setVideoQuality(0); //视频质量
        recorder.setVideoBitrate(100);//码率
        recorder.setVideoOption("tune", "zerolatency");
        recorder.setVideoOption("preset", "ultrafast");
        recorder.setVideoOption("crf", "25");
        // 2000 kb/s, 720P视频的合理比特率范围
        recorder.setVideoBitrate(2000);
//		recorder.setVideoCodecName("h264");//设置视频编码
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);//这种方式也可以
//		recorder.setAudioCodecName("aac");//设置音频编码，这种方式设置音频编码也可以
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);//设置音频编码

        recorder.start();


        CanvasFrame canvas = new CanvasFrame("视频预览");// 新建一个窗口
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame frame = null;

        // 只抓取图像画面
        for (; (frame = grabber.grabImage()) != null; ) {
            try {
                //录制/推流
                recorder.record(frame);
                //显示画面
                canvas.showImage(frame);
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                e.printStackTrace();
            }
        }

        recorder.close();//close包含stop和release方法。录制文件必须保证最后执行stop()方法，才能保证文件头写入完整，否则文件损坏。
        grabber.close();//close包含stop和release方法
        canvas.dispose();
    }

    /**
     * flv-rtmp通用推流【无画板】
     *
     * @param output    可以是flv,http-flv,rtmp
     * @param width     录制/推流的视频图像宽度
     * @param height    录制/推流的视频图像高度
     * @param frameRate 录制/推流的视频帧率
     */
    public static void pushCamerOrRtmp(String output, Integer width, Integer height, Integer frameRate) throws Exception, org.bytedeco.javacv.FrameGrabber.Exception {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0); // 新建opencv抓取器，一般的电脑和移动端设备中摄像头默认序号是0，不排除其他情况
        grabber.start();

        if (width == null || height == null) {
            width = grabber.getImageWidth();
            height = grabber.getImageHeight();
        }

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, width, height, 2);

        //设置flv格式
        recorder.setFormat("flv");

        if (frameRate == null) {
            frameRate = 30;
        }
        recorder.setFrameRate(frameRate);//设置帧率
        //因为我们是直播，如果需要保证最小延迟，gop最好设置成帧率相同或者帧率*2
        //一个gop表示关键帧间隔，假设25帧/秒视频，gop是50，则每隔两秒有一个关键帧，播放器必须加载到关键帧才能够开始解码播放，也就是说这个直播流最多有2秒延迟
        recorder.setGopSize(frameRate * 2);//设置gop
        recorder.setVideoQuality(1.0); //视频质量
        recorder.setVideoBitrate(100);//码率
//		recorder.setVideoCodecName("h264");//设置视频编码
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);//这种方式也可以
//		recorder.setAudioCodecName("aac");//设置音频编码，这种方式设置音频编码也可以
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);//设置音频编码

        recorder.start();
        Frame frame = null;
        // 只抓取图像画面
        for (; (frame = grabber.grab()) != null; ) {
            try {
                //录制/推流
                recorder.record(frame);
                //显示画面
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                e.printStackTrace();
            }
        }
        recorder.close();//close包含stop和release方法。录制文件必须保证最后执行stop()方法，才能保证文件头写入完整，否则文件损坏。
        grabber.close();//close包含stop和release方法
    }

    /**
     * flv-rtmp通用推流【无画板】
     *
     * @param width     录制/推流的视频图像宽度
     * @param height    录制/推流的视频图像高度
     * @param frameRate 录制/推流的视频帧率
     */
    public static void pushCamerOrRtmpWithCanvas(Integer width, Integer height, Integer frameRate) throws Exception, org.bytedeco.javacv.FrameGrabber.Exception {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0); // 新建opencv抓取器，一般的电脑和移动端设备中摄像头默认序号是0，不排除其他情况
        grabber.start();
        CanvasFrame canvas = new CanvasFrame("摄像头");//新建一个窗口
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);
        Frame frame = null;
        // 只抓取图像画面
        for (; (frame = grabber.grab()) != null; ) {
            if(!canvas.isDisplayable()){//窗口是否关闭
                grabber.stop();//停止抓取
                System.exit(2);//退出
                break;
            }
            canvas.showImage(frame);
        }
        grabber.close();//close包含stop和release方法
    }

    public static void main(String[] args) throws Exception, org.bytedeco.javacv.FrameGrabber.Exception {
        //		pushFlvOrRtmp("rtmp://58.200.131.2:1935/livetv/dftv","rtmp://127.0.0.1/live/eguid",400,300,25);
//        pushFlvOrRtmp("http://zhibo.hkstv.tv/livestream/mutfysrq.flv", "eguid.flv", 400, 300, 25);
//		pushFlvOrRtmp("/Users/xianglong/Desktop/JavaCV/gsy.mp4","rtmp://localhost:1935/hls/movie",400,300,25);
//		pushFlvOrRtmp("/Users/xianglong/Desktop/cc.mp4","rtmp://localhost:1935/hls/movie",400,300,25);
//		pushFlvOrRtmp("/Users/xianglong/Desktop/cc.mp4","rtmp://123.56.254.218:1935/hls/movie",400,300,25);
		pushFlvOrRtmp("/Users/xianglong/Desktop/cc.mp4","rtmp://123.56.254.218:1935/live/test",400,300,50);
//        pushCamerOrRtmp("rtmp://123.56.254.218:1935/live/test",400,300,25);
//        pushCamerOrRtmpWithCanvas(400,300,25);
    }

}
