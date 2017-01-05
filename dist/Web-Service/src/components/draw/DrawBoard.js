'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

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

var DrawBoard = function (_Component) {
    _inherits(DrawBoard, _Component);

    function DrawBoard(props) {
        _classCallCheck(this, DrawBoard);

        var _this = _possibleConstructorReturn(this, (DrawBoard.__proto__ || Object.getPrototypeOf(DrawBoard)).call(this, props));

        _this.state = {
            ctx: null,
            mousePressed: false,
            lineValue: 3,
            colorValue: 'red',
            beginX: 0,
            beginY: 0,
            endX: 0,
            endY: 0,
            visible: false
        };
        return _this;
    }

    _createClass(DrawBoard, [{
        key: 'mouseDownHandel',
        value: function mouseDownHandel(e) {
            this.setState({
                mousePressed: true
            });
            this.drawing(e.pageX - e.target.offsetLeft, e.pageY - e.target.offsetTop, false);
        }
    }, {
        key: 'mouseMoveHandel',
        value: function mouseMoveHandel(e) {
            if (this.state.mousePressed) {
                this.drawing(e.pageX - e.target.offsetLeft, e.pageY - e.target.offsetTop, true);
            }
        }
    }, {
        key: 'setMousePressed',
        value: function setMousePressed() {
            this.setState({
                mousePressed: false
            });
        }
    }, {
        key: 'mouseUpHandel',
        value: function mouseUpHandel() {
            this.setMousePressed();
            this.hasProps('drawEnd')();
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

            var ready = this.hasProps('ready'),
                el = this.refs.myCanvas,
                socket = void 0;
            if (ready) {
                socket = ready();
                this.setState({
                    socket: socket
                });
                socket.send('getKeyWord');
                socket.on('keyword', function (keyword) {
                    _this2.setState({
                        keyword: keyword
                    });
                });

                socket.on('successClearArea', function () {
                    _this2.clearArea2();
                });
            }
            el = this.refs.myCanvas;
            this.setState({
                ctx: el.getContext("2d")
            });
        }
    }, {
        key: 'drawChange',
        value: function drawChange(path) {
            var change = this.hasProps('change');
            var el = this.refs.myCanvas;
            if (change) {
                change(_extends({}, this.state));
            }
            var params = {
                // 起始坐标X
                beginX: this.state.beginX,
                // 起始坐标Y
                beginY: this.state.beginY,
                // 终止坐标X
                endX: path.x,
                // 终止坐标Y
                endY: path.y,
                // 线的宽度(1,3,5,7,9这类的数字)
                lineValue: this.state.lineValue,
                // 线的颜色(black、blue)
                colorValue: this.state.colorValue,
                // 设备宽度
                deviceWidth: el.width,
                // 设备高度
                deviceHeight: el.height
            };
            console.log(params);
            this.state.socket.emit('drawPath', params);
        }
    }, {
        key: 'drawing',
        value: function drawing(x, y, isDown) {
            var ctx, timer;
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
                this.drawChange({ x: x, y: y });
            }
            this.setState({
                beginX: x,
                beginY: y
            });
        }
    }, {
        key: 'again',
        value: function again() {
            var _this3 = this;

            this.setState({ visible: false });
            this.state.socket.send('getKeyWord');
            this.state.socket.on('keyword', function (keyword) {
                _this3.setState({
                    keyword: keyword
                });
            });
        }
    }, {
        key: 'clearArea',
        value: function clearArea() {
            var _this4 = this;

            var ctx = this.state.ctx;
            ctx.setTransform(1, 0, 0, 1, 0, 0);
            ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
            this.state.socket.send('clear');
            this.state.socket.send('getKeyWord');
            this.state.socket.on('keyword', function (keyword) {
                _this4.setState({
                    keyword: keyword
                });
            });
        }
    }, {
        key: 'clearArea2',
        value: function clearArea2() {
            var ctx = this.state.ctx;
            ctx.setTransform(1, 0, 0, 1, 0, 0);

            ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
            this.setState({ visible: true });
        }
    }, {
        key: 'componentDidMount',
        value: function componentDidMount() {
            this.ready();
        }
    }, {
        key: 'render',
        value: function render() {
            var _this5 = this;

            return _react2.default.createElement(
                'div',
                { className: 'control-ops' },
                _react2.default.createElement(
                    'div',
                    { className: 'item keyword' },
                    '\u4F60\u8981\u753B: ',
                    _react2.default.createElement(
                        'strong',
                        { style: { color: '#c00' } },
                        this.state.keyword
                    )
                ),
                _react2.default.createElement('canvas', { className: 'canvas', ref: 'myCanvas',
                    onTouchStart: this.mouseDownHandel.bind(this),
                    onTouchMove: this.mouseMoveHandel.bind(this),
                    onTouchCancel: this.mouseUpHandel.bind(this),
                    onTouchEnd: this.setMousePressed.bind(this),
                    onMouseDown: this.mouseDownHandel.bind(this),
                    onMouseMove: this.mouseMoveHandel.bind(this),
                    onMouseUp: this.mouseUpHandel.bind(this),
                    onMouseLeave: this.setMousePressed.bind(this),
                    width: '500',
                    height: '400' }),
                _react2.default.createElement(
                    'div',
                    { className: 'control-bar flex-box' },
                    _react2.default.createElement(
                        'div',
                        { className: 'item' },
                        _react2.default.createElement(
                            'button',
                            { type: 'button', className: 'button button-primary button-pill button-small', onClick: this.clearArea.bind(this) },
                            '\u753B\u9519\u4E86\uFF0C\u91CD\u6765\uFF01'
                        )
                    ),
                    _react2.default.createElement(
                        'div',
                        { className: 'item' },
                        '\u7B14\u5C16\u529B\u5EA6:',
                        _react2.default.createElement(
                            'select',
                            {
                                value: this.state.lineValue,
                                onChange: function onChange(e) {
                                    return _this5.setState({ lineValue: e.target.value });
                                }
                            },
                            _react2.default.createElement(
                                'option',
                                { value: '1' },
                                '1\u53F7\u7B14\u82AF'
                            ),
                            _react2.default.createElement(
                                'option',
                                { value: '3' },
                                '3\u53F7\u7B14\u82AF'
                            ),
                            _react2.default.createElement(
                                'option',
                                { value: '5' },
                                '5\u53F7\u7B14\u82AF'
                            ),
                            _react2.default.createElement(
                                'option',
                                { value: '7' },
                                '7\u53F7\u7B14\u82AF'
                            ),
                            _react2.default.createElement(
                                'option',
                                { value: '9' },
                                '9\u53F7\u7B14\u82AF'
                            ),
                            _react2.default.createElement(
                                'option',
                                { value: '11' },
                                '11\u53F7\u7B14\u82AF'
                            ),
                            _react2.default.createElement(
                                'option',
                                { value: '30' },
                                '30\u53F7\u7B14\u82AF'
                            )
                        )
                    ),
                    _react2.default.createElement(
                        'div',
                        { className: 'item' },
                        '\u5F69\u8272\u6C34\u7B14:',
                        _react2.default.createElement(
                            'select',
                            {
                                value: this.state.colorValue,
                                onChange: function onChange(e) {
                                    return _this5.setState({ lineValue: 3, colorValue: e.target.value });
                                }
                            },
                            _react2.default.createElement(
                                'option',
                                { value: 'black' },
                                '\u9ED1\u8272'
                            ),
                            _react2.default.createElement(
                                'option',
                                { value: 'blue' },
                                '\u84DD\u8272'
                            ),
                            _react2.default.createElement(
                                'option',
                                { value: 'red' },
                                '\u7EA2\u8272'
                            ),
                            _react2.default.createElement(
                                'option',
                                { value: 'green' },
                                '\u7EFF\u8272'
                            ),
                            _react2.default.createElement(
                                'option',
                                { value: 'yellow' },
                                '\u9EC4\u8272'
                            ),
                            _react2.default.createElement(
                                'option',
                                { value: 'gray' },
                                '\u7070\u8272'
                            ),
                            _react2.default.createElement(
                                'option',
                                { value: 'white' },
                                '\u767D\u8272'
                            )
                        )
                    ),
                    _react2.default.createElement(
                        'div',
                        { className: 'item' },
                        _react2.default.createElement(
                            'button',
                            { className: 'eraser-btn button button-primary button-box button-giant button-longshadow-right', onClick: function onClick(e) {
                                    return _this5.setState({ lineValue: 30, colorValue: 'white' });
                                } },
                            _react2.default.createElement(
                                'i',
                                { className: 'iconfont' },
                                '\uE620'
                            )
                        )
                    )
                ),
                _react2.default.createElement(
                    _rodal2.default,
                    { visible: this.state.visible, onClose: this.again.bind(this), animation: 'flip' },
                    _react2.default.createElement(
                        'div',
                        { className: 'header' },
                        '\u63D0\u793A'
                    ),
                    _react2.default.createElement(
                        'div',
                        { className: 'body' },
                        '\u731C\u7684\u4EBA\u7B54\u5BF9\u4E86\u54DF\uFF01'
                    ),
                    _react2.default.createElement(
                        'button',
                        { className: 'rodal-confirm-btn', onClick: this.again.bind(this) },
                        '\u518D\u5F00\u4E00\u5C40'
                    )
                )
            );
        }
    }]);

    return DrawBoard;
}(_react.Component);

exports.default = DrawBoard;
//# sourceMappingURL=DrawBoard.js.map