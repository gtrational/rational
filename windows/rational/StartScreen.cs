using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace rational
{
    public partial class StartScreen : Form
    {
        private MainScreen mainScreen;

        public StartScreen()
        {
            InitializeComponent();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            WebAPI.LoginCallback cb = resp =>
            {
                Model.GetInstance().User = resp;
                if (resp.Success)
                {
                    Hide();
                    mainScreen = new MainScreen();
                    mainScreen.Show();
                }
                else
                {
                    MessageBox.Show("Login Failed: " + resp.ErrMsg);
                }
            };

            WebAPI.Login(textBox1.Text, textBox2.Text, cb);
        }

        private void button2_Click(object sender, EventArgs e)
        {
            string username = textBox4.Text;
            string password = textBox5.Text;
            string cpassword = textBox3.Text;

            if (password.Equals(cpassword))
            {
                WebAPI.BooleanCallback cb = resp =>
                {
                    if (resp.Success)
                    {
                        MessageBox.Show("Registration Success! Please login to continue");
                    }
                    else
                    {
                        MessageBox.Show("Registration Failed: " + resp.ErrMsg);
                    }
                };

                WebAPI.Register(username, password, 0, cb);
            }
            else
            {
                MessageBox.Show("Passwords do not match");
            }
        }

        private void panel3_Paint(object sender, PaintEventArgs e)
        {

        }
    }
}
