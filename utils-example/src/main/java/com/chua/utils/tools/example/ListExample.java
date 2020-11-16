package com.chua.utils.tools.example;

import com.chua.utils.tools.text.IdHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
public class ListExample {
    public static void main(String[] args) {
        List<String> demo = new ArrayList<>();
        demo.add(IdHelper.createUuid());
        demo.add(IdHelper.createUuid());
        demo.add(IdHelper.createUuid());
        demo.add(IdHelper.createUuid());

        System.out.println(demo);
        demo.remove(0);

        System.out.println(demo);
    }
}
