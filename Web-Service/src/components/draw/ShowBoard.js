import React,{Component} from 'react';
import '../draw/draw.less';
import '../draw/button.less';

import Rodal from 'rodal';

import 'rodal/src/rodal.css'


let showBoard = class ShowBoard extends Component{

    constructor(props) {
        super(props);
        this.state = {
            ctx : null,
            mousePressed : false,
            lineValue : 9,
            colorValue : 'blue',
            beginX: 0,
            beginY: 0,
            endX: 0,
            endY: 0,
            visible:false,
            callback:'',
        };
    }
    
    hide(){
        this.setState({visible:false,callback:'',keyword:''});
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
            console.log('no route',this.props);
            propName = this.props[prop];
        }
        
        

        if(propName){
            return propName;
        }
        return false;
    }

    ready() {
        var el = this.refs.myCanvas;
        var ready,socket;
        var that = this;

        this.setState({
            ctx : el.getContext("2d")
        });

        ready = this.hasProps('ready');

        if(ready){
            socket = ready();
            this.setState({
                socket : socket
            });
            socket.on('showPath', (data)=>{
                console.log(data);
                this.drawing(
                    data.endX,
                    data.endY,
                    data.beginX,
                    data.beginY,
                    data.colorValue,
                    data.lineValue,
                    data.deviceHeight,
                    data.deviceWidth
                );
            });

            // 监听答案是否正确
            socket.on('answer', (data)=>{
                switch (data.bingo) {
                    case 1:
                        // layer.open({
                        //     content: '真棒答对了！'
                        // });
                        that.setState({visible:true,callback:'真棒答对了'});
                        // alert('真棒答对了！');
                        console.log(1);
                        socket.send('success');
                        break;
                    case -1:
                        // layer.open({
                        //     content: '请输入答案！'
                        // });                        
                        // alert('请输入答案');
                        that.setState({visible:true,callback:'请输入答案'});
                        console.log(2);
                        break;
                    default:
                        // layer.open({
                        //     content: '你答错了，再发散下思维想想！'
                        // });
                        // alert('愚蠢的地球人！');
                        that.setState({visible:true,callback:'你答错了，再发散下思维想想！('+data.tip+')'});
                        console.log(3);
                        
                }
            });
            //清除画布
            socket.on('showBoardClearArea', ()=>{
                this.clearArea();
            });
            
            socket.on('successClearArea',()=>{
                this.clearArea();
            })
        }
    }

    drawing(x, y, beginX, beginY, colorValue, lineValue,deviceWidth,deviceHeight) {
        var deviceWidth = deviceWidth;
        var deviceHeight = deviceHeight;
        var ctx = this.state.ctx;
        ctx.beginPath();
        ctx.strokeStyle = colorValue;
        ctx.lineWidth = lineValue;
        ctx.lineJoin = "round";
        ctx.moveTo(beginX, beginY);
        ctx.lineTo(x, y);
        ctx.closePath();
        ctx.stroke();
    }

    clearArea() {
        var ctx = this.state.ctx;
        ctx.setTransform(1, 0, 0, 1, 0, 0);
        ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
    }

    componentDidMount(){
        this.ready();
    }

    render(){
        return (
            <div className="control-ops">
                <div className="item keyword">猜猜这家伙画的是啥！</div>
                <canvas className="canvas" ref="myCanvas"
                        width="500"
                        height="400"
                        style={{border:'1px solid #ccc'}}>
                </canvas>
                <div className="keyword">
                    <input type="text" className="keywordInput" value={this.state.keyword} onChange={(e)=>{ this.setState({ keyword : e.target.value.trim() }) }} />
                    <input type="button" value="我猜！" className="button button-glow button-rounded button-primary button-tiny" onClick={()=>this.state.socket.emit('submit', this.state.keyword)} />
                </div>

                <Rodal visible={this.state.visible} onClose={this.hide.bind(this)} animation="flip">
                    <div className="header">提示</div>
                    <div className="body">{this.state.callback}</div>
                    <button className="rodal-confirm-btn" onClick={this.hide.bind(this)}>确定</button>
                    
                </Rodal>

                
            </div>
        );
    }
};
export default showBoard
