package com.chua.utils.tools.classes.reflections;

import org.reflections.Store;
import org.reflections.scanners.Scanner;

import java.util.Set;

/**
 * 内存存储
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
public class StoreFactory extends Store {
    /**
     * 是否已检索
     * @param scanner
     * @return
     */
    public boolean container(Scanner scanner) {
        Set<String> strings = keySet();
        return strings.contains(scanner.getClass().getSimpleName());
    }
}
