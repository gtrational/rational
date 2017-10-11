console.log('----- Starting Rational Backend -----');

//Load config
var config = require('./config.json');

var express = require('express');
var app = express();

var http = require('http').Server(app);

app.get('/', function(req, res) {
    res.send("Hello Daniel");
});

http.listen(config.port, function () {
    console.log('Listening on *:' + config.port);
});
