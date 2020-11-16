package com.chua.utils.tools.example;

import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.function.able.InitializingCacheable;
import com.chua.utils.tools.manager.ContextManager;
import com.chua.utils.tools.manager.ObjectContextManager;
import com.chua.utils.tools.manager.ProfileAdaptorManager;
import com.chua.utils.tools.manager.StrategyContextManager;
import com.chua.utils.tools.manager.builder.CacheStrategyBuilder;
import com.chua.utils.tools.manager.builder.LimitStrategyBuilder;
import com.chua.utils.tools.manager.builder.ProxyStrategyBuilder;
import com.chua.utils.tools.manager.builder.RetryStrategyBuilder;
import com.chua.utils.tools.manager.parser.ClassDescriptionParser;
import com.chua.utils.tools.manager.parser.ClassModifyDescriptionParser;
import com.chua.utils.tools.manager.producer.StandardContextManager;
import com.chua.utils.tools.manager.producer.StandardStrategyContextManager;
import com.chua.utils.tools.predicate.TruePredicate;
import com.chua.utils.tools.spi.Spi;
import com.chua.utils.tools.spi.processor.ReflectionExtensionProcessor;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/27
 */
public class ObjectContextManagerExample {

    private static TDemoInfo tDemoInfo1 = new TDemoInfo();
    private static ContextManager contextManager = new StandardContextManager();

    private static StrategyContextManager strategyContextManager = contextManager.createStrategyContextManager();

    public static void main(String[] args) throws Exception {
        //测试可被缓存对象
        testCacheable();
        //测试对象管理器
        testObjectManager();
        //测试策略管理器
        testStrategyManager();
        //测试配置文件适配器
        testProfileAdaptorManager();
        //测试类描述解析器
        testClassDescriptionParser();

    }

    private static void testClassDescriptionParser() throws Exception {
        System.out.println("==================================测试类描述解析器=============================");

        ClassDescriptionParser<TDemoInfo> parser = contextManager.createClassDescriptionParser(TDemoInfo.class);
        System.out.println("类的接口: " + parser.interfaceDescription().size());
        System.out.println("类的超类: " + parser.superDescription().size());
        System.out.println("类的字段: " + parser.fieldDescription().size());
        System.out.println("类的方法: " + parser.methodDescription().size());

        ClassModifyDescriptionParser<TDemoInfo> modifyDescriptionParser = parser.modify();
        modifyDescriptionParser.addField("text1", String.class);
        modifyDescriptionParser.addMethod("getText1", String.class, null, "return text1;", null);
        modifyDescriptionParser.addMethod("public String getText2() {return text1;}");

        Class<TDemoInfo> aClass = modifyDescriptionParser.toClass().toClass();
        ClassDescriptionParser<TDemoInfo> classDescriptionParser = contextManager.createClassDescriptionParser(aClass);
        System.out.println("***************************修改后************************");
        System.out.println("类的接口: " + classDescriptionParser.interfaceDescription().size());
        System.out.println("类的超类: " + classDescriptionParser.superDescription().size());
        System.out.println("类的字段: " + classDescriptionParser.fieldDescription().size());
        System.out.println("类的方法: " + classDescriptionParser.methodDescription().size());

        System.out.println();
        System.out.println();
    }

    /**
     * 测试配置文件适配器
     */
    private static void testProfileAdaptorManager() {
        System.out.println("==================================测试配置文件解析器=============================");
        ProfileAdaptorManager profileAdaptorManager = contextManager.createProfileAdaptorManager();
        System.out.println("当前支持的配置文件:" + profileAdaptorManager.names());

        System.out.println("获取json文件解析器: " + profileAdaptorManager.get("json"));
    }

    /**
     * 测试可被缓存对象
     */
    private static void testCacheable() {
        ConcurrentMap cache = InitializingCacheable.getValue(StandardStrategyContextManager.class, "CACHE", ConcurrentMap.class);
        System.out.println(cache);
        System.out.println();
        System.out.println();
    }

    /**
     * 测试策略管理器
     */
    private static void testStrategyManager() {
        //测试缓存策略
        testCacheStrategy();
        //测试代理策略
        testProxyStrategy();
        //测试限流策略
        testLimitStrategy();
        //测试重试策略
        testRetryStrategy();
    }

    private static void testRetryStrategy() {
        System.out.println("==================================测试重试策略=============================");
        RetryStrategyBuilder retryStrategyBuilder = strategyContextManager.createRetryStrategyBuilder();
        TDemoInfo tDemoInfo = (TDemoInfo) retryStrategyBuilder.retry(TruePredicate.INSTANCE).create(tDemoInfo1);
        System.out.println(tDemoInfo.getUuid());
        System.out.println();
        System.out.println();
    }

    private static void testLimitStrategy() {
        System.out.println("==================================测试限流策略=============================");
        LimitStrategyBuilder limitStrategy = strategyContextManager.createLimitStrategy();
        TDemoInfo tDemoInfo = (TDemoInfo) limitStrategy.limit(1).create(tDemoInfo1);
        System.out.println(tDemoInfo.getUuid());
        System.out.println();
        System.out.println();
    }

    private static void testObjectManager() {
        ObjectContextManager objectContextManager = contextManager.createObjectContextManager();
        System.out.println("==================================测试对象管理器=============================");
        //获取所有Encrypt.class实现
        System.out.println("******************************获取所有Encrypt.class实现******************************");
        Set<Class<? extends Encrypt>> types = objectContextManager.getSubTypesOf(Encrypt.class);
        System.out.println(types);
        //获取所有带有Spi注解的类
        System.out.println("******************************获取所有带有Spi注解的类******************************");
        Set<Class<?>> annotatedWith = objectContextManager.getTypesAnnotatedWith(Spi.class);
        System.out.println(annotatedWith);
        System.out.println();
        System.out.println();
    }

    private static void testProxyStrategy() {
        System.out.println("==================================测试策略策略=============================");
        System.out.println("******************************代理类******************************");
        ProxyStrategyBuilder proxyStrategyBuilder = strategyContextManager.createProxyStrategy();
        TDemoInfo tDemoInfo = (TDemoInfo) proxyStrategyBuilder.proxy((obj, method, args, proxy) -> "1").create(TDemoInfo.class);
        System.out.println(tDemoInfo.getUuid());
        System.out.println();
        System.out.println();
    }

    private static void testCacheStrategy() {
        System.out.println("==================================测试缓存策略=============================");
        System.out.println(tDemoInfo1.getUuid());
        System.out.println(tDemoInfo1.getUuid());
        CacheStrategyBuilder strategyBuilder = strategyContextManager.createCacheStrategy();
        TDemoInfo tDemoInfo = (TDemoInfo) strategyBuilder.create(tDemoInfo1);
        System.out.println(tDemoInfo.getUuid());
        System.out.println(tDemoInfo.getUuid());
    }
}
