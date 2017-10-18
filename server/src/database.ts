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

interface RatData {
    unique_key?: number;
    created_date: number;
    locationType: string;
    incident_zip: number;
    incidentAddress: string;
    city: string;
    borough: string;
    latitude: number;
    longitude: number;
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

    addRatSighting(ratData: RatData) {
        let query = 'INSERT INTO rat_sightings (created_date, location_type, incident_zip, incident_address, city, borough, latitude, longitude';
        let valueStr = 'VALUES (?, ?, ?, ?, ?, ?, ?, ?';
        let values: Array<string | number> = [ratData.created_date, ratData.locationType, ratData.incident_zip, ratData.incidentAddress, ratData.city, ratData.borough, ratData.latitude, ratData.longitude];
        if (ratData.unique_key) {
            query += ', unique_key';
            valueStr += ', ?';
            values.push(ratData.unique_key);
        }
        query += ') ' + valueStr + ')';

        return this.dbCall(query, values, function (results, resolve, reject) {
            resolve({success: true});
        });
    }

    /**
     * Returns The *limit* entries before startId, exclusive. If startId = 0, returns the last *limit* entries
     * @param startId The exclusive upper bound on the rat sightings to return
     * @param limit The number of rat sightings before startId to return
     * @returns {Promise<T>}
     */
    getRatSightings(startId: number, limit: number) {
        let query: string = 'SELECT * FROM rat_sightings';
        let values: Array<number> = [];
        if (startId > 0) {
            query += ' WHERE unique_key < ?';
            values.push(startId);
        }
        query += ' ORDER BY created_date DESC LIMIT ?';
        values.push(limit);

        return this.dbCall(query, values, function (results, resolve, reject) {
            resolve({ratData: results});
        });
    }
}