import bodyParser = require("body-parser");
import express = require("express");
import {Database} from "./database";
import http = require('http');
let config = require('./config.json');

export class Web {
    httpServer: any;
    app: any;

    constructor(db: Database) {
        let app = express();
        this.app = app;

        //Setup middleware
        app.use(bodyParser.urlencoded({extended: true}));
        app.use(bodyParser.json());

        //Create http server
        this.httpServer = new http.Server(app);

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
    }

    start() {
        this.httpServer.listen(config.port, function () {
            console.log('Listening on *:' + config.port);
        });
    }
}