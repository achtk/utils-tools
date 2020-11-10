package com.chua.utils.tools.example;


import com.chua.utils.tools.collects.HashTripleMap;
import com.chua.utils.tools.collects.TripleMap;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public class TupleExample {

    public static void main(String[] args) {
        TripleMap<String, String, String> tripleMap = new HashTripleMap<>();
        tripleMap.put("1", "1", "1");
        tripleMap.put("1", "1", "2");
        tripleMap.put("1", "2", "2");
        tripleMap.put("2", "3", "4");
        tripleMap.put("3", "4", "5");
        tripleMap.put("", "4", "5");
        tripleMap.put(null, "4", "5");

        System.out.println(tripleMap.byLeft("1"));
        System.out.println(tripleMap.getRight("1", "1"));
        System.out.println(tripleMap.byRight("5"));
        System.out.println(tripleMap.getLeft("4", "5"));
        System.out.println(tripleMap.byMiddle("1"));
        System.out.println(tripleMap.getMiddle("1", "1"));

        System.out.println();
    }
}
