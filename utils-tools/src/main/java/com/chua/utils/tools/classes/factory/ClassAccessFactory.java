package com.chua.utils.tools.classes.factory;

import com.chua.utils.tools.classes.AsmUtils;
import com.chua.utils.tools.classes.ClassAccess;
import com.chua.utils.tools.classes.classloader.AccessClassLoader;
import com.chua.utils.tools.util.ClassUtils;
import com.chua.utils.tools.util.StringUtils;
import org.objectweb.asm.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.objectweb.asm.Opcodes.*;

/**
 * 类访问工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/30
 */
public class ClassAccessFactory<T> {

    private final Class<T> clazz;
    private final ClassWriter cw;

    private final List<ClassPropertyInfo> accessorInfoList = new ArrayList<>();
    private String classAccessInternalName;
    private String classAccessTypeDescriptor;
    private String classTypeDescriptor;
    private static final String MEMBER_TYPE_FIELD = "field";
    private static final String MEMBER_TYPE_PROPERTY = "property";
    private final List<ClassFieldInfo> fieldInfoList = new ArrayList<>();
    private String internalName;
    private final List<ClassMethodInfo> methodInfoList = new ArrayList<>();
    private final List<ClassPropertyInfo> mutatorInfoList = new ArrayList<>();
    private MethodVisitor mv;
    private final Map<Integer, List<ClassMethodInfo>> paramCountMethodsMap = new HashMap<>();
    private final List<ClassPropertyInfo> propertyInfoList = new ArrayList<>();
    private final Map<String, List<ClassPropertyInfo>> typeToAccessorsMap = new HashMap<>();
    private final Map<String, List<ClassFieldInfo>> typeToFieldsMap = new HashMap<>();
    private final Map<String, List<ClassPropertyInfo>> typeToMutatorsMap = new HashMap<>();
    private static final int MAX_METHOD_ACCESS_PARAMETER_COUNT = 22;

    public ClassAccessFactory(Class<T> clazz) {
        this.clazz = clazz;
        initializePropertyDescriptors();
        initializeFields();
        initializeMethods();
        cw = new ClassWriter(0);
    }

    /**
     * 初始化AccessClass
     */
    public void buildClassAccessClass() {
        visitClass();
        AsmUtils.visitDefaultConstructor(cw, classAccessTypeDescriptor);
        visitIndexMethod(MEMBER_TYPE_FIELD, getMemberIndexSwitchCases(fieldInfoList));
        visitFieldAccessMethods();
        visitIndexMethod(MEMBER_TYPE_PROPERTY, getMemberIndexSwitchCases(propertyInfoList));
        visitPropertyAccessMethods();
        visitMethodIndexMethod();
        visitMethodAccessMethods();
        cw.visitEnd();
        AccessClassLoader.get(clazz).defineClass(getClassNameOfClassAccessFor(clazz), cw.toByteArray());
    }

    /**
     * 方法方法
     */
    private void visitMethodAccessMethods() {
        visitMethodAccessInvokeMethod();
        visitMethodAccessInvokeBridge();

        for (int i = 0; i <= MAX_METHOD_ACCESS_PARAMETER_COUNT; i++) {
            visitMethodAccessMethod(i);
            visitMethodAccessBridge(i);
        }
    }

