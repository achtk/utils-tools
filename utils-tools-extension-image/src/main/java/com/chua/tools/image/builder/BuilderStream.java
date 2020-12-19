package com.chua.tools.image.builder;

import lombok.AllArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Coordinate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 构建流
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/18
 */
@AllArgsConstructor(staticName = "of")
public final class BuilderStream<T> {

    private Thumbnails.Builder<T> builder;

    /**
     * 大小
     *
     * @param width  宽度
     * @param height 高度
     * @return this
     */
    public BuilderStream<T> size(int width, int height) {
        builder.size(width, height);
        return this;
    }

    /**
     * 缩放
     *
     * @param scaleWidth  宽度
     * @param scaleHeight 高度
     * @return this
     */
    public BuilderStream<T> scale(double scaleWidth, double scaleHeight) {
        builder.scale(scaleWidth, scaleHeight);
        return this;
    }

    /**
     * 缩放
     *
     * @param scale 缩放
     * @return this
     */
    public BuilderStream<T> scale(double scale) {
        builder.scale(scale);
        return this;
    }

    /**
     * 旋转
     *
     * @param angle 角度
     * @return this
     */
    public BuilderStream<T> rotate(double angle) {
        builder.rotate(angle);
        return this;
    }

    /**
     * 高度
     *
     * @param height 高度
     * @return this
     */
    public BuilderStream<T> height(int height) {
        builder.height(height);
        return this;
    }

    /**
     * 宽度
     *
     * @param width 宽度
     * @return this
     */
    public BuilderStream<T> width(int width) {
        builder.width(width);
        return this;
    }

    /**
     * 是否允许覆盖
     *
     * @param allowOverwrite 是否允许覆盖
     * @return this
     */
    public BuilderStream<T> allowOverwrite(boolean allowOverwrite) {
        builder.allowOverwrite(allowOverwrite);
        return this;
    }

    /**
     * 水印
     *
     * @param position 位置
     * @param image    水印
     * @param opacity  透明度
     * @return this
     */
    public BuilderStream<T> watermark(Position position, BufferedImage image, float opacity) {
        builder.watermark(position, image, opacity);
        return this;
    }

    /**
     * 输出 BufferedImage {@link BufferedImage}
     *
     * @return BufferedImage
     */
    public BufferedImage asBufferedImage() throws IOException {
        return builder.asBufferedImage();
    }

    /**
     * 默认是按照比例缩放的
     *
     * @param keep 按照比例缩放的
     * @return this
     */
    public BuilderStream<T> keepAspectRatio(boolean keep) {
        builder.keepAspectRatio(keep);
        return this;
    }
    /**
     * 修改文件格式
     *
     * @param format 格式
     * @return this
     */
    public BuilderStream<T> outputFormat(String format) {
        builder.outputFormat(format);
        return this;
    }

    /**
     * 输出的图片质量
     *
     * @param quality 质量
     * @return this
     */
    public BuilderStream<T> outputFormat(float quality) {
        builder.outputQuality(quality);
        return this;
    }

    /**
     * 裁剪
     *
     * @param position 位置
     * @return this
     */
    public BuilderStream<T> crop(Position position) {
        builder.crop(position);
        return this;
    }

    /**
     * 裁剪
     *
     * @param x 位置
     * @param y 位置
     * @return this
     */
    public BuilderStream<T> crop(int x, int y) {
        builder.crop(new Coordinate(x, y));
        return this;
    }
    /**
     * 裁剪
     *
     * @param x 位置
     * @param y 位置
     * @return this
     */
    public BuilderStream<T> crop(int x, int y, int x1, int y1) {
        builder.sourceRegion(x, y, x1, y1);
        return this;
    }

    /**
     * 输出
     *
     * @param outFile 文件
     */
    public void toFile(File outFile) throws IOException {
        builder.toFile(outFile);
    }

    /**
     * 输出
     *
     * @param outFiles 文件
     */
    public void toFiles(File... outFiles) throws IOException {
        builder.toFiles(Arrays.asList(outFiles));
    }


    public enum Position implements net.coobird.thumbnailator.geometry.Position {
        /**
         * Calculates the {@link Point} at which an enclosed image should be placed
         * if it is to be placed at the top left-hand corner of the enclosing
         * image.
         */
        TOP_LEFT() {
            @Override
            public Point calculate(int enclosingWidth, int enclosingHeight,
                                   int width, int height, int insetLeft, int insetRight,
                                   int insetTop, int insetBottom) {

                int x = insetLeft;
                int y = insetTop;
                return new Point(x, y);
            }
        },

