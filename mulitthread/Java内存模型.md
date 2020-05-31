[Java Memory Model FAQ](http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html)

Java内存模型描述了在多线程代码中哪些行为是合法的，以及线程如何通过内存进行交互。它描述了“程序中的变量“ 和 ”从内存或者寄存器获取或存储它们的底层细节”之间的关系。Java内存模型通过使用各种各样的硬件和编译器的优化来正确实现以上事情。

Java内存模型是个很复杂的规范，可以从不同的视角来解读，站在程序员的视角，本质上可以理解为：Java内存模型规范了JVM如何提供按需禁用缓存和优化的方法。
> 具体来讲，这些方法包括volatile、synchronized和final 3个关键字，以及六项Happends-Before规则

###### volatile
volatile关键字并不是Java语言的特产，在C语言里面也有，最原始的意义就是禁用CPU缓存

例如，当我们声明一个volatile变量volatile int x = 0时，它表达的意思是：告诉编译器，对这个变量的读写，不能使用CPU缓存，必须从内存中读取或写入

###### final
final修饰变量时，初衷是告诉编译器：这个变量变量生成不变，可以可劲优化

在Java 1.5之后内存模型对final类型变量的重排进行了约束。现在只要我们提供正常的构造函数没有“逸出”，就不会出问题

```
//在构造函数里面将this赋值给了全局变量global.obj，这就是“逸出”，线程通过global.obj读取x的时候有有可能读取到0的。我们要避免逸出
final int x;
// 错误的构造函数
public FinalFieldExample() { 
  x = 3;
  y = 4;
  // 此处就是讲 this 逸出，
  global.obj = this;
}

```



###### Happens-Before规则
Happens-Before要表达的意思是：前面一个操作的结果对后续操作是可见的。
比较正式的说法是：Happens-Before约束了编译器的优化行为，虽允许编译器优化，但是要求编译器优化后一定遵守Happens-Before规则

和程序员相关的规则一共有如下六项，都是关于可见性的：

**1. 程序的顺序性规则**

> 这条规则是指在一个线程中，按程序顺序，前面的操作Happend-Before于后续的任意操作，即程序前面对某个变量的修改一定是对后续操作可见的。

**2. volatile变量规则**

> 这条规则是指一个volatile变量的写操作，Happens-Before于后续对这个volatile变量的计操作

**3. 传递性**

> 这条规则是指如果 A Happens-Before B，且B Happens-Before C，那么A Happens-Before C


```
class VolatileExample {
  int x = 0;
  volatile boolean v = false;
  public void writer() {
    x = 42;
    v = true;
  }
  public void reader() {
    if (v == true) {
      // 这里 x 会是多少呢？
    }
  }
}

```
根据规则1可知:x=42 Happens-Before 写变量v=true

根据规则2可知：写变量v=true Happens-Before 读变量V=true

再根据这个传递性规则，可以得出x=42 Happens-Before 读取变量v=true


**4. 管程中的锁的规则**

这条规则是指对一个锁的解锁Happens-Before于后续对这个锁的加锁

> 管程是一种通用的同步原语，在Java中指的synchronized，synchronized是Java时对管程的实现

管程中的锁在Java里面是隐式实现的，例如在如下代码，在进入同步块之前，会自动加锁，而在代码块执行完成后自动释放锁，加锁和释放锁都是编译器帮我们实现的

```
synchronized (this) { // 此处自动加锁
  // x 是共享变量, 初始值 =10
  if (this.x < 12) {
    this.x = 12; 
  }  
} // 此处自动解锁

```
结合规则4，可以这样理解：假设x的初始值是10，线程A执行完代码块后x的值变成12（执行完后自动释放锁），线程B进入代码块时，能够看到线程A对x的写操作，也就是线程B能够看到x==12

**5. 线程start()的规则**

它是主线程A启动子线程B后，子线程B能够看到主线程在启动子线程B前的操作.即如果线程A调用线程B的start()方法，那么该start()操作Happens-Before于线程B中的任意操作

```
Thread B = new Thread(()->{
  // 主线程调用 B.start() 之前
  // 所有对共享变量的修改，此处皆可见
  // 此例中，var==77
});
// 此处对共享变量 var 修改
var = 77;
// 主线程启动子线程
B.start();

```

**6. 线程join()的规则**

这条是关于线程等待的，它是指主线程A等待子线程B完成(主线程A通过调用子线程B的join()方法来实现)，当子线程B完成后(主线程A中的join()方法返回)，主线程能看到子线程的操作，所谓的看到，指的是对共享变量的操作

换句话说，如果在线程A中，调用线程B的join()方法并成功返回，那么线程B中的任意操作Happens-Before于该join()操作的返回


```
Thread B = new Thread(()->{
  // 此处对共享变量 var 修改
  var = 66;
});
// 例如此处对共享变量修改，
// 则这个修改结果对线程 B 可见
// 主线程启动子线程
B.start();
B.join()
// 子线程所有对共享变量的修改
// 在主线程调用 B.join() 之后皆可见
// 此例中，var==66

```

总结，在Java语言里面，Happens-Before的语义本质上是一种可见性，A Happens-Before意味着A事件对B事件是可见的，无论A事件和B事件是否发生在同一个线程里









