//package me.jingwang.aspectj
//
//import org.aspectj.lang.ProceedingJoinPoint
//import org.aspectj.lang.annotation.Around
//import org.aspectj.lang.annotation.Aspect
//import org.aspectj.lang.annotation.Pointcut
//
///**
// * Created by IntelliJ IDEA.
// * @Author : jingwang
// * @create 2022/9/20 7:29 PM
// *
// */
//@Aspect
//class LogAspectKotlin {
//    @Pointcut("@annotation(me.jingwang.aspectj.LogAnno)")
//    fun pointCut1() {
//    }
//
//    @Around("pointCut1")
//    @Throws(Throwable::class)
//    fun around(proceedingJoinPoint: ProceedingJoinPoint): Any {
//        println("round111")
//        proceedingJoinPoint.proceed()
//        return "aaa"
//    }
//}