package com.hj.aop;


import com.hj.AddWaterMarkUtil;
import com.hj.core.AddWaterMark;
import com.hj.core.enums.WaveMarkMode;
import org.aopalliance.aop.Advice;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Method;

/**
 * 文件转换切面
 */
public class AddWaterMarkAdvisor implements PointcutAdvisor, ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(AddWaterMarkAdvisor.class);

    private final String TOMCAT_TEMP_FILE = "part.fileItem.dfos.outputFile";

    /**
     * 本地缓存路径
     */
    private final String TEMP_PATH = System.getProperty("user.dir");//AddWaterMarkAdvisor.class.getClassLoader().getResource("").getPath();

    /*  完成 spel 解析 与 MultiFile 文件替换  */

    private AddWaterMark addWaterMark;



    public AddWaterMarkAdvisor(AddWaterMark addWaterMark) {
        this.addWaterMark = addWaterMark;
    }

    /**
     * 获取参数容器
     *
     * @param arguments       方法的参数列表
     * @param signatureMethod 被执行的方法体
     * @return 装载参数的容器
     */
    private EvaluationContext getContext(Object[] arguments, Method signatureMethod) {

        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(signatureMethod);
        if (parameterNames == null) {
            throw new RuntimeException("参数列表不能为null");
        }

        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < arguments.length; i++) {
            context.setVariable(parameterNames[i], arguments[i]);
        }
        //  设置 spring  上下文
        context.setBeanResolver(new BeanFactoryResolver(this.applicationContext));
        return context;
    }

    /**
     * 获取spel 定义的参数值
     *
     * @param context 参数容器
     * @param key     key
     * @param clazz   需要返回的类型
     * @param <T>     返回泛型
     * @return 参数值
     */
    private <T> T getValue(EvaluationContext context, String key, Class<T> clazz) {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(key);
        return expression.getValue(context, clazz);
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public Advice getAdvice() {
        return new MethodBeforeAdvice() {

            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
                //解析注解属性
                AddWaterMarkAn annotation = method.getAnnotation(AddWaterMarkAn.class);
                String content = annotation.content();
                String whetherAdd = annotation.whetherAdd();
                //得到spel上下文
                EvaluationContext context = getContext(args, method);
                //是否要加水印
                Boolean whetherAddValue = getValue(context, whetherAdd, Boolean.class);

                WaveMarkMode mode = annotation.mode();
                String picPath = annotation.picPath();


                //需要则完成替换逻辑 否则跳过
                if (whetherAddValue) {
                    //内容
                    String contentValue = getValue(context, content, String.class);
                    for (Object arg : args) {
                        if (arg instanceof MultipartFile) {
                            MultipartFile multipartFile = (MultipartFile) arg;

                            //这里遇到不支持的类型直接返回
                            String originalFilename = multipartFile.getOriginalFilename();
                            String suffix = originalFilename.split("\\.")[1];
                            boolean containsKey = AddWaterMarkUtil.getAddWaterMarkMap().containsKey(suffix);
                            if (!containsKey)
                                return;

                            //这里实现文件替换操作
                            // 实现动态替换 File
                            MetaObject metaObject = SystemMetaObject.forObject(multipartFile);
                            //删除原来的缓存文件
                            File file = (File) metaObject.getValue(TOMCAT_TEMP_FILE);
                            //todo 属性设置
                            addWaterMark.setWaveMarkMode(mode);
                            addWaterMark.setPicPath(picPath);
                            addWaterMark.transfer(file.getPath(), TEMP_PATH + multipartFile.getOriginalFilename(), contentValue);
                            file.delete();
                            metaObject.setValue(TOMCAT_TEMP_FILE, new File(TEMP_PATH + multipartFile.getOriginalFilename()));
                        }
                    }

                }
            }
        };
    }

    @Override
    public boolean isPerInstance() {
        return true;
    }

    @Override
    public Pointcut getPointcut() {
        /**
         * 简单的Pointcut定义，匹配所有类的find方法调用。
         */
        return new Pointcut() {

            @Override
            public ClassFilter getClassFilter() {
                return ClassFilter.TRUE;
            }

            @Override
            public MethodMatcher getMethodMatcher() {
                return new MethodMatcher() {

                    @Override
                    public boolean matches(Method method, Class<?> targetClass) {
                        return method.getAnnotation(AddWaterMarkAn.class) != null;
                    }

                    @Override
                    public boolean isRuntime() {
                        return false;
                    }

                    @Override
                    public boolean matches(Method method, Class<?> targetClass, Object[] args) {
                        return method.getAnnotation(AddWaterMarkAn.class) != null;
                    }

                };
            }

        };
    }

}