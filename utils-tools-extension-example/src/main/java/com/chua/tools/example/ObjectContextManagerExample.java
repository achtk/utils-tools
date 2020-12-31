package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.data.factory.DataFactory;
import com.chua.utils.tools.data.table.wrapper.TableWrapper;
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
public class ObjectContextManagerExample extends BaseExample {

    private static TDemoInfo tDemoInfo1 = new TDemoInfo();
    private static ContextManager contextManager = new StandardContextManager();

    private static StrategyContextManager strategyContextManager = contextManager.createStrategyContextManager();

    public static void main(String[] args) throws Exception {
//        //测试可被缓存对象
//        testCacheable();
//        log.info();
//        //测试对象管理器
//        testObjectManager();
//        log.info();
//        //测试策略管理器
//        testStrategyManager();
//        log.info();
//        //测试配置文件适配器
//        testProfileAdaptorManager();
//        log.info();
//        //测试类描述解析器
//        testClassDescriptionParser();
//        log.info();
//        //测试资源查找器
//        testResourceFinderManager();
//        log.info();
//        //测试MBean
//        //testMBeanTemplate();
//        //log.info();
//        //测试Http
//       // testHttpTemplate();
//        // log.info();
//        //测试消息总线管理器
//        testEventBusManager();
//        log.info();
//        //测试类型转化器
        testTypeConverter();
        //测试数据工厂
        testDataFactory();
    }

    private static void testDataFactory() throws Exception {
        log.info("==================================测试数据工厂=============================");
        DataFactory dataFactory = contextManager.createDataFactory();
        dataFactory.addSchema("system", TableWrapper.createFileTable("test").source("TEMP.bcp").create());

        List<Map<String, Object>> mapList = dataFactory.queryForList("select * from \"test\"");
        log.info("数据: {}", mapList);

    }

    private static void testTypeConverter() {
        log.info("==================================测试类型转化器=============================");
        TypeConverter longTypeConverter = contextManager.createTypeConverter(Long.class);
        log.info("字符串[12]转为Long :" + longTypeConverter.convert("12"));
        TypeConverter dateTypeConverter = contextManager.createTypeConverter(Date.class);
        log.info("字符串[12]转为Date :" + dateTypeConverter.convert("12"));
        log.info("字符串[2020-11-27]转为Date :" + dateTypeConverter.convert("2020-11-27"));
    }

    private static void testEventBusManager() {
        log.info("==================================测试消息总线管理器=============================");
        EventBusContextManager eventBusContextManager = contextManager.createEventBusContextManager();
        //eventBusContextManager.registerEventBus("demo", new GuavaEventBus(), new EventBusExample.EventBusDemo(""));
        // eventBusContextManager.sendEventBus("demo", IdHelper.createUuid());
    }

    private static void testHttpTemplate() throws IOException {
        log.info("==================================测试Http=============================");
        HttpTemplate httpTemplate = contextManager.createHttpTemplate();
        ResponseEntity<Map> entity = httpTemplate.getForEntity("https://gitee.com/xuxueli0323/xxl-crawler/events.json", Map.class);
        log.info("获取到请求地址数据：" + JsonHelper.toFormatJson(entity));
    }

    private static void testMBeanTemplate() throws Exception {
        log.info("==================================测试MBean=============================");
        MbeanTemplate mBeanTemplate = contextManager.createMbeanTemplate();
        mBeanTemplate.register(new TDemoInfo());
        log.info("注册MBean");
    }

    private static void testResourceFinderManager() {
        log.info("==================================测试资源查找器=============================");
        ResourceTemplate resourceTemplate = contextManager.createResourceTemplate();
        Set<Resource> mfs = resourceTemplate.getResources("classpath:**/*.MF");
        log.info("检索到资源文件：" + mfs);
    }

