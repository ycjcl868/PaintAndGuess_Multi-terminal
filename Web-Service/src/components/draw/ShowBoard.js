import React,{Component} from 'react';
import '../draw/draw.less';
export default class ShowBoard extends Component{

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
            endY: 0
        }
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
                        alert('真棒答对了！');
                        break;
                    case -1:
                        alert('请输入答案');
                        break;
                    default:
                        alert('愚蠢的地球人！');
                }
            });
            //清除画布
            socket.on('showBoardClearArea', ()=>{
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
                    <input type="text" className="keywordInput" value={this.state.keyword} onChange={(e)=>{ this.setState({ keyword : e.target.value }) }} />
                    <input type="button" value="我猜！" className="button button-glow button-rounded button-primary button-tiny" onClick={()=>this.state.socket.emit('submit', this.state.keyword)} />
                </div>
            </div>
        );
    }
}
