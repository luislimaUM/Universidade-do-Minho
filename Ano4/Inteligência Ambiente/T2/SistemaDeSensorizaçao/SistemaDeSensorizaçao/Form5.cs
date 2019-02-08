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
    public partial class Form5 : Form
    {
        private static HeartBeatForSleep programa;

        public Form5()
        {
            InitializeComponent();
        }

        public void CarregaDados(HeartBeatForSleep ss)
        {
            programa = ss;
            double auxiliar;
            TotalAvg.Text = ((Double)programa.GetAverageBefor()).ToString();
            DesvioPadrao.Text = programa.GetStdBefor().ToString();
            TotalAvg1.Text = programa.GetAverageWhile().ToString();
            DesvioPadrao1.Text = programa.GetStdWhile().ToString();

            List<KeyValuePair<int, PairHeartBeatSleep>> aux = programa.TopHigherBefor(5);

            char1.Text = DateAndTime((double)aux.ElementAt(0).Key).ToString();
            auxiliar = programa.GetAverageBefor(aux.ElementAt(0).Key); totalCh1.Text = Math.Round(auxiliar, 2).ToString();
            char51.Text = (aux.ElementAt(0).Value.GetAwake()).ToString();
            char61.Text = (aux.ElementAt(0).Value.GetDeep()).ToString();
            char71.Text = (aux.ElementAt(0).Value.GetLight()).ToString();

            char2.Text = DateAndTime((double)aux.ElementAt(1).Key).ToString();
            auxiliar = programa.GetAverageBefor(aux.ElementAt(1).Key); totalCh2.Text = Math.Round(auxiliar, 2).ToString();
            char52.Text = (aux.ElementAt(1).Value.GetAwake()).ToString();
            char62.Text = (aux.ElementAt(1).Value.GetDeep()).ToString();
            char72.Text = (aux.ElementAt(1).Value.GetLight()).ToString();

            char3.Text = DateAndTime((double)aux.ElementAt(2).Key).ToString();
            auxiliar = programa.GetAverageBefor(aux.ElementAt(2).Key); totalCh3.Text = Math.Round(auxiliar, 2).ToString();
            char53.Text = (aux.ElementAt(2).Value.GetAwake()).ToString();
            char63.Text = (aux.ElementAt(2).Value.GetDeep()).ToString();
            char73.Text = (aux.ElementAt(2).Value.GetLight()).ToString();

            char4.Text = DateAndTime((double)aux.ElementAt(3).Key).ToString();
            auxiliar = programa.GetAverageBefor(aux.ElementAt(3).Key); totalCh4.Text = Math.Round(auxiliar, 2).ToString();
            char54.Text = (aux.ElementAt(3).Value.GetAwake()).ToString();
            char64.Text = (aux.ElementAt(3).Value.GetDeep()).ToString();
            char74.Text = (aux.ElementAt(3).Value.GetLight()).ToString();

            char5.Text = DateAndTime((double)aux.ElementAt(4).Key).ToString();
            auxiliar = programa.GetAverageBefor(aux.ElementAt(4).Key); totalCh5.Text = Math.Round(auxiliar, 2).ToString();
            char55.Text = (aux.ElementAt(4).Value.GetAwake()).ToString();
            char65.Text = (aux.ElementAt(4).Value.GetDeep()).ToString();
            char75.Text = (aux.ElementAt(4).Value.GetLight()).ToString();

            List<KeyValuePair<int, PairHeartBeatSleep>> aux1= programa.TopInferiorBefor(5);

            char11.Text = DateAndTime((double)aux1.ElementAt(0).Key).ToString();
            auxiliar = programa.GetAverageBefor(aux1.ElementAt(0).Key); totalCh11.Text = Math.Round(auxiliar, 2).ToString();
            char56.Text = (aux1.ElementAt(0).Value.GetAwake()).ToString();
            char66.Text = (aux1.ElementAt(0).Value.GetDeep()).ToString();
            char76.Text = (aux1.ElementAt(0).Value.GetLight()).ToString();

            char12.Text = DateAndTime((double)aux1.ElementAt(1).Key).ToString();
            auxiliar = programa.GetAverageBefor(aux1.ElementAt(1).Key); totalCh12.Text = Math.Round(auxiliar, 2).ToString();
            char57.Text = (aux1.ElementAt(1).Value.GetAwake()).ToString();
            char67.Text = (aux1.ElementAt(1).Value.GetDeep()).ToString();
            char77.Text = (aux1.ElementAt(1).Value.GetLight()).ToString();

            char13.Text = DateAndTime((double)aux1.ElementAt(2).Key).ToString();
            auxiliar = programa.GetAverageBefor(aux1.ElementAt(2).Key); totalCh13.Text = Math.Round(auxiliar, 2).ToString();
            char58.Text = (aux1.ElementAt(2).Value.GetAwake()).ToString();
            char68.Text = (aux1.ElementAt(2).Value.GetDeep()).ToString();
            char78.Text = (aux1.ElementAt(2).Value.GetLight()).ToString();

            char14.Text = DateAndTime((double)aux1.ElementAt(3).Key).ToString();
            auxiliar = programa.GetAverageBefor(aux1.ElementAt(3).Key); totalCh14.Text = Math.Round(auxiliar, 2).ToString();
            char59.Text = (aux1.ElementAt(3).Value.GetAwake()).ToString();
            char69.Text = (aux1.ElementAt(3).Value.GetDeep()).ToString();
            char79.Text = (aux1.ElementAt(3).Value.GetLight()).ToString();

            char15.Text = DateAndTime((double)aux1.ElementAt(4).Key).ToString();
            auxiliar = programa.GetAverageBefor(aux1.ElementAt(4).Key); totalCh15.Text = Math.Round(auxiliar, 2).ToString();
            char60.Text = (aux1.ElementAt(4).Value.GetAwake()).ToString();
            char70.Text = (aux1.ElementAt(4).Value.GetDeep()).ToString();
            char80.Text = (aux1.ElementAt(4).Value.GetLight()).ToString();

            List<KeyValuePair<int, PairHeartBeatSleep>> aux2 = programa.TopHigherWhile(5);

            char81.Text = DateAndTime((double)aux2.ElementAt(0).Key).ToString();
            auxiliar = programa.GetAverageWhile(aux2.ElementAt(0).Key); char91.Text = Math.Round(auxiliar, 2).ToString();
            char101.Text = (aux2.ElementAt(0).Value.GetAwake()).ToString();
            char111.Text = (aux2.ElementAt(0).Value.GetDeep()).ToString();
            char121.Text = (aux2.ElementAt(0).Value.GetLight()).ToString();

            char82.Text = DateAndTime((double)aux2.ElementAt(1).Key).ToString();
            auxiliar = programa.GetAverageWhile(aux2.ElementAt(1).Key); char92.Text = Math.Round(auxiliar, 2).ToString();
            char102.Text = (aux2.ElementAt(1).Value.GetAwake()).ToString();
            char112.Text = (aux2.ElementAt(1).Value.GetDeep()).ToString();
            char122.Text = (aux2.ElementAt(1).Value.GetLight()).ToString();

            char83.Text = DateAndTime((double)aux2.ElementAt(2).Key).ToString();
            auxiliar = programa.GetAverageWhile(aux2.ElementAt(2).Key); char93.Text = Math.Round(auxiliar, 2).ToString();
            char103.Text = (aux2.ElementAt(2).Value.GetAwake()).ToString();
            char113.Text = (aux2.ElementAt(2).Value.GetDeep()).ToString();
            char123.Text = (aux2.ElementAt(2).Value.GetLight()).ToString();

            char84.Text = DateAndTime((double)aux2.ElementAt(3).Key).ToString();
            auxiliar = programa.GetAverageWhile(aux2.ElementAt(3).Key); char94.Text = Math.Round(auxiliar, 2).ToString();
            char104.Text = (aux2.ElementAt(3).Value.GetAwake()).ToString();
            char114.Text = (aux2.ElementAt(3).Value.GetDeep()).ToString();
            char124.Text = (aux2.ElementAt(3).Value.GetLight()).ToString();

            char85.Text = DateAndTime((double)aux2.ElementAt(4).Key).ToString();
            auxiliar = programa.GetAverageWhile(aux2.ElementAt(4).Key); char95.Text = Math.Round(auxiliar, 2).ToString();
            char105.Text = (aux2.ElementAt(4).Value.GetAwake()).ToString();
            char115.Text = (aux2.ElementAt(4).Value.GetDeep()).ToString();
            char125.Text = (aux2.ElementAt(4).Value.GetLight()).ToString();

            List<KeyValuePair<int, PairHeartBeatSleep>> aux3 = programa.TopInferiorWhile(5);

            char86.Text = DateAndTime((double)aux3.ElementAt(0).Key).ToString();
            auxiliar = programa.GetAverageWhile(aux3.ElementAt(0).Key); char96.Text = Math.Round(auxiliar, 2).ToString();
            char106.Text = (aux3.ElementAt(0).Value.GetAwake()).ToString();
            char116.Text = (aux3.ElementAt(0).Value.GetDeep()).ToString();
            char126.Text = (aux3.ElementAt(0).Value.GetLight()).ToString();

            char87.Text = DateAndTime((double)aux3.ElementAt(1).Key).ToString();
            auxiliar = programa.GetAverageWhile(aux3.ElementAt(1).Key); char97.Text = Math.Round(auxiliar, 2).ToString();
            char107.Text = (aux3.ElementAt(1).Value.GetAwake()).ToString();
            char117.Text = (aux3.ElementAt(1).Value.GetDeep()).ToString();
            char127.Text = (aux3.ElementAt(1).Value.GetLight()).ToString();

            char88.Text = DateAndTime((double)aux3.ElementAt(2).Key).ToString();
            auxiliar = programa.GetAverageWhile(aux3.ElementAt(2).Key); char98.Text = Math.Round(auxiliar, 2).ToString();
            char108.Text = (aux3.ElementAt(2).Value.GetAwake()).ToString();
            char118.Text = (aux3.ElementAt(2).Value.GetDeep()).ToString();
            char128.Text = (aux3.ElementAt(2).Value.GetLight()).ToString();

            char89.Text = DateAndTime((double)aux3.ElementAt(3).Key).ToString();
            auxiliar = programa.GetAverageWhile(aux3.ElementAt(3).Key); char99.Text = Math.Round(auxiliar, 2).ToString();
            char109.Text = (aux3.ElementAt(3).Value.GetAwake()).ToString();
            char119.Text = (aux3.ElementAt(3).Value.GetDeep()).ToString();
            char129.Text = (aux3.ElementAt(3).Value.GetLight()).ToString();

            char90.Text = DateAndTime((double)aux3.ElementAt(4).Key).ToString();
            auxiliar = programa.GetAverageWhile(aux3.ElementAt(4).Key); char100.Text = Math.Round(auxiliar, 2).ToString();
            char110.Text = (aux3.ElementAt(4).Value.GetAwake()).ToString();
            char120.Text = (aux3.ElementAt(4).Value.GetDeep()).ToString();
            char130.Text = (aux3.ElementAt(4).Value.GetLight()).ToString();

        }

        private void Form5_Load(object sender, EventArgs e)
        {

        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = programa.getPrograma().getHeart();
            Form2 f2 = new Form2();
            HeartBeatMetrics hbm = new HeartBeatMetrics(path, programa.getPrograma());
            f2.CarregaDados(hbm);
            f2.ShowDialog();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = programa.getPrograma().getSleep();
            Form3 f3 = new Form3();
            SleepMetrics sbm = new SleepMetrics(path, programa.getPrograma());
            f3.CarregaDados(sbm);
            f3.ShowDialog();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = programa.getPrograma().getSteps();
            Form4 f4 = new Form4();
            StepsMetrics stm = new StepsMetrics(path, programa.getPrograma());
            f4.CarregaDados(stm);
            f4.ShowDialog();
        }

        private void BackButton_Click(object sender, EventArgs e)
        {
            this.Hide();
            Form1 f1 = new Form1();
            f1.ShowDialog();
        }

        private void button5_Click(object sender, EventArgs e)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Hide();
            string path = programa.getPrograma().getSleep();
            string path1 = programa.getPrograma().getSteps();
            SleepMetrics spm = new SleepMetrics(path, programa.getPrograma());
            StepsMetrics stm = new StepsMetrics(path1, programa.getPrograma());
            StepsForSleep aux = new StepsForSleep(spm, stm, programa.getPrograma());
            Form6 f6 = new Form6();
            f6.CarregaDados(aux);
            f6.ShowDialog();
        }

        public DateTime DateAndTime(double unixTimeStamp)
        {
            // Unix timestamp is seconds past epoch
            System.DateTime dtDateTime = new DateTime(1970, 1, 1, 0, 0, 0, 0, System.DateTimeKind.Utc);
            dtDateTime = dtDateTime.AddSeconds(unixTimeStamp).ToLocalTime();
            return dtDateTime;
        }

        private void label25_Click(object sender, EventArgs e)
        {

        }

        private void Form5_FormClosed(object sender, FormClosedEventArgs e)
        {
            Application.Exit();
        }
    }
}
