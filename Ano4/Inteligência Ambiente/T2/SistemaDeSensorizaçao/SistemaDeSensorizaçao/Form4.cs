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
    public partial class Form4 : Form
    {
        StepsMetrics steps_m;

        public Form4()
        {
            InitializeComponent();
        }

        public void CarregaDados(StepsMetrics s)
        {
            steps_m = s;
            
            char1.Text = steps_m.TotalSteps().ToString();
            char3.Text = steps_m.GetAverage_Steps().ToString();
            char4.Text = steps_m.GetAverage_Intensity().ToString();
            char9.Text = steps_m.GetStd_Intensity().ToString();
            char8.Text = steps_m.GetStd_Steps().ToString();

            IEnumerable<KeyValuePair<int, PullSteps>> newdic = steps_m.Top5(1);
            IEnumerable<KeyValuePair<int, PullSteps>> newdic2 = steps_m.Top5(2);
            
            //Dictionary<int, PullSteps> top5 = newdic.ToDictionary(x => x.Key, x => x.Value);

            char21.Text = DateAndTime((double)newdic.ElementAt(0).Key).ToString();
            totalCh1.Text = newdic.ElementAt(0).Value.GetIntensity().ToString();
            char41.Text = newdic.ElementAt(0).Value.GetSteps().ToString();

            char22.Text = DateAndTime((double)newdic.ElementAt(1).Key).ToString();
            totalCh2.Text = newdic.ElementAt(1).Value.GetIntensity().ToString();
            char42.Text = newdic.ElementAt(1).Value.GetSteps().ToString();

            char23.Text = DateAndTime((double)newdic.ElementAt(2).Key).ToString();
            totalCh3.Text = newdic.ElementAt(2).Value.GetIntensity().ToString();
            char43.Text = newdic.ElementAt(2).Value.GetSteps().ToString();

            char24.Text = DateAndTime((double)newdic.ElementAt(3).Key).ToString();
            totalCh4.Text = newdic.ElementAt(3).Value.GetIntensity().ToString();
            char44.Text = newdic.ElementAt(3).Value.GetSteps().ToString();

            char25.Text = DateAndTime((double)newdic.ElementAt(4).Key).ToString();
            totalCh5.Text = newdic.ElementAt(4).Value.GetIntensity().ToString();
            char45.Text = newdic.ElementAt(4).Value.GetSteps().ToString();

            char26.Text = DateAndTime((double)newdic2.ElementAt(0).Key).ToString();
            totalCh6.Text = newdic2.ElementAt(0).Value.GetIntensity().ToString();
            char46.Text = newdic2.ElementAt(0).Value.GetSteps().ToString();

            char27.Text = DateAndTime((double)newdic2.ElementAt(1).Key).ToString();
            totalCh7.Text = newdic2.ElementAt(1).Value.GetIntensity().ToString();
            char47.Text = newdic2.ElementAt(1).Value.GetSteps().ToString();

            char28.Text = DateAndTime((double)newdic2.ElementAt(2).Key).ToString();
            totalCh8.Text = newdic2.ElementAt(2).Value.GetIntensity().ToString();
            char48.Text = newdic2.ElementAt(2).Value.GetSteps().ToString();

            char29.Text = DateAndTime((double)newdic2.ElementAt(3).Key).ToString();
            totalCh9.Text = newdic2.ElementAt(3).Value.GetIntensity().ToString();
            char49.Text = newdic2.ElementAt(3).Value.GetSteps().ToString();

            char30.Text = DateAndTime((double)newdic2.ElementAt(4).Key).ToString();
            totalCh10.Text = newdic2.ElementAt(4).Value.GetIntensity().ToString();
            char50.Text = newdic2.ElementAt(4).Value.GetSteps().ToString();
            
        }

        private void BackButton_Click(object sender, EventArgs e)
        {
            this.Hide();
            Form1 f1 = new Form1();
            f1.ShowDialog();
        }

        private void Form4_Load(object sender, EventArgs e)
        {

        }

        private void button3_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = steps_m.getPrograma().getSleep();
            SleepMetrics programa = new SleepMetrics(path, steps_m.getPrograma());
            Form3 f3 = new Form3();
            f3.CarregaDados(programa);
            f3.ShowDialog();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = steps_m.getPrograma().getHeart();
            HeartBeatMetrics programa = new HeartBeatMetrics(path, steps_m.getPrograma());
            Form2 f2 = new Form2();
            f2.CarregaDados(programa);
            f2.ShowDialog();
        }

        private void button5_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = steps_m.getPrograma().getHeart();
            string path1 = steps_m.getPrograma().getSleep();
            SleepMetrics spm = new SleepMetrics(path1, steps_m.getPrograma());
            HeartBeatMetrics hbm = new HeartBeatMetrics(path, steps_m.getPrograma());
            HeartBeatForSleep programa = new HeartBeatForSleep(spm, hbm, steps_m.getPrograma());
            Form5 f5 = new Form5();
            f5.CarregaDados(programa);
            f5.ShowDialog();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = steps_m.getPrograma().getSleep();
            SleepMetrics spm = new SleepMetrics(path, steps_m.getPrograma());
            StepsForSleep programa = new StepsForSleep(spm, steps_m, steps_m.getPrograma());
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

        private void Form4_FormClosed(object sender, FormClosedEventArgs e)
        {
            Application.Exit();
        }

        private void char1_Click(object sender, EventArgs e)
        {

        }
    }
}
