# Zookeeper

Apache Hadoop 的一个子项目, zookeeper=文件系统+监听通知机制

Zookeeper维护一个类似文件系统的数据结构, 每个子节点称为 znode, 可以存储少量数据

znode的四种类型:

- PERSISTENT-持久化目录节点, 客户端与zookeeper断开连接后，该节点依旧存在
- PERSISTENT_SEQUENTIAL-持久化顺序编号目录节点, 客户端与zookeeper断开连接后，该节点依旧存在, Zookeeper给该节点名称进行顺序编号
- EPHEMERAL-临时目录节点, 客户端与zookeeper断开连接后，该节点被删除
- EPHEMERAL_SEQUENTIAL-临时顺序编号目录节点, 客户端与zookeeper断开连接后，该节点被删除，只是Zookeeper给该节点名称进行顺序编号

client 可以对指定的 znode 进行注册监听, 当目录节点发生变化（数据改变、被删除、子目录节点增加删除）时，zookeeper会通知客户端