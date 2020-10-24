package com.chua.utils.tools.factory;

import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.cache.ConcurrentSetCacheProvider;
import com.chua.utils.tools.cache.ICacheProvider;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.concurrent.ConcurrentMap;

/**
 * member处理工厂
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
public class MemberFactory {

    private ICacheProvider<String, MemberInfo> provider = new ConcurrentCacheProvider<>();
    private ICacheProvider<Class, MemberInfo> subProvider = new ConcurrentSetCacheProvider();
    private ICacheProvider<Class, MemberInfo> annotationProvider = new ConcurrentSetCacheProvider();

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
