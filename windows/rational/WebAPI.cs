using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Http;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Dynamic;

namespace rational
{
    public class LoginResult
    {
        public readonly bool Success;
        public readonly string ErrMsg;
        public readonly string SessionID;
        public readonly int PermissionLevel;

        public LoginResult(dynamic json)
        {
            if (json == null)
            {
                Success = false;
                ErrMsg = "No server response";
            }
            else if (WebAPI.HasProperty(json, "err"))
            {
                Success = false;
                ErrMsg = json.err;
            }
            else
            {
                Success = true;
                SessionID = json.sessionid;
                PermissionLevel = json.permLevel;
            }
        }
    }

    public class BooleanResult
    {
        public readonly bool Success;
        public readonly string ErrMsg;

        public BooleanResult(dynamic json)
        {
            if (json == null)
            {
                Success = false;
                ErrMsg = "No server response";
            }
            else if (WebAPI.HasProperty(json, "err"))
            {
                Success = false;
                ErrMsg = json.err;
            }
            else
            {
                Success = true;
            }
        }
    }

    public class RatData
    {
        public int UniqueKey;
        public long CreatedTime;
        public string LocationType;
        public int IncidentZip;
        public string IncidentAddress;
        public string City;
        public string Borough;
        public double Latitude;
        public double Longitude;

        public RatData(dynamic json)
        {
            UniqueKey = json.unique_key;
            CreatedTime = json.created_date;
            LocationType = json.location_type;
            IncidentZip = json.incident_zip;
            IncidentAddress = json.incident_address;
            City = json.city;
            Borough = json.borough;
            Latitude = json.latitude;
            Longitude = json.longitude;
        }

        public RatData(int UniqueKey,
            long CreatedTime,
            string LocationType,
            int IncidentZip,
            string IncidentAddress,
            string City,
            string Borough,
            double Latitude,
            double Longitude)
        {
            this.UniqueKey = UniqueKey;
            this.CreatedTime = CreatedTime;
            this.LocationType = LocationType;
            this.IncidentZip = IncidentZip;
            this.IncidentAddress = IncidentAddress;
            this.City = City;
            this.Borough = Borough;
            this.Latitude = Latitude;
            this.Longitude = Longitude;
        }

        public dynamic ToJson()
        {
            dynamic json = new ExpandoObject();
            json.unique_key = UniqueKey;
            json.created_date = CreatedTime;
            json.locationType = LocationType;
            json.incident_zip = IncidentZip;
            json.incidentAddress = IncidentAddress;
            json.city = City;
            json.borough = Borough;
            json.latitude = Latitude;
            json.longitude = Longitude;
            return json;
        }
    }

    public class WebAPI
    {
        private static readonly string baseUrl = "http://localhost:8081";

        private static readonly HttpClient client = new HttpClient();

        public delegate void Callback(dynamic resp);
        public delegate void LoginCallback(LoginResult result);
        public delegate void BooleanCallback(BooleanResult result);
        public delegate void RatListCallback(List<RatData> result);

        private static dynamic ParseJSON(string json)
        {
            return JsonConvert.DeserializeObject<dynamic>(json);
        }

        private static string Stringify(dynamic obj)
        {
            return JsonConvert.SerializeObject(obj);
        }

        public static bool HasProperty(dynamic settings, string name)
        {
            if (settings is JObject)
            {
                JToken val;
                return ((JObject)settings).TryGetValue(name, out val);
            }
            if (settings == null)
                return false;
            if (settings is ExpandoObject)
                return ((IDictionary<string, object>)settings).ContainsKey(name);

            return settings.GetType().GetProperty(name) != null;
        }

        public static long Now()
        {
            return DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond;
        }

        private static async void PostRequest(string endpoint, dynamic args, Callback callback)
        {
            var content = new ByteArrayContent(Encoding.UTF8.GetBytes(Stringify(args)));

            content.Headers.Add("Content-Type", "application/json");
            var response = await client.PostAsync(baseUrl + endpoint, content);

            var responseString = await response.Content.ReadAsStringAsync();

            Console.WriteLine("Resp: " + responseString);

            callback(ParseJSON(responseString));
        }

        public static void Login(string email, string password, LoginCallback callback)
        {
            dynamic args = new ExpandoObject();
            args.email = email;
            args.password = password;

            Callback cb = resp =>
            {
                callback(new LoginResult(resp));
            };

            PostRequest("/api/login", args, cb);
        }

        public static void Register(string email, string password, int permLevel, BooleanCallback callback)
        {
            dynamic args = new ExpandoObject();
            args.email = email;
            args.password = password;
            args.permLevel = permLevel;

            Callback cb = resp =>
            {
                callback(new BooleanResult(resp));
            };

            PostRequest("/api/register", args, cb);
        }

        public static void AddRatSighting(RatData ratData, BooleanCallback callback)
        {
            dynamic args = ratData.ToJson();
            args.sessionid = Model.GetInstance().User.SessionID;

            Callback cb = resp =>
            {
                callback(new BooleanResult(resp));
            };

            PostRequest("/api/postRatSightings", args, cb);
        }

        public static void GetRatSightingsAfter(int start_id, RatListCallback callback)
        {
            dynamic args = new ExpandoObject();
            args.sessionid = Model.GetInstance().User.SessionID;
            args.startid = start_id;

            Callback cb = resp =>
            {
                List<RatData> result = new List<RatData>();
                JArray arr = (JArray) resp.ratData;
                for (var i = 0; i < arr.Count; i++)
                {
                    result.Add(new RatData(arr[i]));
                }
                callback(result);
            };

            PostRequest("/api/getRatSightingsAfter", args, cb);
        }

        public static void GetRatSightings(int startId, int limit, RatListCallback callback)
        {
            dynamic args = new ExpandoObject();
            args.sessionid = Model.GetInstance().User.SessionID;
            args.startid = startId;
            args.limit = limit;

            Callback cb = resp =>
            {
                List<RatData> result = new List<RatData>();
                JArray arr = (JArray)resp.ratData;
                for (var i = 0; i < arr.Count; i++)
                {
                    result.Add(new RatData(arr[i]));
                }
                callback(result);
            };

            PostRequest("/api/getRatSightings", args, cb);
        }
    }
}
