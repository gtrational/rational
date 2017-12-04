using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace rational
{
    public class Model
    {

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
    }
}
