using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SistemaDeSensorizaçao.src
{
    /* Classe para guardar os valores de HB associados a um sleep
     */
    public class PairStepsSleep
    {
        
        private StepsMetrics steps_m = null;
        private PullSleep sleep = null;
        private double total_steps = 0;

        /* Class COnstructors */
        public PairStepsSleep(int time_sleep, int end_time, int awake, int deep, int light)
        {
            steps_m = new StepsMetrics();
            sleep = new PullSleep(time_sleep, end_time, awake, deep, light);
        }

        public PairStepsSleep(StepsMetrics steps, int start_time, int end_time, PullSleep ps)
        {
            steps_m = new StepsMetrics(steps, start_time, end_time);
            sleep = new PullSleep(ps);
            total_steps = steps_m.TotalSteps();
        }

        /* Gets */
        public StepsMetrics GetStepsMetrics() { return steps_m; }
        public double GetAverage_Intensity() { return steps_m.GetAverage_Intensity(); }
        public double GetAverage_Steps() { return steps_m.GetAverage_Steps(); }
        public double GetStd_Intensity() { return steps_m.GetStd_Intensity(); }
        public double GetStd_Steps() { return steps_m.GetStd_Steps(); }
        public int GetSleep() { return sleep.GetSleep(); }
        public int GetEndTime() { return sleep.GetEndTime(); }
        public int GetAwake() { return sleep.GetAwake(); }
        public int GetDeep() { return sleep.GetDeep(); }
        public int GetLight() { return sleep.GetLight(); }
        public double GetTotalSteps() { return total_steps; }

        public override string ToString()
        {
            return "Steps For Sleep\n" + sleep.ToString() + "\n" + steps_m.TotalSteps();
        }

    }

    public class StepsForSleep
    {
        /* Start time SLEEP - Metrics Steps for 1 PullSleep */
        private Dictionary<Int32, PairStepsSleep> stepsForSleep = null;
        /* Intensity */
        /*
        Double average_intensity = 0;
        Double std_intensity = 0;
        */
        /* Steps */
        Double average_steps = 0;
        Double std_steps = 0;
        /* total steps */
        double total_steps = 0;
        private SistemaSensorizacao programa;

        /* Class Constructor For Steps before Sleep*/
        public StepsForSleep(SleepMetrics spm, StepsMetrics steps_m, SistemaSensorizacao ss)
        {
            programa = ss;
            stepsForSleep = new Dictionary<int, PairStepsSleep>();
            int start_time = 0;
            foreach (KeyValuePair<Int32, PullSleep> sp in spm.GetDictionary())
            {
                PairStepsSleep pairStepsS = new PairStepsSleep(steps_m, start_time, sp.Key, sp.Value);
                start_time = sp.Value.GetEndTime();
                if (pairStepsS.GetStepsMetrics().GetDictionary().Count > 0)
                {
                    stepsForSleep.Add(sp.Key, pairStepsS);
                    total_steps += pairStepsS.GetTotalSteps();
                }
            }
        }

        /* Gets */
        public Dictionary<Int32, PairStepsSleep> GetDictionary() { return new Dictionary<Int32, PairStepsSleep>(stepsForSleep); }
        /* Gets Intensity */
        /*
        public Double GetAverage_Intensity() { if (average_intensity == 0) Average_Intensity(); return average_intensity; }
        public Double GetStd_Intensity() { if (std_intensity == 0) Std_Intensity(); return std_intensity; }
        public Double GetAverage_Intensity(int time_stamp)
        {
            if (stepsForSleep.ContainsKey(time_stamp))
            {
                PairStepsSleep pair = null;
                stepsForSleep.TryGetValue(time_stamp, out pair);
                return pair.GetAverage_Intensity();
            }
            return 0;
        }
        public Double GetStd_Intensity(int time_stamp)
        {
            if (stepsForSleep.ContainsKey(time_stamp))
            {
                PairStepsSleep pair = null;
                stepsForSleep.TryGetValue(time_stamp, out pair);
                return pair.GetStd_Intensity();
            }
            return 0;
        }
        */
        /* Gets Steps */
        public Double GetAverage_Steps() { if (average_steps == 0) Average_Steps(); return average_steps; }
        public Double GetStd_Steps() { if (std_steps == 0) Std_Steps(); return std_steps; }
        public SistemaSensorizacao GetPrograma()
        {
            return programa;
        }
        /* Média associada a cada sleep */
        public Double GetAverage_Steps(int time_stamp)
        {
            if (stepsForSleep.ContainsKey(time_stamp))
            {
                PairStepsSleep pair = null;
                stepsForSleep.TryGetValue(time_stamp, out pair);
                return pair.GetTotalSteps();
            }
            return 0;
        }
        /* Desvio padrão associado a cada sleep*/
        public Double GetStd_Steps(int time_stamp)
        {
            if (stepsForSleep.ContainsKey(time_stamp))
            {
                PairStepsSleep pair = null;
                stepsForSleep.TryGetValue(time_stamp, out pair);
                return pair.GetStd_Steps();
            }
            return 0;
        }

        /* Methods */
        /* Intensity */
        /*
        public Double Average_Intensity()
        {
            if (stepsForSleep.Count == 0)
                return 0;
            Double r = 0;

            foreach (KeyValuePair<Int32, PairStepsSleep> steps_sleep in stepsForSleep)
            {
                r += (Double)steps_sleep.Value.GetAverage_Intensity();
            }

            r /= (Double)stepsForSleep.Count;

            average_intensity = r;

            return r;
        }

        public Double Std_Intensity()
        {
            if (stepsForSleep.Count == 0)
                return 0;
            if (average_intensity == 0)
                Average_Intensity();

            Double r = 0, aux = 0;

            foreach (KeyValuePair<Int32, PairStepsSleep> steps_sleep in stepsForSleep)
            {
                aux = (Double)steps_sleep.Value.GetAverage_Intensity() - average_intensity;
                r += aux * aux;
            }
            r /= (Double)stepsForSleep.Count;

            r = Math.Sqrt(r);

            std_intensity = r;

            return r;
        }

        /* Top dá média de HB de todos os dias */
        public List<KeyValuePair<int, PairStepsSleep>> TopHigher(int top)
        {
            var aux = new SortedDictionary<Int32, PairStepsSleep>(stepsForSleep);
            List<KeyValuePair<int, PairStepsSleep>> sorted = (from kv in aux orderby kv.Value.GetTotalSteps() descending select kv).ToList();
            return sorted.GetRange(0, top);
        }
        public List<KeyValuePair<int, PairStepsSleep>> TopInferior(int top)
        {
            var aux = new SortedDictionary<Int32, PairStepsSleep>(stepsForSleep);
            List<KeyValuePair<int, PairStepsSleep>> sorted = (from kv in aux orderby kv.Value.GetTotalSteps() ascending select kv).ToList();
            return sorted.GetRange(0, top);
        }


        /* Steps */
        /* Valores globais de todos os intervalos */
        public Double Average_Steps()
        {
            if (stepsForSleep.Count == 0)
                return 0;
            Double r = 0;

            foreach (KeyValuePair<Int32, PairStepsSleep> steps_sleep in stepsForSleep)
            {
                r += (Double)steps_sleep.Value.GetTotalSteps();
            }

            r /= (Double)stepsForSleep.Count;

            average_steps = r;

            return r;
        }
        /* Valores globais de todos os intervalos */
        public Double Std_Steps()
        {
            if (stepsForSleep.Count == 0)
                return 0;
            if (average_steps == 0)
                Average_Steps();

            Double r = 0, aux = 0;

            foreach (KeyValuePair<Int32, PairStepsSleep> steps_sleep in stepsForSleep)
            {
                aux = (Double)steps_sleep.Value.GetTotalSteps() - average_steps;
                r += aux * aux;
            }
            r /= (Double)stepsForSleep.Count;

            r = Math.Sqrt(r);

            std_steps = r;

            return r;
        }

        public override string ToString()
        {
            MemoryStream stream = new MemoryStream();
            StreamWriter writer = new StreamWriter(stream);
            StreamReader reader = new StreamReader(stream);
            foreach (KeyValuePair<Int32, PairStepsSleep> steps_sleep in stepsForSleep)
            {
                string s = "< Start Time " + steps_sleep.Key + " >\n" + steps_sleep.Value.ToString() + "< End Time " + steps_sleep.Value.GetEndTime() + " >\n";
                writer.Write(s);
                writer.Flush();
            }
            stream.Position = 0;
            return reader.ReadToEnd();
        }
    }
}