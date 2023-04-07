```java
// 类声明
[public] [abstract|final] class className [extends superClassName] [implements InterfaceNameList] {
    // 成员变量声明（可为多个）
    [public|protected|private] [static] [final] [transient] [volatile] type variableName;

    // 方法定义及实现（可为多个）
    [public|protected|private] [static] [final|abstract] [native] [synchronized] returnType mathodName([paramList]) [throws exceptionList] {
        // ...
    }
}
```
- 可继承性：public, protected, private
- 可访问性：static
- 可更改性：final
- 是否参与序列化：transient
- 线程同步性：volatile
