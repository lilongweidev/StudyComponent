package com.llw.annotation_compiler;

import com.google.auto.service.AutoService;
import com.llw.annotation.BindPath;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class AnnotationCompiler extends AbstractProcessor {

    // 定义用来生成APT目录下面的文件的对象（例如：ActivityRouterUtil1668396026324）
    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    /**
     * 支持的注解类型
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindPath.class.getCanonicalName());
        return types;
    }

    /**
     * 支持版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    /**
     * 通过注解处理器处理注解，生成代码到build文件夹中
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //获取注解 例如 ：@BindPath("main/MainActivity")
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindPath.class);
        Map<String, String> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            TypeElement typeElement = (TypeElement) element;
            //key为注解的Activity 例如：MainActivity
            String key = typeElement.getQualifiedName().toString() + ".class";
            //value为注解方法中的值 例如："main/MainActivity"
            String value = typeElement.getAnnotation(BindPath.class).value();
            map.put(key, value);
        }
        makefile(map);
        return false;
    }

    private void makefile(Map<String, String> map) {
        if (map.size() > 0) {
            //定义编译时类生成时的包名
            String packageName = "com.llw.util";
            //定义处理器的包名
            String routerPackageName = "com.llw.basic.router";
            //获取接口名IRouter
            ClassName interfaceName = ClassName.get(routerPackageName, "IRouter");
            //获取类名 ARouter
            ClassName className = ClassName.get(routerPackageName, "ARouter");
            //创建类构造器，例如ActivityRouterUtil  加上时间戳是为了防止生成的编译时类名重复报错
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder("ActivityRouterUtil" + System.currentTimeMillis())
                    //添加修饰符 public
                    .addModifiers(Modifier.PUBLIC)
                    //添加实现接口，例如 implements IArouter
                    .addSuperinterface(interfaceName);
            //创建方法构造器 方法名putActivity()
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("putActivity")
                    //添加注解
                    .addAnnotation(Override.class)
                    //添加修饰符
                    .addModifiers(Modifier.PUBLIC);
            //这里遍历是为了给每一个添加了注解进行代码生成
            for (String activityName : map.keySet()) {
                String value = map.get(activityName);
                //例如 com.llw.arouter.ARouter.getInstance().addActivity("login/LoginActivity",com.llw.login.LoginActivity.class);
                methodBuilder.addStatement("$L.getInstance().addActivity($S, $L)", className, value, activityName);
            }
            //在类构造器中添加方法
            classBuilder.addMethod(methodBuilder.build());
            try {
                //最后写入文件
                JavaFile.builder(packageName, classBuilder.build())
                        .build()
                        .writeTo(filer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
