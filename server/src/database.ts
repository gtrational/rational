var mysql = require('mysql');

function checkError(err) {
    if (err) {
        console.trace(err);
        return true;
    }
    return false;
}

function Database(conninfo) {
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

    _this.example = function (name, age) {
        return new Promise(function (resolve, reject) {
            _this.conn.query('SELECT * FROM users WHERE name=? AND age=?', [name, age], function (error, results, fields) {
                if (_this.checkErrorCallback(error,  reject)) return;
                if (results.length == 0) {
                    reject({error: 'User not found'});
                } else {
                    resolve({user: results[0]});
                }
            });
        });
    };
}

module.exports = {
    Database: Database
};