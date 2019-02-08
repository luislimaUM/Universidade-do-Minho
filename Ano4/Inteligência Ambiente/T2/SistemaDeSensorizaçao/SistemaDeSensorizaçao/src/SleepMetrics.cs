using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SistemaDeSensorizaçao.src
{
    public class PullSleep{ 
    //int start_time = 0;
    int end_time = 0;
    int awake = 0, deep = 0;
    int light = 0;
    int sleep = 0;

    public PullSleep(int sleep, int end_time, int awake, int deep, int light)
    {
        this.sleep = sleep;
        this.end_time = end_time;
        this.awake = awake;
        this.deep = deep;
        this.light = light;
    }
    public PullSleep(int end_time, int awake, int deep, int light)
    {
        this.end_time = end_time;
        this.awake = awake;
        this.deep = deep;
        this.light = light;
    }
    public PullSleep(PullSleep ps)
    {
        end_time = ps.GetEndTime();
        awake = ps.GetAwake();
        deep = ps.GetDeep();
        light = ps.GetLight();
        sleep = ps.GetSleep();
    }

    /* Gets */
    public int GetEndTime() { return end_time; }
    public int GetAwake() { return awake; }
    public int GetDeep() { return deep; }
    public int GetLight() { return light; }
    public int GetSleep() { return sleep; }

    public override string ToString()
    {
        string s = "end_time =" + end_time + "; awake=" + awake + "; deep=" + deep + "; light=" + light;
        return s;
    }
}

    public class SleepMetrics
    {
        SistemaSensorizacao sistema;
        /* PullSleep order by time */
        Dictionary<Int32, PullSleep> startTime_sleep = null;
        /* Average of all metrics in pullSleep */
        double average_timeofsleep = 0;
        double average_starttime = 0;
        double average_endtime = 0;
        double average_awake = 0;
        double average_deep = 0;
        double average_light = 0;
        /* Std of all metrics pullSleep */
        int std_timeofsleep = 0;
        double std_starttime = 0;
        double std_endtime = 0;
        double std_awake = 0;
        double std_deep = 0;
        double std_light = 0;

        public SleepMetrics()
        {
            startTime_sleep = new Dictionary<int, PullSleep>();
        }

        public SleepMetrics(string file,SistemaSensorizacao ss)
        {
            sistema = ss;
            startTime_sleep = new Dictionary<int, PullSleep>();
            ImportFromCSV(file);
        }

        public Dictionary<int, PullSleep> GetDictionary() { return new Dictionary<int, PullSleep>(startTime_sleep); }

        /* Gets Average */
        public double GetAverage_Timeofsleep() { return average_timeofsleep; }
        public double GetAverage_Starttime() { return average_starttime; }
        public double GetAverage_Endtime() { return average_endtime; }
        public double GetAverage_Awake() { return average_awake; }
        public double GetAverage_Deep() { return average_deep; }
        public double GetAverage_Light() { return average_light; }

        /* Gets Std`*/
        public double GetStd_Timeofsleep() { return std_timeofsleep; }
        public double GetStd_Starttime() { return std_starttime; }
        public double GetStd_Endtime() { return std_endtime; }
        public double GetStd_Awake() { return std_awake; }
        public double GetStd_Deep() { return std_deep; }
        public double GetStd_Light() { return std_light; }
        public SistemaSensorizacao GetPrograma() { return sistema; }

        /* Import data */
        public void ImportFromCSV(string fileName)
        {
            using (var reader = new StreamReader(fileName))
            {
                reader.ReadLine();
                while (!reader.EndOfStream)
                {
                    var line = reader.ReadLine();
                    var values = line.Split(';');

                    int time = Int32.Parse(values[1]);

                    // public PullSleep(int sleep, int end_time, int awake, int deep, int light)
                    PullSleep pullstep = new PullSleep(Int32.Parse(values[2]), Int32.Parse(values[3]), Int32.Parse(values[4]), Int32.Parse(values[5]));
                    startTime_sleep.Add(time, pullstep);
                }
            }
        }
        /* Average of all variables */
        public void Average()
        {
            int sum_timeofsleep = 0;
            double sum_starttime = 0;
            double sum_endtime = 0;
            int sum_awake = 0;
            int sum_deep = 0;
            int sum_light = 0;

            foreach (KeyValuePair<Int32, PullSleep> pair in startTime_sleep)
            {

                sum_starttime += (double)pair.Key;
                sum_endtime += (double)pair.Value.GetEndTime();
                sum_timeofsleep += (pair.Value.GetEndTime() - pair.Key);
                sum_awake += pair.Value.GetAwake();
                sum_deep += pair.Value.GetDeep();
                sum_light += pair.Value.GetLight();
            }
            average_timeofsleep = (double) sum_timeofsleep / startTime_sleep.Count;
            average_starttime = sum_starttime / startTime_sleep.Count;
            average_endtime = sum_endtime / startTime_sleep.Count;
            average_awake = (double) sum_awake / startTime_sleep.Count;
            average_deep = (double) sum_deep / startTime_sleep.Count;
            average_light = (double) sum_light / startTime_sleep.Count;

        }
        /* Standard derivation of all variables */
        public void Std()
        {
            /* First compute sum[(x-xmedian)^2] */
            int numerator_timeofsleep = 0;
            double numerator_starttime = 0;
            double numerator_endtime = 0;
            int numerator_awake = 0;
            int numerator_deep = 0;
            int numerator_light = 0;

            foreach (KeyValuePair<Int32, PullSleep> pair in startTime_sleep)
            {
                numerator_timeofsleep += (int)Math.Pow((pair.Value.GetEndTime() - pair.Key) - average_timeofsleep, 2);
                numerator_starttime += (double)Math.Pow(pair.Key - average_starttime, 2);
                numerator_endtime += (double)Math.Pow(pair.Value.GetEndTime() - average_endtime, 2);
                numerator_awake += (int)Math.Pow(pair.Value.GetAwake() - average_awake, 2);
                numerator_deep += (int)Math.Pow(pair.Value.GetDeep() - average_deep, 2);
                numerator_light += (int)Math.Pow(pair.Value.GetLight() - average_light, 2);
            }
            std_timeofsleep = (int)Math.Sqrt(numerator_timeofsleep / (startTime_sleep.Count - 1));
            std_starttime = (double)Math.Sqrt(numerator_starttime / (startTime_sleep.Count - 1));
            std_endtime = (double)Math.Sqrt(numerator_endtime / (startTime_sleep.Count - 1));
            std_awake = (double)Math.Sqrt(numerator_awake / (startTime_sleep.Count - 1));
            std_deep = (double)Math.Sqrt(numerator_deep / (startTime_sleep.Count - 1));
            std_light = (double)Math.Sqrt(numerator_light / (startTime_sleep.Count - 1));
        }
        /* Top 5 Sleep */
        
        public IEnumerable<KeyValuePair<int, PullSleep>> Top5(int x)
        {
            /* Dictionary + variable sleep */
            Dictionary<Int32, PullSleep> sleep = new Dictionary<int, PullSleep>();
            foreach (KeyValuePair<Int32, PullSleep> pair in startTime_sleep)
            {
                int sleep_variable = pair.Value.GetEndTime() - pair.Key; // (end_time-start_time)
                PullSleep n = new PullSleep(sleep_variable, pair.Value.GetEndTime(), pair.Value.GetAwake(),
                                            pair.Value.GetDeep(), pair.Value.GetLight());
                sleep.Add(pair.Key, n);
            }
            /* Sort by sleep value */
            var sortedDict = from entry in sleep orderby entry.Value.GetSleep() 
                             descending select entry;
            var sortedDict2 = from entry in sleep orderby entry.Value.GetSleep() 
                              ascending select entry;

            /* Take 5 first elements */
            IEnumerable<KeyValuePair<int, PullSleep>> newdic = sortedDict.Take(5);
            IEnumerable<KeyValuePair<int, PullSleep>> newdic2 = sortedDict2.Take(5);

            
            if (x == 1)
            {
                System.Console.WriteLine("---- Top5 Best Sleep ----");
                foreach (KeyValuePair<Int32, PullSleep> pair in newdic)
                {
                    System.Console.WriteLine("Sleep time: " + (double)pair.Value.GetSleep() / 60 + " min" +
                                             "| Start time: " + DateAndTime((double)pair.Key) +
                                             "| End time: " + DateAndTime(pair.Value.GetEndTime()) +
                                             "| Awake: " + (double)pair.Value.GetAwake() / 60 + " min" +
                                             "| Deep: " + (double)pair.Value.GetDeep() / 60 + " min" +
                                             "| Light: " + (double)pair.Value.GetLight() / 60 + " min");
                }
                return newdic;
            }
            if (x == 2)
            {
                System.Console.WriteLine("---- Top5 Worst Sleep ----");
                foreach (KeyValuePair<Int32, PullSleep> pair in newdic2)
                {
                    System.Console.WriteLine("Sleep time: " + (double)pair.Value.GetSleep() / 60 + " min" +
                                             "| Start time: " + DateAndTime((double)pair.Key) +
                                             "| End time: " + DateAndTime(pair.Value.GetEndTime()) +
                                             "| Awake: " + (double)pair.Value.GetAwake() / 60 + " min" +
                                             "| Deep: " + (double)pair.Value.GetDeep() / 60 + " min" +
                                             "| Light: " + (double)pair.Value.GetLight() / 60 + " min");
                }
                return newdic2;
            }
            
            return null;
        }

        /* Convert timestamp to date and time format */
        public DateTime DateAndTime(double unixTimeStamp)
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
            foreach (KeyValuePair<int, PullSleep> sleep in startTime_sleep)
            {
                string s = "<" + sleep.Key + "; " + sleep.Value.ToString() + ">\n";
                writer.Write(s);
                writer.Flush();
            }
            stream.Position = 0;
            return reader.ReadToEnd();
        }
    }
}
