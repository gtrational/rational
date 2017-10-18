console.log('----- Starting Rational Backend -----');

//Load config
let config = require('./config.json');

//Load modules
import {Database} from "./database"
import {Web} from "./web";
import {Lib} from "./lib";

//Init modules
let db = new Database(config.mysql);
let web = new Web(db);

//Expire old sessions
setInterval(function () {
    let now = Lib.now();
    db.getExpiredSessions(now).then(function (resp: any) {
        let sessions = resp.sessions;
        for (let i = 0; i < sessions.length; i++) {
            db.destroySession(sessions[i].id);
        }
    });
}, 1000);

//Start web
web.start();