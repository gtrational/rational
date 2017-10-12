var mysql = require('mysql');

function checkError(err) {
    if (err) {
        console.trace(err);
        return true;
    }
    return false;
}

function db_Database(conninfo) {
    var _this = this;

    _this.connect = function () {
        _this.conn = mysql.createConnection(conninfo);

        _this.conn.on('error', function (err) {
            console.trace(err);
            setTimeout(_this.connect, conninfo.reconnectdelay);
        });

        _this.conn.connect(function (err) {
            if (err) {
                console.trace(err);
                setTimeout(_this.connect, conninfo.reconnectdelay);
            } else {
                console.log('Connected to database');
            }
        });
    };
    _this.connect();

    _this.checkErrorCallback = function (error, callback) {
        if (checkError(error)) {
            callback({
                error: 'Internal DB error'
            });
            return true;
        }
        return false;
    };

    _this.addRatSighting = function (values) {
        return new Promise(function (resolve, reject) {
            _this.conn.query('INSERT INTO rat_sightings (unique_key, created_date, location_type, incident_zip, incident_address, city, borough, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)', values, function (error, results, fields) {
                if (_this.checkErrorCallback(error,  reject)) return;
                resolve({success: true});
            });
        });
    };

    _this.getPrelimRatData = function () {
        return new Promise(function (resolve, reject) {
            _this.conn.query('SELECT * FROM rat_sightings LIMIT 20', [], function (error, results, fields) {
                if (_this.checkErrorCallback(error, reject)) return;
                resolve({ratData: results});
            });
        });
    };
}

module.exports = {
    Database: db_Database
};
