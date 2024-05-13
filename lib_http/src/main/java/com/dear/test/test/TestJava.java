package com.dear.test.test;

public class TestJava {

    public TestJava getInstance() {
        return new InnerClass().instance;
    }

    private  class InnerClass {
        private  TestJava instance = new TestJava();
    }
}



