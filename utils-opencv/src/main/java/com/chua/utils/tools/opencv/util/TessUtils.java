package com.chua.utils.tools.opencv.util;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.TessBaseAPI;

import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.leptonica.global.lept.pixRead;

/**
 * tessbase工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/24
 */
public class TessUtils {
    /**
     * orc
     *
     * @param language 语言
     * @param dataPath 数据路径
     * @param imageUrl 图片路径
     * @return
     */
    public static String ocr(String language, String dataPath, String imageUrl) {
        BytePointer outText;
        TessBaseAPI api = new TessBaseAPI();

        if (api.Init(dataPath, language) != 0) {
            System.err.println("Could not initialize tesseract.");
            return null;
        }

        PIX image = pixRead(imageUrl);
        api.SetImage(image);
        outText = api.GetUTF8Text();
        try {
            return outText.getString();
        } finally {
            api.End();
            outText.deallocate();
            pixDestroy(image);
        }
    }
}
