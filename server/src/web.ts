import bodyParser = require("body-parser");
import express = require("express");
import socketio = require("socket.io");
import {Database} from "./database";
import {Lib} from "./lib";
import http = require('http');
let config = require('./config.json');

function sendObject(response) {
    return function (obj) {
        response.send(JSON.stringify(obj));
    };
}

export class Web {
    db: Database;
    httpServer: any;
    app: any;

    constructor(db: Database) {
        this.db = db;
        let app = express();
        this.app = app;
        let io = socketio(http);

        io.on('connection', function (socket) {
            socket.on('ping', function (data) {
                var keys = Object.keys(data);
                for (let i = 0; i < keys.length; i++) {
                    data[keys[i]] = data[keys[i]].toUpperCase();
                }
                socket.emit('pong', data);
            });
        });

        //Setup middleware
        app.use(bodyParser.urlencoded({extended: true}));
        app.use(bodyParser.json());

        //Create http server
        this.httpServer = new http.Server(app);

        //Create routes
        app.get('/', function(req, res) {
            res.send("Hello World");
        });

        app.post('/api/login', this.routeWithArgs(['email', 'password'], function (req, res) {
            let email = req.body.email;
            let password = req.body.password;

            let getUniqueSession = function (callback) {
                let newId = Lib.randomStr(32);
                db.getUserIdBySession(newId).then(function () {
                    getUniqueSession(callback);
                }, function () {
                    callback(newId);
                });
            };

            db.getUserByEmail(email).then(function (resp: any) {
                let user = resp.user;
                if (user.password == password) {
                    getUniqueSession(function (newSession) {
                        db.addUserSession(user.id, newSession, Lib.now() + config.sessionlength);
                        sendObject(res)({sessionid: newSession, permLevel: user.permLevel});
                    });
                } else {
                    sendObject(res)({err: 'Invalid credentials'});
                }
            }, sendObject(res));
        }));

        app.post('/api/register', this.routeWithArgs(['email', 'password'], function (req, res) {
            let email = req.body.email;
            let password = req.body.password;

            db.addUser({
                email: email,
                password: password,
                permLevel: 0
            }).then(function () {
                sendObject(res)({success: true});
            }, sendObject(res));
        }));

        app.post('/api/getRatSightings', this.routeAuthWithArgs(['startid', 'limit'], function (req, res, user) {
            db.getRatSightings(parseInt(req.body.startid), parseInt(req.body.limit)).then(sendObject(res), sendObject(res));
        }));

        app.post('/api/getRatSightingsAfter', this.routeAuthWithArgs(['startid'], function (req, res, user) {
            db.getRatSightingsAfter(parseInt(req.body.startid)).then(sendObject(res), sendObject(res));
        }));

        app.post('/api/postRatSightings', this.routeWithArgs(['created_date', 'locationType', 'incident_zip', 'incidentAddress', 'city', 'borough', 'latitude', 'longitude'], function(req, res) {
            let created_date = req.body.created_date;
            let locationType = req.body.locationType;
            let incident_zip = req.body.incident_zip;
            let incidentAddress = req.body.incidentAddress;
            let city = req.body.city;
            let borough = req.body.borough;
            let latitude = req.body.latitude;
            let longitude = req.body.longitude;

            db.addRatSighting({
                created_date: created_date,
                locationType: locationType,
                incident_zip: incident_zip,
                incidentAddress: incidentAddress,
                city: city,
                borough: borough,
                latitude: latitude,
                longitude: longitude
            }).then(function() {
                sendObject(res)({success: true});
            }, sendObject(res));
        }));
    }

    /**
     * Returns a route that will fail if the user does not provide the required arguments
     * @param requiredArgs An array of required arguments the user must post
     * @param callback A function (req, res) {}
     * @returns {(req:any, res:any)=>undefined}
     */
    private routeWithArgs(requiredArgs: Array<string>, callback) {
        return function (req, res) {
            let args = req.body;
            for (let i: number = 0; i < requiredArgs.length; i++) {
                if (!(requiredArgs[i] in args)) {
                    return sendObject(res)({err: "Required argument\"" + requiredArgs[i] + "\" not provided"});
                }
            }

            callback(req, res);
        };
    }

    /**
     * Returns a route that will fail if the user sends an invalid sessionid or does not provide the required arguments
     * @param requiredArgs An array of required arguments the user must post
     * @param callback A function (req, res, user) {}
     * @returns {(req:any, res:any)=>undefined}
     */
    private routeAuthWithArgs(requiredArgs: Array<string>, callback) {
        var _this = this;
        requiredArgs.unshift('sessionid');
        return this.routeWithArgs(requiredArgs, function (req, res) {
            var sessionid = req.body.sessionid;
            _this.db.getUserIdBySession(sessionid).then(function (resp: any) {
                _this.db.getUserById(resp.userid).then(function (resp: any) {
                    callback(req, res, resp.user);
                }, sendObject(res));
            }, function () {
                sendObject(res)({err: 'Invalid or expired session'});
            });
        });
    }

    start() {
        this.httpServer.listen(config.port, function () {
            console.log('Listening on *:' + config.port);
        });
    }
}
