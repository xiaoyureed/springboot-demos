package io.github.xiaoyureed.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * Discards any incoming data.
 *
 * https://netty.io/wiki/user-guide-for-4.x.html
 *
 * 处理器Handler主要分为两种：
 *
 * ChannelInboundHandlerAdapter(入站处理器)、ChannelOutboundHandler(出站处理器)
 *
 * 入站指的是数据从底层java NIO Channel到Netty的Channel。
 *
 * 出站指的是通过Netty的Channel来操作底层的java NIO Channel。
 *
 * ChannelInboundHandlerAdapter处理器常用的事件有：
 *
 * 注册事件 fireChannelRegistered。
 * 连接建立事件 fireChannelActive。
 * 读事件和读完成事件 fireChannelRead、fireChannelReadComplete。
 * 异常通知事件 fireExceptionCaught。
 * 用户自定义事件 fireUserEventTriggered。
 * Channel 可写状态变化事件 fireChannelWritabilityChanged。
 * 连接关闭事件 fireChannelInactive。
 *
 *
 * ChannelOutboundHandler处理器常用的事件有：
 *
 * 端口绑定 bind。
 * 连接服务端 connect。
 * 写事件 write。
 * 刷新时间 flush。
 * 读事件 read。
 * 主动断开连接 disconnect。
 * 关闭 channel 事件 close。
 *
 *
 * 还有一个类似的handler()，主要用于装配parent通道，也就是bossGroup线程。一般情况下，都用不上这个方法。
 *
 * 获取 channel 状态:
 *
 * boolean isOpen(); //如果通道打开，则返回true
 * boolean isRegistered();//如果通道注册到EventLoop，则返回true
 * boolean isActive();//如果通道处于活动状态并且已连接，则返回true
 * boolean isWritable();//当且仅当I/O线程将立即执行请求的写入操作时，返回true。
 *
 * @author xiaoyu
 * @since 1.0
 */
@Data
public class NettyServer {
    private int port;

    public void run() throws Exception {
        // 两个线程组
        //bossGroup 用于监听客户端连接，专门负责与客户端创建连接，并把连接注册到workerGroup的Selector中。
        //workerGroup用于处理每一个连接发生的读写事件。
        //假设想自定义线程数，可以使用有参构造器
        EventLoopGroup bossGroup   = new NioEventLoopGroup(); // (1) accepts an incoming connection.
        EventLoopGroup workerGroup = new NioEventLoopGroup();//handles the traffic of the accepted connection
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)helper class that sets up a server.
            b.group(bossGroup, workerGroup)
                    //instantiate a new server side Channel
                    //to accept incoming connections.
                    .channel(NioServerSocketChannel.class)
                    //The ChannelInitializer is a special handler that is purposed to
                    // help a user configure a new Channel
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // setup the handler for channel
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ////add more handlers to the pipeline
                            // 入站处理, 即这里的 DiscardServerHandler
                            // 出站处理, 即 编码器, 这里没有发送信息, 省略了
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    // specify params for the Channel
                    // ref: https://netty.io/4.1/api/io/netty/channel/ChannelOption.html
                    //
                    // child options:
                    //SO_RCVBUF Socket参数，TCP数据接收缓冲区大小。
                    //TCP_NODELAY TCP参数，立即发送数据，默认值为Ture。
                    //SO_KEEPALIVE Socket参数，连接保活，默认值为False。启用该功能时，TCP会主动探测空闲连接的有效性。
                    //
                    // options:
                    //SO_BACKLOG Socket参数，服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝。默认值，Windows为200，其他为128。
                    //
                    //for the NioServerSocketChannel that accepts incoming connections.也就是boosGroup线程
                    .option(ChannelOption.SO_BACKLOG, 128)// 服务端接受连接的队列长度
                    //for the Channels accepted by the parent ServerChannel,也就是workerGroup线程。
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            // bind() 可被调用多次绑定不同地址
            // netty 中的操作都是异步的, 这里 使用 sync() 进行同步等待
            ChannelFuture f = b.bind(port).sync(); // (7)
            System.out.println(">>> server start ok");

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
            System.out.println(">>> server stop ok");
        } finally {
            // close event loop
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * 测试 telnet localhost 8080
     */
    public static void main(String[] args) throws Exception {
        int port = 8080;
        NettyServer server = new NettyServer();
        server.setPort(port);
        server.run();
        System.out.println(">>> server listen on the port: " + port);
    }
}

