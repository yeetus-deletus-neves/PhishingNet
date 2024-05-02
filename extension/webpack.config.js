const CopyPlugin = require("copy-webpack-plugin");
const path = require('path');
const NodePolyfillPlugin = require("node-polyfill-webpack-plugin");

module.exports = {
    mode: 'production',
    entry: {
        react: './src/index.jsx',
    },
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: '[name].js',
        clean: true
    },
    plugins: [
        new CopyPlugin({
            patterns: [{
                from: path.resolve('static')
            }]
        }),
        new NodePolyfillPlugin()
    ],
    module: {
        rules: [
            {
                test: /.(js|jsx)$/,
                exclude: /node-modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: [
                            '@babel/preset-env',
                            ['@babel/preset-react',{'runtime':'automatic'}]
                        ]
                    }
                }
            }
        ]
    },
    resolve: {
        extensions: ['.js','.jsx']
    }
};


