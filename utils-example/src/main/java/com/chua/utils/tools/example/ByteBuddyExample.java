package com.chua.utils.tools.example;

import com.chua.utils.tools.manager.parser.description.MethodDescription;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/11
 */
public class ByteBuddyExample {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("this is an perform monitor agent.");

        AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                    TypeDescription typeDescription,
                                                    ClassLoader classLoader) {
                return builder
                        .method(ElementMatchers.any())
                        .intercept(MethodDelegation.to(TimeInterceptor.class));
            }
        };

        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, DynamicType dynamicType) {
            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
            }

            @Override
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, Throwable throwable) {
            }

            @Override
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module) {
            }
        };

        new AgentBuilder
                .Default()
                .type(ElementMatchers.any())
                .transform(transformer)
                .with(listener)
                .installOn(inst);
    }

    static class TimeInterceptor {
        @RuntimeType
        public static Object intercept(@Origin Method method,
                                       @SuperCall Callable<?> callable) throws Exception {
            long start = System.currentTimeMillis();
            try {
                // 原有函数执行
                return callable.call();
            } finally {
                System.out.println(method + ": took " + (System.currentTimeMillis() - start) + "ms");
            }
        }
    }
}
