import * as fs from 'fs';

let defaultConfig = require('./config.json');

let configOverrides;

if (!fs.existsSync('./config-overrides.json')) {
    
    console.warn('There is no config-overrides file! If any configurations need to' 
    + ' be overriden, create file: ' + defaultConfig.configDir + 'config-overrides.json');

    configOverrides = {};

} else {
    configOverrides = require('./config-overrides.json');
}

/**
 * 
 * 
 * @param config 
 * @param overrides 
 * @param keyStr 
 */
function recurseOverride(config, overrides, keyStr) {
    for (let key in overrides) {
        const newKeyStr = keyStr === '' ? key : keyStr + '.' + key;

        if (!(key in config)) {
            console.warn('Key "' + newKeyStr + '" in config-overrides not in config. Overwriting anyways...');
            config[key] = overrides[key];
        } else {
            if (typeof overrides[key] === 'object') {
                if (overrides[key] === null || overrides[key] instanceof Array) {
                    config[key] = overrides[key];
                } else {
                    recurseOverride(config[key], overrides[key], newKeyStr);
                }
            } else {
                config[key] = overrides[key];
            }
        }

    }   
}

recurseOverride(defaultConfig, configOverrides, '');


module.exports.config = defaultConfig;