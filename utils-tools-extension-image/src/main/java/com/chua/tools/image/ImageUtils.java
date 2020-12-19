package com.chua.tools.image;

import com.chua.tools.image.builder.BuilderStream;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 图像工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/18
 */
public class ImageUtils {
    /**
     * 缩放
     *
     * @param file  文件
     * @param scale 缩放比例
     */
    public static void scale(final double scale, final File file) throws IOException {
        Thumbnails.of(file).scale(scale).toFile(file);
    }

    /**
     * 缩放
     *
     * @param file        文件
     * @param scaleWidth  缩放比例
     * @param scaleHeight 缩放比例
     */
    public static void scale(final double scaleWidth, final double scaleHeight, final File file) throws IOException {
        Thumbnails.of(file).scale(scaleWidth, scaleHeight).toFile(file);
    }

    /**
     * 缩放
     *
     * @param files 文件
     * @param scale 缩放比例
     */
    public static void scale(final double scale, final File... files) throws IOException {
        Thumbnails.of(files).scale(scale).toFiles(Arrays.asList(files));
    }

    /**
     * 缩放
     *
     * @param files       文件
     * @param scaleWidth  缩放比例
     * @param scaleHeight 缩放比例
     */
    public static void scale(final double scaleWidth, final double scaleHeight, final File... files) throws IOException {
        Thumbnails.of(files).scale(scaleWidth, scaleHeight).toFiles(Arrays.asList(files));
    }

    /**
     * 缩放
     *
     * @param file   文件
     * @param width  宽度
     * @param height 高度
     */
    public static void size(final int width, final int height, final File file) throws IOException {
        Thumbnails.of(file).size(width, height).toFile(file);
    }

    /**
     * 缩放
     *
     * @param files  文件
     * @param width  宽度
     * @param height 高度
     */
    public static void size(final int width, final int height, final File... files) throws IOException {
        Thumbnails.of(files).size(width, height).toFiles(Arrays.asList(files));
    }

    /**
     * 旋转
     *
     * @param files 文件
     * @param angle 角度
     */
    public static void rotate(final double angle, final File... files) throws IOException {
        Thumbnails.of(files).rotate(angle).toFiles(Arrays.asList(files));
    }

    /**
     * 旋转
     *
     * @param file  文件
     * @param angle 角度
     */
    public static void rotate(final double angle, final File file) throws IOException {
        Thumbnails.of(file).rotate(angle).toFile(file);
    }

    /**
     * stream
     *
     * @param file 文件
     * @return BuilderStream
     */
    public static BuilderStream<File> stream(final File file) throws IOException {
        Thumbnails.Builder<File> builder = Thumbnails.of(file);
        return BuilderStream.of(builder);
    }
}