        /**
         * Calculates the {@link Point} at which an enclosed image should be placed
         * if it is to be horizontally centered at the top of the enclosing image.
         */
        TOP_CENTER() {
            @Override
            public Point calculate(int enclosingWidth, int enclosingHeight,
                                   int width, int height, int insetLeft, int insetRight,
                                   int insetTop, int insetBottom) {

                int x = (enclosingWidth / 2) - (width / 2);
                int y = insetTop;
                return new Point(x, y);
            }
        },

        /**
         * Calculates the {@link Point} at which an enclosed image should be placed
         * if it is to be placed at the top right-hand corner of the enclosing
         * image.
         */
        TOP_RIGHT() {
            @Override
            public Point calculate(int enclosingWidth, int enclosingHeight,
                                   int width, int height, int insetLeft, int insetRight,
                                   int insetTop, int insetBottom) {

                int x = enclosingWidth - width - insetRight;
                int y = insetTop;
                return new Point(x, y);
            }
        },

        /**
         * Calculates the {@link Point} at which an enclosed image should be placed
         * if it is to be placed vertically centered at the left-hand corner of
         * the enclosing image.
         */
        CENTER_LEFT() {
            @Override
            public Point calculate(int enclosingWidth, int enclosingHeight,
                                   int width, int height, int insetLeft, int insetRight,
                                   int insetTop, int insetBottom) {

                int x = insetLeft;
                int y = (enclosingHeight / 2) - (height / 2);
                return new Point(x, y);
            }
        },

        /**
         * Calculates the {@link Point} at which an enclosed image should be placed
         * horizontally and vertically centered in the enclosing image.
         */
        CENTER() {
            @Override
            public Point calculate(int enclosingWidth, int enclosingHeight,
                                   int width, int height, int insetLeft, int insetRight,
                                   int insetTop, int insetBottom) {

                int x = (enclosingWidth / 2) - (width / 2);
                int y = (enclosingHeight / 2) - (height / 2);
                return new Point(x, y);
            }
        },

        /**
         * Calculates the {@link Point} at which an enclosed image should be placed
         * if it is to be placed vertically centered at the right-hand corner of
         * the enclosing image.
         */
        CENTER_RIGHT() {
            @Override
            public Point calculate(int enclosingWidth, int enclosingHeight,
                                   int width, int height, int insetLeft, int insetRight,
                                   int insetTop, int insetBottom) {

                int x = enclosingWidth - width - insetRight;
                int y = (enclosingHeight / 2) - (height / 2);
                return new Point(x, y);
            }
        },

        /**
         * Calculates the {@link Point} at which an enclosed image should be placed
         * if it is to be placed at the bottom left-hand corner of the enclosing
         * image.
         */
        BOTTOM_LEFT() {
            @Override
            public Point calculate(int enclosingWidth, int enclosingHeight,
                                   int width, int height, int insetLeft, int insetRight,
                                   int insetTop, int insetBottom) {

                int x = insetLeft;
                int y = enclosingHeight - height - insetBottom;
                return new Point(x, y);
            }
        },

        /**
         * Calculates the {@link Point} at which an enclosed image should be placed
         * if it is to be horizontally centered at the bottom of the enclosing
         * image.
         */
        BOTTOM_CENTER() {
            @Override
            public Point calculate(int enclosingWidth, int enclosingHeight,
                                   int width, int height, int insetLeft, int insetRight,
                                   int insetTop, int insetBottom) {

                int x = (enclosingWidth / 2) - (width / 2);
                int y = enclosingHeight - height - insetBottom;
                return new Point(x, y);
            }
        },

        /**
         * Calculates the {@link Point} at which an enclosed image should be placed
         * if it is to be placed at the bottom right-hand corner of the enclosing
         * image.
         */
        BOTTOM_RIGHT() {
            @Override
            public Point calculate(int enclosingWidth, int enclosingHeight,
                                   int width, int height, int insetLeft, int insetRight,
                                   int insetTop, int insetBottom) {

                int x = enclosingWidth - width - insetRight;
                int y = enclosingHeight - height - insetBottom;
                return new Point(x, y);
            }
        },
    }
}
