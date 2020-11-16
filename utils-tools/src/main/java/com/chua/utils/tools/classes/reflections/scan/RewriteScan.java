package com.chua.utils.tools.classes.reflections.scan;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.reflections.RewriteStore;
import com.chua.utils.tools.classes.reflections.configuration.RewriteConfiguration;
import com.chua.utils.tools.classes.reflections.scanner.*;
import lombok.Getter;
import org.reflections.*;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.reflections.ReflectionUtils.*;
import static org.reflections.util.Utils.*;

/**
 * 重写Scan
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/12
 */
public class RewriteScan extends Reflections {

    protected transient RewriteConfiguration rewriteConfiguration;
    protected static final RewriteStore STORE = new RewriteStore();
    @Getter
    private Map<String, Throwable> throwableMap = new HashMap<>();

    @Override
    protected void scan() {
        if (rewriteConfiguration.getUrls() == null || rewriteConfiguration.getUrls().isEmpty()) {
            if (log != null) {
                log.warn("given scan urls are empty. set urls in the configuration");
            }
            return;
        }

        if (log != null && log.isDebugEnabled()) {
            log.debug("going to scan these urls: {}", rewriteConfiguration.getUrls());
        }

        long time = System.currentTimeMillis();
        int scannedUrls = 0;
        ExecutorService executorService = rewriteConfiguration.getExecutorService();
        List<Future<?>> futures = new ArrayList<>();

        for (final URL url : rewriteConfiguration.getUrls()) {
            if (STORE.container(url)) {
                continue;
            }
            try {
                if (executorService != null) {
                    futures.add(executorService.submit(() -> {
                        if (log != null) {
                            log.debug("[{}] scanning {}", Thread.currentThread().toString(), url);
                        }
                        scan(url);
                    }));
                } else {
                    scan(url);
                }
                scannedUrls++;
            } catch (ReflectionsException e) {
                if (log != null) {
                    log.warn("could not create Vfs.Dir from url. ignoring the exception and continuing", e);
                }
            }
        }

        if (executorService != null) {
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //gracefully shutdown the parallel scanner executor service.
        if (executorService != null) {
            executorService.shutdown();
            executorService.shutdownNow();
        }

        if (log != null) {
            log.info(format("ReflectionsFactory took %d ms to scan %d urls, producing %s %s",
                    System.currentTimeMillis() - time, scannedUrls, "",
                    executorService instanceof ThreadPoolExecutor ?
                            format("[using %d cores]", ((ThreadPoolExecutor) executorService).getMaximumPoolSize()) : ""));
        }

        if (rewriteConfiguration.shouldExpandSuperTypes()) {
            expandSuperTypes();
        }
    }

    @Override
    public void expandSuperTypes() {
        String index = index(SubTypesScanner.class);
        Set<String> keys = STORE.keys(index);
        keys.removeAll(STORE.values(index));
        keys.parallelStream().forEach(key -> {
            final Class<?> type = ClassHelper.forName(key, rewriteConfiguration.getClassLoaders());
            if (type != null) {
                expandSupertypes(STORE, key, type);
            }
        });
    }

    private void expandSupertypes(Store store, String key, Class<?> type) {
        for (Class<?> supertype : ReflectionUtils.getSuperTypes(type)) {
            if (store.put(SubTypesScanner.class, supertype.getName(), key)) {
                if (log != null) {
                    log.debug("expanded subtype {} -> {}", supertype.getName(), key);
                }
                expandSupertypes(store, supertype.getName(), supertype);
            }
        }
    }


    @Override
    protected void scan(URL url) {
        Vfs.Dir dir = Vfs.fromURL(url);

        try {
            for (final Vfs.File file : dir.getFiles()) {
                Predicate<String> inputsFilter = rewriteConfiguration.getInputsFilter();
                String path = file.getRelativePath();
                String fqn = path.replace('/', '.');
                if (inputsFilter == null || inputsFilter.test(path) || inputsFilter.test(fqn)) {
                    Object classObject = null;
                    for (AbstractRewriteScanner scanner : rewriteConfiguration.getRewriteScanners()) {
                        scanner.setConfiguration(rewriteConfiguration);
                        scanner.setUrl(url);
                        try {
                            if (scanner.acceptsInput(path) || scanner.acceptsInput(fqn)) {
                                classObject = scanner.scan(file, classObject, store);
                            }
                        } catch (Exception e) {
                            if (log != null) {
                                throwableMap.put(file.getRelativePath(), e);
                            }
                        }
                    }
                }
            }
        } finally {
            dir.close();
        }
    }


    @Override
    public Set<String> getAllTypes() {
        Set<String> allTypes = new HashSet<>(store.getAll(RewriteSubTypesScanner.class, Object.class.getName()));
        if (allTypes.isEmpty()) {
            throw new ReflectionsException("Couldn't find subtypes of Object. " +
                    "Make sure SubTypesScanner initialized to include Object class - new SubTypesScanner(false)");
        }
        return allTypes;
    }

    @Override
    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
        return ClassHelper.forNames(store.getAll(RewriteSubTypesScanner.class, type.getName()), rewriteConfiguration.getClassLoaders());
    }

