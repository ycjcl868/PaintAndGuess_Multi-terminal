'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _DrawBoard = require('../components/draw/DrawBoard');

var _DrawBoard2 = _interopRequireDefault(_DrawBoard);

var _ShowBoard = require('../components/draw/ShowBoard');

var _ShowBoard2 = _interopRequireDefault(_ShowBoard);

require('../components/draw/draw.less');

require('../components/draw/button.less');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

// import '../components/draw/font-awesome.min.css'

var Draw = function (_Component) {
    _inherits(Draw, _Component);

    function Draw(props) {
        _classCallCheck(this, Draw);

        var _this = _possibleConstructorReturn(this, (Draw.__proto__ || Object.getPrototypeOf(Draw)).call(this, props));

        _this.state = {
            player: 0
        };
        return _this;
    }

    _createClass(Draw, [{
        key: 'render',
        value: function render() {
            var _this2 = this;

            var renderNode = void 0;
            switch (this.state.player) {
                // 画者
                case 1:
                    renderNode = _react2.default.createElement(_DrawBoard2.default, {
                        ready: function ready() {
                            return io();
                        },
                        drawEnd: function drawEnd() {
                            console.log('stop');
                        } });
                    break;
                // 猜者
                case 2:
                    renderNode = _react2.default.createElement(_ShowBoard2.default, { ready: function ready() {
                            return io();
                        } });
                    break;
                default:
                    renderNode = _react2.default.createElement(
                        'div',
                        null,
                        _react2.default.createElement('img', { className: 'indexImg', src: './src/imgs/index.jpg', alt: '' }),
                        _react2.default.createElement(
                            'div',
                            { className: 'draw-wrap flex-box' },
                            _react2.default.createElement(
                                'a',
                                { onClick: function onClick() {
                                        return _this2.setState({ player: 1 });
                                    }, title: '\u6211\u6765\u753B', className: 'button button-3d button-primary button-pill' },
                                _react2.default.createElement(
                                    'i',
                                    { className: 'iconfont' },
                                    '\uE665'
                                ),
                                '\u6211\u6765\u753B'
                            ),
                            _react2.default.createElement(
                                'a',
                                { onClick: function onClick() {
                                        return _this2.setState({ player: 2 });
                                    }, title: '\u6211\u6765\u731C', className: 'button button-3d button-royal button-pill' },
                                _react2.default.createElement(
                                    'i',
                                    { className: 'iconfont' },
                                    '\uE628'
                                ),
                                '\u6211\u6765\u731C'
                            )
                        )
                    );
            }

            return _react2.default.createElement(
                'div',
                null,
                renderNode
            );
        }
    }]);

    return Draw;
}(_react.Component);

exports.default = Draw;
//# sourceMappingURL=App.js.map