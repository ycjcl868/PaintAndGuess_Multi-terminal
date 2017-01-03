let path = require('path');
let webpack = require('webpack');
//定义了一些文件夹的路径
let ROOT_PATH = path.resolve(__dirname);
let APP_PATH = path.resolve(ROOT_PATH, 'app');
let BUILD_PATH = path.resolve(ROOT_PATH, 'public');
let ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
    entry: __dirname + '/app/main.js',
    output: {
        path: BUILD_PATH,
        filename: 'bundle.js'
    },

    module: {
        loaders: [
            {
                test : /\.(less|css)$/,
                loader: ExtractTextPlugin.extract('style', 'css!less')
            },
            {
                test: /\.jsx?$/,
                loader: 'babel',
                include: APP_PATH,
                resolve: {
                    extensions: ['', '.js', '.jsx']
                }
            }
        ]
    },
    postcss: [
        require('autoprefixer')//调用autoprefixer插件
    ],
    plugins: [
        //new webpack.optimize.UglifyJsPlugin(),
        //new webpack.optimize.CommonsChunkPlugin('common.js'),
        new ExtractTextPlugin('css/[name].css')
    ]
}