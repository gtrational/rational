const webpack = require('webpack');
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

const HtmlWebpackPluginConfig = new HtmlWebpackPlugin({
  template: './public/index.html',
  filename: 'index.html',
  inject: 'body'
})

const BUILD_DIR = path.resolve(__dirname, 'public/');
const APP_DIR = path.resolve(__dirname, 'src/');

module.exports = {
    entry: APP_DIR + '/index.jsx',
    output: {
        path: BUILD_DIR,
        filename: 'bundle.js'
    },
    module : {
        loaders : [
            {
                test : /\.(js|jsx|es6)$/,
                include : APP_DIR,
                loader : 'babel-loader'
            },
            {
                test : /\.(css)/,
                loaders: ['style-loader', 'css-loader'],
                include : APP_DIR
            },
            {
                test: /\.(svg|jpe?g|png|gif)$/i,
                loaders: [
    'file-loader?hash=sha512&digest=hex&name=[hash].[ext]',
    'image-webpack-loader?bypassOnDebug&optimizationLevel=7&interlaced=false'
]
,
                include : APP_DIR
            }
        ]
    },
	resolve: {
		extensions: ['*', '.js', '.jsx', '.es6']
	},
    plugins: [HtmlWebpackPluginConfig],
	target: 'web'
};
