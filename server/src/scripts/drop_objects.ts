import * as mysql from 'mysql';
import * as fs from 'fs';
import * as path from 'path';

import config from '../config/config';

const filePath = path.join(__dirname, '..', 'database', 'rat-drop-objects.sql')
const queries = fs.readFileSync(filePath).toString();

// console.log(queries);

// Allow multiple statements in a mysql query
config.mysql.multipleStatements = true;

let connection = mysql.createConnection(config.mysql);
connection.connect((err) => {
    if (err) {
        console.error('Could not connect with credentials ' + config.mysqlRoot + ': ' + err.stack);
        return;
    }
    console.log('Connected as ' + JSON.stringify(config.mysqlRoot));
});

console.log('Warning: dropping all data in database ratdb')

connection.beginTransaction((err) => {
    if (err) {
        throw err;
    }
    connection.query(queries, (err, result) => {
        if (err) {
            connection.rollback(() => {
                throw err;
            });  
        }

        console.log(result);
        connection.commit((err) => {
            if (err) {
                connection.rollback(() => {
                    throw err;
                })
            }
            console.log('Transaction complete');
            connection.end();
        });
    });


});

