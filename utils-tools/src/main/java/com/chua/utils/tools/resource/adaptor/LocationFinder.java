package com.chua.utils.tools.resource.adaptor;

import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.common.UrlHelper;
import com.chua.utils.tools.resource.Resource;
import com.chua.utils.tools.resource.context.ResourceContext;
import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 本地文件查找器
 *
 * @author CH
 * @since 1.0
 */
public class LocationFinder implements IResourceAdaptor {


    @Override
    public ResourceContext analyze(String path) throws IOException {
        if(!path.startsWith(UrlHelper.FILE_URL_PREFIX)) {
            return null;
        }

        path = path.replace("\\", "/");
        String substring = path.substring(UrlHelper.FILE_URL_PREFIX.length());
        int index = substring.indexOf("*");
        if(index == -1) {
            index = substring.indexOf("?");
        }

        if(index == -1) {
            return new ResourceContext().addResource(Resource.getResource(new File(path).toURI().toURL()));
        }

        String parentPath = substring.substring(0, index).trim();
        String patternRegex = substring.substring(index);

        Map<String, Resource> resourceSet = new HashMap<>();
        if(!Strings.isNullOrEmpty(parentPath)) {
            File parents = new File(parentPath);
            //深度检索
            doFindFileAndPathByDepth(parents, patternRegex, resourceSet);
        } else {
            int count = 0;
            Iterable<Path> directories = FileSystems.getDefault().getRootDirectories();
            Iterator<Path> iterator = directories.iterator();
            while (iterator.hasNext()) {
                ++ count;
                iterator.next();
            }

            ExecutorService executorService = ThreadHelper.newMinThreadExecutor(count, ThreadHelper.processor());
            CountDownLatch countDownLatch = new CountDownLatch(count);


            for (Path directory : directories) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        //深度检索
                        try {
                            doFindFileAndPathByDepth(directory.toFile(), patternRegex, resourceSet);
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
                });
            }

            ExecutorService service = ThreadHelper.newSingleThreadExecutor();
            Future<Object> submit = service.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    countDownLatch.await();
                    return 0;
                }
            });

            try {
                submit.get(60L, TimeUnit.SECONDS);
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                executorService.shutdownNow();
                service.shutdownNow();
            }

        }

        ResourceContext resourceContext = new ResourceContext();
        resourceContext.setResources(resourceSet);
        return resourceContext;
    }

    /**
     * @param parents
     * @param patternRegex
     * @param resourceSet
     */
    private void doFindFileAndPathByDepth(File parents, String patternRegex, Map<String, Resource> resourceSet) {
        //获取广度为2的文件/文件夹
        List<File> parentFile = doFindFileAndPathByBreadth(2, parents, patternRegex, resourceSet);
        FileHelper.doFindFileAndPathByDepth(parents, parentFile, patternRegex, resourceSet);
    }


    /**
     * 获取广度文件夹/文件
     * @param i
     * @param parents
     * @param patternRegex
     * @param resourceSet
     * @return
     */
    private List<File> doFindFileAndPathByBreadth(int i, File parents, String patternRegex, Map<String, Resource> resourceSet) {
        return FileHelper.doFindFileAndPathByBreadth(i, parents, patternRegex, resourceSet);
    }

}
