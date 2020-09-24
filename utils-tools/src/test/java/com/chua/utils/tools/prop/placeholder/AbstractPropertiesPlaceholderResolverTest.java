package com.chua.utils.tools.prop.placeholder;

import com.chua.utils.tools.mockito.MockitoHelper;
import com.google.common.collect.HashMultimap;
import org.testng.annotations.Test;

import java.util.List;

/**
 * AbstractPropertiesPlaceholderFactory 测试
 * @see AbstractPropertiesPlaceholderResolver
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
        List<HashMultimap> mapList = MockitoHelper.batchHashMultimapData(0);
        mapList.add(hashMultimap);

        PropertiesPlaceholderFactory placeholderFactory = PropertiesPlaceholderFactory.newBuilder().dataMapper(hashMultimap).build();
        System.out.println(placeholderFactory.placeholder(value));
        System.out.println(placeholderFactory.placeholders());
        System.out.println(mapList);


    }

}