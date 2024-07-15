package com.zuu.chatroom.common.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/15 19:04
 */
public class SpElUtils {
    /**
     *  SpEL 表达式解析器，用于解析 SpEL 表达式
     */
    private static final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 创建一个参数名发现器，用于获取方法的参数名。
     * 由spring提供的，因此初始化获取到的方法参数列表是按照arg1,arg2...进行排列的
     * el表达式根据参数名获取具体的值就需要先获取参数名
     */
    private static final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    public static String parseEl(Method method, Object[] args, String spEl){
        // 获取方法的参数名数组，如果获取不到则返回空数组
        String[] params = Optional.ofNullable(parameterNameDiscoverer.getParameterNames(method)).orElse(new String[]{});
        // 创建 SpEL 解析上下文对象
        EvaluationContext context = new StandardEvaluationContext();
        // 将方法的参数名和对应的参数值放入上下文中
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        // 解析 SpEL 表达式
        Expression expression = parser.parseExpression(spEl);
        // 获取解析结果并转换为字符串
        return expression.getValue(context, String.class);
    }

    /**
     * 生成方法的唯一标识符字符串。
     */
    public static String getMethodKey(Method method) {
        //method.getDeclaringClass(): 获取方法所属的类。
        //method.getName(): 获取方法名。
        return method.getDeclaringClass() + "#" + method.getName();
    }
}
