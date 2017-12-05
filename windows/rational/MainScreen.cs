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
    public partial class MainScreen : Form
    {
        public MainScreen()
        {
            InitializeComponent();
        }

        private string ParseTime(long time)
        {
            return time + "";
        }

        private void RefreshView()
        {
            listView1.Items.Clear();

            List<RatData> rats = Model.GetInstance().RatDataList;

            for (var i = 0; i < rats.Count; i++)
            {
                RatData rat = rats[i];
                var item = new ListViewItem(new string[] { ParseTime(rat.CreatedTime), rat.UniqueKey + "", rat.Borough });
                listView1.Items.Add(item);
            }

            listView1.Items[listView1.Items.Count - 1].EnsureVisible();
        }

        private void LoadMoreOld()
        {
            Model.CallbackVoid cb = () =>
            {
                RefreshView();
            };

            Model.GetInstance().LoadMoreRats(cb);
        }

        private void LoadNew()
        {
            Model.CallbackVoid cb = () =>
            {
                RefreshView();
            };

            Model.GetInstance().LoadNewRats(cb);
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            LoadMoreOld();
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

            dialog.FormClosed += Dialog_FormClosed;

            dialog.Show();
        }

        private void Dialog_FormClosed(object sender, FormClosedEventArgs e)
        {
            LoadNew();
        }

        private void listView1_SelectedIndexChanged_1(object sender, EventArgs e)
        {

        }

        private void refresh_data(object sender, TabControlCancelEventArgs e)
        {
            Console.WriteLine(tabControl1.SelectedIndex);
            if (tabControl1.SelectedIndex == 3)
            {
                LoadNew();
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            LoadMoreOld();
        }
    }
}
