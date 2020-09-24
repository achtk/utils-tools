package com.chua.utils.tools.resource;

import org.testng.annotations.Test;

import java.util.Set;

import static org.testng.Assert.*;

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