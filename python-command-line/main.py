import webapi
from model import Model

theModel = Model()
webapi.theModel = theModel


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
        return
    elif inp == 3:
        theModel.sessionID = ""
        print("Logged out Successfully!")


def viewRatMode():
    curPage = 0

    while True:
        try:
            rats = theModel.getPage(curPage)
            theString = ""
            for rat in rats:
                theString += "{}: {}\n".format(rat['unique_key'], rat['borough'])
            print("Page {}:\n{}".format(curPage + 1, theString))
            inp = int(input("View Rats\n1) Refresh\n2) Previous Page\n3) Next Page\n4) Back to Main Menu"))
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


print("Welcome to Rational v3.10.4!")

while True:
    try:
        if len(theModel.sessionID) == 0:
            loginMode()
        else:
            loggedInMode()
    except:
        continue
