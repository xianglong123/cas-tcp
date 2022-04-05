package com.cas.view;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import javax.swing.*;
/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/4/4 1:03 下午
 * @desc openCV获取摄像头数据
 */
public class JavavcCameraTest{
    static OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

    public static void main(String[] args) throws Exception, InterruptedException{
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0); // 新建opencv抓取器，一般的电脑和移动端设备中摄像头默认序号是0，不排除其他情况
        grabber.start();   //开始获取摄像头数据
        CanvasFrame canvas = new CanvasFrame("摄像头");//新建一个窗口
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);
        int ex = 0;
        while(true){
            if(!canvas.isDisplayable()){//窗口是否关闭
                grabber.stop();//停止抓取
                System.exit(2);//退出
                break;
            }    
            canvas.showImage(grabber.grab());//获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像
//            Mat mat = converter.convertToMat(grabber.grabFrame());
//            ex++;
//            opencv_imgcodecs.imwrite("/Users/xianglong/IdeaProjects/cas-tcp/src/test/java/com/cas/view/" + ex + ".png", mat);
//            Thread.sleep(10);//50毫秒刷新一次图像
        }
    }
}