package com.chua.utils.tools.logger;

import com.chua.utils.tools.common.DateHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * 日志工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/14
 */
@Slf4j
public class LogUtils {
    /**
     * 输出日志
     *
     * @param message
     */
    public static void time(String message) {
        if (!log.getClass().isInterface()) {
            log.info("{} : {}", DateHelper.currentString(), message);
            return;
        }
        System.out.println(DateHelper.currentString()+" : "+ message);
    }

}
