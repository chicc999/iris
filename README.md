# iris
High performance message queue

## 通信协议格式
具体通信指令分为header和body两部分。其中header为基类command中固定字段，body中为具体命令自定义字段
totalLength | header Length | header content | body length | body content
* totalLength：4字节，后面四部分的总长度
* header Length: 4字节，Command中header长度，即 header content 长度
* header content: header的实际内容
* body length ：4字节，Command中body长度，即 body content 长度
* body content：body实际内容