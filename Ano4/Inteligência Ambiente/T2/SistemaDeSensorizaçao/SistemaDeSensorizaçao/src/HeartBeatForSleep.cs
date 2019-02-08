using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SistemaDeSensorizaçao.src
{
    public class PairHeartBeatSleep
    {

        private HeartBeatMetrics hb_m = null;
        private PullSleep sleep = null;

        /* Class COnstructors */
        public PairHeartBeatSleep(int time_sleep, int end_time, int awake, int deep, int light)
        {
            hb_m = new HeartBeatMetrics();
            sleep = new PullSleep(time_sleep, end_time, awake, deep, light);
        }

        public PairHeartBeatSleep(HeartBeatMetrics hbm, int start_time, PullSleep ps)
        {
            hb_m = new HeartBeatMetrics(hbm, start_time, ps.GetEndTime());
            sleep = new PullSleep(ps);
        }

        public PairHeartBeatSleep(HeartBeatMetrics hbm, int start_time, int end_time, PullSleep ps)
        {
            hb_m = new HeartBeatMetrics(hbm, start_time, ps.GetEndTime());
            sleep = new PullSleep(ps);
        }
        /* Gets */
        public HeartBeatMetrics GetHeartBeatMetrics() { return hb_m.Clone(); }
        public Double GetAverage() { return hb_m.GetAverage(); }
        public Double GetStd() { return hb_m.GetStd(); }
        public int GetSleep() { return sleep.GetSleep(); }
        public int GetEndTime() { return sleep.GetEndTime(); }
        public int GetAwake() { return sleep.GetAwake(); }
        public int GetDeep() { return sleep.GetDeep(); }
        public int GetLight() { return sleep.GetLight(); }
        public List<KeyValuePair<int, int>> GetTopHigher(int top) { return hb_m.TopHigher(top); }
        public List<KeyValuePair<int, int>> GetTopInferior(int top) { return hb_m.TopInferior(top); }

        public override string ToString()
        {
            return "Heart Beat For Sleep\n" + sleep.ToString() + "\n" + hb_m.ToString();
        }
    }

    public class HeartBeatForSleep
    {
        /* Start time SLEEP - Metrics HeartBeat for 1 PullSleep */
        /* Neste dicionário são guardados os valores dos batimentos cardíacos que foram
         * lidos no período de sono, correspondente ao intervalo [start_time,end_time] do sleep associado. 
         */
        private Dictionary<Int32, PairHeartBeatSleep> heartBeatWhileSleep = null;
        /* De igual modo que o dicionario While, mas este guarda os valores dos batimentos para o período
         * de tempo antes do sono.
         */
        private Dictionary<Int32, PairHeartBeatSleep> heartBeatBeforSleep = null;

        Double average_while = 0;
        Double std_while = 0;

        Double average_befor = 0;
        Double std_befor = 0;
        SistemaSensorizacao programa;

        /* Class Constructor For HeartBeat Sleep*/
        public HeartBeatForSleep(SleepMetrics spm, HeartBeatMetrics hbm, SistemaSensorizacao ss)
        {
            programa = ss;
            // While Sleep
            heartBeatWhileSleep = new Dictionary<int, PairHeartBeatSleep>();
            foreach (KeyValuePair<Int32, PullSleep> sp in spm.GetDictionary())
            {
                // sp.Key -> start time sleep; sp.Value.GetEndTime() -> end time sleep
                PairHeartBeatSleep pairHBS = new PairHeartBeatSleep(hbm, sp.Key, sp.Value);
                if (pairHBS.GetHeartBeatMetrics().GetDictionary().Count > 0)
                    heartBeatWhileSleep.Add(sp.Key, pairHBS);
            }

            // Befor Sleep
            heartBeatBeforSleep = new Dictionary<int, PairHeartBeatSleep>();
            int start_time = 0;
            foreach (KeyValuePair<Int32, PullSleep> sp in spm.GetDictionary())
            {
                //start time = 0 or = last end time; sp.Key = start time sleep
                PairHeartBeatSleep pairHBS = new PairHeartBeatSleep(hbm, start_time, sp.Key, sp.Value);
                start_time = sp.Value.GetEndTime();
                if (pairHBS.GetHeartBeatMetrics().GetDictionary().Count > 0)
                    heartBeatBeforSleep.Add(sp.Key, pairHBS);
            }
        }

        /* Gets */
        public Dictionary<Int32, PairHeartBeatSleep> GetDictionaryWhile() { return new Dictionary<Int32, PairHeartBeatSleep>(heartBeatWhileSleep); }
        public Dictionary<Int32, PairHeartBeatSleep> GetDictionaryBefor() { return new Dictionary<Int32, PairHeartBeatSleep>(heartBeatBeforSleep); }

        /* ------------------------ WHILE ------------------------ */
        /* Média de todos os valores */
        public Double GetAverageWhile() { if (average_while == 0) AverageWhile(); return average_while; }
        /* Desvio padrão de todos os valores */
        public Double GetStdWhile() { if (std_while == 0) StdWhile(); return std_while; }

        /* ------------------------ BEFOR ------------------------ */
        /* Média de todos os valores */
        public Double GetAverageBefor() { if (average_befor == 0) AverageBefor(); return average_befor; }
        /* Desvio padrão de todos os valores */
        public Double GetStdBefor() { if (std_befor == 0) StdBefor(); return std_befor; }
        public SistemaSensorizacao getPrograma() { return programa; }
        /* ------------------------ WHILE ------------------------
        /* Média de um dado sleep, em que o time_stamp é o start_time do sleep */
        public Double GetAverageWhile(int time_stamp)
        {
            if (heartBeatWhileSleep.ContainsKey(time_stamp))
            {
                PairHeartBeatSleep pair = null;
                heartBeatWhileSleep.TryGetValue(time_stamp, out pair);
                return pair.GetAverage();
            }
            return 0;
        }
        /* Desvio padrão de um dado sleep, em que o time_stamp é o start_time do sleep */
        public Double GetStdWhile(int time_stamp)
        {
            if (heartBeatWhileSleep.ContainsKey(time_stamp))
            {
                PairHeartBeatSleep pair = null;
                heartBeatWhileSleep.TryGetValue(time_stamp, out pair);
                return pair.GetStd();
            }
            return 0;
        }
        /* Top dos valores mais altos dos batimentos cardíacos de um dado sleep, 
         * em que o time_stamp indica o sleep, sendo que o time_stamp é o start_time do sleep;
         * a variável de input top indica o tamanho da lista que é devolvida.
         */
        public List<KeyValuePair<int, int>> GetTopHigherWhile(int time_stamp, int top)
        {
            heartBeatWhileSleep.TryGetValue(time_stamp, out PairHeartBeatSleep pair);
            return pair.GetTopHigher(top);
        }
        /* Top dos valores mais baixos, funciona de modo semelhante ao método anterior*/
        public List<KeyValuePair<int, int>> GetTopInferiorWhile(int time_stamp, int top)
        {
            heartBeatWhileSleep.TryGetValue(time_stamp, out PairHeartBeatSleep pair);
            return pair.GetTopInferior(top);
        }


        /* ------------------------ BEFOR ------------------------ */
        /* Média de um dado sleep, em que o time_stamp é o start_time do sleep */
        public Double GetAverageBefor(int time_stamp)
        {
            if (heartBeatBeforSleep.ContainsKey(time_stamp))
            {
                PairHeartBeatSleep pair = null;
                heartBeatBeforSleep.TryGetValue(time_stamp, out pair);
                return pair.GetAverage();
            }
            return 0;
        }
        /* Desvio padrão de um dado sleep, em que o time_stamp é o start_time do sleep */
        public Double GetStdBefor(int time_stamp)
        {
            if (heartBeatBeforSleep.ContainsKey(time_stamp))
            {
                PairHeartBeatSleep pair = null;
                heartBeatBeforSleep.TryGetValue(time_stamp, out pair);
                return pair.GetStd();
            }
            return 0;
        }
        /* Top dos valores mais altos dos batimentos cardíacos de um dado sleep, 
        * em que o time_stamp indica o sleep, sendo que o time_stamp é o start_time do sleep;
        * a variável de input top indica o tamanho da lista que é devolvida.
        */
        public List<KeyValuePair<int, int>> GetTopHigherBefor(int time_stamp, int top)
        {
            heartBeatBeforSleep.TryGetValue(time_stamp, out PairHeartBeatSleep pair);
            return pair.GetTopHigher(top);
        }
        /* Top dos valores mais baixos, funciona de modo semelhante ao método anterior*/
        public List<KeyValuePair<int, int>> GetTopInferiorBefor(int time_stamp, int top)
        {
            heartBeatBeforSleep.TryGetValue(time_stamp, out PairHeartBeatSleep pair);
            return pair.GetTopInferior(top);
        }

        /* Top dá média de HB de todos os dias */
        public List<KeyValuePair<int, PairHeartBeatSleep>> TopHigherBefor(int top)
        {
            var aux = new SortedDictionary<Int32, PairHeartBeatSleep>(heartBeatBeforSleep);
            List<KeyValuePair<int, PairHeartBeatSleep>> sorted = (from kv in aux orderby kv.Value.GetAverage() descending select kv).ToList();
            return sorted.GetRange(0, top);
        }
        public List<KeyValuePair<int, PairHeartBeatSleep>> TopInferiorBefor(int top)
        {
            var aux = new SortedDictionary<Int32, PairHeartBeatSleep>(heartBeatBeforSleep);
            List<KeyValuePair<int, PairHeartBeatSleep>> sorted = (from kv in aux orderby kv.Value.GetAverage() ascending select kv).ToList();
            return sorted.GetRange(0, top);
        }

        public List<KeyValuePair<int, PairHeartBeatSleep>> TopHigherWhile(int top)
        {
            var aux = new SortedDictionary<Int32, PairHeartBeatSleep>(heartBeatWhileSleep);
            List<KeyValuePair<int, PairHeartBeatSleep>> sorted = (from kv in aux orderby kv.Value.GetAverage() descending select kv).ToList();
            return sorted.GetRange(0, top);
        }
        public List<KeyValuePair<int, PairHeartBeatSleep>> TopInferiorWhile(int top)
        {
            var aux = new SortedDictionary<Int32, PairHeartBeatSleep>(heartBeatWhileSleep);
            List<KeyValuePair<int, PairHeartBeatSleep>> sorted = (from kv in aux orderby kv.Value.GetAverage() ascending select kv).ToList();
            return sorted.GetRange(0, top);
        }


        /* ------------------------ WHILE ------------------------ */
        /* Média global, pode ser usada directamente, há alguma redondáncia com a função GetAverageWhile()*/
        public Double AverageWhile()
        {
            if (heartBeatWhileSleep.Count == 0)
                return 0;
            Double r = 0;

            foreach (KeyValuePair<Int32, PairHeartBeatSleep> hb_sleep in heartBeatWhileSleep)
            {
                r += (Double)hb_sleep.Value.GetAverage();
            }

            r /= (Double)heartBeatWhileSleep.Count;

            average_while = r;

            return r;
        }
        /* Desvio padrão global */
        public Double StdWhile()
        {
            if (heartBeatWhileSleep.Count == 0)
                return 0;
            if (average_while == 0)
                AverageWhile();

            Double r = 0, aux = 0;

            foreach (KeyValuePair<Int32, PairHeartBeatSleep> hb_sleep in heartBeatWhileSleep)
            {
                aux = (Double)hb_sleep.Value.GetAverage() - average_while;
                r += aux * aux;
            }
            r /= (Double)heartBeatWhileSleep.Count;

            r = Math.Sqrt(r);

            std_while = r;

            return r;
        }


        /* ------------------------ BEFOR ------------------------ */
        /* Média global, pode ser usada directamente, há alguma redondáncia com a função GetAverageBefor()*/
        public Double AverageBefor()
        {
            if (heartBeatBeforSleep.Count == 0)
                return 0;
            Double r = 0;

            foreach (KeyValuePair<Int32, PairHeartBeatSleep> hb_sleep in heartBeatBeforSleep)
            {
                r += (Double)hb_sleep.Value.GetAverage();
            }

            r /= (Double)heartBeatBeforSleep.Count;

            average_befor = r;

            return r;
        }
        /* Desvio padrão global */
        public Double StdBefor()
        {
            if (heartBeatBeforSleep.Count == 0)
                return 0;
            if (average_befor == 0)
                AverageBefor();

            Double r = 0, aux = 0;

            foreach (KeyValuePair<Int32, PairHeartBeatSleep> hb_sleep in heartBeatBeforSleep)
            {
                aux = (Double)hb_sleep.Value.GetAverage() - average_befor;
                r += aux * aux;
            }
            r /= (Double)heartBeatBeforSleep.Count;

            r = Math.Sqrt(r);

            std_befor = r;

            return r;
        }

        public override string ToString()
        {
            MemoryStream stream = new MemoryStream();
            StreamWriter writer = new StreamWriter(stream);
            StreamReader reader = new StreamReader(stream);
            writer.Write("\n********************************************************************************\n" +
                         "************************************  BEFOR ************************************\n" +
                         "********************************************************************************\n");
            writer.Flush();
            foreach (KeyValuePair<Int32, PairHeartBeatSleep> hb_sp in heartBeatBeforSleep)
            {
                string s = "< Start Time " + hb_sp.Key + " >\n" + hb_sp.Value.ToString() + "< End Time " + hb_sp.Value.GetEndTime() + " >\n";
                writer.Write(s);
                writer.Flush();
            }
            writer.Write("\n********************************************************************************\n" +
                         "************************************  WHILE ************************************\n" +
                         "********************************************************************************\n");
            writer.Flush();
            foreach (KeyValuePair<Int32, PairHeartBeatSleep> hb_sp in heartBeatWhileSleep)
            {
                string s = "< Start Time " + hb_sp.Key + " >\n" + hb_sp.Value.ToString() + "< End Time " + hb_sp.Value.GetEndTime() + " >\n";
                writer.Write(s);
                writer.Flush();
            }
            stream.Position = 0;
            return reader.ReadToEnd();
        }
    }
}

