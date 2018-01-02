package com.rum;

import com.sem.Classy2;
import com.sem.internal.ClassyStar;
import io.github.bashar.scoply.processor.Scoped;

@Scoped
public class Classical {
    Classy2 classy2;
    public ClassyStar classy2(){
        return new ClassyStar();
    }
}
