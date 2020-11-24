package com.chua.utils.tools.opencv.util;

import lombok.extern.slf4j.Slf4j;
import org.tensorflow.*;
import org.tensorflow.types.UInt8;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * tensor-flow
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/24
 */
@Slf4j
public class TensorFlowUtils {
    /**
     * 检测模型
     *
     * @param model 模型
     * @param image 图片
     * @return
     * @throws Exception
     */
    public static Double detect(String model, final String image) throws Exception {
        final int H = 224;
        final int W = 224;
        final float mean = 117f;
        final float scale = 1f;

        try (Graph g = new Graph()) {
            log.info("TensorFlow Version : {}", TensorFlow.version());
            g.importGraphDef(Files.readAllBytes(Paths.get(model)));
            GraphBuilder graphBuilder = new GraphBuilder(g);
            final Output<String> input = graphBuilder.constant("input", Files.readAllBytes(Paths.get(image)));
            final Output<Float> output =
                    graphBuilder.div(
                            graphBuilder.sub(
                                    graphBuilder.resizeBilinear(
                                            graphBuilder.expandDims(
                                                    graphBuilder.cast(graphBuilder.decodeJpeg(input, 3), Float.class),
                                                    graphBuilder.constant("make_batch", 0)),
                                            graphBuilder.constant("size", new int[]{H, W})),
                                    graphBuilder.constant("mean", mean)),
                            graphBuilder.constant("scale", scale));

            try (Session s = new Session(g)) {
                Tensor<Float> tensor = s.runner().fetch(output.op().name()).run().get(0).expect(Float.class);
                return tensor.doubleValue();
            }
        }
    }


    static class GraphBuilder {
        GraphBuilder(Graph g) {
            this.g = g;
        }

        Output<Float> div(Output<Float> x, Output<Float> y) {
            return binaryOp("Div", x, y);
        }

        <T> Output<T> sub(Output<T> x, Output<T> y) {
            return binaryOp("Sub", x, y);
        }

        <T> Output<Float> resizeBilinear(Output<T> images, Output<Integer> size) {
            return binaryOp3("ResizeBilinear", images, size);
        }

        <T> Output<T> expandDims(Output<T> input, Output<Integer> dim) {
            return binaryOp3("ExpandDims", input, dim);
        }

        <T, U> Output<U> cast(Output<T> value, Class<U> type) {
            DataType dtype = DataType.fromClass(type);
            return g.opBuilder("Cast", "Cast")
                    .addInput(value)
                    .setAttr("DstT", dtype)
                    .build()
                    .<U>output(0);
        }

        Output<UInt8> decodeJpeg(Output<String> contents, long channels) {
            return g.opBuilder("DecodeJpeg", "DecodeJpeg")
                    .addInput(contents)
                    .setAttr("channels", channels)
                    .build()
                    .<UInt8>output(0);
        }

        <T> Output<T> constant(String name, Object value, Class<T> type) {
            try (Tensor<T> t = Tensor.<T>create(value, type)) {
                return g.opBuilder("Const", name)
                        .setAttr("dtype", DataType.fromClass(type))
                        .setAttr("value", t)
                        .build()
                        .<T>output(0);
            }
        }

        Output<String> constant(String name, byte[] value) {
            return this.constant(name, value, String.class);
        }

        Output<Integer> constant(String name, int value) {
            return this.constant(name, value, Integer.class);
        }

        Output<Integer> constant(String name, int[] value) {
            return this.constant(name, value, Integer.class);
        }

        Output<Float> constant(String name, float value) {
            return this.constant(name, value, Float.class);
        }

        private <T> Output<T> binaryOp(String type, Output<T> in1, Output<T> in2) {
            return g.opBuilder(type, type).addInput(in1).addInput(in2).build().<T>output(0);
        }

        private <T, U, V> Output<T> binaryOp3(String type, Output<U> in1, Output<V> in2) {
            return g.opBuilder(type, type).addInput(in1).addInput(in2).build().<T>output(0);
        }

        private Graph g;
    }

}
