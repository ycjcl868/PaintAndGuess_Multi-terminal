# 多终端(Java端、安卓端、Web端)你猜我画小游戏V1.0

![](./设计图/94752d99jw1dnrq2bfvjbj.jpg)

## 项目概述

-----------

>Java课大作业，做一个小游戏，三人Git协作，不同终端，一个分支三个文件夹互不影响

### 原理图
当玩家1在使用画笔在画板上进行绘图工作时，把当前这个玩家的绘图的数据传递到服务器，然后由服务器把该数据广播到其他玩家，其他玩家的画笔将根据这些数据自动在画板上进行绘制。

![](./设计图/原理图1.png)



### 所使用的技术及人员分配
* PC端: Java_GUI画图板 **(王灿)**
* 安卓: Android_画图API  **(王程远)**
* Web: Canvas 画图API **(金朝麟)**
* 服务器端：Socket服务器编写 **(金朝麟)**
* UI: 界面设计 **(付健)**


## Socket服务器端(数据交互)

-----------

`http://119.29.6.75:9999/`


服务端关键代码：

```javascript

// socket监听的事件
io.on('connection', function(socket) {
    /**
     * 画者事件 drawPath
     * 传入JSON：data
     * 例：
     *  {
            beginX: 68
            beginY: 182
            colorValue: "red"
            deviceHeight: 400
            deviceWidth: 500
            endX: 74
            endY: 181
            lineValue: 3
     *  }
     */
    socket.on('drawPath', function(data) {
        /**
         * 广播事件 showPath
         * 将画者事件接收的data数据，向连接到socket服务器的客户端(猜者)进行广播
         * 例：
         * socket.on('showPath', (data)=>{
                // 自定义画的方法
                this.drawing({json});
            });
         * 
         */
        socket.broadcast.emit('showPath', data);
    });



    // 监听客户端的socket.send(message)方法
    socket.on('message', function(message){
        // 画者生成一个随机的关键字
        if(message == 'getKeyWord'){
            KEYWORD = keyword[Math.floor(Math.random() * keyword.length)];
            // 将生成的关键字发送到画者的客户端
            socket.emit('keyword', KEYWORD);
            
        // 画者清空画布 socket.send('clear')    
        }else if(message == 'clear'){
            // 猜者端清空画布
            socket.emit('showBoardClearArea');
        }
    }); 
    
    
    
    /**
     * 猜者提交 submit
     * 传入str: keyword
     * 
     * this.state.socket.emit('submit', keyword)}
     */
    socket.on('submit', function(keyword) {
        // 标志位
        var bingo = 0;
        // 如果
        if (KEYWORD.toLocaleLowerCase() == keyword.toLocaleLowerCase()) {
            bingo = 1;
        }
        // 将flag标志位传到连接的客户端
        socket.emit('answer', {
            bingo
        });
    });



    socket.on('disconnect', function() {});
});
```


[测试工具](http://www.blue-zero.com/WebSocket/)：

![](./设计图/测试.jpg)



## 设计图

-----------




