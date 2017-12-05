import requests
from model import Model

baseUrl = "http://rational.tk"
theModel = Model()


def postreq(endpoint, args):
    #print("Calling {} with args {}".format(endpoint, args))
    r = requests.post(baseUrl + endpoint, args)
    resp = r.json()
    #print("Got {}".format(resp))
    return resp


class LoginResp:
    def __init__(self, success, errMsg, sessionId):
        self.success = success
        self.errMsg = errMsg
        self.sessionId = sessionId


class BooleanResp:
    def __init__(self, success, errMsg):
        self.success = success
        self.errMsg = errMsg


def parseBool(r):
    if 'err' in r:
        return BooleanResp(False, r['err'])
    else:
        return BooleanResp(True, None)


def login(email, password):
    r = postreq('/api/login', {
        "email": email,
        "password": password
    })

    if 'err' in r:
        return LoginResp(False, r['err'], None)
    else:
        return LoginResp(True, None, r['sessionid'])


def register(email, password):
    return parseBool(postreq('/api/register', {
        "email": email,
        "password": password
    }))


def addRatSighting(ratData):
    ratData["sessionid"] = theModel.sessionID
    return parseBool(postreq('/api/postRatSightings', ratData))


def getRatSightingsAfter(startId):
    r = postreq('/api/getRatSightingsAfter', {
        "sessionid": theModel.sessionID,
        "startid": startId
    })
    return r['ratData']


def getRatSightings(startId, limit):
    r = postreq('/api/getRatSightings', {
        "sessionid": theModel.sessionID,
        "startid": startId,
        "limit": limit
    })
    return r['ratData']