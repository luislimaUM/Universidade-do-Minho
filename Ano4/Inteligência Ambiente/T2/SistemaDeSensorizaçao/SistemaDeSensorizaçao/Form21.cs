using Microsoft.VisualBasic;
using SistemaDeSensorizaçao.src;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace SistemaDeSensorizaçao
{
    public partial class Form21 : Form
    {
        private static HeartBeatMetrics hb_m = null;

        public Form21(int min, HeartBeatMetrics prog)
        {
            InitializeComponent();
            hb_m = prog;
            HeartBMForIntervals hbm_min = new HeartBMForIntervals(min, hb_m);

            double total = hbm_min.Average(); TotalAvg.Text = total.ToString();
            double desvio = hbm_min.Std(); DesvioPadrao.Text = desvio.ToString();
            List<KeyValuePair<int, PairEndTimeHB>> sorted = hbm_min.TopHigher(10);

            Dictionary<int, PairEndTimeHB> top10 = sorted.ToDictionary(x => x.Key, x => x.Value);
            char1.Text = DateAndTime((double)top10.ElementAt(0).Key).ToString(); totalCh1.Text = (top10.ElementAt(0).Value.GetHeartBeat()).ToString(); char31.Text = DateAndTime((double)top10.ElementAt(0).Value.GetEndTime()).ToString();
            char2.Text = DateAndTime((double)top10.ElementAt(1).Key).ToString(); totalCh2.Text = (top10.ElementAt(1).Value.GetHeartBeat()).ToString(); char32.Text = DateAndTime((double)top10.ElementAt(1).Value.GetEndTime()).ToString();
            char3.Text = DateAndTime((double)top10.ElementAt(2).Key).ToString(); totalCh3.Text = (top10.ElementAt(2).Value.GetHeartBeat()).ToString(); char33.Text = DateAndTime((double)top10.ElementAt(2).Value.GetEndTime()).ToString();
            char4.Text = DateAndTime((double)top10.ElementAt(3).Key).ToString(); totalCh4.Text = (top10.ElementAt(3).Value.GetHeartBeat()).ToString(); char34.Text = DateAndTime((double)top10.ElementAt(3).Value.GetEndTime()).ToString();
            char5.Text = DateAndTime((double)top10.ElementAt(4).Key).ToString(); totalCh5.Text = (top10.ElementAt(4).Value.GetHeartBeat()).ToString(); char35.Text = DateAndTime((double)top10.ElementAt(4).Value.GetEndTime()).ToString();
            char6.Text = DateAndTime((double)top10.ElementAt(5).Key).ToString(); totalCh6.Text = (top10.ElementAt(5).Value.GetHeartBeat()).ToString(); char36.Text = DateAndTime((double)top10.ElementAt(5).Value.GetEndTime()).ToString();
            char7.Text = DateAndTime((double)top10.ElementAt(6).Key).ToString(); totalCh7.Text = (top10.ElementAt(6).Value.GetHeartBeat()).ToString(); char37.Text = DateAndTime((double)top10.ElementAt(6).Value.GetEndTime()).ToString();
            char8.Text = DateAndTime((double)top10.ElementAt(7).Key).ToString(); totalCh8.Text = (top10.ElementAt(7).Value.GetHeartBeat()).ToString(); char38.Text = DateAndTime((double)top10.ElementAt(7).Value.GetEndTime()).ToString();
            char9.Text = DateAndTime((double)top10.ElementAt(8).Key).ToString(); totalCh9.Text = (top10.ElementAt(8).Value.GetHeartBeat()).ToString(); char39.Text = DateAndTime((double)top10.ElementAt(8).Value.GetEndTime()).ToString();
            char10.Text = DateAndTime((double)top10.ElementAt(9).Key).ToString(); totalCh10.Text = (top10.ElementAt(9).Value.GetHeartBeat()).ToString(); char40.Text = DateAndTime((double)top10.ElementAt(9).Value.GetEndTime()).ToString();

            List<KeyValuePair<int, PairEndTimeHB>> sorted1 = hbm_min.TopInferior(10);
            top10 = sorted1.ToDictionary(x => x.Key, x => x.Value);
            char11.Text = DateAndTime((double)top10.ElementAt(0).Key).ToString(); totalCh11.Text = (top10.ElementAt(0).Value.GetHeartBeat()).ToString(); char41.Text = DateAndTime((double)top10.ElementAt(0).Value.GetEndTime()).ToString();
            char12.Text = DateAndTime((double)top10.ElementAt(1).Key).ToString(); totalCh12.Text = (top10.ElementAt(1).Value.GetHeartBeat()).ToString(); char42.Text = DateAndTime((double)top10.ElementAt(1).Value.GetEndTime()).ToString();
            char13.Text = DateAndTime((double)top10.ElementAt(2).Key).ToString(); totalCh13.Text = (top10.ElementAt(2).Value.GetHeartBeat()).ToString(); char43.Text = DateAndTime((double)top10.ElementAt(2).Value.GetEndTime()).ToString();
            char14.Text = DateAndTime((double)top10.ElementAt(3).Key).ToString(); totalCh14.Text = (top10.ElementAt(3).Value.GetHeartBeat()).ToString(); char44.Text = DateAndTime((double)top10.ElementAt(3).Value.GetEndTime()).ToString();
            char15.Text = DateAndTime((double)top10.ElementAt(4).Key).ToString(); totalCh15.Text = (top10.ElementAt(4).Value.GetHeartBeat()).ToString(); char45.Text = DateAndTime((double)top10.ElementAt(4).Value.GetEndTime()).ToString();
            char16.Text = DateAndTime((double)top10.ElementAt(5).Key).ToString(); totalCh16.Text = (top10.ElementAt(5).Value.GetHeartBeat()).ToString(); char46.Text = DateAndTime((double)top10.ElementAt(5).Value.GetEndTime()).ToString();
            char17.Text = DateAndTime((double)top10.ElementAt(6).Key).ToString(); totalCh17.Text = (top10.ElementAt(6).Value.GetHeartBeat()).ToString(); char47.Text = DateAndTime((double)top10.ElementAt(6).Value.GetEndTime()).ToString();
            char18.Text = DateAndTime((double)top10.ElementAt(7).Key).ToString(); totalCh18.Text = (top10.ElementAt(7).Value.GetHeartBeat()).ToString(); char48.Text = DateAndTime((double)top10.ElementAt(7).Value.GetEndTime()).ToString();
            char19.Text = DateAndTime((double)top10.ElementAt(8).Key).ToString(); totalCh19.Text = (top10.ElementAt(8).Value.GetHeartBeat()).ToString(); char49.Text = DateAndTime((double)top10.ElementAt(8).Value.GetEndTime()).ToString();
            char20.Text = DateAndTime((double)top10.ElementAt(9).Key).ToString(); totalCh20.Text = (top10.ElementAt(9).Value.GetHeartBeat()).ToString(); char50.Text = DateAndTime((double)top10.ElementAt(9).Value.GetEndTime()).ToString();

        }

        private void Form21_Load(object sender, EventArgs e)
        {

        }

        private void label37_Click(object sender, EventArgs e)
        {

        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Hide();
            Form2 f2 = new Form2();
            f2.CarregaDados(hb_m);
            f2.ShowDialog();
        }

        private void label4_Click(object sender, EventArgs e)
        {

        }

        private void button3_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = hb_m.GetPrograma().getSleep();
            SleepMetrics programa = new SleepMetrics(path, hb_m.GetPrograma());
            Form3 f3 = new Form3();
            f3.CarregaDados(programa);
            f3.ShowDialog();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = hb_m.GetPrograma().getSteps();
            StepsMetrics programa = new StepsMetrics(path, hb_m.GetPrograma());
            Form4 f4 = new Form4();
            f4.CarregaDados(programa);
            f4.ShowDialog();
        }

        private void button5_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = hb_m.GetPrograma().getSleep();
            SleepMetrics spm = new SleepMetrics(path, hb_m.GetPrograma());
            HeartBeatForSleep programa = new HeartBeatForSleep(spm, hb_m, hb_m.GetPrograma());
            Form5 f5 = new Form5();
            f5.CarregaDados(programa);
            f5.ShowDialog();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = hb_m.GetPrograma().getSleep();
            string path1 = hb_m.GetPrograma().getSteps();
            SleepMetrics spm = new SleepMetrics(path, hb_m.GetPrograma());
            StepsMetrics stm = new StepsMetrics(path1, hb_m.GetPrograma());
            StepsForSleep programa = new StepsForSleep(spm, stm, hb_m.GetPrograma());
            Form6 f6 = new Form6();
            f6.CarregaDados(programa);
            f6.ShowDialog();
        }

        public DateTime DateAndTime(double unixTimeStamp)
        {
            // Unix timestamp is seconds past epoch
            System.DateTime dtDateTime = new DateTime(1970, 1, 1, 0, 0, 0, 0, System.DateTimeKind.Utc);
            dtDateTime = dtDateTime.AddSeconds(unixTimeStamp).ToLocalTime();
            return dtDateTime;
        }

        private void Form21_FormClosed(object sender, FormClosedEventArgs e)
        {
            Application.Exit();
        }

        private void button6_Click(object sender, EventArgs e)
        {
            int valor = Int32.Parse(Interaction.InputBox("Select an integer for the range (minutes)", "For Intervals", "1440", -1, -1));

            this.Hide();
            Form21 f21 = new Form21(valor, hb_m);
            f21.ShowDialog();
        }
    }
}