    private static void testClassDescriptionParser() throws Exception {
        log.info("==================================测试类描述解析器=============================");

        ClassDescriptionParser<TDemoInfo> parser = contextManager.createClassDescriptionParser(TDemoInfo.class);
        log.info("类的接口: " + parser.interfaceDescription().size());
        log.info("类的超类: " + parser.superDescription().size());
        log.info("类的字段: " + parser.fieldDescription().size());
        log.info("类的方法: " + parser.methodDescription().size());

        ClassModifyDescriptionParser<TDemoInfo> modifyDescriptionParser = parser.modify();
        modifyDescriptionParser.addField("text1", String.class);
        modifyDescriptionParser.addMethod("getText1", String.class, null, "return text1;", null);
        modifyDescriptionParser.addMethod("public String getText2() {return this.uuid;}");

        Class<TDemoInfo> aClass = modifyDescriptionParser.toClass().toClass();
        ClassDescriptionParser<TDemoInfo> classDescriptionParser = contextManager.createClassDescriptionParser(aClass);
        log.info("***************************修改后************************");
        log.info("类的接口: " + classDescriptionParser.interfaceDescription().size());
        log.info("类的超类: " + classDescriptionParser.superDescription().size());
        log.info("类的字段: " + classDescriptionParser.fieldDescription().size());
        log.info("类的方法: " + classDescriptionParser.methodDescription().size());
        log.info("测试追加的方法(getText2): " + classDescriptionParser.findMethodDescription("getText2").invoke(null));


    }

    /**
     * 测试配置文件适配器
     */
    private static void testProfileAdaptorManager() {
        log.info("==================================测试配置文件解析器=============================");
        ProfileAdaptorManager profileAdaptorManager = contextManager.createProfileAdaptorManager();
        log.info("当前支持的配置文件:" + profileAdaptorManager.names());
        log.info("获取json文件解析器: " + profileAdaptorManager.get("json"));
    }

    /**
     * 测试可被缓存对象
     */
    private static void testCacheable() {
        log.info("==================================测试获取可被缓存对象=============================");
        ConcurrentMap cache = InitializingCacheable.getValue(StandardStrategyContextManager.class, "CACHE", ConcurrentMap.class);
        log.info("缓存对象: {}", cache);
    }

    private static void testObjectManager() {
        ObjectContextManager objectContextManager = contextManager.createObjectContextManager();
        log.info("==================================测试对象管理器=============================");
        //获取所有Encrypt.class实现
        log.info("******************************获取所有Encrypt.class实现******************************");
        Set<Class<? extends Encrypt>> types = objectContextManager.getSubTypesOf(Encrypt.class);
        log.info("子类: {}", types);
        //获取所有带有Spi注解的类
        log.info("******************************获取所有带有Spi注解的类******************************");
        Set<Class<?>> annotatedWith = objectContextManager.getTypesAnnotatedWith(Spi.class);
        log.info("获取所有带有Spi注解的类： {}", annotatedWith);
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
        log.info("==================================测试重试策略=============================");
        RetryStrategyBuilder retryStrategyBuilder = strategyContextManager.createRetryStrategyBuilder();
        TDemoInfo tDemoInfo = (TDemoInfo) retryStrategyBuilder.retry(TruePredicate.INSTANCE).create(tDemoInfo1);
        log.info(tDemoInfo.getUuid());
    }

    private static void testLimitStrategy() {
        log.info("==================================测试限流策略=============================");
        LimitStrategyBuilder limitStrategy = strategyContextManager.createLimitStrategy();
        TDemoInfo tDemoInfo = (TDemoInfo) limitStrategy.limit(1).create(tDemoInfo1);
        log.info(tDemoInfo.getUuid());
    }

    private static void testProxyStrategy() {
        log.info("==================================测试策略策略=============================");
        log.info("******************************代理类******************************");
        ProxyStrategyBuilder proxyStrategyBuilder = strategyContextManager.createProxyStrategy();
        TDemoInfo tDemoInfo = (TDemoInfo) proxyStrategyBuilder.proxy((obj, method, args, proxy) -> "1").create(TDemoInfo.class);
        log.info(tDemoInfo.getUuid());
    }

    private static void testCacheStrategy() {
        log.info("==================================测试缓存策略=============================");
        log.info(tDemoInfo1.getUuid());
        log.info(tDemoInfo1.getUuid());
        CacheStrategyBuilder strategyBuilder = strategyContextManager.createCacheStrategy();
        TDemoInfo tDemoInfo = (TDemoInfo) strategyBuilder.create(tDemoInfo1);
        log.info(tDemoInfo.getUuid());
        log.info(tDemoInfo.getUuid());
    }
}
