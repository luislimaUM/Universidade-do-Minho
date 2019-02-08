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
    public partial class Form3 : Form
    {
        //sleep
        public SleepMetrics sleep_m;

        public Form3()
        {
            InitializeComponent();
        }

        public void CarregaDados(SleepMetrics s)
        {
            sleep_m = s;
            sleep_m.Average();
            double avg =  sleep_m.GetAverage_Timeofsleep()/3600;  char1.Text = TimeSpan.FromHours(avg).ToString();
            double avgAwake = sleep_m.GetAverage_Awake(); char3.Text = TimeSpan.FromMinutes(avgAwake).ToString();
            double avgDeep = sleep_m.GetAverage_Deep(); char4.Text = TimeSpan.FromMinutes(avgDeep).ToString();
            double avgLight = sleep_m.GetAverage_Light(); char5.Text = TimeSpan.FromMinutes(avgLight).ToString();
            sleep_m.Std();
            double stdSleep = sleep_m.GetStd_Timeofsleep()/3600; char6.Text = TimeSpan.FromHours(stdSleep).ToString();
            double std2 = sleep_m.GetStd_Awake(); char8.Text = TimeSpan.FromMinutes(std2).ToString();
            double std3 = sleep_m.GetStd_Deep(); char9.Text = TimeSpan.FromMinutes(std3).ToString();
            double std4 = sleep_m.GetStd_Light(); char10.Text = TimeSpan.FromMinutes(std4).ToString();

            IEnumerable<KeyValuePair<int, PullSleep>> newdic = sleep_m.Top5(1);
            IEnumerable<KeyValuePair<int, PullSleep>> newdic2 = sleep_m.Top5(2);

            Dictionary<int, PullSleep> top5 = newdic.ToDictionary(x => x.Key, x => x.Value);

            char21.Text = TimeSpan.FromMinutes((double) top5.ElementAt(0).Value.GetSleep() / 60).ToString();
            totalCh1.Text = sleep_m.DateAndTime((double)top5.ElementAt(0).Key).ToString();
            char41.Text = sleep_m.DateAndTime((double)top5.ElementAt(0).Value.GetEndTime()).ToString();
            char51.Text = TimeSpan.FromHours((double)top5.ElementAt(0).Value.GetAwake() / 60).ToString();
            char61.Text = TimeSpan.FromHours((double)top5.ElementAt(0).Value.GetDeep() / 60).ToString();
            char71.Text = TimeSpan.FromHours((double)top5.ElementAt(0).Value.GetLight() / 60).ToString();

            char22.Text = TimeSpan.FromMinutes((double)top5.ElementAt(1).Value.GetSleep() / 60).ToString();
            totalCh2.Text = sleep_m.DateAndTime((double)top5.ElementAt(1).Key).ToString();
            char42.Text = sleep_m.DateAndTime((double)top5.ElementAt(1).Value.GetEndTime()).ToString();
            char52.Text = TimeSpan.FromHours((double)top5.ElementAt(1).Value.GetAwake() / 60).ToString();
            char62.Text = TimeSpan.FromHours((double)top5.ElementAt(1).Value.GetDeep() / 60).ToString();
            char72.Text = TimeSpan.FromHours((double)top5.ElementAt(1).Value.GetLight() / 60).ToString();

            char23.Text = TimeSpan.FromMinutes((double)top5.ElementAt(2).Value.GetSleep() / 60).ToString();
            totalCh3.Text = sleep_m.DateAndTime((double)top5.ElementAt(2).Key).ToString();
            char43.Text = sleep_m.DateAndTime((double)top5.ElementAt(2).Value.GetEndTime()).ToString();
            char53.Text = TimeSpan.FromHours((double)top5.ElementAt(2).Value.GetAwake() / 60).ToString();
            char63.Text = TimeSpan.FromHours((double)top5.ElementAt(2).Value.GetDeep() / 60).ToString();
            char73.Text = TimeSpan.FromHours((double)top5.ElementAt(2).Value.GetLight() / 60).ToString();

            char24.Text = TimeSpan.FromMinutes((double)top5.ElementAt(3).Value.GetSleep() / 60).ToString();
            totalCh4.Text = sleep_m.DateAndTime((double)top5.ElementAt(3).Key).ToString();
            char44.Text = sleep_m.DateAndTime((double)top5.ElementAt(3).Value.GetEndTime()).ToString();
            char54.Text = TimeSpan.FromHours((double)top5.ElementAt(3).Value.GetAwake() / 60).ToString();
            char64.Text = TimeSpan.FromHours((double)top5.ElementAt(3).Value.GetDeep() / 60).ToString();
            char74.Text = TimeSpan.FromHours((double)top5.ElementAt(3).Value.GetLight() / 60).ToString();

            char25.Text = TimeSpan.FromMinutes((double)top5.ElementAt(4).Value.GetSleep() / 60).ToString();
            totalCh5.Text = sleep_m.DateAndTime((double)top5.ElementAt(4).Key).ToString();
            char45.Text = sleep_m.DateAndTime((double)top5.ElementAt(4).Value.GetEndTime()).ToString();
            char55.Text = TimeSpan.FromHours((double)top5.ElementAt(4).Value.GetAwake() / 60).ToString();
            char65.Text = TimeSpan.FromHours((double)top5.ElementAt(4).Value.GetDeep() / 60).ToString();
            char75.Text = TimeSpan.FromHours((double)top5.ElementAt(4).Value.GetLight() / 60).ToString();

            top5 = newdic2.ToDictionary(x => x.Key, x => x.Value);

            char26.Text = TimeSpan.FromMinutes((double)top5.ElementAt(0).Value.GetSleep() / 60).ToString();
            totalCh6.Text = sleep_m.DateAndTime((double)top5.ElementAt(0).Key).ToString();
            char46.Text = sleep_m.DateAndTime((double)top5.ElementAt(0).Value.GetEndTime()).ToString();
            char56.Text = TimeSpan.FromHours((double)top5.ElementAt(0).Value.GetAwake() / 60).ToString();
            char66.Text = TimeSpan.FromHours((double)top5.ElementAt(0).Value.GetDeep() / 60).ToString();
            char76.Text = TimeSpan.FromHours((double)top5.ElementAt(0).Value.GetLight() / 60).ToString();

            char27.Text = TimeSpan.FromMinutes((double)top5.ElementAt(1).Value.GetSleep() / 60).ToString();
            totalCh7.Text = sleep_m.DateAndTime((double)top5.ElementAt(1).Key).ToString();
            char47.Text = sleep_m.DateAndTime((double)top5.ElementAt(1).Value.GetEndTime()).ToString();
            char57.Text = TimeSpan.FromHours((double)top5.ElementAt(1).Value.GetAwake() / 60).ToString();
            char67.Text = TimeSpan.FromHours((double)top5.ElementAt(1).Value.GetDeep() / 60).ToString();
            char77.Text = TimeSpan.FromHours((double)top5.ElementAt(1).Value.GetLight() / 60).ToString();

            char28.Text = TimeSpan.FromMinutes((double)top5.ElementAt(2).Value.GetSleep() / 60).ToString();
            totalCh8.Text = sleep_m.DateAndTime((double)top5.ElementAt(2).Key).ToString();
            char48.Text = sleep_m.DateAndTime((double)top5.ElementAt(2).Value.GetEndTime()).ToString();
            char58.Text = TimeSpan.FromHours((double)top5.ElementAt(2).Value.GetAwake() / 60).ToString();
            char68.Text = TimeSpan.FromHours((double)top5.ElementAt(2).Value.GetDeep() / 60).ToString();
            char78.Text = TimeSpan.FromHours((double)top5.ElementAt(2).Value.GetLight() / 60).ToString();

            char29.Text = TimeSpan.FromMinutes((double)top5.ElementAt(3).Value.GetSleep() / 60).ToString();
            totalCh9.Text = sleep_m.DateAndTime((double)top5.ElementAt(3).Key).ToString();
            char49.Text = sleep_m.DateAndTime((double)top5.ElementAt(3).Value.GetEndTime()).ToString();
            char59.Text = TimeSpan.FromHours((double)top5.ElementAt(3).Value.GetAwake() / 60).ToString();
            char69.Text = TimeSpan.FromHours((double)top5.ElementAt(3).Value.GetDeep() / 60).ToString();
            char79.Text = TimeSpan.FromHours((double)top5.ElementAt(3).Value.GetLight() / 60).ToString();

            char30.Text = TimeSpan.FromMinutes((double)top5.ElementAt(4).Value.GetSleep() / 60).ToString();
            totalCh10.Text = sleep_m.DateAndTime((double)top5.ElementAt(4).Key).ToString();
            char50.Text = sleep_m.DateAndTime((double)top5.ElementAt(4).Value.GetEndTime()).ToString();
            char60.Text = TimeSpan.FromHours((double)top5.ElementAt(4).Value.GetAwake() / 60).ToString();
            char70.Text = TimeSpan.FromHours((double)top5.ElementAt(4).Value.GetDeep() / 60).ToString();
            char80.Text = TimeSpan.FromHours((double)top5.ElementAt(4).Value.GetLight() / 60).ToString();

        }

        private void Form3_Load(object sender, EventArgs e)
        {

        }

        private void BackButton_Click(object sender, EventArgs e)
        {
            this.Hide();
            Form1 f1 = new Form1();
            f1.ShowDialog();
        }

        private void label25_Click(object sender, EventArgs e)
        {

        }

        private void label36_Click(object sender, EventArgs e)
        {

        }

        private void label29_Click(object sender, EventArgs e)
        {

        }

        private void totalCh1_Click(object sender, EventArgs e)
        {

        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = sleep_m.GetPrograma().getHeart();
            Form2 f2 = new Form2();
            HeartBeatMetrics programa = new HeartBeatMetrics(path, sleep_m.GetPrograma());
            f2.CarregaDados(programa);
            f2.ShowDialog();
        }

        private void button3_Click(object sender, EventArgs e)
        {

        }

        private void button4_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = sleep_m.GetPrograma().getSteps();
            StepsMetrics programa = new StepsMetrics(path, sleep_m.GetPrograma());
            Form4 f4 = new Form4();
            f4.CarregaDados(programa);
            f4.ShowDialog();
        }

        private void button5_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = sleep_m.GetPrograma().getHeart();
            HeartBeatMetrics hbm = new HeartBeatMetrics(path, sleep_m.GetPrograma());
            HeartBeatForSleep programa = new HeartBeatForSleep(sleep_m, hbm, sleep_m.GetPrograma());
            Form5 f5 = new Form5();
            f5.CarregaDados(programa);
            f5.ShowDialog();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path1 = sleep_m.GetPrograma().getSteps();
            StepsMetrics stm = new StepsMetrics(path1, sleep_m.GetPrograma());
            StepsForSleep programa = new StepsForSleep(sleep_m, stm, sleep_m.GetPrograma());
            Form6 f6 = new Form6();
            f6.CarregaDados(programa);
            f6.ShowDialog();
        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

        private void Form3_FormClosed(object sender, FormClosedEventArgs e)
        {
            Application.Exit();
        }
    }
}
