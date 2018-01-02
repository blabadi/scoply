package io.github.bashar.scoply.processor;

import com.sun.source.tree.*;
import com.sun.source.util.Trees;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementScanner8;
import java.util.HashSet;
import java.util.Set;

//https://stackoverflow.com/questions/14734445/java-annotation-processing-api-accessing-import-statements
public class UsageScanner extends ElementScanner8<Void, Void> {

    private Set<String> types = new HashSet<>();

    public Set<String> getUsedTypes() {
        return types;
    }


    public UsageScanner(){
        
    }
    @Override
    public Void visitType(TypeElement e, Void p) {
//        System.out.println(">>>>" + e.getSuperclass());
         if (e.getSuperclass().toString().startsWith("java")) {
             return super.visitType(e, p);
         }

        for(TypeMirror interfaceType : e.getInterfaces()) {
            types.add(interfaceType.toString());
        }
        types.add(e.getSuperclass().toString());
        return super.visitType(e, p);
    }

    @Override
    public Void visitExecutable(ExecutableElement e, Void p) {
        if (e.getReturnType().toString().startsWith("java")) {
            return super.visitExecutable(e, p);
        }

        if(e.getReturnType().getKind() == TypeKind.DECLARED) {
            types.add(e.getReturnType().toString());
        }

//        System.out.println(" enclosed in " + e + "=" + e.getEnclosedElements());

//        //2-methods scanner
         // doesn't provide fully qualified names.
        // doesn't detected nested blocks (if, while, etc)
//        MethodScanner methodScanner = new MethodScanner();
//        MethodTree methodTree = methodScanner.scan(e, this.trees);
//        methodTree.getBody().getStatements().stream().filter(s-> s instanceof VariableTree).forEach(s-> storeType(s));

        return super.visitExecutable(e, p);
    }

    private void storeType(StatementTree s) {
        types.add(((VariableTree)s).getType().toString());
        if ((((VariableTree)s).getInitializer()) instanceof NewClassTree && ((NewClassTree)((VariableTree)s).getInitializer()).getIdentifier() instanceof IdentifierTree) {
            IdentifierTree identifierTree = IdentifierTree.class.cast(((NewClassTree)((VariableTree)s).getInitializer()).getIdentifier());
            Name n = identifierTree.getName();
            types.add(n.toString());
        }

    }
    @Override
    public Void visitTypeParameter(TypeParameterElement e, Void p) {
        if (e.asType().toString().startsWith("java")) {
            return super.visitTypeParameter(e, p);
        }
        if(e.asType().getKind() == TypeKind.DECLARED) {
            types.add(e.asType().toString());
        }
        return super.visitTypeParameter(e, p);
    }

    @Override
    public Void visitVariable(VariableElement e, Void p) {
//        System.out.println(">>>>" + e.asType());
        if (e.asType().toString().startsWith("java")) {
            return super.visitVariable(e, p);
        }
        if(e.asType().getKind() == TypeKind.DECLARED) {
            types.add(e.asType().toString());
        }
        return super.visitVariable(e, p);
    }
}
