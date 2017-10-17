import * as fs from 'fs';

let default_config = require('./config.json');

if (!fs.existsSync('./config-overrides.json')) {
    fs.writeFileSync('./config-overrides.json', default_config.config_overrides_defaults);
}

let config_overrides = require('./config-overrides.json');
