package com.chua.utils.tools.resource.adaptor;


import com.chua.utils.tools.resource.context.ResourceContext;
import com.chua.utils.tools.resource.entity.Lazy;
import com.chua.utils.tools.resource.entity.Resource;

import java.io.IOException;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * jar资源处理器
 *
 * @author CH
 * @since 1.0
 */
public class JarResourceAdaptor implements IResourceAdaptor {

    @Override
    public ResourceContext analyze(String path) throws IOException {
        ResourceContext resources = new ResourceContext();

        try (JarFile jarFile = new JarFile(path)) {
            final String newPath = "/" + path.replace("\\", "/");

            Manifest manifest = jarFile.getManifest();
            Attributes attributes = manifest.getMainAttributes();
            if (null != attributes) {
                for (Map.Entry<Object, Object> entry : attributes.entrySet()) {
                    resources.addAttribute(entry.getKey().toString(), entry.getValue().toString());
                }
            }

            resources.addAttribute("size", jarFile.size());
            resources.addAttribute("name", jarFile.getName());


            jarFile.stream().parallel().forEach(entry -> {
                String entryName = entry.getName();
                Resource resource = new Resource();
                resource.setUrl(Lazy.LAZY);

                resources.addResource(resource);
            });
        }
        return resources;
    }
}
