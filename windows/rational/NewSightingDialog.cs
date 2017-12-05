using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp1
{
    public partial class NewSightingDialog : Form
    {
        public NewSightingDialog()
        {
            InitializeComponent();
        }

        private void NewSightingDialog_Load(object sender, EventArgs e)
        {

        }

        private void Form_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Escape)
            {
                this.Close();
            }
        }

        private void button1_Click(object sender, EventArgs e)
        { 
            String locationType = comboBox1.Text;
            String addressLine1 = textBox1.Text;
            String addressLine2 = textBox2.Text;
            String addressCity = textBox3.Text;
            String addressState = textBox4.Text;
            String addressZIP = textBox5.Text;
            String borough = comboBox2.Text;

            List<String> errorList = new List<String>();

            if (string.IsNullOrEmpty(locationType))
            {
                errorList.Add("Location Type");
            }

            if (string.IsNullOrEmpty(addressLine1) || addressLine1 == "Line 1")
            {
                errorList.Add("Address (Line 1)");
            }

            if (string.IsNullOrEmpty(addressCity) || addressCity == "City")
            {
                errorList.Add("City");
            }

            if (string.IsNullOrEmpty(addressState) || addressState == "State")
            {
                errorList.Add("State");
            }

            if (string.IsNullOrEmpty(addressZIP) || !int.TryParse(addressZIP, out int n))
            {
                errorList.Add("ZIP Code");
            }

            if (string.IsNullOrEmpty(borough))
            {
                errorList.Add("Borough");
            }

            if (errorList.Count == 0)
            {
                // Process thing
                Console.WriteLine("YAY!!!");
                this.Close();
            }
            else
            {
                String allMissing = string.Join("\n", errorList.ToArray());
                DialogResult res = MessageBox.Show("Please check the following fields: \n" + allMissing, "Input Error");
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}
