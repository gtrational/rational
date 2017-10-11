//Load config
var config = require('./config.json');
var fs = require('fs');

//Load database
var Database = require('./database').Database;
var db = new Database(config.mysql);

fs.readFile('/home/shyamal/Downloads/Rat_Sightings.csv', 'utf8', function(err, data) {
    if (err) throw err;
    data = data.replace(" /g", "");
    data = data.split("\n");
    data.forEach(function(row) {
        if (row.length != 0) {
            row = row.split(",");
            row = [row[0], row[1], row[7], row[8], row[9], row[16], row[23], row[49], row[50]];
            db.addRatSighting(row);
        }
    });
});
