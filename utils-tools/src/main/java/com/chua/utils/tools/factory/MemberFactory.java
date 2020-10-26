package com.chua.utils.tools.factory;

import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.cache.ConcurrentSetValueCacheProvider;
import com.chua.utils.tools.cache.CacheProvider;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Member;

/**
 * member处理工厂
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
public class MemberFactory {

    private CacheProvider<String, MemberInfo> provider = new ConcurrentCacheProvider<>();
    private CacheProvider<Class, MemberInfo> subProvider = new ConcurrentSetValueCacheProvider();
    private CacheProvider<Class, MemberInfo> annotationProvider = new ConcurrentSetValueCacheProvider();

    /**
     * 添加Member
     * @param member
     */
    public void add(Member member) {
        provider.put(member.getName(), new MemberInfo(member));
    }

    /**
     * 添加 Class
     * @param aClass
     */
    public void addSubType(Class aClass) {
        subProvider.put(aClass, new MemberInfo(null, aClass));
    }

    /**
     * 添加 Class
     * @param aClass
     */
    public void addAnnotationType(Class aClass) {
        annotationProvider.put(aClass, new MemberInfo(null, aClass));
    }

    @AllArgsConstructor
    @RequiredArgsConstructor
    class MemberInfo {
        @NonNull
        private Member member;

        private Class aClass;
    }
}
