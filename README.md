### Netty 重新学习

NIO基本介绍，就是一个主线程通过Selector选择器监听其他多个工作线程的read/accept/write等操作。
netty 类似多个NIO的模型，其中多个主线程就是所谓的WorkerGroup

NIO 三大核心 _`Buffer <- Channel <- Selector`_     
Selector管理多个Channel， Channel 与对应的Buffer一一对应进行管理
数据总是从通道读到缓冲区中，或者从缓冲区写入到通道中，Selector用于监听多个通道的事件，这样单个线程就可以监听到多个客户端通道

####Selector 会根据不同的事件在各个通道上切换  
open() 得到一个选择器  
select(long timeout) 监控所有注册的通道, 当其中有IO操作可以进行时，将对应的SelectionKey加到内部集合中并返回，参数可以用来设置超时事件  
selectedKeys(); //从内部集合中得到所有的SelectionKey

在对于网络IO的过程中，当客户端端连接时，会通过ServerSocketChannel得到SocketChannel,将socketChannel注册到Selector上，register(Selector sel, int pos)一个selector可以注册多个SocketChannel
注册后会返回一个SelectionKey,会和该Selector关联。Selector进行监听select方法，返回有事件发生的通道个数。

####Buffer
Buffer就是一个内存块，底层是一个数组  
数据的读取和写入是通过Buffer，但是需要flip()方法进行读写切换  
mark 是标记 position 下一个要读或写的位置 limit 表示缓冲区的当前终点，不能对缓冲区超过极限的位置进行操作 capacity 表示缓冲区最大容纳数据量  
Buffer在进行put和get时,要按照对应的数据类型和顺序进行操作  
Buffer可以获取只读buffer,循环读取时 可以通过 `buffer.hasRemaining()` 来判断是否还有数据未读取完毕  

####Channel
通道可以同时进行读写，而流只能读或者只能写
通道可以实现异步读写数据
通道可以从缓冲读数据，也可以写数据到缓冲，需要注册到Selector#open()中

`FileChannel` 主要对本地文件进行IO操作  
`read(ByteBuffer dist)` 从通道读取数据并放到缓冲区中
`write(ByteBuffer src)` 把缓冲区的数据写到通道中
`transferFrom(ReadableByteChannel src, long position, long count)` 从目标通道中复制数据到当前通道   
`transferTo(long position, long count, WritableByteChannel target)` 从目标通道中复制数据到当前通道，底层时零拷贝


MapperByteBuffer 可以让文件直接在内存中修改，操作系统不需要拷贝一次  
在利用ByteBuffer数据的形式进行接收数据的时候，channel会按照顺序的方式填充ByteBuffer数组


SelectionKey作用 
绑定了对应的channel(channel())和Buffer(attachment())，同样我们可以查看该key绑定的channel的状态，是否是可读可写和accept的状态


#### 零拷贝基本介绍
传统IO 数据从硬盘经过DMA拷贝到内核Buffer，然后又从内核Buffer中CPU拷贝到用户空间的user buffer，再从用户空间通过CPU拷贝到Socket buffer，最后经过DMA拷贝到协议栈，mmap需要4次上下文切换，3次数据拷贝

mmap
通过内存映射，将文件映射到内核缓冲区，同时用户空间可以共享内核空间的数据，减少了内核空间到用户空间的CPU拷贝
这样在进行网络传输时，就可以减少内核空间到用户空间的拷贝次数
![图片](./1675336554187.jpg)

sendFile需要3次上下文切换，最少3次数据拷贝，主要利用DMA方式，减少CPU拷贝
sendFile函数优化，数据根本不经过用户态，直接从内核缓冲区冲入SocketBuffer，同时由于和用户态完全无关，就减少了一次上下文切换  
更少的上下文切换，更少的CPU缓存伪共享以及无CPU校验和计算
![](./1675337063648.jpg)

