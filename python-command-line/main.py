import webapi
from model import Model
import time

theModel = Model()
webapi.theModel = theModel


def now():
    return int(round(time.time() * 1000))


def loginMode():
    inp = int(input("Press 1 to login or 2 to register"))
    if inp == 1:
        username = input("Username:")
        password = input("Password:")
        resp = webapi.login(username, password)
        if resp.success:
            print("Login Successful!")
            theModel.sessionID = resp.sessionId
        else:
            print("Error: {}".format(resp.errMsg))
    elif inp == 2:
        username = input("Username:")
        password = input("Password:")
        cpassword = input("Confirm Password:")
        if password != cpassword:
            print("Passwords do not match")
            return
        resp = webapi.register(username, password)
        if resp.success:
            print("Registration Successful! Now login")
        else:
            print("Error: {}".format(resp.errMsg))


def loggedInMode():
    inp = int(input("Main Menu\n1) View Rats\n2) I see a rat!\n3) Logout"))
    if inp == 1:
        viewRatMode()
    elif inp == 2:
        addRatMode()
    elif inp == 3:
        theModel.sessionID = ""
        print("Logged out Successfully!")


def viewRatMode():
    curPage = 0

    while True:
        try:
            rats = theModel.getPage(curPage)
            theString = ""
            cur = 0
            alpha = 'abcdefghijklmnopqrstuvwxyz'
            for rat in rats:
                theString += "{}) {}: {}\n".format(alpha[cur], rat['unique_key'], rat['borough'])
                cur += 1
            print("Page {}:\n{}".format(curPage + 1, theString))
            rawInp = input("Type in a rat's letter to view more information!\n1) Refresh\n"
                           "2) Previous Page\n3) Next Page\n4) Back to Main Menu")
            idx = alpha.find(rawInp)
            if idx >= 0:
                rat = rats[idx]
                print("Unique Key: {}\nCreated Date: {}\nLocation Type: {}\nZip Code: {}\nAddress: {}\nCity: {}\nBorough: {}\nLatitude: {}\nLongitude: {}".format(rat['unique_key'], time.strftime('%x %X', time.gmtime(rat['created_date'] / 1000.0)), rat['location_type'], rat['incident_zip'], rat['incident_address'], rat['city'], rat['borough'], rat['latitude'], rat['longitude']))
            else:
                inp = int(rawInp)
                if inp == 1:
                    continue
                elif inp == 2:
                    curPage = max(curPage - 1, 0)
                elif inp == 3:
                    curPage += 1
                elif inp == 4:
                    return
        except Exception as e:
            print("{}".format(e))
            continue


def addRatMode():
    ratData = {"created_date": now(), "locationType": input("Location Type:"), "incident_zip": input("Zip Code:"),
               "incidentAddress": input("Address:"), "city": input("City:"), "borough": input("Borough:"),
               "latitude": 0, "longitude": 0}
    webapi.addRatSighting(ratData)
    print("Added successfully!")


print("Welcome to Rational v3.10.4!")

while True:
    try:
        if len(theModel.sessionID) == 0:
            loginMode()
        else:
            loggedInMode()
    except:
        continue
