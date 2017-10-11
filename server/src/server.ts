console.log('----- Starting Rational Backend -----');

//Load config
var config = require('./config.json');

//Load database
var Database = require('./database').Database;
var db = new Database(config.mysql);

var express = require('express');
var app = express();

var http = require('http').Server(app);

app.get('/', function(req, res) {
    res.send("Hello Daniel");
});

app.post('/api/fetchPrelimRatData', function(req, res) {
    db.getPrelimRatData().then(function (data) {
        res.send(JSON.stringify(data));
    }, function (err) {
        res.send(JSON.stringify({err: err}));
    });
});

http.listen(config.port, function () {
    console.log('Listening on *:' + config.port);
});
