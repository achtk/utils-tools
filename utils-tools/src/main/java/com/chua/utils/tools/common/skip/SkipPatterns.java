package com.chua.utils.tools.common.skip;


import com.chua.utils.tools.common.FileHelper;

import java.io.File;
import java.util.*;

/**
 * Skip
 *
 * @author CH
 */
public class SkipPatterns {

    public static final Set<String> JDK_LIB;
    private static final Set<String> SKIP_PATTERNS;

    static {
        // Same as Tomcat
        Set<String> patterns = new LinkedHashSet<String>();
        patterns.add("ant-*.jar");
        patterns.add("aspectj*.jar");
        patterns.add("commons-beanutils*.jar");
        patterns.add("commons-codec*.jar");
        patterns.add("commons-collections*.jar");
        patterns.add("commons-dbcp*.jar");
        patterns.add("commons-digester*.jar");
        patterns.add("commons-fileupload*.jar");
        patterns.add("commons-httpclient*.jar");
        patterns.add("commons-io*.jar");
        patterns.add("commons-lang*.jar");
        patterns.add("commons-logging*.jar");
        patterns.add("commons-math*.jar");
        patterns.add("commons-pool*.jar");
        patterns.add("geronimo-spec-jaxrpc*.jar");
        patterns.add("h2*.jar");
        patterns.add("hamcrest*.jar");
        patterns.add("hibernate*.jar");
        patterns.add("jmx*.jar");
        patterns.add("jmx-tools-*.jar");
        patterns.add("jta*.jar");
        patterns.add("junit-*.jar");
        patterns.add("httpclient*.jar");
        patterns.add("log4j-*.jar");
        patterns.add("mail*.jar");
        patterns.add("org.hamcrest*.jar");
        patterns.add("slf4j*.jar");
        patterns.add("tomcat-embed-core-*.jar");
        patterns.add("tomcat-embed-logging-*.jar");
        patterns.add("tomcat-jdbc-*.jar");
        patterns.add("tomcat-juli-*.jar");
        patterns.add("tools.jar");
        patterns.add("wsdl4j*.jar");
        patterns.add("xercesImpl-*.jar");
        patterns.add("xmlParserAPIs-*.jar");
        patterns.add("xml-apis-*.jar");
        SKIP_PATTERNS = Collections.unmodifiableSet(patterns);
    }

    private static final Set<String> ADDITIONAL;

    static {
        // Additional typical for Spring Boot applications
        Set<String> patterns = new LinkedHashSet<String>();
        patterns.add("antlr-*.jar");
        patterns.add("aopalliance-*.jar");
        patterns.add("aspectjrt-*.jar");
        patterns.add("aspectjweaver-*.jar");
        patterns.add("classmate-*.jar");
        patterns.add("dom4j-*.jar");
        patterns.add("ecj-*.jar");
        patterns.add("ehcache-core-*.jar");
        patterns.add("hibernate-core-*.jar");
        patterns.add("hibernate-commons-annotations-*.jar");
        patterns.add("hibernate-entitymanager-*.jar");
        patterns.add("hibernate-jpa-2.1-api-*.jar");
        patterns.add("hibernate-validator-*.jar");
        patterns.add("hsqldb-*.jar");
        patterns.add("jackson-annotations-*.jar");
        patterns.add("jackson-core-*.jar");
        patterns.add("jackson-databind-*.jar");
        patterns.add("jandex-*.jar");
        patterns.add("javassist-*.jar");
        patterns.add("jboss-logging-*.jar");
        patterns.add("jboss-transaction-api_*.jar");
        patterns.add("jcl-over-slf4j-*.jar");
        patterns.add("jdom-*.jar");
        patterns.add("jul-to-slf4j-*.jar");
        patterns.add("log4j-over-slf4j-*.jar");
        patterns.add("logback-classic-*.jar");
        patterns.add("logback-core-*.jar");
        patterns.add("rome-*.jar");
        patterns.add("slf4j-api-*.jar");
        patterns.add("spring-aop-*.jar");
        patterns.add("spring-aspects-*.jar");
        patterns.add("spring-beans-*.jar");
        patterns.add("spring-boot-*.jar");
        patterns.add("spring-core-*.jar");
        patterns.add("spring-context-*.jar");
        patterns.add("spring-data-*.jar");
        patterns.add("spring-expression-*.jar");
        patterns.add("spring-jdbc-*.jar,");
        patterns.add("spring-orm-*.jar");
        patterns.add("spring-oxm-*.jar");
        patterns.add("spring-tx-*.jar");
        patterns.add("snakeyaml-*.jar");
        patterns.add("tomcat-embed-el-*.jar");
        patterns.add("validation-api-*.jar");
        patterns.add("xml-apis-*.jar");
        ADDITIONAL = Collections.unmodifiableSet(patterns);
    }

    private static final Set<String> CGY;

    static {
        Set<String> patterns = new LinkedHashSet<String>();
        patterns.add("utils-*.jar");
        CGY = Collections.unmodifiableSet(patterns);
    }

    public static final Set<String> DEFAULT;
    public static final Set<String> DEFAULT_INCLUDE_WORK_SPACES;

    static {
        Set<String> patterns = new LinkedHashSet<String>();
        patterns.addAll(SKIP_PATTERNS);
        patterns.addAll(ADDITIONAL);
        patterns.addAll(CGY);
        DEFAULT = Collections.unmodifiableSet(patterns);
        Set<String> patternsSpace = new LinkedHashSet<String>();
        patternsSpace.addAll(SKIP_PATTERNS);
        patternsSpace.addAll(ADDITIONAL);
        DEFAULT_INCLUDE_WORK_SPACES = Collections.unmodifiableSet(patternsSpace);
    }

    static {
        JDK_LIB = new HashSet<>();
        Collection<File> files = FileHelper.listFiles(new File(System.getProperty("java.home")), new String[]{"jar"}, true);
        if(null != files) {
            Iterator<File> iterator = files.iterator();
            while (iterator.hasNext()) {
                File file = iterator.next();
                JDK_LIB.add(file.getName());
            }
        }
        JDK_LIB.add("hotswap*.jar");
        JDK_LIB.add("idea_rt.jar");
        JDK_LIB.add("debugger-*.jar");
    }
    private SkipPatterns() {
    }
}