class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * ByteBuf 是 netty 的数据容器, 是对 java nio 中 ByteBuffer 的替换
     * ByteBuffer 劣势:
     * - 长度不可调, 一旦buffer分配完成，它的容量不能动态扩展或者收缩，当需要编码的POJO对象大于ByteBuffer容量时，会发生索引越界异常
     * - 只有一个标识位置的指针position。读写是需要手动flip和rewind等，需要十分小心使用这些API，否则很容易导致异常
     *  ByteBuf 's advantages:
     *  - 容量根据需求可扩展( 如同JDK的StringBuilder )
     *  - 读和写使用不同的索引，即有两个索引, no need to use ByteBuffer的flip方法 来从写模式转换到读模式;
     *
     *
     *
     *
     *  bytebuf 特点: (一个由不同的索引分别控制读访问和写访问的字节数组)
     *
     *  - readerIndex ≤ writeIndex (ByteBuf容量 = writerIndex。ByteBuf可读容量 = writerIndex - readerIndex), 尝试读取大于writeIndex位置的数据，将触发IndexOutOfBoundsException
     *  - readXXX(), skipxxx()和writeXXX()方法将会推进其对应的索引readerIndex和writerIndex。自动推进
     *  - getXXX()和setXXX()方法用于访问数据，对writerIndex和readerIndex无影响
     *
     *
     *
     * ByteBuf分配创建:
     * 按需分配(ByteBufAllocator)、Unpooled缓冲区和ByteBufUtil类
     *
     *按需分配(ByteBufAllocator接口):
     * 提供了两个实现：PooledByteBufAllocator (默认)和UnpooledByteBufAllocator。可以通过BootStrap中的Config为每个Channel提供独立的ByteBufAllocator实例
     * 实现了(ByteBuf的)池化
     * 1. ctx.channel().alloc().buffer() ----- 本质就是: ByteBufAllocator.DEFAULT
     * 3. ByteBufAllocator.DEFAULT ----- 有两种类型: UnpooledByteBufAllocator.DEFAULT(非池化)和PooledByteBufAllocator.DEFAULT(池化)。
     * 2. ByteBufAllocator.DEFAULT.buffer() ----- 返回一个基于堆或者直接内存存储的Bytebuf。默认是堆内存
     * 对于Java程序，默认使用PooledByteBufAllocator(池化)。对于安卓，默认使用UnpooledByteBufAllocator(非池化)
     *
     * Unpooled帮助类: (非池化)
     * 当你没有一个ByteBufAllocator引用时，Netty提供了一个可利用的类叫Unpooled，Unpooled提供了静态的帮助方法去创建一个非池的ByteBuf实例
     * Unpooled类使ByteBuf能在在非网络项目中有效使用
     *
     * - buffer()方法，返回一个未池化的基于堆内存存储的ByteBuf
     * - wrappedBuffer() ----- 创建一个视图，返回一个包装了给定数据的ByteBuf。非常实用
     *
     * ByteBufUtil类
     * 1. hexdump() ----- 以十六进制的表示形式打印ByteBuf的内容。非常有价值
     * 2. equals() ----- 判断两个ByteBuf实例的相等性
     *
     *
     *
     *
     *  ByteBuf主要3种使用模式：①Heap Buffers —— 堆缓冲区；②Direct Buffers —— 直接缓冲区；③Composite Buffers —— 复合缓冲区
     *
     *  heap buffers: 将数据存放在JVM的堆空间
     *  - advantage: 由于数据存储在Jvm堆中可以快速创建和快速释放，并且提供了数组直接快速访问的方法
     *  - dis: 每次数据与I/O进行传输时，都需要将数据拷贝到直接缓冲区
     *  这种模式被称为支撑数组 （backing array），它能在没有使用池化的情况下提供快速的分配和释放。非常适合于有遗留的数据需要处理的情况。
     *
             public static void heapBuffer() {
                 // 创建Java堆缓冲区
                 ByteBuf heapBuf = Unpooled.buffer();
                 if (heapBuf.hasArray()) { // 是数组支撑
                     byte[] array = heapBuf.array();
                     int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
                     int length = heapBuf.readableBytes();
                     handleArray(array, offset, length);
                 }
             }

     * direct buffers: 堆外分配的直接内存，不会占用堆的容量
     * - 优点: 使用Socket传递数据时性能很好，避免了数据从Jvm堆内存拷贝到直接缓冲区的过程。提高了性能
     * - 缺点: 相对于堆缓冲区而言，Direct Buffer分配内存空间和释放更为昂贵
     * 对于涉及大量I/O的数据读写(如一些IO通信线程中读写缓冲时)，建议使用Direct Buffer。而对于用于后端的业务消息编解码模块建议使用Heap Buffer
     *
                public static void directBuffer() {
                    ByteBuf directBuf = Unpooled.directBuffer();
                    if (!directBuf.hasArray()) {
                        int length = directBuf.readableBytes();
                        byte[] array = new byte[length];
                        directBuf.getBytes(directBuf.readerIndex(), array);
                        handleArray(array, 0, length);
                    }
                 }

     * Composite Buffer: 是Netty特有的缓冲区。本质上类似于提供一个或多个ByteBuf的组合视图，可以根据需要添加和删除不同类型的ByteBuf。
     * - 不支持访问其支撑数组。因此如果要访问，需要先将内容拷贝到堆内存中，再进行访问

             //组合缓冲区
             CompositeByteBuf compBuf = Unpooled.compositeBuffer();
             //堆缓冲区
             ByteBuf heapBuf = Unpooled.buffer(8);
             //直接缓冲区
             ByteBuf directBuf = Unpooled.directBuffer(16);
             //添加ByteBuf到CompositeByteBuf
             compBuf.addComponents(heapBuf, directBuf);
             //删除第一个ByteBuf
             compBuf.removeComponent(0);
             Iterator<ByteBuf> iter = compBuf.iterator();
             while(iter.hasNext()){
                 System.out.println(iter.next().toString());
             }

             //使用数组访问数据
             if(!compBuf.hasArray()){
                 int len = compBuf.readableBytes();
                 byte[] arr = new byte[len];
                 compBuf.getBytes(0, arr);
             }
    *
     *
     * 读写操作:
     *
     * getInt(int) 从给定索引处读取 int 值
     * .....
     *
     *
     * 判断操作:
     *
     * isReadable() 是否还有字节可读
     * iswritable() 是否还有空间可写
     * readablebytes() 返回所有可悲读取的字节数
     * writablebytes() 返回可写字节数
     * capacity()    返回容量字节数, 此后, 尝试扩容  直到达到 maxcapacity()
     maxcapacity()   可容纳最大字节数    *
     hasArray()     bytebuf 是否由一个字节数组支撑 (堆缓冲区模式)
     array()       若 bytebuf 由一个字节数组支撑, 返回这个数组, 否则异常
     *

     * 字节操作:
     *
     * 随机遍历字节
     public static void byteBufRelativeAccess() {
         ByteBuf buffer = Unpooled.buffer(); //get reference form somewhere
         for (int i = 0; i < buffer.capacity(); i++) {
             byte b = buffer.getByte(i);// 不改变readerIndex值
             System.out.println((char) b);
         }
     }

     * 顺序访问索引
     * ByteBuf被读索引和写索引划分成3个区域：
     * 可丢弃字节区域: [0，readerIndex), discardReadBytes()方法丢弃已经读过的字节, 将可读字节区域(CONTENT)往前移动readerIndex位，同时修改读索引和写索引。如果频繁调用，会有多次数据复制开销，对性能有一定的影响
     * 可读字节区域: [readerIndex, writerIndex)
     * 可写字节区域: [writerIndex, capacity)之间的区域
     *
     *
     *
     *
     *
     * 索引管理
     *
     * - markReaderIndex()+resetReaderIndex() : markReaderIndex()是标记当前的readerIndex，resetReaderIndex()则是恢复到刚刚标记的readerIndex。常用于dump ByteBuf的内容，又不想影响原来ByteBuf的readerIndex的值
     * - readerIndex(int) , writerIndex(int) 设置读写索引为指定值
     * - clear()效果是: readerIndex=0, writerIndex=0。不会清除内存; 调用clear()比调用discardReadBytes()轻量的多。仅仅重置readerIndex和writerIndex的值，不会拷贝任何内存，开销较小
     *
     * 查找操作
     *
     *     // 使用indexOf()方法来查找
     *     buffer.indexOf(buffer.readerIndex(), buffer.writerIndex(), (byte)8);
     *     // 使用ByteProcessor查找给定的值
     *     int index = buffer.forEachByte(ByteProcessor.FIND_CR);
     *
     *
     *
     * 复制拷贝
     * 如果需要拷贝现有缓冲区的真实副本，请使用copy()或copy(int, int)方法
     *
     * 派生缓冲区 ----- 视图
     *视图不做任何拷贝操作, 都会返回一个新的ByteBuf实例，具有自己的读索引和写索引。但是，其内部存储是与原对象是共享的
     * 若修改视图实例, 对应的源实例也会被修改
     *
     * 1. duplicate()
     *
     * 2. slice()
     *
     * 3. slice(int, int)
     *
     * 4. Unpooled.unmodifiableBuffer(...)
     *
     * 5. Unpooled.wrappedBuffer(...)
     *
     * 6. order(ByteOrder)
     *
     * 7. readSlice(int)
     *
     *
     *
     *
     * 引用计数:
     * 1. 谁负责释放: 一般来说，是由最后访问(引用计数)对象的那一方来负责将它释放
     * 2. buffer.release() ----- 引用计数减1
     * 3. buffer.retain() ----- 引用计数加1
     * 4. buffer.refCnt() ----- 返回当前对象引用计数值
     * 5. buffer.touch() ----- 记录当前对象的访问位置，主要用于调试。
     * 6. ByteBuf的三种模式: 堆缓冲区(heap Buffer)、直接缓冲区(dirrect Buffer)和复合缓冲区(Composite Buffer)都使用了引用计数
     * 7. 如果使用了Netty的ByteBuf，建议功能测试时，打开内存检测: -Dio.netty.leakDetectionLevel=paranoid
     *
     *
     * ByteBufHolder
     * 是ByteBuf的容器, 默认实现: DefaultByteBufHolder
     * 为Netty的高级特性提供了支持，如缓冲区池化，可以从池中借用ByteBuf，并且在需要时自动释放
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf in = (ByteBuf) msg;
            while (in.isReadable()) {
                System.out.println(">>> " + (char) in.readByte());
                System.out.flush();
            }
