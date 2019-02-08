using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SistemaDeSensorizaçao.src
{
    public class PullSteps
    {
        double intensity = 0;
        double steps = 0;
        double category = 0;

        public PullSteps(double intensity, double steps)
        {
            this.intensity = intensity;
            this.steps = steps;

        }

        /* Gets */
        public double GetIntensity() { return intensity; }
        public double GetSteps() { return steps; }
        public double GetCategory() { return category; }

        public override string ToString()
        {
            string s = "intensity =" + intensity + "; steps=" + steps + "; category=" + category;
            return s;
        }

    }

    public class StepsMetrics
    {
        /* PullSteps order by date */
        Dictionary<int, PullSteps> time_steps = null;
        Dictionary<DateTime, PullSteps> date_steps = null;
        Dictionary<DateTime, int> count_date_steps = null;
        /* Average of all pullSteps */
        double average_intensity = 0;
        double average_steps = 0;
        /* Average of all pullSteps */
        double std_intensity = 0;
        double std_steps = 0;
        /* Total steps */
        double total_steps = 0;
        SistemaSensorizacao programa;

        public StepsMetrics()
        {
            time_steps = new Dictionary<int, PullSteps>();
            date_steps = new Dictionary<DateTime, PullSteps>();
            count_date_steps = new Dictionary<DateTime, int>();
        }

        public StepsMetrics(string file, SistemaSensorizacao ss)
        {
            programa = ss;
            time_steps = new Dictionary<int, PullSteps>();
            date_steps = new Dictionary<DateTime, PullSteps>();
            count_date_steps = new Dictionary<DateTime, int>();
            ImportFromCSV(file);
            Average();
            Std();
        }

        public StepsMetrics(StepsMetrics steps_m, int start_time, int end_time)
        {
            time_steps = new Dictionary<int, PullSteps>();
            date_steps = new Dictionary<DateTime, PullSteps>();
            ImportData(steps_m, start_time, end_time);
        }

        public void ImportData(StepsMetrics steps_m, int start_time, int end_time)
        {
            total_steps = 0;
            foreach (KeyValuePair<Int32, PullSteps> steps in steps_m.GetDictionary())
            {
                if (steps.Key >= start_time && steps.Key <= end_time)
                {
                    time_steps.Add(steps.Key, steps.Value);
                    total_steps += steps.Value.GetSteps();
                }

            }
        }

        /* Get Dictionary */
        public Dictionary<int, PullSteps> GetDictionary() { return time_steps; }
        public Dictionary<DateTime, PullSteps> GetDictionary2() { return date_steps; }

        /* Get Average */
        public double GetAverage_Intensity() { return average_intensity; }
        public double GetAverage_Steps() { return average_steps; }

        public Dictionary<DateTime, PullSteps> GetStepsByDay()
        {
            return date_steps;
        }

        /* Get Std */
        public double GetStd_Intensity() { return std_intensity; }
        public double GetStd_Steps() { return std_steps; }
        public SistemaSensorizacao getPrograma() { return programa; }

        /* Import data */
        public void ImportFromCSV(string fileName)
        {
            using (var reader = new StreamReader(fileName))
            {
                reader.ReadLine();//ignore the first line
                while (!reader.EndOfStream)
                {
                    var line = reader.ReadLine();
                    /* line : "time,intensity,steps"*/
                    var values = line.Split(';');

                    int time = Int32.Parse(values[0]);
                    var date = DateAndTime(time).Date;
                    PullSteps pullstep = new PullSteps(Int32.Parse(values[1]), Int32.Parse(values[2]));
                    time_steps.Add(time, pullstep);

                    if (!date_steps.ContainsKey(date))
                    {
                        date_steps.Add(date, pullstep);
                        count_date_steps.Add(date, 1);
                    }
                    else
                    {
                        double intensity = date_steps[date].GetIntensity() + pullstep.GetIntensity();
                        double steps = date_steps[date].GetSteps() + pullstep.GetSteps();
                        PullSteps novo = new PullSteps(intensity, steps);
                        date_steps[date] = novo;
                        count_date_steps[date]++;

                    }
                }
            }
        }
        /* Number of total steps */
        public double TotalSteps() { return total_steps; }

        public Dictionary<DateTime, PullSteps> Average_by_day()
        {
            Dictionary<DateTime, PullSteps> average_by_day = new Dictionary<DateTime, PullSteps>();
            foreach (KeyValuePair<DateTime, PullSteps> pair in date_steps)
            {

                double average_intensity = (double) pair.Value.GetIntensity() / count_date_steps[pair.Key];
                double average_steps = (double) pair.Value.GetSteps() / count_date_steps[pair.Key];
                PullSteps novo = new PullSteps(average_intensity, average_steps);
                average_by_day.Add(pair.Key, novo);
            }
            foreach (KeyValuePair<DateTime, PullSteps> pair in average_by_day)
                System.Console.WriteLine("Average Steps in " + pair.Key + ": " + pair.Value.GetSteps());

            return average_by_day;
        }
        
        /* Average of all variables */
        public void Average()
        {
            double sum_intensity = 0;

            foreach (KeyValuePair<int, PullSteps> pair in time_steps)
            {
                sum_intensity += pair.Value.GetIntensity();
                total_steps += pair.Value.GetSteps();
                //  System.Console.WriteLine("Steps in " + pair.Key + ": " + pair.Value.GetSteps());

            }
            average_intensity = sum_intensity / time_steps.Count;
            average_steps = total_steps / time_steps.Count;
        }
        
        /* Standard derivation of all variables */
        public void Std()
        {
            /* First compute sum[(x-xmedian)^2] */
            double numerator_intensity = 0;
            double numerator_steps = 0;

            foreach (KeyValuePair<int, PullSteps> pair in time_steps)
            {
                numerator_intensity += (double)Math.Pow(pair.Value.GetIntensity() - average_intensity, 2);
                numerator_steps += (int)Math.Pow(pair.Value.GetSteps() - average_steps, 2);
            }

            std_intensity = Math.Sqrt((double)numerator_intensity / (time_steps.Count - 1));
            std_steps = Math.Sqrt((double)numerator_steps / (time_steps.Count - 1));
        }

        /* Top 5 Steps */
        public IEnumerable<KeyValuePair<int, PullSteps>> Top5(int x)
        {
            /* Sort by step value */
            var sortedDict = from entry in time_steps orderby entry.Value.GetSteps() descending select entry;
            var sortedDict2 = from entry in time_steps orderby entry.Value.GetSteps() ascending select entry;

            /* Take 5 first elements */
            IEnumerable<KeyValuePair<int,PullSteps>> newdic = sortedDict.Take(5);
            IEnumerable<KeyValuePair<int, PullSteps>> newdic2 = sortedDict2.Take(5);

            
            if (x == 1)
            {
                System.Console.WriteLine("---- Top5 Best Steps ----");
                foreach (KeyValuePair<int, PullSteps> pair in newdic)
                {
                    System.Console.WriteLine("Time: " + pair.Key +
                                             "| Intensity: " + pair.Value.GetIntensity() +
                                             "| Steps: " + pair.Value.GetSteps());
                }
                return newdic;
            }
            
            if (x == 2)
            {
                
                System.Console.WriteLine("---- Top5 Worst Steps ----");
                foreach (KeyValuePair<int, PullSteps> pair in newdic2)
                {
                    System.Console.WriteLine("Time: " + pair.Key +
                                             "| Intensity: " + pair.Value.GetIntensity() +
                                             "| Steps: " + pair.Value.GetSteps());
                }
                return newdic2;
            } 
            return null;
        }
        
        /* Convert timestamp to date and time format */
        public static DateTime DateAndTime(double unixTimeStamp)
        {
            // Unix timestamp is seconds past epoch
            System.DateTime dtDateTime = new DateTime(1970, 1, 1, 0, 0, 0, 0, System.DateTimeKind.Utc);
            dtDateTime = dtDateTime.AddSeconds(unixTimeStamp).ToLocalTime();
            return dtDateTime;
        }
        
        /* To String */
        public override string ToString()
        {
            MemoryStream stream = new MemoryStream();
            StreamWriter writer = new StreamWriter(stream);
            StreamReader reader = new StreamReader(stream);
            foreach (KeyValuePair<int, PullSteps> steps in time_steps)
            {
                string s = "<" + steps.Key + "; " + steps.Value.ToString() + ">\n";
                writer.Write(s);
                writer.Flush();
            }
            stream.Position = 0;
            return reader.ReadToEnd();
        }
    }
}