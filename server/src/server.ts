console.log('----- Starting Rational Backend -----');

//Load config
var config = require('./config.json');

//Load modules
var lib = require('./lib').lib;

//Load database
var db;
(function () {
    var Database = require('./database').Database;
    db = new Database(config.mysql);
})();

//Init web server
var web;
(function () {
    var app = require('express')();

    //Setup middleware
    var bodyParser = require('body-parser');
    app.use(bodyParser.urlencoded({extended: true}));
    app.use(bodyParser.json());

    //Create http server
    var http = require('http').Server(app);

    //Create routes
    app.get('/', function(req, res) {
        res.send("Hello World");
    });

    app.post('/api/fetchPrelimRatData', function(req, res) {
        db.getPrelimRatData().then(function (data) {
            res.send(JSON.stringify(data));
        }, function (err) {
            res.send(JSON.stringify({err: err}));
        });
    });

    web = {
        start: function () {
            http.listen(config.port, function () {
                console.log('Listening on *:' + config.port);
            });
        },
        app: app
    }
})();

//Handle login & register
var token;
(function () {
    var User = function (username, password, permLevel) {
        var _this = this;

        _this.username = username;
        _this.password = password;
        _this.permLevel = permLevel;
        _this.sessions = [];

        _this.newToken = function () {
            var code;
            do {
                code = lib.randomStr(20);
            } while (getByToken(code));
            _this.sessions.push(code);
            return code;
        };
    };

    var users = [];

    var getByUsername = function (username) {
        for (var i = 0; i < users.length; i++) {
            if (users[i].username == username) {
                return users[i];
            }
        }
    };

    var getByToken = function (token) {
        for (var i = 0; i < users.length; i++) {
            if (users[i].sessions.indexOf(token) >= 0) {
                return users[i];
            }
        }
    };

    web.app.post('/api/login', function (req, res) {
        var resolve = function (obj) {
            res.send(JSON.stringify(obj));
        };

        var user = getByUsername(req.body.username);
        if (!user) {
            return resolve({err: 'User not found'});
        }

        var pass = req.body.password;

        if (user.password != pass) {
            return resolve({err: 'Invalid credentials'});
        }

        resolve({sessionID: user.newToken()});
    });

    web.app.post('/api/register', function (req, res) {
        var resolve = function (obj) {
            res.send(JSON.stringify(obj));
        };

        if (getByUsername(req.body.username)) {
            return resolve({err: 'Username taken'});
        }

        var user = new User(req.body.username, req.body.password, req.body.permLevel);
        users.push(user);

        resolve({success: true});
    });

    token = {
        verify: function (token) {
            var user = getByToken(token);
            return !!user;
        }
    };
})();

//Start web
web.start();