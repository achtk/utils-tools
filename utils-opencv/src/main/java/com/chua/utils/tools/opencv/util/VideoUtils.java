package com.chua.utils.tools.opencv.util;

import com.google.common.base.Strings;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

/**
 * ffmpeg工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/24
 */
public class VideoUtils {
    /**
     * 转视频
     *
     * @param images 图片集合
     * @param video  视频
     */
    public static void toVideo(final File images, final File video) throws Exception {
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(video.getName(), 640, 480);
        //设置视频编码层模式
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        //设置视频为25帧每秒
        recorder.setFrameRate(25);
        //设置视频图像数据格式
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        recorder.setFormat(FilenameUtils.getExtension(video.getName()));
        recorder.start();
        //
        OpenCVFrameConverter.ToIplImage conveter = new OpenCVFrameConverter.ToIplImage();
        // 列出目录中所有的图片，都是jpg的，以1.jpg,2.jpg的方式，方便操作
        File[] files = images.listFiles();
        Arrays.stream(files).forEach(item -> {
            IplImage image = cvLoadImage(item.getAbsolutePath());
            try {
                recorder.record(conveter.convert(image));
            } catch (FrameRecorder.Exception ignore) {

            } finally {
                opencv_core.cvReleaseImage(image);
            }
        });
        recorder.stop();
        recorder.release();
    }

    /**
     * 转图片
     *
     * @param sourceFile 源文件
     * @param destFile   目标文件
     */
    public static void toPicture(File sourceFile, File destFile) throws FileNotFoundException {
        toPicture(new FileInputStream(sourceFile), FilenameUtils.getBaseName(sourceFile.getName()), FilenameUtils.getExtension(sourceFile.getName()), destFile);
    }

    /**
     * 转图片
     *
     * @param inputStream 源文件
     * @param destFile    目标文件
     * @param name        文件名
     * @param extension   文件后缀
     */
    public static void toPicture(InputStream inputStream, String name, String extension, File destFile) {
        //抓帧器
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inputStream);
        //帧的转换器
        Java2DFrameConverter converter = new Java2DFrameConverter();
        if (!destFile.exists()) {
            destFile.mkdirs();
        }
        //帧
        Frame frame = null;
        try {
            frameGrabber.start();
            String rotate = frameGrabber.getVideoMetadata("rotate");

            LongAdder longAdder = new LongAdder();
            while (true) {
                try {
                    frame = frameGrabber.grabFrame();
                    if (null == frame) {
                        break;
                    }
                    //创建BufferedImage对象
                    BufferedImage bufferedImage = converter.getBufferedImage(frame);
                    if (rotate != null) {
                        //旋转图片
                        bufferedImage = rotate(bufferedImage, Integer.parseInt(rotate));
                    }
                    ImageIO.write(bufferedImage, "jpeg", new File(destFile.getAbsolutePath(), name + longAdder.intValue() + "." + extension));
                    longAdder.increment();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                frameGrabber.stop();
                frameGrabber.release();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩
     *
     * @param sourceFile 源文件
     * @param destFile   目标文件
     * @param videoConf  配置参数
     */
    public static void compression(File sourceFile, File destFile, VideoConf videoConf) {
        //抓帧器
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(sourceFile.getAbsolutePath());
        //帧记录器
        FFmpegFrameRecorder recorder = null;
        //帧
        Frame frame = null;
        try {
            frameGrabber.start();
            //获取高度
            int height = frameGrabber.getImageHeight();
            //获取宽度
            int width = frameGrabber.getImageWidth();

            recorder = new FFmpegFrameRecorder(destFile, width * videoConf.getScale(), height * videoConf.getScale(), frameGrabber.getAudioChannels());
            recorder.setFrameRate(videoConf.getFrameRate() <= 0 ? frameGrabber.getFrameRate() : videoConf.getFrameRate());
            recorder.setSampleRate(frameGrabber.getSampleRate());
            if (null == videoConf.getVideoOption()) {
                recorder.setVideoOption("preset", "veryfast");
            } else {
                recorder.setVideoOptions(videoConf.getVideoOption());
            }
            // yuv420p,像素
            recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);

            if (Strings.isNullOrEmpty(videoConf.getFormat())) {
                recorder.setFormat(FilenameUtils.getExtension(sourceFile.getName()));
            } else {
                recorder.setFormat(videoConf.getFormat());
            }
            recorder.setVideoBitrate(frameGrabber.getVideoBitrate());
            recorder.start();

            while (true) {
                try {
                    frame = frameGrabber.grabFrame();
                    if (null == frame) {
                        break;
                    }
                    recorder.setTimestamp(frameGrabber.getTimestamp());
                    recorder.record(frame);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                recorder.stop();
                recorder.release();
                frameGrabber.stop();
                frameGrabber.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 旋转
     *
     * @param src   源图片
     * @param angel 角度
     * @return BufferedImage
     */
    private static BufferedImage rotate(BufferedImage src, int angel) {
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        int type = src.getColorModel().getTransparency();
        Rectangle rectangle = calcRotatedSize(new Rectangle(new Dimension(width, height)), angel);
        BufferedImage bi = new BufferedImage(rectangle.width, rectangle.height, type);
        Graphics2D g2 = bi.createGraphics();
        g2.translate((rectangle.width - width) / 2, (rectangle.height - height) / 2);
        g2.rotate(Math.toRadians(angel), width / 2, height / 2);
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return bi;
    }

    /**
     * 旋转信息
     *
     * @param src
     * @param angel
     * @return
     */
    private static Rectangle calcRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angelAlpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angelDaltaWidth = Math.atan((double) src.height / src.width);
        double angelDaltaHeight = Math.atan((double) src.width / src.height);
        int lenDaltaWidth = (int) (len * Math.cos(Math.PI - angelAlpha - angelDaltaWidth));
        int lenDaltaHeight = (int) (len * Math.cos(Math.PI - angelAlpha - angelDaltaHeight));
        int desWidth = src.width + lenDaltaWidth * 2;
        int desHeight = src.height + lenDaltaHeight * 2;
        return new Rectangle(new Dimension(desWidth, desHeight));
    }


    @Data
    public static class VideoConf {
        /**
         * 比例
         */
        private int scale = 1;
        /**
         * 帧率
         */
        private int frameRate;
        /**
         * 视频配置
         */
        private Map<String, String> videoOption;
        /**
         * 格式
         */
        private String format;
    }
}
