package com.chua.utils.tools.resource.adaptor;


import com.chua.utils.tools.resource.context.ResourceContext;
import com.chua.utils.tools.resource.entity.Lazy;
import com.chua.utils.tools.resource.entity.Resource;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * zip 资源处理器
 *
 * @author CH
 * @since 1.0
 */
public class ZipResourceAdaptor implements IResourceAdaptor {

    @Override
    public ResourceContext analyze(String path) throws IOException {
        ResourceContext resources = new ResourceContext();
        try (ZipFile zipFile = new ZipFile(path)) {
            resources.addAttribute("size", zipFile.size());
            resources.addAttribute("name", zipFile.getName());

            zipFile.stream().parallel().forEach((Consumer<ZipEntry>) zipEntry -> {
                String entryName = zipEntry.getName();
                Resource resource = new Resource();
                resource.setUrl(Lazy.LAZY);

                resources.addResource(resource);
            });
        }
        return resources;
    }
}
