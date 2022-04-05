package com.cas.view;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/4/5 2:47 下午
 * @desc macos/ios 录制屏幕基于 avfoundation [失败]
 */
public class JavavcFFGrabberTest {

    public static void main(String[] args) throws IOException {
        //读取视频源或者摄像头、屏幕等,eguid原创
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("/Users/xianglong/Desktop/JavaCV/gsy.mp4");
        grabber.start();

        FileOutputStream fos = new FileOutputStream("/Users/xianglong/Desktop/JavaCV/gsy.gif");
        //gif录制器，eguid原创文章，请勿转载
        FFmpegFrameRecorder recorder =new FFmpegFrameRecorder(fos, 10, 10,0);
        recorder.setPixelFormat(avutil.AV_PIX_FMT_RGB4_BYTE);//设置像素格式
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_GIF);//设置录制的视频/图片编码
//        if(frameRate!=null) {
            recorder.setFrameRate(50f);//设置帧率
//        }
        recorder.start();

        for(;;){
            //该操作完成了采集屏幕图像的操作，得到的是像素图像
            Frame frame=grabber.grab();
            recorder.record(frame);
        }
    }

    private static CanvasFrame getCanvasFrame() {
        CanvasFrame canvas = new CanvasFrame("摄像头");//新建一个窗口
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);
        return canvas;
    }

}
