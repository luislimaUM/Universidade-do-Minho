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
    public partial class Form6 : Form
    {
        private static StepsForSleep programa;

        public Form6()
        {
            InitializeComponent();
        }

        public void CarregaDados(StepsForSleep ss)
        {
            programa = ss;
            TotalAvg.Text = programa.GetAverage_Steps().ToString();
            DesvioPadrao.Text = programa.GetStd_Steps().ToString();

            List<KeyValuePair<int, PairStepsSleep>> aux = programa.TopHigher(5);

            char1.Text = DateAndTime((double)aux.ElementAt(0).Key).ToString();
            totalCh1.Text = aux.ElementAt(0).Value.GetTotalSteps().ToString();
            char51.Text = (aux.ElementAt(0).Value.GetAwake()).ToString();
            char61.Text = (aux.ElementAt(0).Value.GetDeep()).ToString();
            char71.Text = (aux.ElementAt(0).Value.GetLight()).ToString();

            char2.Text = DateAndTime((double)aux.ElementAt(1).Key).ToString();
            totalCh2.Text = aux.ElementAt(1).Value.GetTotalSteps().ToString();
            char52.Text = (aux.ElementAt(1).Value.GetAwake()).ToString();
            char62.Text = (aux.ElementAt(1).Value.GetDeep()).ToString();
            char72.Text = (aux.ElementAt(1).Value.GetLight()).ToString();

            char3.Text = DateAndTime((double)aux.ElementAt(2).Key).ToString();
            totalCh3.Text = aux.ElementAt(2).Value.GetTotalSteps().ToString();
            char53.Text = (aux.ElementAt(2).Value.GetAwake()).ToString();
            char63.Text = (aux.ElementAt(2).Value.GetDeep()).ToString();
            char73.Text = (aux.ElementAt(2).Value.GetLight()).ToString();

            char4.Text = DateAndTime((double)aux.ElementAt(3).Key).ToString();
            totalCh4.Text = aux.ElementAt(3).Value.GetTotalSteps().ToString();
            char54.Text = (aux.ElementAt(3).Value.GetAwake()).ToString();
            char64.Text = (aux.ElementAt(3).Value.GetDeep()).ToString();
            char74.Text = (aux.ElementAt(3).Value.GetLight()).ToString();

            char5.Text = DateAndTime((double)aux.ElementAt(4).Key).ToString();
            totalCh5.Text = aux.ElementAt(4).Value.GetTotalSteps().ToString();
            char55.Text = (aux.ElementAt(4).Value.GetAwake()).ToString();
            char65.Text = (aux.ElementAt(4).Value.GetDeep()).ToString();
            char75.Text = (aux.ElementAt(4).Value.GetLight()).ToString();

            List<KeyValuePair<int, PairStepsSleep>> aux1 = programa.TopInferior(5);

            char11.Text = DateAndTime((double)aux1.ElementAt(0).Key).ToString();
            totalCh11.Text = aux1.ElementAt(0).Value.GetTotalSteps().ToString();
            char56.Text = (aux1.ElementAt(0).Value.GetAwake()).ToString();
            char66.Text = (aux1.ElementAt(0).Value.GetDeep()).ToString();
            char76.Text = (aux1.ElementAt(0).Value.GetLight()).ToString();

            char12.Text = DateAndTime((double)aux1.ElementAt(1).Key).ToString();
            totalCh12.Text = aux1.ElementAt(1).Value.GetTotalSteps().ToString();
            char57.Text = (aux1.ElementAt(1).Value.GetAwake()).ToString();
            char67.Text = (aux1.ElementAt(1).Value.GetDeep()).ToString();
            char77.Text = (aux1.ElementAt(1).Value.GetLight()).ToString();

            char13.Text = DateAndTime((double)aux1.ElementAt(2).Key).ToString();
            totalCh13.Text = aux1.ElementAt(2).Value.GetTotalSteps().ToString();
            char58.Text = (aux1.ElementAt(2).Value.GetAwake()).ToString();
            char68.Text = (aux1.ElementAt(2).Value.GetDeep()).ToString();
            char78.Text = (aux1.ElementAt(2).Value.GetLight()).ToString();

            char14.Text = DateAndTime((double)aux1.ElementAt(3).Key).ToString();
            totalCh14.Text = aux1.ElementAt(3).Value.GetTotalSteps().ToString();
            char59.Text = (aux1.ElementAt(3).Value.GetAwake()).ToString();
            char69.Text = (aux1.ElementAt(3).Value.GetDeep()).ToString();
            char79.Text = (aux1.ElementAt(3).Value.GetLight()).ToString();

            char15.Text = DateAndTime((double)aux1.ElementAt(4).Key).ToString();
            totalCh15.Text = aux1.ElementAt(4).Value.GetTotalSteps().ToString();
            char60.Text = (aux1.ElementAt(4).Value.GetAwake()).ToString();
            char70.Text = (aux1.ElementAt(4).Value.GetDeep()).ToString();
            char80.Text = (aux1.ElementAt(4).Value.GetLight()).ToString();
        }

        private void Form6_Load(object sender, EventArgs e)
        {

        }

        private void button4_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = programa.GetPrograma().getSteps();
            Form4 f4 = new Form4();
            StepsMetrics stm = new StepsMetrics(path, programa.GetPrograma());
            f4.CarregaDados(stm);
            f4.ShowDialog();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = programa.GetPrograma().getSleep();
            Form3 f3 = new Form3();
            SleepMetrics sbm = new SleepMetrics(path, programa.GetPrograma());
            f3.CarregaDados(sbm);
            f3.ShowDialog();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = programa.GetPrograma().getHeart();
            Form2 f2 = new Form2();
            HeartBeatMetrics hbm = new HeartBeatMetrics(path, programa.GetPrograma());
            f2.CarregaDados(hbm);
            f2.ShowDialog();
        }

        private void button1_Click(object sender, EventArgs e)
        {

        }

        private void button5_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = programa.GetPrograma().getSleep();
            string path1 = programa.GetPrograma().getHeart();
            SleepMetrics spm = new SleepMetrics(path, programa.GetPrograma());
            HeartBeatMetrics hbm = new HeartBeatMetrics(path1, programa.GetPrograma());
            HeartBeatForSleep a = new HeartBeatForSleep(spm, hbm, programa.GetPrograma());
            Form5 f5 = new Form5();
            f5.CarregaDados(a);
            f5.ShowDialog();
        }

        private void BackButton_Click(object sender, EventArgs e)
        {
            this.Hide();
            Form1 f1 = new Form1();
            f1.ShowDialog();
        }

        public DateTime DateAndTime(double unixTimeStamp)
        {
            // Unix timestamp is seconds past epoch
            System.DateTime dtDateTime = new DateTime(1970, 1, 1, 0, 0, 0, 0, System.DateTimeKind.Utc);
            dtDateTime = dtDateTime.AddSeconds(unixTimeStamp).ToLocalTime();
            return dtDateTime;
        }

        private void Form6_FormClosed(object sender, FormClosedEventArgs e)
        {
            Application.Exit();
        }
    }
}
