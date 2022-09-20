package me.jingwang.aspectj;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : jingwang
 * @create 2022/9/20 5:50 PM
 */
public class Student {
    @LogAnno
    public void read() {
        System.out.println("student read...");
    }
}
