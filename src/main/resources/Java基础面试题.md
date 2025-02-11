

## ThreadLocal和InheritableThreadLocal的区别
ThreadLocal和InheritableThreadLocal都可以用通过线程来共享数据，区别在于当前线程在InheritableThreadLocal中设置的值可以被子线程继承，并且是复制（也就是子线程和父线程一开始InheritableThreadLocal中的值时一致的，但是后续的修改互不影响），而当前线程在ThreadLocal中设置的值不会被子线程所继承。

## 如何理解Java中的装箱与拆箱
装箱，就是int类型包装为Integer类型，拆箱，就是反过来，因为Java中支持8种基本数据类型，每种基本类型都有对应的包装类型，装箱会调用valueOf()方法，传入基本类型，返回包装类型，这个方法中通常会有一个缓存，比如用来缓存数字1对应的Integer对象，拆箱会调用intValue()方法，返回基本类型，不要过多的进行装箱和拆箱，毕竟是在调方法，是消耗性能的。

## Java中为什么要有基础类型
Java是面向对象的，一切都是对象，但是像字符、数字这些常用类型，每次用的时候也去new对象，就会比较费性能和内存了，所以Java设计了8种基础类型，在使用基础类型时，对应的内存空间是直接分配在栈上的，而不是分配在堆上，这样性能也更好。

## 说说进程和线程的区别
一个操作系统上会运行很多个程序，这些程序都有自己的代码,以及都要用内存来存代码，和代码运行过程中产生的数据，进程就是用来隔离各个程序的内存空间的，使得程序之间互不干扰，还是这多个程序，为了让它们能同时运行，CPU就需要先执行这个程序的几条指令，然后切换到另外一个程序去执行，然后再切回来，就像同时在运行多条指令流水线，而这个流水线就是线程，是CPU调度的最小单位

## 为什么Java不支持多继承？
首先，思考这么一种场景，假如现在A类继承了B类和C类，并且B类和C类中，都存在test()方法，那么当A类对象调用test()方法时，该调用B类的test()呢？还是C类的test()呢？是没有答案的，所以Java中不允许多继承。

那么Java中为什么可以支持接口的多继承呢？

## String、StringBuffer、StringBuilder的区别

1. String是不可变的，如果尝试去修改，会新生成一个字符串对象，StringBuffer和StringBuilder是可变的
2. StringBuffer是线程安全的，StringBuilder是线程不安全的，所以在单线程环境下StringBuilder效率会更高

## ArrayList和LinkedList有哪些区别
1. 首先，他们的底层数据结构不同，ArrayList底层是基于数组实现的，LinkedList底层是基于链表实现的
2. 由于底层数据结构不同，他们所适用的场景也不同，ArrayList更适合随机查找，LinkedList更适合删除和添加，查询、添加、删除的时间复杂度不同
3. 另外ArrayList和LinkedList都实现了List接口，但是LinkedList还额外实现了Deque接口，所以LinkedList还可以当做队列来使用



## CopyOnWriteArrayList的底层原理是怎样的

1. 首先CopyOnWriteArrayList内部也是用数组来实现的，在向CopyOnWriteArrayList添加元素时，会复制一个新的数组，写操作在新数组上进行，读操作在原数组上进行
2. 并且，写操作会加锁，防止出现并发写入丢失数据的问题
3. 写操作结束之后会把原数组指向新数组
4. CopyOnWriteArrayList允许在写操作时来读取数据，大大提高了读的性能，因此适合读多写少的应用场景，但是CopyOnWriteArrayList会比较占内存，同时可能读到的数据不是实时最新的数据，所以不适合实时性要求很高的场景



## HashMap在什么条件下会扩容？
一般情况下是在 HashMap 中元素个数超过 12 个之后就会扩容，这里12 是 16*0.75 的结果，16 是默认数组大小，0.75 是默认加载因子，加载因子就是用来控制扩容的条件的，扩容是指数组的扩容，在 HashMap 中是双倍扩容，比如原本数组大小是 16，扩容会创建一个新数组，大小为 32，然后再把老数组上的元素，链表，红黑树转移到新数组上去，另外还有一种情况会进行扩容，就是当某个链表的长度超过8之后，如果此时属于长度小于64，就会进行扩容，大于等于64才会把链表改成红黑树，因为通过扩容，能把一个链表拆分成为两个短链表，也能优化链表的查询效率。

## ConcurrentHashMap的扩容机制
1.7版本

1. 1.7版本的ConcurrentHashMap是基于Segment分段实现的
2. 每个Segment相对于一个小型的HashMap
3. 每个Segment内部会进行扩容，和HashMap的扩容逻辑类似
4. 先生成新的数组，然后转移元素到新数组中
5. 扩容的判断也是每个Segment内部单独判断的，判断是否超过阈值

1.8版本

1. 1.8版本的ConcurrentHashMap不再基于Segment实现
2. 当某个线程进行put时，如果发现ConcurrentHashMap正在进行扩容那么该线程一起进行扩容
3. 如果某个线程put时，发现没有正在进行扩容，则将key-value添加到ConcurrentHashMap中，然后判断是否超过阈值，超过了则进行扩容
4. ConcurrentHashMap是支持多个线程同时扩容的
5. 扩容之前也先生成一个新的数组
6. 在转移元素时，先将原数组分组，将每组分给不同的线程来进行元素的转移，每个线程负责一组或多组的元素转移工作



## ThreadLocal的底层原理

1. ThreadLocal是Java中所提供的线程本地存储机制，可以利用该机制将数据缓存在某个线程内部，该线程可以在任意时刻、任意方法中获取缓存的数据
2. ThreadLocal底层是通过ThreadLocalMap来实现的，每个Thread对象（注意不是ThreadLocal对象）中都存在一个ThreadLocalMap，Map的key为ThreadLocal对象，Map的value为需要缓存的值
3. 如果在线程池中使用ThreadLocal会造成内存泄漏，因为当ThreadLocal对象使用完之后，应该要把设置的key，value，也就是Entry对象进行回收，但线程池中的线程不会回收，而线程对象是通过强引用指向ThreadLocalMap，ThreadLocalMap也是通过强引用指向Entry对象，线程不被回收，Entry对象也就不会被回收，从而出现内存泄漏，解决办法是，在使用了ThreadLocal对象之后，手动调用ThreadLocal的remove方法，手动清楚Entry对象
4. ThreadLocal经典的应用场景就是连接管理（一个线程持有一个连接，该连接对象可以在不同的方法之间进行传递，线程之间不共享同一个连接）

![](https://cdn.nlark.com/yuque/0/2021/png/365147/1622816023795-3ae4931c-bcab-4e8c-a987-4fecf53f9855.png#averageHue=%23ffffff&height=305&id=FGjad&originHeight=443&originWidth=863&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=&width=595)


## 说说ArrayList的工作原理
ArrayList就是对数组做的一层封装，提供了一些对数组更方便的操作，当我们添加一个元素时，就是把元素添加到数组里面，只不过在添加之前要确保数组有足够的容量，不够的情况下会进行扩容，扩容按照1.5倍的方式扩容，这个跟HashMap不一样，HashMap是2倍扩容，不一样的原因主要是HashMap中的为了根据key计算数组下标方便，数组大小必须是2的n次方，也就导致扩容时是2倍扩容，如果也是1.5倍那新数组的大小就不是2的n次方了，ArrayList在添加元素时并不需要计算下标，直接就是从0开始，按顺序添加即可。