//              System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));
        } finally {
            // 当有写操作时，不需要手动释放msg的引用
            // 当只有读操作时，才需要手动释放msg的引用
            ReferenceCountUtil.release(msg);
        }

        ////获取ChannelPipeline对象
        //ChannelPipeline pipeline = ctx.channel().pipeline();
        ////往pipeline中添加ChannelHandler处理器，装配流水线
        //pipeline.addLast(new MyServerHandler());
    }
}

class EchoServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * - netty 屏蔽了 Java nio 中的 flip(), because it has two pointers; one for read operations and the other
     * for write operations. The writer index increases when you write something to a ByteBuf while the reader
     * index does not change. The reader index and the writer index represents where the message starts and ends .
     *
     * - A ChannelFuture represents an I/O operation which has not yet occurred. It means, any requested
     * operation might not have been performed yet because all operations are asynchronous in Netty.
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write(msg);// 这一步会自动 release msg
        ctx.flush();
        //ctx.writeAndFlush(msg)
    }
}

class TimeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当连接被建立后就触发
     *
     * 回写一个数字, 之后关闭 server
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)
        // 4 bytes
        final ByteBuf time = ctx.alloc().buffer(4); // (2)
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
        f.addListener(new ChannelFutureListener() {
            // 等待 msg 发送完之后触发
            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;

                //判断是否操作成功
                if (future.isSuccess()) {
                    System.out.println("连接成功");
                } else {
                    System.out.println("连接失败");
                }

                ctx.close();
            }
        }); // (4)
        //f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

/**
 * 长时间的操作交给taskQueue异步处理
 */
class LongTimeTaskHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //长时间操作，不至于长时间的业务操作导致Handler阻塞
                    Thread.sleep(1000);
                    System.out.println("长时间的业务处理");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

/**
 * scheduleTaskQueue延时任务队列
 */
class ScheduleTaskHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.channel().eventLoop().schedule(() -> {
            try {
                //长时间操作，不至于长时间的业务操作导致Handler阻塞
                Thread.sleep(1000);
                System.out.println("长时间的业务处理");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5, TimeUnit.SECONDS);// 延时操作
    }
}