    @Override
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation, boolean honorInherited) {
        Set<String> annotated = store.get(RewriteTypeAnnotationsScanner.class, annotation.getName());
        annotated.addAll(getAllAnnotated(annotated, annotation, honorInherited));
        return ClassHelper.forNames(annotated, rewriteConfiguration.getClassLoaders());
    }

    @Override
    public Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        return getMethodsFromDescriptors(store.get(RewriteMethodAnnotationsScanner.class, annotation.getName()), rewriteConfiguration.getClassLoaders());
    }

    @Override
    public Set<Constructor> getConstructorsAnnotatedWith(Class<? extends Annotation> annotation) {
        return getConstructorsFromDescriptors(store.get(RewriteMethodAnnotationsScanner.class, annotation.getName()), rewriteConfiguration.getClassLoaders());
    }

    @Override
    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return super.getTypesAnnotatedWith(annotation);
    }

    @Override
    public Set<Class<?>> getTypesAnnotatedWith(Annotation annotation) {
        return super.getTypesAnnotatedWith(annotation);
    }

    @Override
    public Set<Class<?>> getTypesAnnotatedWith(Annotation annotation, boolean honorInherited) {
        Set<String> annotated = store.get(RewriteTypeAnnotationsScanner.class, annotation.annotationType().getName());
        Set<Class<?>> allAnnotated = filter(forNames(annotated, rewriteConfiguration.getClassLoaders()), withAnnotation(annotation));
        Set<Class<?>> classes = forNames(filter(getAllAnnotated(names(allAnnotated), annotation.annotationType(), honorInherited), s -> !annotated.contains(s)), rewriteConfiguration.getClassLoaders());
        allAnnotated.addAll(classes);
        return allAnnotated;
    }

    @Override
    public Set<Method> getMethodsAnnotatedWith(Annotation annotation) {
        return super.getMethodsAnnotatedWith(annotation);
    }

    @Override
    public Set<Method> getMethodsMatchParams(Class<?>... types) {
        return getMethodsFromDescriptors(store.get(RewriteMethodParameterScanner.class, names(types).toString()), rewriteConfiguration.getClassLoaders());
    }

    @Override
    public Set<Method> getMethodsReturn(Class returnType) {
        return getMethodsFromDescriptors(store.get(RewriteMethodParameterScanner.class, names(returnType)), rewriteConfiguration.getClassLoaders());
    }

    @Override
    public Set<Method> getMethodsWithAnyParamAnnotated(Class<? extends Annotation> annotation) {
        return getMethodsFromDescriptors(store.get(RewriteMethodParameterScanner.class, annotation.getName()), rewriteConfiguration.getClassLoaders());
    }

    @Override
    public Set<Method> getMethodsWithAnyParamAnnotated(Annotation annotation) {
        return super.getMethodsWithAnyParamAnnotated(annotation);
    }

    @Override
    public Set<Constructor> getConstructorsAnnotatedWith(Annotation annotation) {
        return super.getConstructorsAnnotatedWith(annotation);
    }

    @Override
    public Set<Constructor> getConstructorsMatchParams(Class<?>... types) {
        return getConstructorsFromDescriptors(store.get(RewriteMethodParameterScanner.class, names(types).toString()), rewriteConfiguration.getClassLoaders());
    }

    @Override
    public Set<Constructor> getConstructorsWithAnyParamAnnotated(Class<? extends Annotation> annotation) {
        return getConstructorsFromDescriptors(store.get(RewriteMethodParameterScanner.class, annotation.getName()), rewriteConfiguration.getClassLoaders());
    }

    @Override
    public Set<Constructor> getConstructorsWithAnyParamAnnotated(Annotation annotation) {
        return super.getConstructorsWithAnyParamAnnotated(annotation);
    }

    @Override
    public Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
        return store.get(RewriteFieldAnnotationsScanner.class, annotation.getName()).stream()
                .map(annotated -> getFieldFromString(annotated, rewriteConfiguration.getClassLoaders()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Field> getFieldsAnnotatedWith(Annotation annotation) {
        return super.getFieldsAnnotatedWith(annotation);
    }

    @Override
    public Set<String> getResources(Predicate<String> namePredicate) {
        Set<String> resources = filter(store.keys(index(RewriteResourcesScanner.class)), namePredicate);
        return store.get(RewriteResourcesScanner.class, resources);
    }

    @Override
    public Set<String> getResources(Pattern pattern) {
        return super.getResources(pattern);
    }

    @Override
    public List<String> getMethodParamNames(Method method) {
        Set<String> names = store.get(RewriteMethodParameterNamesScanner.class, name(method));
        return names.size() == 1 ? Arrays.asList(names.iterator().next().split(", ")) : Collections.emptyList();
    }

    @Override
    public List<String> getConstructorParamNames(Constructor constructor) {
        Set<String> names = store.get(RewriteMethodParameterNamesScanner.class, Utils.name(constructor));
        return names.size() == 1 ? Arrays.asList(names.iterator().next().split(", ")) : Collections.emptyList();
    }

    @Override
    public Set<Member> getFieldUsage(Field field) {
        return getMembersFromDescriptors(store.get(RewriteMemberUsageScanner.class, name(field)));
    }

    @Override
    public Set<Member> getMethodUsage(Method method) {
        return getMembersFromDescriptors(store.get(RewriteMemberUsageScanner.class, name(method)));
    }

    @Override
    public Set<Member> getConstructorUsage(Constructor constructor) {
        return getMembersFromDescriptors(store.get(RewriteMemberUsageScanner.class, name(constructor)));
    }

    @Override
    protected Collection<String> getAllAnnotated(Collection<String> annotated, Class<? extends Annotation> annotation, boolean honorInherited) {
        if (honorInherited) {
            if (annotation.isAnnotationPresent(Inherited.class)) {
                Set<String> subTypes = store.get(RewriteSubTypesScanner.class, filter(annotated, input -> {
                    final Class<?> type = forName(input, rewriteConfiguration.getClassLoaders());
                    return type != null && !type.isInterface();
                }));
                return store.getAllIncluding(RewriteSubTypesScanner.class, subTypes);
            } else {
                return annotated;
            }
        } else {
            Collection<String> subTypes = store.getAllIncluding(RewriteTypeAnnotationsScanner.class, annotated);
            return store.getAllIncluding(RewriteSubTypesScanner.class, subTypes);
        }
    }
}
