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
            error: 'Internal DB error'
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

export class Database {
    conn : IConnection;
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

    addRatSighting(values: Array<any>) {
        var _this = this;

        return new Promise(function (resolve, reject) {
            _this.conn.query('INSERT INTO rat_sightings (unique_key, created_date, location_type, incident_zip, incident_address, city, borough, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)', values, function (error, results, fields) {
                if (checkErrorCallback(error,  reject)) return;
                resolve({success: true});
            });
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