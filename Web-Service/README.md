## React版我画你猜

![效果预览][1]

之前有看到过一个Vue版本的 `我画你猜` 然后用 `React` 也做了一个。技术栈：`React + Nodejs + Socket.io + Webpack + Less`

## 主要注意下面几点：
#### 1.Socket.io在express中的问题
具体可以参考官方，[Socket.io](https://github.com/socketio/socket.io);

#### 2.Socket.io-client
`index.html`中，我在开头引入了`./node_modules/socket.io-client/socket.io.js`,如果路径变更记得修改，主要是出于对socket.io的不熟悉，不知道怎么样在react中通过import方式去引入，有好的做法欢迎提出。

#### 3.其他
写在最后，还是那句，第1次接触Websocket，不知道`socket.io`有没有双向发送消息的机制，所以在`server.js`中你会看到来来回回了好几次。

```
io.on('connection', function(socket) {
    //接收path
    socket.on('drawPath', function(data) {
        socket.broadcast.emit('showPath', data);
    });

    socket.on('submit', function(keyword) {
        var bingo = 0;
        if (KEYWORD.toLocaleLowerCase() == keyword.toLocaleLowerCase()) {
            bingo = 1;
        }
        socket.emit('answer', {
            bingo
        });
    });

    socket.on('message', function(message){
        if(message == 'getKeyWord'){
            KEYWORD = keyword[Math.floor(Math.random() * keyword.length)];
            socket.emit('keyword', KEYWORD);
        }else if(message == 'clear'){
            socket.emit('showBoardClearArea');
        }
    });

    socket.on('disconnect', function() {});
});
```
>写完之后没有整理，比较乱。就是想大概实现一下，有兴趣的自己看吧！做了亏心事背景要灰一点。。。。


### Installation
```
git clone https://github.com/zhoulijie/draw-something.git

```

### Run

```
//安装依赖
npm install
//run
npm start
```

[1]: https://raw.githubusercontent.com/zhoulijie/draw-something/master/src/imgs/draw.gif
