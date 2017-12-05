import request from 'superagent';

const serverUrl = "http://rational.tk:80";

function post(endpoint, data) {
    return request
        .post(serverUrl + endpoint)
        .set('Content-Type', 'application/json')
        .send(data);
}

export function register(email, password, permissionLevel) {
    const loginRequest = {email: email, password: password, permLevel: permissionLevel};
    return new Promise(function (resolve, reject) {
        post("/api/register", loginRequest)
            .then(function (response) {
                if (response == null) {
                    let registerResult = {success: false, err: "No server response"};
                    reject(registerResult);
                    return;
                }
                let result = JSON.parse(response.text);
                if (result.hasOwnProperty('err')) {
                    let registerResult = {success: false, err: result['err']};
                    reject(registerResult);
                } else {
                    let registerResult = {success: true, err: null};
                    resolve(registerResult);
                }
            }, function (err) {
                console.log(err);
            });
    });
}

export function login(email, password) {
    const loginRequest = {email: email, password: password};
    return new Promise(function (resolve, reject) {
        post("/api/login", loginRequest)
            .then(function (response) {
                if (response == null) {
                    let loginResult = {
                        success: false,
                        err: "No server response",
                        sessionID: null,
                        permissionLevelOrdinal: 0
                    };
                    reject(loginResult);
                    return;
                }
                let result = JSON.parse(response.text);
                if (result.hasOwnProperty('err')) {
                    let loginResult = {success: false, err: result.err, sessionID: null, permissionLevelOrdinal: 0};
                    reject(loginResult);
                } else {
                    let loginResult = {
                        success: true,
                        err: null,
                        sessionID: result.sessionid,
                        permissionLevelOrdinal: result.permLevel
                    };
                    localStorage.sessionID = loginResult.sessionID;
                    localStorage.permLevel = loginResult.permissionLevelOrdinal;
                    localStorage.email = email;
                    resolve(loginResult);
                }
            }, function (err) {
                console.log(err);
            });
    });
}

export function addRatSighting(ratData) {
    let copy = {};
    Object.assign(copy, ratData);
    copy.sessionId = localStorage.sessionId;
    return new Promise(function (resolve, reject) {
        post("/api/postRatSightings", copy)
            .then(function () {
                resolve({success: true});
            }, function () {
                reject({success: false});
            });
    });
}

export function getRatSightingsAfter(start_id) {
    let request = {sessionid: localStorage.sessionID, startid: start_id};
    return new Promise(function (resolve, reject) {
        post("/api/getRatSightingsAfter", request)
            .then(function (response) {
                    if (response == null) {
                        let result = {ratData: null, success: false};
                        reject(result);
                        return;
                    }
                    let result = JSON.parse(response.text);
                    result.success = true;
                    resolve(result)
                }, function (err) {
                    console.log(err)
                }
            );
    });
}

export function getRatSightings(start_id, limit) {
    let request = {sessionid: localStorage.sessionID, startid: start_id, limit: limit};
    return new Promise(function (resolve, reject) {
        post("/api/getRatSightings", request)
            .then(function (response) {
                if (response == null) {
                    let result = {ratData: null, success: false};
                    reject(result);
                    return
                }
                let result = JSON.parse(response.text);
                result.success = true;
                resolve(result);
            }, function (err) {
                console.log(err);
            });
    });
}
