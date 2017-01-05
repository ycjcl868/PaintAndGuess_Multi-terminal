'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

require('../draw/draw.less');

require('../draw/button.less');

var _rodal = require('rodal');

var _rodal2 = _interopRequireDefault(_rodal);

require('rodal/src/rodal.css');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var showBoard = function (_Component) {
    _inherits(ShowBoard, _Component);

    function ShowBoard(props) {
        _classCallCheck(this, ShowBoard);

        var _this = _possibleConstructorReturn(this, (ShowBoard.__proto__ || Object.getPrototypeOf(ShowBoard)).call(this, props));

        _this.state = {
            ctx: null,
            mousePressed: false,
            lineValue: 9,
            colorValue: 'blue',
            beginX: 0,
            beginY: 0,
            endX: 0,
            endY: 0,
            visible: false,
            callback: ''
        };
        return _this;
    }

    _createClass(ShowBoard, [{
        key: 'hide',
        value: function hide() {
            this.setState({ visible: false, callback: '', keyword: '' });
        }
    }, {
        key: 'hasProps',
        value: function hasProps(prop) {
            if (!prop) {
                return false;
            };

            var propName;
            if (this.props.route && this.props.route[prop]) {
                propName = this.props.route[prop];
            }

            if (this.props[prop]) {
                console.log('no route', this.props);
                propName = this.props[prop];
            }

            if (propName) {
                return propName;
            }
            return false;
        }
    }, {
        key: 'ready',
        value: function ready() {
            var _this2 = this;

            var el = this.refs.myCanvas;
            var ready, socket;
            var that = this;

            this.setState({
                ctx: el.getContext("2d")
            });

            ready = this.hasProps('ready');

            if (ready) {
                socket = ready();
                this.setState({
                    socket: socket
                });
                socket.on('showPath', function (data) {
                    console.log(data);
                    _this2.drawing(data.endX, data.endY, data.beginX, data.beginY, data.colorValue, data.lineValue, data.deviceHeight, data.deviceWidth);
                });

                // 监听答案是否正确
                socket.on('answer', function (data) {
                    switch (data.bingo) {
                        case 1:
                            // layer.open({
                            //     content: '真棒答对了！'
                            // });
                            that.setState({ visible: true, callback: '真棒答对了' });
                            // alert('真棒答对了！');
                            console.log(1);
                            socket.send('success');
                            break;
                        case -1:
                            // layer.open({
                            //     content: '请输入答案！'
                            // });                        
                            // alert('请输入答案');
                            that.setState({ visible: true, callback: '请输入答案' });
                            console.log(2);
                            break;
                        default:
                            // layer.open({
                            //     content: '你答错了，再发散下思维想想！'
                            // });
                            // alert('愚蠢的地球人！');
                            that.setState({ visible: true, callback: '你答错了，再发散下思维想想！(' + data.tip + ')' });
                            console.log(3);

                    }
                });
                //清除画布
                socket.on('showBoardClearArea', function () {
                    _this2.clearArea();
                });

                socket.on('successClearArea', function () {
                    _this2.clearArea();
                });
            }
        }
    }, {
        key: 'drawing',
        value: function drawing(x, y, beginX, beginY, colorValue, lineValue, deviceWidth, deviceHeight) {
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
    }, {
        key: 'clearArea',
        value: function clearArea() {
            var ctx = this.state.ctx;
            ctx.setTransform(1, 0, 0, 1, 0, 0);
            ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
        }
    }, {
        key: 'componentDidMount',
        value: function componentDidMount() {
            this.ready();
        }
    }, {
        key: 'render',
        value: function render() {
            var _this3 = this;

            return _react2.default.createElement(
                'div',
                { className: 'control-ops' },
                _react2.default.createElement(
                    'div',
                    { className: 'item keyword' },
                    '\u731C\u731C\u8FD9\u5BB6\u4F19\u753B\u7684\u662F\u5565\uFF01'
                ),
                _react2.default.createElement('canvas', { className: 'canvas', ref: 'myCanvas',
                    width: '500',
                    height: '400',
                    style: { border: '1px solid #ccc' } }),
                _react2.default.createElement(
                    'div',
                    { className: 'keyword' },
                    _react2.default.createElement('input', { type: 'text', className: 'keywordInput', value: this.state.keyword, onChange: function onChange(e) {
                            _this3.setState({ keyword: e.target.value.trim() });
                        } }),
                    _react2.default.createElement('input', { type: 'button', value: '\u6211\u731C\uFF01', className: 'button button-glow button-rounded button-primary button-tiny', onClick: function onClick() {
                            return _this3.state.socket.emit('submit', _this3.state.keyword);
                        } })
                ),
                _react2.default.createElement(
                    _rodal2.default,
                    { visible: this.state.visible, onClose: this.hide.bind(this), animation: 'flip' },
                    _react2.default.createElement(
                        'div',
                        { className: 'header' },
                        '\u63D0\u793A'
                    ),
                    _react2.default.createElement(
                        'div',
                        { className: 'body' },
                        this.state.callback
                    ),
                    _react2.default.createElement(
                        'button',
                        { className: 'rodal-confirm-btn', onClick: this.hide.bind(this) },
                        '\u786E\u5B9A'
                    )
                )
            );
        }
    }]);

    return ShowBoard;
}(_react.Component);
exports.default = showBoard;
//# sourceMappingURL=ShowBoard.js.map