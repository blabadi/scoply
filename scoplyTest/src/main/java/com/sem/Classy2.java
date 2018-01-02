package com.sem;

import com.sem.internal.Classy1;
import io.github.bashar.scoply.processor.Scoped;
//import com.sem.internal.ClassyStar;

@Scoped(pkg = "com.sem")
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
