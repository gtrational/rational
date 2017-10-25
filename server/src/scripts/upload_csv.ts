//Load config
import config from '../config/config';
var fs = require('fs');

//Load database
var Database = require('./database').Database;
var db = new Database(config.mysql);

//Load csv lib
var csv = require('fast-csv');

function parseTime(str) {
    var date = new Date();

    var spl = str.split(' ');
    var spl2 = spl[0].split('/');
    date.setMonth(parseInt(spl2[0]) - 1);
    date.setDate(parseInt(spl2[1]));
    date.setFullYear(parseInt(spl2[2]));

    spl2 = spl[1].split(':');
    var hour = parseInt(spl2[0]);
    var min = parseInt(spl2[1]);
    var sec = parseInt(spl2[2]);
    if (spl[2] == 'PM' && hour < 12) {
        hour += 12;
    }
    if (spl[2] == 'AM' && hour == 12) {
        hour = 0;
    }
    date.setHours(hour);
    date.setMinutes(min);
    date.setSeconds(sec);
    date.setMilliseconds(0);
    return date.getTime();
}

var stream = fs.createReadStream('data/Rat_Sightings.csv');
var i = 0;
var max: number = 100649;
var lastPerc: string = '';

var proms: Array<any> = [];
var done: number = 0;

function doProm() {
    if (proms.length == 0) {
        console.log('Finished uploading to database!');
        return;
    }
    var cur: any = proms.splice(0, 1)[0];
    cur.then(function () {
        done++;
        var perc: string = (Math.floor(done * 10000 / max) / 100) + '';
        if (perc != lastPerc) {
            console.log(perc + '% uploaded to db');
            lastPerc = perc;
        }
        doProm();
    });
}

var csvStream = csv()
    .on("data", function (data) {
        if (i > 0) {
            var createdDate = data[1];
            var millis = parseTime(createdDate);
            var row = [data[0], millis, data[7], data[8], data[9], data[16], data[23], data[49], data[50]];
            proms.push(db.addRatSighting(row));
        }
        i++;
        var perc: string = Math.floor(i * 100 / max) + '';
        if (perc != lastPerc) {
            console.log(perc + '% read');
            lastPerc = perc;
        }
    })
    .on("end", function () {
        max = proms.length;
        doProm();
    });
stream.pipe(csvStream);
/*
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
 */