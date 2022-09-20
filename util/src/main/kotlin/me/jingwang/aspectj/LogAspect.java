package me.jingwang.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : jingwang
 * @create 2022/9/20 5:24 PM
 */
@Aspect
public class LogAspect {
    @Pointcut("@annotation(me.jingwang.aspectj.LogAnno)")
    public void pointCut1() {}

    @Around("pointCut1()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("round");
        proceedingJoinPoint.proceed();
        return "aaa";
    }

    @Before("pointCut1()")
    public void before() {
        System.out.println("before");
    }


}
