import React,{Component} from 'react';
import '../draw/draw.less';
import '../draw/button.less'

import Rodal from 'rodal';

import 'rodal/src/rodal.css'


export default class DrawBoard extends Component{

    constructor(props) {
        super(props);
        this.state = {
            ctx : null,
            mousePressed : false,
            lineValue : 3,
            colorValue : 'red',
            beginX: 0,
            beginY: 0,
            endX: 0,
            endY: 0,
            visible:false
        }
    }

    mouseDownHandel(e){
        this.setState({
            mousePressed : true
        });
        this.drawing(e.pageX - e.target.offsetLeft , e.pageY - e.target.offsetTop , false);
    }

    mouseMoveHandel(e){
        if (this.state.mousePressed) {
            this.drawing(e.pageX - e.target.offsetLeft, e.pageY - e.target.offsetTop, true);
        }
    }

    setMousePressed(){
        this.setState({
            mousePressed : false
        })
    }

    mouseUpHandel(){
        this.setMousePressed();
        this.hasProps('drawEnd')();
    }

    hasProps(prop){
        if(!prop){
            return false
        };

        var propName;
        if( this.props.route && this.props.route[prop] ){
            propName = this.props.route[prop];
        }

        if (this.props[prop]) {
            propName = this.props[prop];
        }

        if(propName){
            return propName;
        }

        return false;
    }

    ready() {
        let ready = this.hasProps('ready'),
            el = this.refs.myCanvas,
            socket;
        if(ready){
            socket = ready();
            this.setState({
                socket : socket
            });
            socket.send('getKeyWord');
            socket.on('keyword', (keyword)=>{
                this.setState({
                    keyword
                })
            });
            
            socket.on('successClearArea',()=>{
                this.clearArea2();
            })
        }
        el = this.refs.myCanvas;
        this.setState({
            ctx : el.getContext("2d")
        });
    }

    drawChange(path){
        let change = this.hasProps('change');
        let el = this.refs.myCanvas;
        if(change){
            change({
                ...this.state
            });
        }
        var params = {
            // 起始坐标X
            beginX : this.state.beginX,
            // 起始坐标Y
            beginY : this.state.beginY,
            // 终止坐标X
            endX : path.x,
            // 终止坐标Y
            endY : path.y,
            // 线的宽度(1,3,5,7,9这类的数字)
            lineValue : this.state.lineValue,
            // 线的颜色(black、blue)
            colorValue : this.state.colorValue,
            // 设备宽度
            deviceWidth: el.width,
            // 设备高度
            deviceHeight:el.height
        };
        console.log(params);
        this.state.socket.emit('drawPath',params)
    }
    

    drawing(x, y, isDown) {
        var ctx,timer;
        if (isDown) {
            ctx = this.state.ctx;
            ctx.beginPath();
            ctx.strokeStyle = this.state.colorValue;
            ctx.lineWidth = this.state.lineValue;
            ctx.lineJoin = "round";
            ctx.moveTo(this.state.beginX, this.state.beginY);
            ctx.lineTo(x, y);
            ctx.closePath();
            ctx.stroke();
            this.drawChange({x,y});
        }
        this.setState({
            beginX : x,
            beginY : y
        })
    }

    again(){
        this.setState({visible:false});
        this.state.socket.send('getKeyWord');
        this.state.socket.on('keyword', (keyword)=>{
            this.setState({
                keyword
            })
        });
    }

    clearArea() {
        var ctx = this.state.ctx;
        ctx.setTransform(1, 0, 0, 1, 0, 0);
        ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
        this.state.socket.send('clear');
        this.state.socket.send('getKeyWord');
        this.state.socket.on('keyword', (keyword)=>{
            this.setState({
                keyword
            })
        });
    }
    
    clearArea2(){
        var ctx = this.state.ctx;
        ctx.setTransform(1, 0, 0, 1, 0, 0);
        
        ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
        this.setState({visible:true});
        
        
    }

    componentDidMount(){
        this.ready();
    }

    render(){
        return (
            <div className="control-ops">
                <div className="item keyword">你要画: <strong style={{color:'#c00'}}>{this.state.keyword}</strong></div>
                <canvas className="canvas" ref="myCanvas"
                        onTouchStart={this.mouseDownHandel.bind(this)}
                        onTouchMove={this.mouseMoveHandel.bind(this)}
                        onTouchCancel={this.mouseUpHandel.bind(this)}
                        onTouchEnd={this.setMousePressed.bind(this)}
                    onMouseDown={this.mouseDownHandel.bind(this)}
                    onMouseMove={this.mouseMoveHandel.bind(this)}
                    onMouseUp={this.mouseUpHandel.bind(this)}
                    onMouseLeave={this.setMousePressed.bind(this)}
                    width="500"
                    height="400">
                </canvas>
                <div className="control-bar flex-box">
                    <div className="item">
                        
                        <button type="button" className="button button-primary button-pill button-small" onClick={this.clearArea.bind(this)}>画错了，重来！</button>
                    </div>
                    <div className="item">
                        笔尖力度:
                        <select
                            value={this.state.lineValue}
                            onChange={(e)=>this.setState({lineValue: e.target.value})}
                        >
                            <option value="1">1号笔芯</option>
                            <option value="3">3号笔芯</option>
                            <option value="5">5号笔芯</option>
                            <option value="7">7号笔芯</option>
                            <option value="9">9号笔芯</option>
                            <option value="11">11号笔芯</option>
                            <option value="30">30号笔芯</option>
                        </select>
                    </div>
                    <div className="item">
                        彩色水笔:
                        <select
                            value={this.state.colorValue}
                            onChange={(e)=>this.setState({lineValue:3,colorValue: e.target.value})}
                        >
                            <option value="black">黑色</option>
                            <option value="blue">蓝色</option>
                            <option value="red">红色</option>
                            <option value="green">绿色</option>
                            <option value="yellow">黄色</option>
                            <option value="gray">灰色</option>
                            <option value="white">白色</option>
                        </select>
                    </div>
                    
                    <div className="item">
                        <button className="eraser-btn button button-primary button-box button-giant button-longshadow-right" onClick={(e)=>this.setState({lineValue:30,colorValue: 'white'})}>
                            <i className="iconfont">&#xe620;</i>
                        </button>
                    </div>
                </div>

                <Rodal visible={this.state.visible} onClose={this.again.bind(this)} animation="flip">
                    <div className="header">提示</div>
                    <div className="body">猜的人答对了哟！</div>
                    <button className="rodal-confirm-btn" onClick={this.again.bind(this)}>再开一局</button>
                </Rodal>
                
            </div>
        );
    }
}
