package com.chua.utils.tools.resource.adaptor;


import com.chua.utils.tools.resource.Lazy;
import com.chua.utils.tools.resource.Resource;
import com.chua.utils.tools.resource.context.ResourceContext;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * zip 资源处理器
 * @author CH
 * @since 1.0
 */
public class ZipResourceAdaptor implements IResourceAdaptor {

    @Override
    public ResourceContext analyze(String path) throws IOException {
        ResourceContext resources = new ResourceContext();
        ZipFile zipFile = new ZipFile(path);
        resources.addAttribute("size", zipFile.size());
        resources.addAttribute("name", zipFile.getName());

        zipFile.stream().parallel().forEach(new Consumer<ZipEntry>() {
            @Override
            public void accept(ZipEntry zipEntry) {
                String entryName = zipEntry.getName();
                Resource resource = new Resource();
                resource.setType("zip");
                resource.setPath("/" + entryName);
                resource.setUrl(Lazy.LAZY);
                resource.setName(entryName);

                resources.addResource(resource);
            }
        });
        return resources;
    }
}
