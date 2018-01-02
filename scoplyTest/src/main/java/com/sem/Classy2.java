package com.sem;

import com.sem.internal.Classy1;
//import com.sem.internal.ClassyStar;

public class Classy2 {
    Classy1 classy1 = new Classy1();

    public void test() {
        //next line wont be inspected due to default scanner limitations
//        ClassyStar cs = new ClassyStar();
    }

    public void test2() {
        int x = 1;
        return;
    }
}
