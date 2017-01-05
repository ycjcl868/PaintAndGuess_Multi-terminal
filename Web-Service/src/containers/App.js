import React, { Component, PropTypes } from 'react';
import DrawBoard from '../components/draw/DrawBoard';
import ShowBoard from '../components/draw/ShowBoard';
import '../components/draw/draw.less';
import '../components/draw/button.less'





// import '../components/draw/font-awesome.min.css'

export default class Draw extends Component{
    constructor(props){
        super(props);
        this.state = {
            player : 0
        }
    }

    render(){
        let renderNode;
        switch (this.state.player) {
            // 画者
            case 1:
                renderNode = <DrawBoard
                                ready={()=>io()}
                                drawEnd={()=>{console.log('stop')}} />;
                break;
            // 猜者
            case 2:
                renderNode = <ShowBoard ready={()=>io()} />;
               break;
            default:
                renderNode = (
                    <div>
                        <img className="indexImg" src="./src/imgs/index.jpg" alt=""/>
                        <div className="draw-wrap flex-box">
                            <a onClick={()=>this.setState({player:1})} title="我来画" className="button button-3d button-primary button-pill"><i className="iconfont">&#xe665;</i>我来画</a>
                            <a onClick={()=>this.setState({player:2})} title="我来猜" className="button button-3d button-royal button-pill"><i className="iconfont">&#xe628;</i>我来猜</a>
                        </div>
                    </div>
                    )
        }

        return (
            <div>{renderNode}</div>
        );
    }
    
}
