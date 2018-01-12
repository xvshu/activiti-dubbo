package com.xvshu.test.clas;

/**
 * Created by xvshu on 2017/11/13.
 */
public class TestClasss {
    public static void main(String[] args) {
        TestClasss testClasss = new TestClasss();
        testClasss.print();
    }

    public void print(){
        Math math1 = new Math();
        math1.print();
    }

    public class Math{
        public void print(){
            System.out.println("====math:print()");
        }
    }
}
