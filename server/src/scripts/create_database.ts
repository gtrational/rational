import * as mysql from 'mysql';
import * as fs from 'fs';
import * as path from 'path';

import config from '../config/config';

const filePath = path.join(__dirname, '..', 'database', 'rat-create-db.sql')
const queries = fs.readFileSync(filePath).toString();

// console.log(queries);

// Allow multiple statements in a mysql query
config.mysqlRoot.multipleStatements = true;

let connection = mysql.createConnection(config.mysqlRoot);
connection.connect((err) => {
    if (err) {
        console.error('Could not connect with credentials ' + config.mysqlRoot + ': ' + err.stack);
        return;
    }
    console.log('Connected as ' + JSON.stringify(config.mysqlRoot));
});

connection.beginTransaction((err) => {
    if (err) {
        throw err;
    }
    console.log('Creating database ratdb and user ratuser')
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

