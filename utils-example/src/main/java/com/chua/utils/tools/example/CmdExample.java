package com.chua.utils.tools.example;

import com.chua.utils.tools.process.ProcessHelper;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public class CmdExample {

    public static void main(String[] args) {
        System.out.println(ProcessHelper.exec("ipconfig"));
    }
}
