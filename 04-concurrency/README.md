# 04 · 并发基础

**建议：3 周** · 先完成 [`03-functional`](../03-functional/README.md)。

## 目标

能写对共享状态的同步，并理解线程、锁、happens-before 的最低必要集。

## 要学什么

- `Thread`、`Runnable`、`Callable`、`ExecutorService`
- `synchronized`、`volatile`、原子类；死锁与可见性
- `ConcurrentHashMap`、`BlockingQueue`、`CountDownLatch`
- `CompletableFuture` 组合异步
- Java 21+ 虚拟线程：适用场景，以及不要在虚拟线程里做会 pin 住载体线程的阻塞 JNI/锁

## 练习题（写入本目录 `src/`）

用固定线程池处理一组任务；再改成虚拟线程对比。必须能复现并修一个竞态。

## 过关标准

能画出一段代码的共享变量，并说明为什么加锁或改用不可变能修好。

## 这一阶段先别碰

分布式锁、Kafka、微服务编排。
