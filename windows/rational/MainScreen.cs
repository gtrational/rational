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
    public partial class MainScreen : Form
    {
        public MainScreen()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void listView1_SelectedIndexChanged(object sender, EventArgs e)
        {

        }

        private void menuStrip1_ItemClicked(object sender, ToolStripItemClickedEventArgs e)
        {

        }

        private void newSightingToolStripMenuItem_Click(object sender, EventArgs e)
        {
            NewSightingDialog dialog = new NewSightingDialog();
            dialog.Show();
        }

        private void listView1_SelectedIndexChanged_1(object sender, EventArgs e)
        {

        }

        private void refresh_data(object sender, TabControlCancelEventArgs e)
        {
            Console.WriteLine(tabControl1.SelectedIndex);
            if (tabControl1.SelectedIndex == 3)
            {

            }
        }
    }
}
