package com.chua.utils.tools.example;

import com.google.common.collect.HashMultimap;
import org.testng.annotations.Test;

/**
 * AbstractPropertiesPlaceholderFactory 测试
 * @author CH
 */
public class AbstractPropertiesPlaceholderResolverTest {

    @Test
    public void demo() {
        String value = "${value12}";
        HashMultimap hashMultimap = HashMultimap.create();
        hashMultimap.put("value11", "${data11}");
        hashMultimap.put("data11", "11");

        hashMultimap.put("value12", "${data12}");
        hashMultimap.put("data12", "${data122}");
        hashMultimap.put("data122", "${dd:1}");

        hashMultimap.put("value13", "${data31}");
        hashMultimap.put("data31", "#{user.home}");

    }

}