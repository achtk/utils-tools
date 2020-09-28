package com.chua.utils.tools.example;

import com.chua.utils.tools.resource.FastResourceHelper;
import com.chua.utils.tools.resource.Resource;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * @author CH
 */
public class FastResourceHelperTest {

    @Test
    public void testGetResources() {
        Set<Resource> resources = FastResourceHelper.getResources("classpath:*.json");
        System.out.println(resources);
    }
}