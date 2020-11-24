package com.chua.utils.tools.opencv.util;

import com.google.common.base.Strings;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import static org.bytedeco.opencv.global.opencv_core.CV_32SC1;

/**
 * opencv工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/24
 */
public class OpencvUtils {

    /**
     * 相关性阈值，应大于多少，越接近1表示越像，最大为1
     */
    double HISTCMP_CORREL_THRESHOLD = 0.7;
    /**
     * 卡方阈值，应小于多少，越接近0表示越像
     */
    double HISTCMP_CHISQR_THRESHOLD = 2;
    /**
     * 交叉阈值，应大于多少，数值越大表示越像
     */
    double HISTCMP_INTERSECT_THRESHOLD = 1.2;
    /**
     * 巴氏距离阈值，应小于多少，越接近0表示越像
     */
    double HISTCMP_BHATTACHARYYA_THRESHOLD = 0.3;

    /**
     * 读取图片
     *
     * @param img 图片
     * @return Mat
     */
    public static Mat read(String img) {
        return opencv_imgcodecs.imread(img, 0);
    }

    /**
     * 加载检测器
     *
     * @param classifier 检测器
     * @return 检测器
     */
    public static CascadeClassifier classifier(String classifier) {
        return new CascadeClassifier(classifier);
    }

    /**
     * 灰度化
     *
     * @param image 图片
     * @return Mat
     */
    public static void grayscale(Mat image) {
        opencv_imgproc.cvtColor(image, image, Imgproc.COLOR_BGRA2GRAY);
    }

    /**
     * 直方均衡
     *
     * @param image 图片
     * @return Mat
     */
    public static void equalizeHist(Mat image) {
        opencv_imgproc.equalizeHist(image, image);
    }

    /**
     * 检测人脸
     *
     * @param image          图片
     * @param classifierPath 检测器
     * @return 检测的人脸集合
     */
    public static List<Mat> discernFace(String image, String classifierPath) {
        //加载图片
        Mat mat = read(image);
        //结果集
        RectVector faces = new RectVector();
        //识别文件
        String regPath = Strings.isNullOrEmpty(classifierPath) ? OpencvUtils.class.getProtectionDomain().getCodeSource().getLocation().toExternalForm() + "!/haarcascades_cuda/haarcascade_frontalface_alt.xml" : classifierPath;
        //初始化人脸检测器
        CascadeClassifier classifier = classifier(regPath);
        //灰度化
        grayscale(mat);
        //直方均衡
        equalizeHist(mat);
        //使用检测器进行检测，把结果放进集合中
        classifier.detectMultiScale(mat, faces);
        //返回结果集
        List<Mat> result = new ArrayList<>();
        //获取人脸 Mat
        for (int i = 0; i < faces.size(); i++) {
            Rect rect = faces.get(i);
            Mat rectMat = new Mat(mat, rect);

            result.add(rectMat);
        }

        return result;
    }

    /**
     * hsv转化
     *
     * @param mat 图片
     */
    public static void hsv(Mat mat) {
        // 转换成HSV
        cvtColor(mat, Imgproc.COLOR_BGR2HSV);
    }

    /**
     * 颜色通道转化
     *
     * @param mat  图片
     * @param code 颜色通道
     */
    public static void cvtColor(Mat mat, int code) {
        opencv_imgproc.cvtColor(mat, mat, code);
    }

    /**
     * 训练
     *
     * @param path       文件夹
     * @param outputFile 输出
     */
    public static void train(Map<Character, File> path, File outputFile) {
        //训练样本
        MatVector images = new MatVector(path.size());
        //标签
        Mat lables = new Mat(path.size(), 1, CV_32SC1);
        //写入标签值
        CharBuffer lablesBuf = lables.createBuffer();

        LongAdder longAdder = new LongAdder();
        for (Map.Entry<Character, File> entry : path.entrySet()) {
            Mat mat = read(entry.getValue().getAbsolutePath());
            lablesBuf.put(longAdder.intValue(), entry.getKey());
            images.put(longAdder.intValue(), mat);

            longAdder.increment();
        }

        //创建人脸分类器，有Fisher、Eigen、LBPH，选哪种自己决定，这里使用FisherFaceRecognizer
        FaceRecognizer fr = FisherFaceRecognizer.create();
        //训练
        fr.train(images, lables);
        //保存训练结果
        fr.save(outputFile.getAbsolutePath());
    }
}
