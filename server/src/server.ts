console.log('----- Starting Rational Backend -----');

//Load config
let config = require('./config.json');

//Load modules
import {Lib} from "./lib";
import {Database} from "./database"
import {Web} from "./web";

//Init modules
let lib = new Lib();
let db = new Database(config.mysql);
let web = new Web(db);

//Handle login & register
var token;
(function () {
    class User {
        username: string;
        password: string;
        permLevel: string;
        sessions: Array<string>;

        constructor(username: string, password: string, permLevel: string) {
            this.username = username;
            this.password = password;
            this.permLevel = permLevel;
            this.sessions = [];
        }

        newToken(): string {
            let code: string;
            do {
                code = lib.randomStr(20);
            } while (getByToken(code));
            this.sessions.push(code);
            return code;
        }
    }

    var users: Array<User> = [];

    var getByUsername = function (username) {
        for (let i = 0; i < users.length; i++) {
            if (users[i].username == username) {
                return users[i];
            }
        }
    };

    var getByToken = function (token) {
        for (let i = 0; i < users.length; i++) {
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