    /**
     * visitMethodAccessBridge
     * @param parameterCount parameterCount
     */
    private void visitMethodAccessBridge(int parameterCount) {
        //public Method* call(Object):void
        mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "call", getMethodAccessMethodDescriptor(parameterCount, "Ljava/lang/Object;"), null, null);
        mv.visitCode();
        mv.visitLabel(new Label());
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitTypeInsn(CHECKCAST, internalName);
        mv.visitVarInsn(ILOAD, 2);
        for (int i = 0; i < parameterCount; i++) {
            mv.visitVarInsn(ALOAD, 3 + i);
        }
        mv.visitMethodInsn(INVOKEVIRTUAL, classAccessInternalName, "call", getMethodAccessMethodDescriptor(parameterCount), false);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(3 + parameterCount, 3 + parameterCount);
        mv.visitEnd();
    }
    /**
     * visitMethodAccessMethod
     *
     * @param parameterCount parameterCount
     */
    private void visitMethodAccessMethod(int parameterCount) {
        List<ClassMethodInfo> methodsWithParamCount = paramCountMethodsMap.get(parameterCount);
        mv = cw.visitMethod(ACC_PUBLIC, "call", getMethodAccessMethodDescriptor(parameterCount), null, null);
        mv.visitCode();
        Label firstLabel = new Label();
        mv.visitLabel(firstLabel);
        mv.visitVarInsn(ILOAD, 2);
        Label defaultCaseLabel = new Label();
        if (methodsWithParamCount == null || methodsWithParamCount.isEmpty()) {
            mv.visitInsn(POP);
            mv.visitLabel(defaultCaseLabel);
            visitMethodAccessLastPart(firstLabel, parameterCount);
            return;
        }
        boolean useTableSwitch = useTableSwitch(methodsWithParamCount);
        Label[] labels = useTableSwitch ? getTableSwitchLabelsForAccess(defaultCaseLabel, methodsWithParamCount) : newLabelArray(methodsWithParamCount.size());
        if (useTableSwitch) {
            mv.visitTableSwitchInsn(methodsWithParamCount.get(0).memberIndex, methodsWithParamCount.get(methodsWithParamCount.size() - 1).memberIndex, defaultCaseLabel, labels);
        } else {
            mv.visitLookupSwitchInsn(defaultCaseLabel, methodsWithParamCount.stream().mapToInt(f -> f.memberIndex).toArray(), labels);
        }
        for (int i = 0; i < methodsWithParamCount.size(); i++) {
            ClassMethodInfo methodInfo = methodsWithParamCount.get(i);
            mv.visitLabel(labels[i]);
            mv.visitFrame(F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 1);
            List<ParameterInfo> parameters = methodInfo.parameters;
            for (int j = 0; j < parameters.size(); j++) {
                ParameterInfo parameter = parameters.get(j);
                mv.visitVarInsn(ALOAD, 3 + j);
                checkCast(parameter);
            }
            mv.visitMethodInsn(methodInfo.invokeOpcode, internalName, methodInfo.name, methodInfo.descriptor, true);
            if (methodInfo.method.getReturnType() == Void.TYPE) {
                mv.visitLabel(new Label());
                mv.visitInsn(ACONST_NULL);
            }
            mv.visitInsn(ARETURN);
        }
        mv.visitLabel(defaultCaseLabel);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        visitMethodAccessLastPart(firstLabel, parameterCount);
    }

    /**
     * getMethodAccessMethodDescriptor
     *
     * @param parameterCount parameterCount
     * @return String
     */
    private String getMethodAccessMethodDescriptor(int parameterCount) {
        return getMethodAccessMethodDescriptor(parameterCount, classTypeDescriptor);
    }

    /**
     * getMethodAccessMethodDescriptor
     *
     * @param parameterCount parameterCount
     * @param typeDescriptor typeDescriptor
     * @return String
     */
    private String getMethodAccessMethodDescriptor(int parameterCount, String typeDescriptor) {
        StringBuilder desc = new StringBuilder("(" + typeDescriptor + "I");
        for (int i = 0; i < parameterCount; i++) {
            desc.append("Ljava/lang/Object;");
        }
        return desc.append(")Ljava/lang/Object;").toString();
    }

    /**
     * 调用链
     */
    private void visitMethodAccessInvokeBridge() {
        //public Method* invoke(Object, Object): Object
        mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_VARARGS + ACC_SYNTHETIC, "invoke", "(Ljava/lang/Object;I[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        mv.visitCode();
        mv.visitLabel(new Label());
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitTypeInsn(CHECKCAST, internalName);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKEVIRTUAL, classAccessInternalName, "invoke", "(" + classTypeDescriptor + "I[Ljava/lang/Object;)Ljava/lang/Object;", false);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(4, 4);
        mv.visitEnd();
    }

    /**
     * 执行方法
     */
    private void visitMethodAccessInvokeMethod() {
        //public Method invoke(Object): Object
        mv = cw.visitMethod(ACC_PUBLIC + ACC_VARARGS, "invoke", "(" + classTypeDescriptor + "I[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        mv.visitCode();
        Label firstLabel = new Label();
        mv.visitLabel(firstLabel);
        mv.visitVarInsn(ILOAD, 2);
        Label defaultCaseLabel = new Label();
        if (methodInfoList == null || methodInfoList.isEmpty()) {
            mv.visitInsn(POP);
            mv.visitLabel(defaultCaseLabel);
            visitMethodAccessInvokeMethodLastPart(firstLabel);
            return;
        }
        Label[] labels = getTableSwitchLabelsForAccess(defaultCaseLabel, methodInfoList); /* Always use a table switch because there are no gaps between method indices*/
        mv.visitTableSwitchInsn(methodInfoList.get(0).memberIndex, methodInfoList.get(methodInfoList.size() - 1).memberIndex, defaultCaseLabel, labels);
        for (int i = 0; i < methodInfoList.size(); i++) {
            ClassMethodInfo methodInfo = methodInfoList.get(i);
            mv.visitLabel(labels[i]);
            mv.visitFrame(F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 1);
            List<ParameterInfo> parameters = methodInfo.parameters;
            for (int j = 0; j < parameters.size(); j++) {
                ParameterInfo parameter = parameters.get(j);
                mv.visitVarInsn(ALOAD, 3);
                AsmUtils.visitZeroOperandInt(mv, j);
                mv.visitInsn(AALOAD);
                checkCast(parameter);
            }
            mv.visitMethodInsn(methodInfo.invokeOpcode, internalName, methodInfo.name, methodInfo.descriptor, true);
            if (methodInfo.method.getReturnType() == Void.TYPE) {
                mv.visitLabel(new Label());
                mv.visitInsn(ACONST_NULL);
            }
            mv.visitInsn(ARETURN);
        }
        mv.visitLabel(defaultCaseLabel);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        visitMethodAccessInvokeMethodLastPart(firstLabel);
    }

    /**
     * 最后一部分方法
     *
     * @param firstLabel firstLabel
     */
    private void visitMethodAccessInvokeMethodLastPart(Label firstLabel) {
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("No method with index: ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(ATHROW);
        Label lastLabel = new Label();
        mv.visitLabel(lastLabel);
        mv.visitLocalVariable("this", classAccessTypeDescriptor, null, firstLabel, lastLabel, 0);
        mv.visitLocalVariable("obj", classTypeDescriptor, null, firstLabel, lastLabel, 1);
        mv.visitLocalVariable("methodIndex", "I", null, firstLabel, lastLabel, 2);
        mv.visitLocalVariable("args", "[Ljava/lang/Object;", null, firstLabel, lastLabel, 3);
        mv.visitMaxs(5, 4);
        mv.visitEnd();
    }

    /**
     * 最后一部分方法
     *
     * @param firstLabel     firstLabel
     * @param parameterCount parameterCount
     */
    private void visitMethodAccessLastPart(Label firstLabel, int parameterCount) {
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("No method with index ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitLabel(new Label());
        mv.visitVarInsn(ILOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
        mv.visitLdcInsn(" with " + parameterCount + " parameter(s)");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitLabel(new Label());
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(ATHROW);
        Label lastLabel = new Label();
        mv.visitLabel(lastLabel);
        mv.visitLocalVariable("this", classAccessTypeDescriptor, null, firstLabel, lastLabel, 0);
        mv.visitLocalVariable("obj", classTypeDescriptor, null, firstLabel, lastLabel, 1);
        mv.visitLocalVariable("methodIndex", "I", null, firstLabel, lastLabel, 2);

        for (int i = 0; i < parameterCount; i++) {
            mv.visitLocalVariable("arg" + i, "Ljava/lang/Object;", null, firstLabel, lastLabel, 3 + i);
        }

        mv.visitMaxs(5, 3 + parameterCount);
        mv.visitEnd();
    }

    /**
     * 访问方法索引
     */
    private void visitMethodIndexMethod() {
        //public Method methodIndex(String, Class): int
        mv = cw.visitMethod(ACC_PUBLIC + ACC_VARARGS, "methodIndex", "(Ljava/lang/String;[Ljava/lang/Class;)I", "(Ljava/lang/String;[Ljava/lang/Class<*>;)I", null);
        mv.visitCode();
        final Label firstLabel = new Label();
        mv.visitLabel(firstLabel);
        List<MethodNameReturnIndex> methodIndices = getMethodIndexSwitchCases();
        if (methodIndices.isEmpty()) {
            visitMethodIndexMethodLastPart(firstLabel, methodIndices);
            return;
        }
        mv.visitVarInsn(ALOAD, 1);
        mv.visitInsn(DUP);
        mv.visitVarInsn(ASTORE, 3);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "hashCode", "()I", false);
        final Label defaultCaseLabel = new Label();
        Collections.sort(methodIndices, StringCaseReturnIndex::compareHashCode);
        int[] methodNameHashCodes = new int[methodIndices.size()];
        Label[] caseLabels = new Label[methodIndices.size()];
        for (int i = 0; i < methodIndices.size(); i++) {
            methodNameHashCodes[i] = methodIndices.get(i).hashCode;
            caseLabels[i] = new Label();
        }
        mv.visitLookupSwitchInsn(defaultCaseLabel, methodNameHashCodes, caseLabels);
        for (int i = 0; i < methodIndices.size(); i++) {
            MethodNameReturnIndex methodIndex = methodIndices.get(i);
            mv.visitLabel(caseLabels[i]);
            if (i > 0) {
                mv.visitFrame(F_SAME, 0, null, 0, null);
            } else {
                mv.visitFrame(F_APPEND, 1, new Object[]{"java/lang/String"}, 0, null);
            }
            mv.visitVarInsn(ALOAD, 3);
            mv.visitLdcInsn(methodIndex.name);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
            mv.visitJumpInsn(IFNE, methodIndex.returnIndexLabel);
            mv.visitJumpInsn(GOTO, defaultCaseLabel);
        }
        Collections.sort(methodIndices, StringCaseReturnIndex::compareIndex);
        for (int i = 0; i < methodIndices.size(); i++) {
            visitMethodIndexReturnStatements(methodIndices.get(i), defaultCaseLabel);
        }
        mv.visitLabel(defaultCaseLabel);
        mv.visitFrame(F_CHOP, 1, null, 0, null);
        visitMethodIndexMethodLastPart(firstLabel, methodIndices);
    }

    /**
     * 设置最有一部分方法
     *
     * @param firstLabel    第一个Label
     * @param methodIndices 方法信息
     */
    private void visitMethodIndexMethodLastPart(Label firstLabel, List<MethodNameReturnIndex> methodIndices) {
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("No method called ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitLabel(new Label());
        mv.visitLdcInsn(" with parameters ");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKESTATIC, "java/util/Arrays", "stream", "([Ljava/lang/Object;)Ljava/util/stream/Stream;", false);
        mv.visitLabel(new Label());
        mv.visitInvokeDynamicInsn("apply", "()Ljava/util/function/Function;", new Handle(H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", false), new Object[]{Type.getType("(Ljava/lang/Object;)Ljava/lang/Object;"), new Handle(H_INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;", false), Type.getType("(Ljava/lang/Class;)Ljava/lang/String;")});
        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/stream/Stream", "map", "(Ljava/util/function/Function;)Ljava/util/stream/Stream;", true);
        mv.visitLabel(new Label());
        mv.visitMethodInsn(INVOKESTATIC, "java/util/stream/Collectors", "toList", "()Ljava/util/stream/Collector;", false);
        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/stream/Stream", "collect", "(Ljava/util/stream/Collector;)Ljava/lang/Object;", true);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitLabel(new Label());
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(ATHROW);
        Label lastLabel = new Label();
        mv.visitLabel(lastLabel);
        mv.visitLocalVariable("this", classAccessTypeDescriptor, null, firstLabel, lastLabel, 0);
        mv.visitLocalVariable("name", "Ljava/lang/String;", null, firstLabel, lastLabel, 1);
        mv.visitLocalVariable("parameterTypes", "[Ljava/lang/Class;", null, firstLabel, lastLabel, 2);
        mv.visitMaxs(5, methodIndices.isEmpty() ? 3 : 4);
        mv.visitEnd();
    }

    /**
     * 方法返回追
     *
     * @param methodIndex      方法索引
     * @param defaultCaseLabel 默认Label
     */
    private void visitMethodIndexReturnStatements(MethodNameReturnIndex methodIndex, Label defaultCaseLabel) {
        Label jumpLabel = methodIndex.returnIndexLabel;
        List<ClassMethodInfo> methods = methodIndex.methods;
        for (int i = 0; i < methods.size(); i++) {
            ClassMethodInfo methodInfo = methods.get(i);
            Class<?>[] parameterTypes = methodInfo.method.getParameterTypes();
            mv.visitLabel(jumpLabel);
            mv.visitFrame(F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 2);
            AsmUtils.visitZeroOperandInt(mv, parameterTypes.length);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
            for (int j = 0; j < parameterTypes.length; j++) {
                mv.visitInsn(DUP);
                AsmUtils.visitZeroOperandInt(mv, j);
                Class<?> parameterType = parameterTypes[j];
                if (parameterType.isPrimitive()) {
                    mv.visitFieldInsn(GETSTATIC, Type.getInternalName(ClassUtils.primitiveToWrapper(parameterType)), "TYPE", "Ljava/lang/Class;");
                } else {
                    mv.visitLdcInsn(Type.getType(parameterType));
                }
                mv.visitInsn(AASTORE);
            }
            mv.visitMethodInsn(INVOKESTATIC, "java/util/Arrays", "equals", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", false);
            jumpLabel = i + 1 >= methods.size() ? defaultCaseLabel : new Label();
            mv.visitJumpInsn(IFEQ, jumpLabel);
            mv.visitLabel(new Label());
            AsmUtils.visitZeroOperandInt(mv, methodInfo.memberIndex);
            mv.visitInsn(IRETURN);
        }
    }

    /**
     * 获取方法索引对象
     *
     * @return 方法索引对象
     */
    private List<MethodNameReturnIndex> getMethodIndexSwitchCases() {
        List<MethodNameReturnIndex> methodIndices = new ArrayList<>();
        Map<String, List<ClassMethodInfo>> overloadedMap = new HashMap<>();

        for (int i = 0; i < methodInfoList.size(); i++) {
            ClassMethodInfo methodInfo = methodInfoList.get(i);

            List<ClassMethodInfo> overloadedMethods = overloadedMap.get(methodInfo.name);
            if (overloadedMethods == null) {
                overloadedMethods = new ArrayList<>();
                overloadedMap.put(methodInfo.name, overloadedMethods);

                overloadedMethods.add(methodInfo);
                methodIndices.add(new MethodNameReturnIndex(overloadedMethods));
            } else {
                overloadedMethods.add(methodInfo);
            }
        }

        return methodIndices;
    }

    /**
     * 访问属性访问方法
     */
    private void visitPropertyAccessMethods() {
        List<AccessInfo> propertyAccessInfoList = Collections.unmodifiableList(
                Arrays.asList(
                        // Primitive types
                        //
                        AccessInfo.forPrimitive(MEMBER_TYPE_PROPERTY, Type.BOOLEAN_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_PROPERTY, Type.BYTE_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_PROPERTY, Type.CHAR_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_PROPERTY, Type.DOUBLE_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_PROPERTY, Type.FLOAT_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_PROPERTY, Type.INT_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_PROPERTY, Type.LONG_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_PROPERTY, Type.SHORT_TYPE),
                        // Primitive wrapper types
                        //
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_PROPERTY, Boolean.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_PROPERTY, Byte.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_PROPERTY, Character.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_PROPERTY, Double.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_PROPERTY, Float.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_PROPERTY, Integer.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_PROPERTY, Long.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_PROPERTY, Short.class),
                        // Common reference types
                        //
                        AccessInfo.forReferenceType(MEMBER_TYPE_PROPERTY, BigDecimal.class),
                        AccessInfo.forReferenceType(MEMBER_TYPE_PROPERTY, Date.class),
                        AccessInfo.forReferenceType(MEMBER_TYPE_PROPERTY, LocalDate.class),
                        AccessInfo.forReferenceType(MEMBER_TYPE_PROPERTY, LocalDateTime.class),
                        AccessInfo.forReferenceType(MEMBER_TYPE_PROPERTY, String.class)
                )
        );

        for (int i = 0; i < propertyAccessInfoList.size(); i++) {
            AccessInfo propertyAccessInfo = propertyAccessInfoList.get(i);

            visitAccessGetter(typeToAccessorsMap.get(propertyAccessInfo.className), propertyAccessInfo);
            visitAccessGetterBridge(propertyAccessInfo);
            visitAccessSetter(typeToMutatorsMap.get(propertyAccessInfo.className), propertyAccessInfo);
            visitAccessSetterBridge(propertyAccessInfo);
        }

        visitGeneralAccessGetter("getProperty", MEMBER_TYPE_PROPERTY, accessorInfoList);
        visitAccessGetterBridge("getProperty", "Ljava/lang/Object;", ARETURN);
        visitGeneralAccessSetter("setProperty", MEMBER_TYPE_PROPERTY, mutatorInfoList);
        visitAccessSetterBridge("setProperty", ALOAD, "Ljava/lang/Object;");
    }

    /**
     * 访问字段
     */
    private void visitFieldAccessMethods() {
        List<AccessInfo> fieldAccessInfoList = Collections.unmodifiableList(
                Arrays.asList(
                        // Primitive types
                        //
                        AccessInfo.forPrimitive(MEMBER_TYPE_FIELD, Type.BOOLEAN_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_FIELD, Type.BYTE_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_FIELD, Type.CHAR_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_FIELD, Type.DOUBLE_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_FIELD, Type.FLOAT_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_FIELD, Type.INT_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_FIELD, Type.LONG_TYPE),
                        AccessInfo.forPrimitive(MEMBER_TYPE_FIELD, Type.SHORT_TYPE),
                        // Primitive wrapper types
                        //
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_FIELD, Boolean.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_FIELD, Byte.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_FIELD, Character.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_FIELD, Double.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_FIELD, Float.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_FIELD, Integer.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_FIELD, Long.class),
                        AccessInfo.forPrimitiveWrapper(MEMBER_TYPE_FIELD, Short.class),
                        // Common reference types
                        //
                        AccessInfo.forReferenceType(MEMBER_TYPE_FIELD, BigDecimal.class),
                        AccessInfo.forReferenceType(MEMBER_TYPE_FIELD, Date.class),
                        AccessInfo.forReferenceType(MEMBER_TYPE_FIELD, LocalDate.class),
                        AccessInfo.forReferenceType(MEMBER_TYPE_FIELD, LocalDateTime.class),
                        AccessInfo.forReferenceType(MEMBER_TYPE_FIELD, String.class)
                )
        );

        for (int i = 0; i < fieldAccessInfoList.size(); i++) {
            AccessInfo fieldAccessInfo = fieldAccessInfoList.get(i);
            List<ClassFieldInfo> fieldInfoList = typeToFieldsMap.get(fieldAccessInfo.className);

            visitAccessGetter(fieldInfoList, fieldAccessInfo);
            visitAccessGetterBridge(fieldAccessInfo);
            visitAccessSetter(fieldInfoList, fieldAccessInfo);
            visitAccessSetterBridge(fieldAccessInfo);
        }

        visitGeneralAccessGetter("getField", MEMBER_TYPE_FIELD, fieldInfoList);
        visitAccessGetterBridge("getField", "Ljava/lang/Object;", ARETURN);
        visitGeneralAccessSetter("setField", MEMBER_TYPE_FIELD, fieldInfoList);
        visitAccessSetterBridge("setField", ALOAD, "Ljava/lang/Object;");
    }

    /**
     * visitGeneralAccessSetter
     *
     * @param methodName     methodName
     * @param memberType     memberType
     * @param memberInfoList memberInfoList
     */
    private void visitGeneralAccessSetter(String methodName, String memberType, List<? extends AssignableInfo> memberInfoList) {
        //public void setField/setProperty
        mv = cw.visitMethod(ACC_PUBLIC, methodName, "(" + classTypeDescriptor + "ILjava/lang/Object;)V", null, null);
        mv.visitCode();
        Label firstLabel = new Label();
        mv.visitLabel(firstLabel);
        mv.visitVarInsn(ILOAD, 2);
        Label defaultCaseLabel = new Label();
        if (memberInfoList == null || memberInfoList.isEmpty()) {
            mv.visitInsn(POP);
            mv.visitLabel(defaultCaseLabel);
            visitAccessSetterLastPart(memberType, "Ljava/lang/Object;", firstLabel, null);
            return;
        }
        Label[] labels = getTableSwitchLabelsForAccess(defaultCaseLabel, memberInfoList); /* Always use a table switch because there are no gaps between member indices*/
        mv.visitTableSwitchInsn(memberInfoList.get(0).memberIndex, memberInfoList.get(memberInfoList.size() - 1).memberIndex, defaultCaseLabel, labels);
        Label breakLabel = new Label();
        for (int i = 0; i < memberInfoList.size(); i++) {
            AssignableInfo member = memberInfoList.get(i);
            mv.visitLabel(labels[i]);
            mv.visitFrame(F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 3);
            checkCast(member);
            switch (memberType) {
                case MEMBER_TYPE_FIELD:
                    mv.visitFieldInsn(((ClassFieldInfo) member).setFieldOpcode, internalName, member.name, member.descriptor);
                    break;
                case MEMBER_TYPE_PROPERTY:
                    mv.visitMethodInsn(INVOKEVIRTUAL, internalName, ((ClassPropertyInfo) member).writeMethodName, "(" + member.descriptor + ")V", false);
                    break;
            }
            mv.visitLabel(new Label());
            mv.visitJumpInsn(GOTO, breakLabel);
        }
        mv.visitLabel(defaultCaseLabel);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        visitAccessSetterLastPart(memberType, "Ljava/lang/Object;", firstLabel, breakLabel);
    }

    /**
     * 检测映射
     *
     * @param castable 映射
     */
    private void checkCast(Castable castable) {
        String internalNameOfCast = castable.getInternalName();
        if (castable.getType().isPrimitive()) {
            Class<?> wrapperType = ClassUtils.primitiveToWrapper(castable.getType());
            internalNameOfCast = Type.getInternalName(wrapperType);
        }
        mv.visitTypeInsn(CHECKCAST, internalNameOfCast);

        if (castable.getType().isPrimitive()) {
            mv.visitMethodInsn(INVOKEVIRTUAL, internalNameOfCast, castable.getType().getName() + "Value", "()" + castable.getDescriptor(), false);
        }
    }

    /**
     * visitGeneralAccessGetter
     *
     * @param methodName     methodName
     * @param memberType     memberType
     * @param memberInfoList memberInfoList
     */
    private void visitGeneralAccessGetter(String methodName, String memberType, List<? extends AssignableInfo> memberInfoList) {
        // public Field getField/getProperty()
        mv = cw.visitMethod(ACC_PUBLIC, methodName, "(" + classTypeDescriptor + "I)Ljava/lang/Object;", null, null);
        mv.visitCode();
        Label firstLabel = new Label();
        mv.visitLabel(firstLabel);
        mv.visitVarInsn(ILOAD, 2);
        Label defaultCaseLabel = new Label();
        if (memberInfoList == null || memberInfoList.isEmpty()) {
            mv.visitInsn(POP);
            mv.visitLabel(defaultCaseLabel);
            visitAccessGetterLastPart(memberType, firstLabel);
            return;
        }
        Label[] labels = getTableSwitchLabelsForAccess(defaultCaseLabel, memberInfoList); /* Always use a table switch because there are no gaps between member indices*/
        mv.visitTableSwitchInsn(memberInfoList.get(0).memberIndex, memberInfoList.get(memberInfoList.size() - 1).memberIndex, defaultCaseLabel, labels);
        for (int i = 0; i < memberInfoList.size(); i++) {
            AssignableInfo member = memberInfoList.get(i);
            mv.visitLabel(labels[i]);
            mv.visitFrame(F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 1);
            switch (memberType) {
                case MEMBER_TYPE_FIELD:
                    mv.visitFieldInsn(((ClassFieldInfo) member).getFieldOpcode, internalName, member.name, member.descriptor);
                    break;
                case MEMBER_TYPE_PROPERTY:
                    mv.visitMethodInsn(INVOKEVIRTUAL, internalName, ((ClassPropertyInfo) member).readMethodName, "()" + member.descriptor, false);
                    break;
            }
            if (member.type.isPrimitive()) {
                Class<?> wrapperType = ClassUtils.primitiveToWrapper(member.type);
                mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(wrapperType), "valueOf", "(" + member.descriptor + ")" + Type.getDescriptor(wrapperType), false);
            }
            mv.visitInsn(ARETURN);
        }
        mv.visitLabel(defaultCaseLabel);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        visitAccessGetterLastPart(memberType, firstLabel);
    }

    /**
     * 设置Setter
     *
     * @param memberInfoList memberInfoList
     * @param accessInfo     accessInfo
     */
    private void visitAccessSetter(List<? extends AssignableInfo> memberInfoList, AccessInfo accessInfo) {
        //public void <setMethodName>: void
        mv = cw.visitMethod(ACC_PUBLIC, accessInfo.setMethodName, "(" + classTypeDescriptor + "I" + accessInfo.descriptor + ")V", null, null);
        mv.visitCode();
        Label firstLabel = new Label();
        mv.visitLabel(firstLabel);
        mv.visitVarInsn(ILOAD, 2);
        Label defaultCaseLabel = new Label();
        if (memberInfoList == null || memberInfoList.isEmpty()) {
            mv.visitInsn(POP);
            mv.visitLabel(defaultCaseLabel);
            visitAccessSetterLastPart(accessInfo.memberType, accessInfo.descriptor, firstLabel, null);
            return;
        }
        boolean useTableSwitch = useTableSwitch(memberInfoList);
        Label[] labels = useTableSwitch ? getTableSwitchLabelsForAccess(defaultCaseLabel, memberInfoList) : newLabelArray(memberInfoList.size());
        if (useTableSwitch) {
            mv.visitTableSwitchInsn(memberInfoList.get(0).memberIndex, memberInfoList.get(memberInfoList.size() - 1).memberIndex, defaultCaseLabel, labels);
        } else {
            mv.visitLookupSwitchInsn(defaultCaseLabel, memberInfoList.stream().mapToInt(f -> f.memberIndex).toArray(), labels);
        }
        Label breakLabel = new Label();
        for (int i = 0; i < memberInfoList.size(); i++) {
            MemberInfo memberInfo = memberInfoList.get(i);
            mv.visitLabel(labels[i]);
            mv.visitFrame(F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(accessInfo.loadOpcode, 3);
            switch (accessInfo.memberType) {
                case MEMBER_TYPE_FIELD:
                    mv.visitFieldInsn(((ClassFieldInfo) memberInfo).setFieldOpcode, internalName, memberInfo.name, accessInfo.descriptor);
                    break;
                case MEMBER_TYPE_PROPERTY:
                    mv.visitMethodInsn(INVOKEVIRTUAL, internalName, ((ClassPropertyInfo) memberInfo).writeMethodName, "(" + accessInfo.descriptor + ")V", false);
                    break;
            }
            mv.visitLabel(new Label());
            mv.visitJumpInsn(GOTO, breakLabel);
        }
        mv.visitLabel(defaultCaseLabel);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        visitAccessSetterLastPart(accessInfo.memberType, accessInfo.descriptor, firstLabel, breakLabel);
    }

    /**
     * visitAccessSetterBridge
     *
     * @param accessInfo accessInfo
     */
    private void visitAccessSetterBridge(AccessInfo accessInfo) {
        visitAccessSetterBridge(accessInfo.setMethodName, accessInfo.loadOpcode, accessInfo.descriptor);
    }

    /**
     * visitAccessSetterBridge
     *
     * @param methodName methodName
     * @param loadOpcode loadOpcode
     * @param descriptor descriptor
     */
    private void visitAccessSetterBridge(String methodName, int loadOpcode, String descriptor) {
        //public Method* <methodName>: void
        mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, methodName, "(Ljava/lang/Object;I" + descriptor + ")V", null, null);
        mv.visitCode();
        mv.visitLabel(new Label());
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitTypeInsn(CHECKCAST, internalName);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitVarInsn(loadOpcode, 3);
        mv.visitMethodInsn(INVOKEVIRTUAL, classAccessInternalName, methodName, "(" + classTypeDescriptor + "I" + descriptor + ")V", false);
        mv.visitInsn(RETURN);
        if (isDescriptorDoubleOrLong(descriptor)) {
            mv.visitMaxs(5, 5);
        } else {
            mv.visitMaxs(4, 4);
        }
        mv.visitEnd();
    }

    /**
     * 是否是Long/Double
     *
     * @param descriptor descriptor
     * @return boolean
     */
    private static boolean isDescriptorDoubleOrLong(String descriptor) {
        return descriptor.equals(Type.DOUBLE_TYPE.getDescriptor())
                || descriptor.equals(Type.LONG_TYPE.getDescriptor());
    }

    /**
     * 访问最后一个Setter
     *
     * @param memberType      memberType
     * @param fieldDescriptor fieldDescriptor
     * @param firstLabel      firstLabel
     * @param breakLabel      breakLabel
     */
    private void visitAccessSetterLastPart(String memberType, String fieldDescriptor, Label firstLabel, Label breakLabel) {
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("No " + memberType + " with index: ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(ATHROW);
        if (breakLabel != null) {
            mv.visitLabel(breakLabel);
            mv.visitFrame(F_SAME, 0, null, 0, null);
            mv.visitInsn(RETURN);
        }
        Label lastLabel = new Label();
        mv.visitLabel(lastLabel);
        mv.visitLocalVariable("this", classAccessTypeDescriptor, null, firstLabel, lastLabel, 0);
        mv.visitLocalVariable("obj", classTypeDescriptor, null, firstLabel, lastLabel, 1);
        mv.visitLocalVariable(memberType + "Index", "I", null, firstLabel, lastLabel, 2);
        mv.visitLocalVariable("x", fieldDescriptor, null, firstLabel, lastLabel, 3);
        mv.visitMaxs(5, isDescriptorDoubleOrLong(fieldDescriptor) ? 5 : 4);
        mv.visitEnd();
    }

    /**
     * visitAccessGetterBridge
     *
     * @param accessInfo accessInfo
     */
    private void visitAccessGetterBridge(AccessInfo accessInfo) {
        visitAccessGetterBridge(accessInfo.getMethodName, accessInfo.descriptor, accessInfo.returnOpcode);
    }

    /**
     * visitAccessGetterBridge
     *
     * @param methodName           methodName
     * @param returnTypeDescriptor returnTypeDescriptor
     * @param returnOpcode         returnOpcode
     */
    private void visitAccessGetterBridge(String methodName, String returnTypeDescriptor, int returnOpcode) {
        //public Method* <methodName>: <returnTypeDescriptor>
        mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, methodName, "(Ljava/lang/Object;I)" + returnTypeDescriptor, null, null);
        mv.visitCode();
        mv.visitLabel(new Label());
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitTypeInsn(CHECKCAST, internalName);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, classAccessInternalName, methodName, "(" + classTypeDescriptor + "I)" + returnTypeDescriptor, false);
        mv.visitInsn(returnOpcode);
        mv.visitMaxs(3, 3);
        mv.visitEnd();
    }

    /**
     * 访问Getter
     *
     * @param memberInfoList memberInfoList
     * @param accessInfo     accessInfo
     */
    private void visitAccessGetter(List<? extends AssignableInfo> memberInfoList, AccessInfo accessInfo) {
        mv = cw.visitMethod(ACC_PUBLIC, accessInfo.getMethodName, "(" + classTypeDescriptor + "I)" + accessInfo.descriptor, null, null);
        mv.visitCode();
        Label firstLabel = new Label();
        mv.visitLabel(firstLabel);
        mv.visitVarInsn(ILOAD, 2);

        Label defaultCaseLabel = new Label();

        if (memberInfoList == null
                || memberInfoList.isEmpty()) {
            mv.visitInsn(POP);
            mv.visitLabel(defaultCaseLabel);
            visitAccessGetterLastPart(accessInfo.memberType, firstLabel);
            return;
        }

        boolean useTableSwitch = useTableSwitch(memberInfoList);
        Label[] labels = useTableSwitch ? getTableSwitchLabelsForAccess(defaultCaseLabel, memberInfoList)
                : newLabelArray(memberInfoList.size());

        if (useTableSwitch) {
            mv.visitTableSwitchInsn(
                    memberInfoList.get(0).memberIndex,
                    memberInfoList.get(memberInfoList.size() - 1).memberIndex,
                    defaultCaseLabel,
                    labels
            );
        } else {
            mv.visitLookupSwitchInsn(defaultCaseLabel, memberInfoList.stream().mapToInt(f -> f.memberIndex).toArray(), labels);
        }

        for (int i = 0; i < memberInfoList.size(); i++) {
            MemberInfo memberInfo = memberInfoList.get(i);

            mv.visitLabel(labels[i]);
            mv.visitFrame(F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 1);

            switch (accessInfo.memberType) {
                case MEMBER_TYPE_FIELD:
                    mv.visitFieldInsn(((ClassFieldInfo) memberInfo).getFieldOpcode, internalName, memberInfo.name, accessInfo.descriptor);
                    break;
                case MEMBER_TYPE_PROPERTY:
                    mv.visitMethodInsn(INVOKEVIRTUAL, internalName, ((ClassPropertyInfo) memberInfo).readMethodName, "()" + accessInfo.descriptor, false);
                    break;
                default:
            }

            mv.visitInsn(accessInfo.returnOpcode);
        }

        mv.visitLabel(defaultCaseLabel);
        mv.visitFrame(F_SAME, 0, null, 0, null);

        visitAccessGetterLastPart(accessInfo.memberType, firstLabel);
    }

    /**
     * getTableSwitchLabelsForAccess
     *
     * @param defaultCaseLabel defaultCaseLabel
     * @param members          members
     * @return Label[]
     */
    private static Label[] getTableSwitchLabelsForAccess(Label defaultCaseLabel, List<? extends MemberInfo> members) {
        return AsmUtils.getTableSwitchLabels(defaultCaseLabel, members.stream().mapToInt(m -> m.memberIndex).toArray());
    }

    /**
     * new Label array
     *
     * @param size size
     * @return Label array
     */
    private static Label[] newLabelArray(int size) {
        Label[] labels = new Label[size];
        for (int i = 0; i < size; i++) {
            labels[i] = new Label();
        }
        return labels;
    }

    /**
     * 访问最后一个Getter
     *
     * @param memberType memberType
     * @param firstLabel firstLabel
     */
    private void visitAccessGetterLastPart(String memberType, Label firstLabel) {
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("No " + memberType + " with index: ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(ATHROW);
        Label lastLabel = new Label();
        mv.visitLabel(lastLabel);
        mv.visitLocalVariable("this", classAccessTypeDescriptor, null, firstLabel, lastLabel, 0);
        mv.visitLocalVariable("obj", classTypeDescriptor, null, firstLabel, lastLabel, 1);
        mv.visitLocalVariable(memberType + "Index", "I", null, firstLabel, lastLabel, 2);
        mv.visitMaxs(5, 3);
        mv.visitEnd();
    }

    /**
     * 构建方法
     *
     * @param categoryOfStringCase    categoryOfStringCase
     * @param stringCaseReturnIndices stringCaseReturnIndices
     */
    private void visitIndexMethod(String categoryOfStringCase, List<StringCaseReturnIndex> stringCaseReturnIndices) {
        mv = cw.visitMethod(ACC_PUBLIC, categoryOfStringCase + "Index", "(Ljava/lang/String;)I", null, null);
        mv.visitCode();
        final Label firstLabel = new Label();
        mv.visitLabel(firstLabel);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitInsn(DUP);
        mv.visitVarInsn(ASTORE, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "hashCode", "()I", false);

        final Label defaultCaseLabel = new Label();

        if (stringCaseReturnIndices.isEmpty()) {
            mv.visitInsn(POP);
            mv.visitLabel(defaultCaseLabel);
            visitIndexMethodLastPart(categoryOfStringCase, firstLabel);
            return;
        }

        Collections.sort(stringCaseReturnIndices, StringCaseReturnIndex::compareHashCode);

        int[] stringHashCodes = new int[stringCaseReturnIndices.size()];
        Label[] caseLabels = new Label[stringCaseReturnIndices.size()];
        for (int i = 0; i < stringCaseReturnIndices.size(); i++) {
            stringHashCodes[i] = stringCaseReturnIndices.get(i).hashCode;
            caseLabels[i] = new Label();
        }

        mv.visitLookupSwitchInsn(defaultCaseLabel, stringHashCodes, caseLabels);

        for (int i = 0; i < stringCaseReturnIndices.size(); i++) {
            StringCaseReturnIndex stringCaseReturnIndex = stringCaseReturnIndices.get(i);

            mv.visitLabel(caseLabels[i]);

            if (i > 0) {
                mv.visitFrame(F_SAME, 0, null, 0, null);
            } else {
                mv.visitFrame(F_APPEND, 1, new Object[]{"java/lang/String"}, 0, null);
            }

            mv.visitVarInsn(ALOAD, 2);
            mv.visitLdcInsn(stringCaseReturnIndex.name);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
            mv.visitJumpInsn(IFNE, stringCaseReturnIndex.returnIndexLabel);
            mv.visitJumpInsn(GOTO, defaultCaseLabel);
        }

        Collections.sort(stringCaseReturnIndices, StringCaseReturnIndex::compareIndex);
        for (int i = 0; i < stringCaseReturnIndices.size(); i++) {
            StringCaseReturnIndex stringCaseReturnIndex = stringCaseReturnIndices.get(i);

            mv.visitLabel(stringCaseReturnIndex.returnIndexLabel);
            mv.visitFrame(F_SAME, 0, null, 0, null);
            AsmUtils.visitZeroOperandInt(mv, stringCaseReturnIndex.index);
            mv.visitInsn(IRETURN);
        }

        mv.visitLabel(defaultCaseLabel);
        mv.visitFrame(F_SAME, 0, null, 0, null);

        visitIndexMethodLastPart(categoryOfStringCase, firstLabel);
    }

    /**
     * 结束方法
     *
     * @param categoryOfStringCase categoryOfStringCase
     * @param firstLabel           firstLabel
     */
    private void visitIndexMethodLastPart(String categoryOfStringCase, Label firstLabel) {
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("No " + categoryOfStringCase + " with name: ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(ATHROW);
        Label lastLabel = new Label();
        mv.visitLabel(lastLabel);
        mv.visitLocalVariable("this", classAccessTypeDescriptor, null, firstLabel, lastLabel, 0);
        mv.visitLocalVariable("name", "Ljava/lang/String;", null, firstLabel, lastLabel, 1);
        mv.visitMaxs(5, 3);
        mv.visitEnd();
    }

    /**
     * 获取Member索引
     *
     * @param memberInfoList Member列表
     * @return
     */
    private List<StringCaseReturnIndex> getMemberIndexSwitchCases(List<? extends MemberInfo> memberInfoList) {
        List<StringCaseReturnIndex> memberIndexSwitchCases = new ArrayList<>();
        for (int i = 0; i < memberInfoList.size(); i++) {
            memberIndexSwitchCases.add(new StringCaseReturnIndex(memberInfoList.get(i).name, i));
        }
        return memberIndexSwitchCases;
    }

    /**
     * 访问类
     */
    private void visitClass() {
        //1.待操作的类
        //e.g. com.chua.tools.example.entity.TDemoInfo
        // ->  com/chua/tools/example/entity/TDemoInfo
        internalName = Type.getInternalName(clazz);
        //2.创建类的 ClassAccess 类名
        // TDemoInfoAccess
        String classAccessSimpleName = clazz.getSimpleName() + "ClassAccess";
        //3.创建类的 ClassAccess 类
        // com/chua/tools/example/entity/TDemoInfo$FooClassAccess
        classAccessInternalName = internalName + "$" + classAccessSimpleName;
        //4.创建类
        //版本 1.8
        //public
        //      class
        //           com/chua/tools/example/entity/TDemoInfo$FooClassAccess
        //                  extends
        //                      sun/reflect/MagicAccessorImpl
        //                          implements
        //                              com/chua/utils/tools/classes/ClassAccess
        //public class TDemoInfo$FooClassAccess extends MagicAccessorImpl implements ClassAccess {
        //
        // }
        cw.visit(
                V1_8,
                ACC_PUBLIC + ACC_SUPER,
                classAccessInternalName,
                internalName,
                "sun/reflect/MagicAccessorImpl",
                new String[]{Type.getInternalName(ClassAccess.class)});
        // visitSource(com/chua/tools/example/entity/TDemoInfo.java)
        cw.visitSource(clazz.getSimpleName() + ".java", null);
        //
        cw.visitInnerClass(classAccessInternalName, internalName, classAccessSimpleName, ACC_PUBLIC + ACC_STATIC);

        // For the lamda function Class::getName in methodIndex() method
        // Appears not to be required
//		cw.visitInnerClass(
//				"java/lang/invoke/MethodHandles$Lookup",
//				"java/lang/invoke/MethodHandles",
//				"Lookup",
//				ACC_PUBLIC + ACC_FINAL + ACC_STATIC);

        classAccessTypeDescriptor = "L" + classAccessInternalName + ";";
        classTypeDescriptor = "L" + internalName + ";";
    }

    /**
     * 初始化描述
     */
    private void initializePropertyDescriptors() {
        List<PropertyDescriptor> propertyDescriptors;
        try {
            BeanInfo info = Introspector.getBeanInfo(clazz);
            propertyDescriptors = Collections.unmodifiableList(Arrays.stream(info.getPropertyDescriptors()).filter(prop -> !prop.getName().equals("class")).collect(toList()));
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < propertyDescriptors.size(); i++) {
            addPropertyInfo(new ClassPropertyInfo(propertyDescriptors.get(i), i));
        }
    }

    /**
     * 初始化字段
     */
    private void initializeFields() {
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .sorted(comparing(Field::getName))
                .collect(toList());

        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            ClassUtils.setAccessible(field);
            addFieldInfo(new ClassFieldInfo(field, i));
        }
    }

    /**
     * 初始化方法
     */
    private void initializeMethods() {
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .sorted((a, b) -> {
                    int compareMethodName = a.getName().compareTo(b.getName());
                    if (compareMethodName != 0) {
                        return compareMethodName;
                    }

                    Class<?>[] aParameterTypes = a.getParameterTypes();
                    Class<?>[] bParameterTypes = b.getParameterTypes();

                    int len = Math.min(aParameterTypes.length, bParameterTypes.length);
                    for (int i = 0; i < len; i++) {
                        int compareParamType = aParameterTypes[i].getName().compareTo(bParameterTypes[i].getName());
                        if (compareParamType != 0) {
                            return compareParamType;
                        }
                    }
                    return Integer.compare(aParameterTypes.length, bParameterTypes.length);
                }).collect(toList());

        for (int i = 0; i < methods.size(); i++) {
            Method method = methods.get(i);
            ClassUtils.setAccessible(method);
            addMethodInfo(new ClassMethodInfo(method, i));
        }
    }

    /**
     * 添加方法
     *
     * @param methodInfo 方法
     */
    private void addMethodInfo(ClassMethodInfo methodInfo) {
        List<ClassMethodInfo> methodsWithParamCount = paramCountMethodsMap.get(methodInfo.parameterCount);
        if (methodsWithParamCount == null) {
            methodsWithParamCount = new ArrayList<>();
            paramCountMethodsMap.put(methodInfo.parameterCount, methodsWithParamCount);
        }

        methodsWithParamCount.add(methodInfo);
        methodInfoList.add(methodInfo);
    }

    /**
     * 添加字段
     *
     * @param fieldInfo 字段
     */
    private void addFieldInfo(ClassFieldInfo fieldInfo) {
        String key = fieldInfo.type.getName();

        List<ClassFieldInfo> fieldsOfType = typeToFieldsMap.get(key);
        if (fieldsOfType == null) {
            fieldsOfType = new ArrayList<>();
            typeToFieldsMap.put(key, fieldsOfType);
        }

        fieldsOfType.add(fieldInfo);
        fieldInfoList.add(fieldInfo);
    }

    /**
     * 添加属性
     *
     * @param propertyInfo
     */
    private void addPropertyInfo(ClassPropertyInfo propertyInfo) {
        String key = propertyInfo.type.getName();

        if (propertyInfo.readMethodName != null) {
            List<ClassPropertyInfo> accessorsOfType = typeToAccessorsMap.get(key);
            if (accessorsOfType == null) {
                accessorsOfType = new ArrayList<>();
                typeToAccessorsMap.put(key, accessorsOfType);
            }

            accessorsOfType.add(propertyInfo);
            accessorInfoList.add(propertyInfo);
        }

        if (propertyInfo.writeMethodName != null) {
            List<ClassPropertyInfo> mutatorsOfType = typeToMutatorsMap.get(key);
            if (mutatorsOfType == null) {
                mutatorsOfType = new ArrayList<>();
                typeToMutatorsMap.put(key, mutatorsOfType);
            }

            mutatorsOfType.add(propertyInfo);
            mutatorInfoList.add(propertyInfo);
        }

        propertyInfoList.add(propertyInfo);
    }

    /**
     * useTableSwitch
     * @param members members
     * @return boolean
     */
    private static boolean useTableSwitch(List<? extends MemberInfo> members) {
        return AsmUtils.useTableSwitch(members.stream()
                .mapToInt(m -> m.memberIndex)
                .toArray());
    }
    /**
     * 返回值与索引
     */
    private static class StringCaseReturnIndex {
        private static int compareIndex(StringCaseReturnIndex a, StringCaseReturnIndex b) {
            return Integer.compare(a.index, b.index);
        }

        private static int compareHashCode(StringCaseReturnIndex a, StringCaseReturnIndex b) {
            return Integer.compare(a.hashCode, b.hashCode);
        }

        protected final int hashCode;
        protected final int index;
        protected final String name;
        protected final Label returnIndexLabel = new Label();

        protected StringCaseReturnIndex(String name, int index) {
            hashCode = name.hashCode();
            this.index = index;
            this.name = name;
        }
    }

    /**
     * 访问信息
     */
    private static class AccessInfo {
        private static AccessInfo forPrimitive(String memberType, Type type) {
            String camelCaseClassName = StringUtils.capitalize(type.getClassName());
            String capitalizedMemberType = StringUtils.capitalize(memberType);

            return new AccessInfo(memberType, "get" + camelCaseClassName + capitalizedMemberType, "set" + camelCaseClassName + capitalizedMemberType, type.getClassName(), type.getDescriptor(), type.getOpcode(ILOAD), type.getOpcode(IRETURN));
        }

        private static AccessInfo forPrimitiveWrapper(String memberType, Class<?> clazz) {
            Class<?> primitiveClass = ClassUtils.wrapperToPrimitive(clazz);
            String camelCaseClassName = StringUtils.capitalize(primitiveClass.getName());
            String capitalizedMemberType = StringUtils.capitalize(memberType);

            return new AccessInfo(memberType, "getBoxed" + camelCaseClassName + capitalizedMemberType, "setBoxed" + camelCaseClassName + capitalizedMemberType, clazz.getName(), Type.getDescriptor(clazz), ALOAD, ARETURN);
        }

        private static AccessInfo forReferenceType(String memberType, Class<?> clazz) {
            String capitalizedMemberType = StringUtils.capitalize(memberType);

            return new AccessInfo(memberType, "get" + clazz.getSimpleName() + capitalizedMemberType, "set" + clazz.getSimpleName() + capitalizedMemberType, clazz.getName(), Type.getDescriptor(clazz), ALOAD, ARETURN);
        }

        private final String className;
        private final String descriptor;
        private final String getMethodName;
        private final int loadOpcode;
        private final String memberType;
        private final int returnOpcode;
        private final String setMethodName;

        private AccessInfo(String memberType, String getMethodName, String setMethodName, String className, String descriptor, int loadOpcode, int returnOpcode) {
            this.className = className;
            this.descriptor = descriptor;
            this.getMethodName = getMethodName;
            this.loadOpcode = loadOpcode;
            this.memberType = memberType;
            this.returnOpcode = returnOpcode;
            this.setMethodName = setMethodName;
        }
    }

    /**
     * ClassPropertyInfo
     */
    private static class ClassPropertyInfo extends AssignableInfo {
        private final String readMethodName;
        private final String writeMethodName;

        private ClassPropertyInfo(PropertyDescriptor propertyDescriptor, int propertyIndex) {
            super(propertyDescriptor.getName(), propertyIndex, propertyDescriptor.getPropertyType());
            readMethodName = Optional.ofNullable(propertyDescriptor.getReadMethod()).map(prop -> prop.getName()).orElse(null);
            writeMethodName = Optional.ofNullable(propertyDescriptor.getWriteMethod()).map(prop -> prop.getName()).orElse(null);
        }
    }

    /**
     * 字段
     */
    private static class ClassFieldInfo extends AssignableInfo {
        private final int getFieldOpcode;
        private final int setFieldOpcode;

        private ClassFieldInfo(Field field, int fieldIndex) {
            super(field.getName(), fieldIndex, field.getType());
            getFieldOpcode = Modifier.isStatic(field.getModifiers()) ? GETSTATIC : GETFIELD;
            setFieldOpcode = Modifier.isStatic(field.getModifiers()) ? PUTSTATIC : PUTFIELD;
        }
    }

    /**
     * AssignableInfo
     */
    private static abstract class AssignableInfo extends MemberInfo implements Castable {
        protected final String internalName;
        protected final Class<?> type;

        @Override
        public String getDescriptor() {
            return descriptor;
        }

        @Override
        public String getInternalName() {
            return internalName;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Class getType() {
            return type;
        }

        private AssignableInfo(String name, int memberIndex, Class<?> type) {
            super(name, memberIndex, Type.getDescriptor(type));
            this.internalName = Type.getInternalName(type);
            this.type = type;
        }
    }

    /**
     * 方法索引
     */
    private static class MethodNameReturnIndex extends StringCaseReturnIndex {
        private final List<ClassMethodInfo> methods;

        private MethodNameReturnIndex(List<ClassMethodInfo> methods) {
            super(methods.get(0).name, methods.get(0).memberIndex);
            this.methods = methods;
        }
    }

    /**
     * 方法
     */
    private static class ClassMethodInfo extends MemberInfo {
        private final int invokeOpcode;
        private final Method method;
        private final int parameterCount;
        private final List<ParameterInfo> parameters;

        private ClassMethodInfo(Method method, int index) {
            super(method.getName(), index, Type.getMethodDescriptor(method));
            invokeOpcode = method.getDeclaringClass().isInterface() ? INVOKEINTERFACE : Modifier.isStatic(method.getModifiers()) ? INVOKESTATIC : INVOKEVIRTUAL;
            this.method = method;
            parameterCount = method.getParameterCount();
            parameters = Collections.unmodifiableList(Arrays.stream(method.getParameterTypes()).map(ParameterInfo::new).collect(toList()));
        }
    }

    private static abstract class MemberInfo {
        protected final String descriptor;
        protected final int memberIndex;
        protected final String name;

        private MemberInfo(String name, int memberIndex, String descriptor) {
            this.descriptor = descriptor;
            this.name = name;
            this.memberIndex = memberIndex;
        }
    }

    /**
     * 字段
     */
    private static class ParameterInfo implements Castable {
        private final String descriptor;
        private final String internalName;
        private final Class<?> type;

        @Override
        public String getDescriptor() {
            return descriptor;
        }

        @Override
        public String getInternalName() {
            return internalName;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Class getType() {
            return type;
        }

        private ParameterInfo(Class<?> type) {
            descriptor = Type.getDescriptor(type);
            internalName = Type.getInternalName(type);
            this.type = type;
        }
    }

    /**
     *
     */
    private static interface Castable {
        String getDescriptor();

        String getInternalName();

        @SuppressWarnings("rawtypes")
        Class getType();
    }

    /**
     * 创建方法名
     *
     * @param clazz 类
     * @return 方法名
     */
    public static String getClassNameOfClassAccessFor(Class<?> clazz) {
        return clazz.getName() + "$" + clazz.getSimpleName() + "ClassAccess";
    }
}
