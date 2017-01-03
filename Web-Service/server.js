var path = require('path');
var express = require('express');
var webpack = require('webpack');
var config = require('./webpack.config');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server);
var compiler = webpack(config);

var keyword = ['猫', '大象', '飞机', '钱', '炸弹', '猪'], KEYWORD;

app.use(express.static(path.join(__dirname, '/')));
    //use in webpack development mode
app.use(require('webpack-dev-middleware')(compiler, {
    noInfo: true,
    publicPath: config.output.publicPath
}));
app.use(require('webpack-hot-middleware')(compiler));

//use in webpack production mode
//app.use(express.static(__dirname));

app.get('/', function(req, res) {
    res.sendFile(path.join(__dirname, 'index.html'));
});

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
            io.sockets.emit('showBoardClearArea');
            // socket.emit('showBoardClearArea');
            
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
        console.log(keyword);
        if(keyword && KEYWORD){
            if (KEYWORD.toLocaleLowerCase() == keyword.toLocaleLowerCase()) {
                bingo = 1;
            }
        }else{
            bingo = -1;
        }
        console.log(bingo);

        // 将flag标志位传到连接的客户端
        socket.emit('answer', {
            bingo
        });
    });



    socket.on('disconnect', function() {});
});

server.listen(3007, 'localhost', function(err) {
    if (err) {
        return console.log(err);
    }
    console.log('Listening at http://localhost:3007');
});
