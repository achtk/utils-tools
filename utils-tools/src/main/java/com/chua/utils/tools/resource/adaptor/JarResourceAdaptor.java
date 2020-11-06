package com.chua.utils.tools.resource.adaptor;


import com.chua.utils.tools.resource.Lazy;
import com.chua.utils.tools.resource.Resource;
import com.chua.utils.tools.resource.context.ResourceContext;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static com.chua.utils.tools.constant.StringConstant.FILE_URL_PREFIX;

/**
 * jar资源处理器
 * @author CH
 * @since 1.0
 */
public class JarResourceAdaptor implements IResourceAdaptor {

    @Override
    public ResourceContext analyze(String path) throws IOException {
        ResourceContext resources = new ResourceContext();

        try(JarFile jarFile = new JarFile(path)) {
            final String newPath = "/" + path.replace("\\", "/");

            Manifest manifest = jarFile.getManifest();
            Attributes attributes = manifest.getMainAttributes();
            if(null != attributes) {
                for (Map.Entry<Object, Object> entry : attributes.entrySet()) {
                    resources.addAttribute(entry.getKey().toString(), entry.getValue().toString());
                }
            }

            resources.addAttribute("size", jarFile.size());
            resources.addAttribute("name", jarFile.getName());


            jarFile.stream().parallel().forEach(new Consumer<JarEntry>() {
                @Override
                public void accept(JarEntry entry) {
                    String entryName = entry.getName();
                    Resource resource = new Resource();
                    resource.setType("jar");
                    resource.setPath("/" + entryName);
                    resource.setDirectory(entry.isDirectory());
                    resource.setLocal(FILE_URL_PREFIX + newPath);
                    resource.setUrl(Lazy.LAZY);
                    resource.setName(entryName);

                    resources.addResource(resource);
                }
            });
        }
        return resources;
    }
}
