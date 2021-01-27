package com.chua.utils.tools.example;

import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.data.factory.DataFactory;
import com.chua.utils.tools.data.table.wrapper.TableWrapper;
import com.chua.utils.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.function.able.InitializingCacheable;
import com.chua.utils.tools.function.converter.TypeConverter;
import com.chua.utils.tools.http.entity.ResponseEntity;
import com.chua.utils.tools.manager.*;
import com.chua.utils.tools.manager.builder.CacheStrategyBuilder;
import com.chua.utils.tools.manager.builder.LimitStrategyBuilder;
import com.chua.utils.tools.manager.builder.ProxyStrategyBuilder;
import com.chua.utils.tools.manager.builder.RetryStrategyBuilder;
import com.chua.utils.tools.manager.parser.ClassDescriptionParser;
import com.chua.utils.tools.manager.parser.ClassModifyDescriptionParser;
import com.chua.utils.tools.manager.producer.StandardContextManager;
import com.chua.utils.tools.manager.producer.StandardStrategyContextManager;
import com.chua.utils.tools.manager.template.HttpTemplate;
import com.chua.utils.tools.manager.template.MbeanTemplate;
import com.chua.utils.tools.predicate.TruePredicate;
import com.chua.utils.tools.resource.entity.Resource;
import com.chua.utils.tools.resource.template.ResourceTemplate;
import com.chua.utils.tools.spi.Spi;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

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
//        //测试可被缓存对象
//        testCacheable();
//        System.out.println();
//        //测试对象管理器
//        testObjectManager();
//        System.out.println();
//        //测试策略管理器
//        testStrategyManager();
//        System.out.println();
//        //测试配置文件适配器
//        testProfileAdaptorManager();
//        System.out.println();
//        //测试类描述解析器
//        testClassDescriptionParser();
//        System.out.println();
//        //测试资源查找器
//        testResourceFinderManager();
//        System.out.println();
//        //测试MBean
//        //testMBeanTemplate();
//        //System.out.println();
//        //测试Http
//       // testHttpTemplate();
//        // System.out.println();
//        //测试消息总线管理器
//        testEventBusManager();
//        System.out.println();
//        //测试类型转化器
        testTypeConverter();
        System.out.println();
        //测试数据工厂
        testDataFactory();
    }

    private static void testDataFactory() throws Exception {
        System.out.println("==================================测试数据工厂=============================");
        DataFactory dataFactory = contextManager.createDataFactory();
        dataFactory.addSchema("system", TableWrapper.createFileTable("test").source("TEMP.bcp").create());

        List<Map<String, Object>> mapList = dataFactory.queryForList("select * from \"test\"");
        System.out.println(mapList);

    }

    private static void testTypeConverter() {
        System.out.println("==================================测试类型转化器=============================");
        TypeConverter longTypeConverter = contextManager.createTypeConverter(Long.class);
        System.out.println("字符串[12]转为Long :"+ longTypeConverter.convert("12"));
        TypeConverter dateTypeConverter = contextManager.createTypeConverter(Date.class);
        System.out.println("字符串[12]转为Date :"+ dateTypeConverter.convert("12"));
        System.out.println("字符串[2020-11-27]转为Date :"+ dateTypeConverter.convert("2020-11-27"));
    }

    private static void testEventBusManager() {
        System.out.println("==================================测试消息总线管理器=============================");
        EventBusContextManager eventBusContextManager = contextManager.createEventBusContextManager();
        //eventBusContextManager.registerEventBus("demo", new GuavaEventBus(), new EventBusExample.EventBusDemo(""));
       // eventBusContextManager.sendEventBus("demo", IdHelper.createUuid());
    }

    private static void testHttpTemplate() throws IOException {
        System.out.println("==================================测试Http=============================");
        HttpTemplate httpTemplate = contextManager.createHttpTemplate();
        ResponseEntity<Map> entity = httpTemplate.getForEntity("https://gitee.com/xuxueli0323/xxl-crawler/events.json", Map.class);
        System.out.println("获取到请求地址数据：" + JsonHelper.toFormatJson(entity));
    }

    private static void testMBeanTemplate() throws Exception {
        System.out.println("==================================测试MBean=============================");
        MbeanTemplate mBeanTemplate = contextManager.createMbeanTemplate();
        mBeanTemplate.register(new TDemoInfo());
        System.out.println("注册MBean");
    }

    private static void testResourceFinderManager() {
        System.out.println("==================================测试资源查找器=============================");
        ResourceTemplate resourceTemplate = contextManager.createResourceTemplate();
        Set<Resource> mfs = resourceTemplate.getResources("classpath:**/*.MF");
        System.out.println("检索到资源文件：" + mfs);
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
        modifyDescriptionParser.addMethod("public String getText2() {return this.uuid;}");

        Class<TDemoInfo> aClass = modifyDescriptionParser.toClass().toClass();
        ClassDescriptionParser<TDemoInfo> classDescriptionParser = contextManager.createClassDescriptionParser(aClass);
        System.out.println("***************************修改后************************");
        System.out.println("类的接口: " + classDescriptionParser.interfaceDescription().size());
        System.out.println("类的超类: " + classDescriptionParser.superDescription().size());
        System.out.println("类的字段: " + classDescriptionParser.fieldDescription().size());
        System.out.println("类的方法: " + classDescriptionParser.methodDescription().size());
        System.out.println("测试追加的方法(getText2): " + classDescriptionParser.findMethodDescription("getText2").invoke(null));


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
        System.out.println("==================================测试获取可被缓存对象=============================");
        ConcurrentMap cache = InitializingCacheable.getValue(StandardStrategyContextManager.class, "CACHE", ConcurrentMap.class);
        System.out.println(cache);
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
        System.out.println(tDemoInfo.uuid());
    }

    private static void testLimitStrategy() {
        System.out.println("==================================测试限流策略=============================");
        LimitStrategyBuilder limitStrategy = strategyContextManager.createLimitStrategy();
        TDemoInfo tDemoInfo = (TDemoInfo) limitStrategy.limit(1).create(tDemoInfo1);
        System.out.println(tDemoInfo.uuid());
    }

    private static void testProxyStrategy() {
        System.out.println("==================================测试策略策略=============================");
        System.out.println("******************************代理类******************************");
        ProxyStrategyBuilder proxyStrategyBuilder = strategyContextManager.createProxyStrategy();
        TDemoInfo tDemoInfo = (TDemoInfo) proxyStrategyBuilder.proxy((obj, method, args, proxy) -> "1").create(TDemoInfo.class);
        System.out.println(tDemoInfo.uuid());
    }

    private static void testCacheStrategy() {
        System.out.println("==================================测试缓存策略=============================");
        System.out.println(tDemoInfo1.uuid());
        System.out.println(tDemoInfo1.uuid());
        CacheStrategyBuilder strategyBuilder = strategyContextManager.createCacheStrategy();
        TDemoInfo tDemoInfo = (TDemoInfo) strategyBuilder.create(tDemoInfo1);
        System.out.println(tDemoInfo.uuid());
        System.out.println(tDemoInfo.uuid());
    }
}
