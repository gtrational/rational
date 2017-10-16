import * as mysql from "mysql";
import {IConnection} from "mysql";

function checkError(err) {
    if (err) {
        console.trace(err);
        return true;
    }
    return false;
}

function checkErrorCallback(error, callback) {
    if (checkError(error)) {
        callback({
            err: 'Internal DB error'
        });
        return true;
    }
    return false;
}

interface ConnInfo {
    host: string;
    user: string;
    password: string;
    database: string;
    reconnectdelay: number;
}

interface UserInfo {
    email: string;
    password: string;
    permLevel: number;
}

export class Database {
    conn: IConnection;
    conninfo: ConnInfo;

    constructor(conninfo: ConnInfo) {
        this.conninfo = conninfo;
        this.connect();
    }

    connect() {
        var _this = this;

        _this.conn = mysql.createConnection(_this.conninfo);

        _this.conn.on('error', function (err) {
            console.trace(err);
            setTimeout(_this.connect, _this.conninfo.reconnectdelay);
        });

        _this.conn.connect(function (err) {
            if (err) {
                console.trace(err);
                setTimeout(_this.connect, _this.conninfo.reconnectdelay);
            } else {
                console.log('Connected to database');
            }
        });
    }

    private dbCall(query, values, callback) {
        var _this = this;

        return new Promise(function (resolve, reject) {
            _this.conn.query(query, values, function (error, results) {
                if (checkErrorCallback(error, reject)) return;

                callback(results, resolve, reject);
            });
        });
    }

    getUserByEmail(email: string) {
        return this.dbCall('SELECT * FROM users WHERE email=?', [email], function (results, resolve, reject) {
            if (results.length == 0) {
                return reject({err: 'User not found'});
            }

            resolve({user: results[0]});
        });
    }

    getUserById(id: number) {
        return this.dbCall('SELECT * FROM users WHERE id=?', [id], function (results, resolve, reject) {
            if (results.length == 0) {
                return reject({err: 'User not found'});
            }

            resolve({user: results[0]});
        });
    }

    addUser(info: UserInfo) {
        var _this = this;

        return new Promise(function (resolve, reject) {
            _this.getUserByEmail(info.email).then(function () {
                reject({err: 'User already registered with this email'});
            }, function () {
                _this.dbCall('INSERT INTO users(email, password, permLevel) VALUES(?, ?, ?)', [info.email, info.password, info.permLevel], function (results, resolve, reject) {
                    resolve({userId: results.insertId});
                }).then(resolve, reject);
            });
        });
    }

    getUserIdBySession(sessionid: string) {
        return this.dbCall('SELECT * FROM usersessions WHERE sessionid=?', [sessionid], function (results, resolve, reject) {
            if (results.length == 0) {
                return reject({err: 'User not found'});
            }

            resolve({userid: results[0].userid});
        });
    }

    addUserSession(userid: number, sessionid: string, expires: number) {
        return this.dbCall('INSERT INTO usersessions(userid, sessionid, expires) VALUES(?, ?, ?)', [userid, sessionid, expires], function (results, resolve, reject) {
            resolve({success: true});
        });
    }

    getExpiredSessions(curtime: number) {
        return this.dbCall('SELECT * FROM usersessions WHERE expires < ?', [curtime], function (results, resolve, reject) {
            resolve({sessions: results});
        });
    }

    destroySession(id: number) {
        return this.dbCall('DELETE FROM usersessions WHERE id=?', [id], function (results, resolve, reject) {
            resolve({success: true});
        });
    }

    addRatSighting(values: Array<any>) {
        return this.dbCall('INSERT INTO rat_sightings (unique_key, created_date, location_type, incident_zip, incident_address, city, borough, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)', values, function (results, resolve, reject) {
            resolve({success: true});
        });
    }

    getPrelimRatData() {
        var _this = this;

        return new Promise(function (resolve, reject) {
            _this.conn.query('SELECT * FROM rat_sightings LIMIT 20', [], function (error, results, fields) {
                if (checkErrorCallback(error, reject)) return;
                resolve({ratData: results});
            });
        });
    }
}