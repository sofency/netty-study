### Netty 重新学习

NIO基本介绍，就是一个主线程通过Selector选择器监听其他多个工作线程的read/accept/write等操作。
netty 类似多个NIO的模型，其中多个主线程就是所谓的WorkerGroup