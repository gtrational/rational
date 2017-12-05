using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using GMap.NET.WindowsForms;
using GMap.NET.MapProviders;
using GMap.NET.WindowsForms.Markers;
using GMap.NET;

namespace rational
{
    public partial class MainScreen : Form
    {
        private GMapControl map;
        private GMapOverlay overlay;

        public MainScreen()
        {
            InitializeComponent();

            overlay = new GMapOverlay();

            map = new GMapControl();
            map.MapProvider = GMapProviders.OpenStreetMap;
            map.Position = new PointLatLng(40.7143, -73.9376);
            map.MinZoom = 1;
            map.MaxZoom = 24;
            map.Zoom = 11;
            map.Size = mapPanel.Size;
            map.CanDragMap = true;

            map.Overlays.Add(overlay);

            mapPanel.Controls.Add(map);

            map.OnMarkerClick += Map_OnMarkerClick;
        }

        private void Map_OnMarkerClick(GMapMarker item, MouseEventArgs e)
        {
            DisplayRatData((RatData)item.Tag);
        }

        private void ReInitMap()
        {
            overlay.Markers.Clear();

            List<RatData> rats = Model.GetInstance().RatDataList;

            for (var i = 0; i < rats.Count; i++)
            {
                RatData rat = rats[i];
                GMapMarker marker = new GMarkerGoogle(new PointLatLng(rat.Latitude, rat.Longitude), GMarkerGoogleType.arrow);
                marker.ToolTipText = rat.UniqueKey + "";
                marker.Tag = rat;
                marker.ToolTipMode = MarkerTooltipMode.Always;
                overlay.Markers.Add(marker);
            }
        }

        private void RefreshView()
        {
            listView1.Items.Clear();

            List<RatData> rats = Model.GetInstance().RatDataList;

            for (var i = 0; i < rats.Count; i++)
            {
                RatData rat = rats[i];
                var item = new ListViewItem(new string[] { WebAPI.ParseTime(rat.CreatedTime), rat.UniqueKey + "", rat.Borough });
                item.Tag = rat;
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
            switch (tabControl1.SelectedIndex)
            {
                case 2:
                    ReInitMap();
                    break;
                case 3:
                    LoadNew();
                    break;
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            LoadMoreOld();
        }

        private void listView1_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            if (listView1.SelectedItems.Count == 0) return;
            ListViewItem item = listView1.SelectedItems[0];

            RatData data = (RatData)item.Tag;

            DisplayRatData(data);
        }

        private void DisplayRatData(RatData data)
        {
            MessageBox.Show(data.ToString(), data.UniqueKey + "");
        }

        private void MainScreen_Resize(object sender, EventArgs e)
        {
            mapPanel.Size = tabPage3.Size;
            map.Size = mapPanel.Size;
        }
    }
}
