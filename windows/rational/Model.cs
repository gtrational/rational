using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace rational
{
    public class Model
    {
        public delegate void CallbackVoid();

        public static int PER_PAGE = 20;

        private static Model instance;

        public static Model GetInstance()
        {
            if (instance == null)
            {
                instance = new Model();
            }

            return instance;
        }

        public LoginResult User;

        //List from most recent to least recent
        public List<RatData> RatDataList;

        public Model()
        {
            RatDataList = new List<RatData>();
        }

        private int GetLastRatId()
        {
            if (RatDataList.Count == 0)
            {
                return 0;
            }

            return RatDataList[RatDataList.Count - 1].UniqueKey;
        }

        public void LoadMoreRats(CallbackVoid callback)
        {
            WebAPI.RatListCallback cb = resp =>
            {
                for (var i = 0; i < resp.Count; i++)
                {
                    RatDataList.Add(resp[i]);
                }

                callback();
            };

            WebAPI.GetRatSightings(GetLastRatId(), PER_PAGE, cb);
        }
    }
}
