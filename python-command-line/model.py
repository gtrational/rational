import webapi


class Model:
    def __init__(self):
        self.sessionID = ""
        self.ratSightings = []

    def getFirstRatId(self):
        if len(self.ratSightings) == 0:
            return 0
        else:
            return self.ratSightings[0]['unique_key']

    def getLastRatId(self):
        if len(self.ratSightings) == 0:
            return 0
        else:
            return self.ratSightings[len(self.ratSightings) - 1]['unique_key']

    def fetchNew(self):
        awRats = webapi.getRatSightingsAfter(self.getFirstRatId())
        self.ratSightings = awRats + self.ratSightings

    def fetchOld(self):
        awRats = webapi.getRatSightings(self.getLastRatId(), 20)
        self.ratSightings = self.ratSightings + awRats

    def getPage(self, page):
        low = page * 20  # inclusive
        high = low + 20  # exclusive

        while high > len(self.ratSightings):
            self.fetchOld()

        self.fetchNew()

        return self.ratSightings[low:high]
