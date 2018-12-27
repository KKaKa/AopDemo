[TOC]

# AopDemo
Aop使用的Demo

## 描述
项目中使用AOP技术实现跳转界面前判断登陆状态，登陆状态在项目中以一个简单的布尔值来判断，不做具体逻辑。当为登陆状态则跳转到下个界面，反之，跳转到登陆界面。

## 添加AspectJ支持
build.gradle
```
dependencies {
		....
        classpath 'org.aspectj:aspectjtools:1.8.9'
        classpath 'org.aspectj:aspectjweaver:1.8.9'
    }
```

build.gradle(app)
```
implementation 'org.aspectj:aspectjrt:1.8.9'
```
添加gradle脚本( 在build.gradle(app)中 )
```
final def log = project.logger
final def variants = project.android.applicationVariants
variants.all { variant ->
    if (!variant.buildType.isDebuggable()) {
        log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
        return;
    }

    JavaCompile javaCompile = variant.javaCompile
    javaCompile.doLast {
        String[] args = ["-showWeaveInfo",
                         "-1.8",
                         "-inpath", javaCompile.destinationDir.toString(),
                         "-aspectpath", javaCompile.classpath.asPath,
                         "-d", javaCompile.destinationDir.toString(),
                         "-classpath", javaCompile.classpath.asPath,
                         "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
        log.debug "ajc args: " + Arrays.toString(args)

        MessageHandler handler = new MessageHandler(true);
        new Main().run(args, handler);
        for (IMessage message : handler.getMessages(null, true)) {
            switch (message.getKind()) {
                case IMessage.ABORT:
                case IMessage.ERROR:
                case IMessage.FAIL:
                    log.error message.message, message.thrown
                    break;
                case IMessage.WARNING:
                    log.warn message.message, message.thrown
                    break;
                case IMessage.INFO:
                    log.info message.message, message.thrown
                    break;
                case IMessage.DEBUG:
                    log.debug message.message, message.thrown
                    break;
            }
        }
    }
}
```

## 自定义切点
```
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLogin {
    boolean isLogin();
}
```

## 切面
```
@Aspect
public class CheckLoginAspectJ {
    private static final String TAG = "CheckLoginAspectJ";

    @Pointcut("execution(@com.laizexin.sdj.aopdemo.aspectj.CheckLogin * *(..))")
    public void pointCut(){

    }

    @Before("pointCut()")
    public void before(JoinPoint point){
        Log.i(TAG,"CheckLoginAspectJ.before");
    }

    @Around("pointCut()")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable{
        ...
        return joinPoint.proceed();
    }

    @After("pointCut()")
    public void after(JoinPoint point){
        Log.i(TAG,"CheckLoginAspectJ.after");
    }

    @AfterThrowing(value = "pointcut()", throwing = "ex")
    public void afterThrowing(Throwable ex) {
        Log.i(TAG,"CheckLoginAspectJ.afterThrowing.ex = " + ex.getMessage());
    }
}

```

## 获取切点中自定义的值
```
MethodSignature signature = (MethodSignature) joinPoint.getSignature();
Method method = signature.getMethod();
CheckLogin checkLogin = method.getAnnotation(CheckLogin.class);
boolean isLogin = checkLogin.isLogin();
```