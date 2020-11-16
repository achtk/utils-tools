package com.chua.utils.tools.example;

import com.chua.utils.tools.util.SystemUtil;
import org.jooq.util.derby.sys.Sys;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class SystemUtilExample {

    public static void main(String[] args) {
        System.out.println("pid: " + SystemUtil.getPid());
        System.out.println("window: " + SystemUtil.isWindow());
    }
}
