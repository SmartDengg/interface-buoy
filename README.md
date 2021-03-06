# interface-buoy

[![](https://jitpack.io/v/SmartDengg/interface-buoy.svg)](https://jitpack.io/#SmartDengg/interface-buoy)

Kotlin and groovy provide very convenient operators, the operator `?.` is only invoked when the reference object is not empty, but unfortunately, this utility operator is not provided in java.

I wrote a [blog](https://www.jianshu.com/p/151023036adf) to analyze the principles of the `?.` operators in kotlin and groovy. The kotlinc and groovyc compilers compile the `?.` to the same effect as `if (callback != null) {} ` bytecode.

**Interface-buoy** is an Android gradle plugin that uses ASM to dynamically modify java bytecode during the build process. Combine dynamic proxies and reflections to achieve the same effect as the '?.' effect.

Note that we only modify the method defined in the java interface, that is which will trigger the [invokeinterface](https://cs.au.dk/~mis/dOvs/jvmspec/ref--32.html) instruction at runtime.


Blog link : [在Java 中安全使用接口引用](https://www.jianshu.com/p/151023036adf)

## Usage

#### Command Line

```bash
$ git clone git@github.com:SmartDengg/interface-buoy.git
$ cd interface-buoy/
$ ./gradlew build
```


#### AGP

Add Android gradle plugin dependency to your any application or library module's build.gradle.


```groovy
buildscript {
    repositories {
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath 'com.github.SmartDengg.interface-buoy:interface-buoy-gradle:1.3.0'
    }
}

apply plugin: 'com.android.application' // or apply plugin: 'com.android.library'
apply plugin: 'com.smartdengg.interfacebuoy' // or apply plugin: 'interfacebuoy'

dependencies {
  implementation fileTree(include: ['*.jar'], dir: 'libs')
  implementation 'com.github.SmartDengg.interface-buoy:interface-buoy-runtime:1.3.0'
}

```

#### Optional

In the java code you can use `BuoySettings.loggable = true` to control whether to print the log, the default is not to print the log.


#### Support

If the invoked interface method has a return type but not a `void` type, its bytecode will not be modified. Currently only the `void` return type is supported.

- [x] void return type
- [ ] other return type


## Principle

Before:



```

invokeinterface #3,  2            // InterfaceMethod Callback.onProgress:(I)V


```


After:


```

invokestatic  #23                 // Method buoy$onProgress:(LCallback;I)V


static void buoy$onProgress(JavaSample$Callback, int);
Code:
   0: aload_0
   1: ldc           #25                 // String Callback
   3: ldc           #27                 // String Callback.onProgress:(int)void
   5: invokestatic  #33                 // Method com/smartdengg/interfacebuoy/compiler/InterfaceBuoy.proxy:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;
   8: iload_1
   9: invokeinterface #37,  2           // InterfaceMethod Callback.onProgress:(I)V
  14: return


```

1. Replaces `invokeinterface ` with `invokestatic` at compile time.

2. Generated `Buoy$onProgress` method at compile time to prevent calling interface methods on an null object.


## About me

email : hi4joker@gmail.com

blog  : [小鄧子](https://www.jianshu.com/u/df40282480b4)

weibo : [-小鄧子-](https://weibo.com/5367097592/profile?topnav=1&wvr=6)


## License

See the [LICENSE](LICENSE) file for license rights and limitations (MIT).
