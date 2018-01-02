package com.sem;

//import com.some.test.Classy1;
//import com.some.test.ClassyStar;

import io.github.bashar.scoply.processor.Scoped;

@Scoped(pkg="com.sem")
public class Classy2 {
//    Classy1 classy1 = new Classy1();
//
//    public void test() {
//        //wont be detected due to default scanner limitations
//        ClassyStar cs = new ClassyStar();
//    }

    public void test2() {
        int x = 1;
        return;
    }
}
