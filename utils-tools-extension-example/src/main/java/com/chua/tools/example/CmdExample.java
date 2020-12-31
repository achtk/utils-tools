package com.chua.tools.example;

import com.chua.utils.tools.process.ProcessHelper;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public class CmdExample extends BaseExample {

    public static void main(String[] args) {
        String cmd = "ipconfig";
        log.info(cmd, ProcessHelper.exec(cmd));
    }
}
