using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using rational;

namespace WindowsFormsApp1
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            WebAPI.LoginCallback cb = resp =>
            {
                Model.GetInstance().User = resp;
                Console.WriteLine("Success: " + resp.Success);
                Console.WriteLine("SessionID: " + resp.SessionID);

                WebAPI.RatListCallback cb2 = resp2 =>
                {
                    for (var i = 0; i < resp2.Count; i++)
                    {
                        Console.WriteLine(resp2[i].UniqueKey);
                    }
                };

                WebAPI.GetRatSightings(0, 20, cb2);
            };

            WebAPI.Login("testuser", "testpass", cb);

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new MainScreen());
        }
    }
}
