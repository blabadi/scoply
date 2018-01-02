package io.github.bashar.scoply.processor;

import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

//http://www.logicbig.com/tutorials/core-java-tutorial/java-se-annotation-processing-api/annotation-processor-validation/
@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedAnnotationTypes({Scoped.class.getName()})
public class ScopeProcessor extends AbstractProcessor {

    private Trees trees;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        trees = Trees.instance(processingEnv);
    }

    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        System.out.println(roundEnv.getRootElements());
        for (TypeElement typeElement : annotations) {
            Map<String, Element> annotatedClasses = roundEnv.getElementsAnnotatedWith(typeElement).stream().collect(Collectors.toMap(e-> e.toString(), Function.identity()));
            System.out.println(" ===== annotated classes : " + annotatedClasses);
            for (Element e : roundEnv.getRootElements()) {
                //1- semantics scanner
                UsageScanner scanner = new UsageScanner();
                scanner.scan(e, null);
                System.out.println(" ===== scanning : " + e.toString());
                Set<String> usedTypes = scanner.getUsedTypes();

                //2- import statements scanner
                ImportScanner is  = new ImportScanner();
                is.scan(this.trees.getPath(e).getCompilationUnit(), null) ;

                //merge outputs
                usedTypes.addAll(is.imports);
                
                System.out.println(" ===== imported classes : " + usedTypes);
                for (String className : usedTypes) {
                    if (annotatedClasses.containsKey(className)) {
                        System.out.println(" ===== class : " + className + " annotated with Scoped");
                        Element classEle = annotatedClasses.get(className);
                        Scoped annotation = classEle.getAnnotation(Scoped.class);
                        String pkg = annotation.pkg();
                        //if public package continue as there is no scope comparision needed
                        if (pkg.equalsIgnoreCase("*")) {
                            continue;
                        }
                        System.out.println(" ===== scope : " + pkg);
                        String importedInPkg = e.toString().substring(0, e.toString().indexOf(e.getSimpleName().toString())-1);
                        System.out.println(" ===== importing class pkg : " + importedInPkg);
                        if (!importedInPkg.contains(pkg)) {
                            messager.printMessage(Diagnostic.Kind.ERROR, e + " has illegal import for : " + className.toString());
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList(Scoped.class.getName()));
    }
}


//https://stackoverflow.com/questions/6373145/accessing-source-code-from-java-annotation-processor
class MethodScanner extends TreePathScanner<List<MethodTree>, Trees> {
    private List<MethodTree> methodTrees = new ArrayList<>();

    public MethodTree scan(ExecutableElement methodElement, Trees trees) {
        assert methodElement.getKind() == ElementKind.METHOD;
        List<MethodTree> methodTrees = this.scan(trees.getPath(methodElement), trees);
        assert methodTrees.size() == 1;
        return methodTrees.get(0);
    }

    @Override
    public List<MethodTree> scan(TreePath treePath, Trees trees) {
        super.scan(treePath, trees);
        return this.methodTrees;
    }

    @Override
    public List<MethodTree> visitMethod(MethodTree methodTree, Trees trees) {
        this.methodTrees.add(methodTree);
        return super.visitMethod(methodTree, trees);
    }
    
}

//scans the import statements to find types used by a class
class ImportScanner extends TreePathScanner<List<ImportTree>, Void> {
    public  Set<String> imports = new HashSet<>();
    
    @Override
    public List<ImportTree> visitImport(ImportTree node, Void v) {
        imports.add(node.getQualifiedIdentifier().toString());
        return super.visitImport(node, null);
    }
}
//Test here
class Driver {
    public static void main(String[] args) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        try {
            StandardJavaFileManager fileManager = compiler
                    .getStandardFileManager(null, null, null);

            List<File> files = new LinkedList<File>();
            files.add(new File(
                    "/Users/basharallabadi/scoply/scoplyTest/src/main/java/com/some/test/Classy1.java"));

            files.add(new File(
                    "/Users/basharallabadi/scoply/scoplyTest/src/main/java/com/some/test/ClassyStar.java"));

            files.add(
                    new File(
                            "/Users/basharallabadi/scoply/scoplyTest/src/main/java/com/sem/Classy2.java"));

            files.add(
                    new File(
                            "/Users/basharallabadi/scoply/scoplyTest2/src/main/java/com/rum/Classical.java"));

            Iterable<? extends JavaFileObject> compilationUnits1 = fileManager
                    .getJavaFileObjectsFromFiles(files);

            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null,
                    null, null, compilationUnits1);

            LinkedList<ScopeProcessor> processors = new LinkedList<ScopeProcessor>();

            processors.add(new ScopeProcessor());

            task.setProcessors(processors);

            task.call();

            try {
                fileManager.close();
            } catch (IOException e) {
            }
        } catch (Throwable t) {
            System.out.println(t.getLocalizedMessage());
        }
    }
}