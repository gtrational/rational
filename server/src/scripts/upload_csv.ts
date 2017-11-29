//Load config
import config from '../config/config';
var fs = require('fs');

//Load database
import {Database, RatData} from '../database';
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

var stream = fs.createReadStream('/home/daniel/Downloads/Rat_Sightings.csv');
var i = 0;
var max: number = 100639;
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
        var perc: string = Math.floor(done * 100 / max) + '';
        if (perc != lastPerc) {
            console.log(perc + '% uploaded to db');
            lastPerc = perc;
        }
        doProm();
    });
}

let csvStream = csv()
    .on("data", function (data) {
        if (i > 0) {
            let createdDate = data[1];
            let millis = parseTime(createdDate);
            let row: RatData = {
                unique_key: data[0], 
                created_date: millis, 
                locationType: data[7], 
                incident_zip: data[8], 
                incidentAddress: data[9], 
                city: data[16], 
                borough: data[23], 
                latitude: data[49], 
                longitude: data[50]
            };
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