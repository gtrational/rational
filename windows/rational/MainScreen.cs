using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using rational;

namespace WindowsFormsApp1
{
    public partial class MainScreen : Form
    {
        public MainScreen()
        {
            InitializeComponent();
        }

        private void LoadMore()
        {
            Model.CallbackVoid cb = () =>
            {
                listView1.Items.Clear();

                List<RatData> rats = Model.GetInstance().RatDataList;

                for (var i = 0; i < rats.Count; i++)
                {
                    RatData rat = rats[i];
                    var item = new ListViewItem(new string[] { rat.UniqueKey + "", rat.Borough});
                    listView1.Items.Add(item);
                }
            };

            Model.GetInstance().LoadMoreRats(cb);
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            LoadMore();
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
    }
}
