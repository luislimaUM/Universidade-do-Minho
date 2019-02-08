using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SistemaDeSensorizaçao.src
{
    /*
     * Classe usada para as métricas dos batimentos cardíacos de 2 em 2 min,
     * ou conforme os ficheiros CSV.
     */
    public class HeartBeatMetrics
    {
        /* Heartbeat order by time 2 min */
        Dictionary<Int32, Int32> time_HrBeat = null;
        /* Average of all heartbeat */
        Double average = 0;
        /* Std of all heartbeat */
        Double std = 0;
        SistemaSensorizacao programa;

        /* Class Constructor */
        public HeartBeatMetrics()
        {
            time_HrBeat = new Dictionary<int, int>();
            average = 0;
            std = 0;
        }

        /* Import data from File CSV */
        public HeartBeatMetrics(string file,SistemaSensorizacao s)
        {
            programa = s;
            time_HrBeat = new Dictionary<int, int>();
            ImportFromCSV(file);
            GlobalStd();
        }

        /* Import data from HBM, with start_time and end_time*/
        public HeartBeatMetrics(HeartBeatMetrics hbm, int start_time, int end_time)
        {
            time_HrBeat = new Dictionary<int, int>();
            ImportData(hbm, start_time, end_time);
            GlobalStd();
        }

        /* Import all data from HBM */
        public HeartBeatMetrics(HeartBeatMetrics hbm)
        {
            time_HrBeat = new Dictionary<int, int>();
            ImportData(hbm);
            GlobalStd();
        }

        /* Gets */
        public Dictionary<int, int> GetDictionary() { return new Dictionary<int, int>(time_HrBeat); }
        public Double GetAverage() { if (average == 0) GlobalAverage(); return average; }
        public Double GetStd() { if (std == 0) GlobalStd(); return std; }
        public int GetSize() { return time_HrBeat.Count; }
        public SistemaSensorizacao GetPrograma() { return programa; }

        /* Global Metrics */

        public Double GlobalAverage()
        {
            if (time_HrBeat.Count == 0)
                return 0;
            Double r = 0;

            foreach (KeyValuePair<Int32, Int32> time in time_HrBeat)
            {
                r += (Double)time.Value;
            }

            r = r / time_HrBeat.Values.Count;

            average = r;

            return r;
        }

        public Double GlobalStd()
        {
            if (time_HrBeat.Count == 0)
                return 0;
            Double r = 0, aux = 0;

            if (average == 0)
                GlobalAverage();

            foreach (KeyValuePair<Int32, Int32> t in time_HrBeat)
            {
                aux = (Double)(t.Value - average);
                r += aux * aux;
            }

            r = Math.Sqrt(r / time_HrBeat.Count);

            std = r;

            return r;
        }

        /* Top mos higher HB */
        public List<KeyValuePair<int, int>> TopHigher(int top)
        {
            var aux = new SortedDictionary<Int32, Int32>(time_HrBeat);
            List<KeyValuePair<int, int>> sorted = (from kv in aux orderby kv.Value descending select kv).ToList();
            return sorted.GetRange(0, top);
        }

        /* Top last HB */
        public List<KeyValuePair<int, int>> TopInferior(int top)
        {
            var aux = new SortedDictionary<Int32, Int32>(time_HrBeat);
            List<KeyValuePair<int, int>> sorted = (from kv in aux orderby kv.Value ascending select kv).ToList();
            return sorted.GetRange(0, top);
        }

        /* Import data from CSV file*/
        public void ImportFromCSV(string fileName)
        {
            using (var reader = new StreamReader(fileName))
            {
                reader.ReadLine();//ignore the first line
                while (!reader.EndOfStream)
                {
                    var line = reader.ReadLine();
                    /* line : "time,heartbeat" */
                    var values = line.Split(';');

                    int time = Int32.Parse(values[0]);
                    int heartbeat = Int32.Parse(values[1]);

                    time_HrBeat.Add(time, heartbeat);
                }
            }
        }

        /* Import data for normal HeartBM, with start and end time */
        /* Função usada para criar as metricas HB por sleep 
         * Cada vez que é processado um sleep é criado um novo HBM 
         * em que o tempo inicial e o tempo final correspondem ao tempo decorrido
         * antes do sono ou durante o sono, ambos os modos estão implementados na classe HeartBeatFor Sleep
         */
        public void ImportData(HeartBeatMetrics hbm, int start_time, int end_time)
        {
            foreach (KeyValuePair<Int32, Int32> hb in hbm.GetDictionary())
            {
                if (hb.Key >= start_time && hb.Key <= end_time)
                {
                    time_HrBeat.Add(hb.Key, hb.Value);
                }
            }
        }

        /* Import data for normal HeartBM*/
        /* Função usada para o Clone do objecto
         */
        public void ImportData(HeartBeatMetrics hbm)
        {
            foreach (KeyValuePair<Int32, Int32> hb in hbm.GetDictionary())
            {
                time_HrBeat.Add(hb.Key, hb.Value);
            }
        }

        /* To String */
        public override string ToString()
        {
            MemoryStream stream = new MemoryStream();
            StreamWriter writer = new StreamWriter(stream);
            StreamReader reader = new StreamReader(stream);
            foreach (KeyValuePair<int, int> time_hb in time_HrBeat)
            {
                string s = "<" + time_hb.Key + "; " + time_hb.Value + ">\n";
                writer.Write(s);
                writer.Flush();
            }
            stream.Position = 0;
            return reader.ReadToEnd();
        }

        public HeartBeatMetrics Clone()
        {
            return new HeartBeatMetrics(this);
        }
    }

    /* Classe auxiliar da classe HBMForIntervals
     * Está classe permite ter intervalos de valores para os HB,
     * uma vez que na classe anterior (HBM), os valores que são armazenados no objecto
     * só precisavam dos stamp_time e do batimentos, nesta classe precisamos de definir
     * um intervalo de tempo para os batimentos, deste modo temos de acrescentar uma variável
     * de tempo.
     */

    class PairEndTimeHB
    {
        int end_time = 0;
        int heartBeat = 0;

        /* Class Constructor */
        public PairEndTimeHB(int end, int hb)
        {
            end_time = end;
            heartBeat = hb;
        }

        public int GetEndTime() { return end_time; }
        public int GetHeartBeat() { return heartBeat; }

        public override string ToString()
        {
            return (" EndTime = " + end_time + "; HeartBeat = " + heartBeat);
        }
    }

    /* Esta classe permite ordenar as métricas dos passos para diferentes medidas de tempo
     * O HeartBeatMetrics funciona conforme as medidas apresentadas no ficheiro csv ou a partir
     * de outro objecto da mesma classe, no HeartBMForIntervals podemos alterar as medidadas de tempo,
     * por exemplo, se no caso normal temos leituras de HB de 2 em 2 min na classe HearBMForInterval, 
     * podemos mudar essas leituras, de modo a ter valore de 10 em 10 minutos ( usando a média dos
     * valores pertencentes a cada intervalo de 10 min ).
     */
    class HeartBMForIntervals
    {
        /* Heartbeat order by start time */
        /* <Start Time, (End Time, Heart Beat)>*/
        /* <Start Time, PairEndTimeHB> */
        Dictionary<Int32, PairEndTimeHB> time_HrBeat = null;
        /* Average of all heartbeat */
        Double average = 0;
        /* Std of all heartbeat */
        Double std = 0;
        /* Interval in minutes */
        int interval_in_min;

        /* Interval in min */
        /* Class Constructor */
        public HeartBMForIntervals(int interval)
        {
            interval_in_min = interval;
            time_HrBeat = new Dictionary<int, PairEndTimeHB>();
            average = 0;
            std = 0;
        }

        public HeartBMForIntervals(int interval, HeartBeatMetrics hbm)
        {
            interval_in_min = interval;
            time_HrBeat = new Dictionary<int, PairEndTimeHB>();
            average = 0;
            std = 0;
            ImportData(hbm);
        }

        /* Gets */
        public Dictionary<int, PairEndTimeHB> GetDictionary() { return new Dictionary<int, PairEndTimeHB>(time_HrBeat); }
        public Double GetAverage() { Average(); return average; }
        public Double GetStd() { Std(); return std; }
        public int GetSize() { return time_HrBeat.Count; }

        /* Global Metrics */

        public Double Average()
        {
            if (time_HrBeat.Count == 0)
                return 0;
            Double r = 0;

            foreach (KeyValuePair<Int32, PairEndTimeHB> time in time_HrBeat)
            {
                r += (Double)time.Value.GetHeartBeat();
            }

            r = r / time_HrBeat.Values.Count;

            average = r;

            return r;
        }

        public Double Std()
        {
            if (time_HrBeat.Count == 0)
                return 0;
            Double r = 0, aux = 0;

            if (average == 0)
                Average();

            foreach (KeyValuePair<Int32, PairEndTimeHB> t in time_HrBeat)
            {
                aux = (Double)(t.Value.GetHeartBeat() - average);
                r += aux * aux;
            }

            r = Math.Sqrt(r / time_HrBeat.Count);

            std = r;

            return r;
        }

        public List<KeyValuePair<int, PairEndTimeHB>> TopHigher(int top)
        {
            var aux = new SortedDictionary<Int32, PairEndTimeHB>(time_HrBeat);
            List<KeyValuePair<int, PairEndTimeHB>> sorted = (from kv in aux orderby kv.Value.GetHeartBeat() descending select kv).ToList();
            return sorted.GetRange(0, top);
        }

        public List<KeyValuePair<int, PairEndTimeHB>> TopInferior(int top)
        {
            var aux = new SortedDictionary<Int32, PairEndTimeHB>(time_HrBeat);
            List<KeyValuePair<int, PairEndTimeHB>> sorted = (from kv in aux orderby kv.Value.GetHeartBeat() ascending select kv).ToList();
            return sorted.GetRange(0, top);
        }

        /* Import data for normal HeartBM*/
        /* Nesta classe não é preciso voltar a ler do ficheiro, é mais rápido usar um objecto
         * HBM já criado para criar um HBMForIntervals
         */
        public void ImportData(HeartBeatMetrics hbm)
        {
            int start_time = 0;
            int end_time = 0;
            int averageAux = 0;
            int count = 0;
            foreach (KeyValuePair<Int32, Int32> hb in hbm.GetDictionary())
            {
                if (start_time == 0 || (end_time != 0 && hb.Key > end_time))
                {
                    //Quando já passaram intervalo_in_min minutos
                    if (end_time != 0 && hb.Key > end_time)
                    {
                        averageAux = averageAux / count;
                        PairEndTimeHB pair = new PairEndTimeHB(end_time, averageAux);
                        time_HrBeat.Add(start_time, pair);
                    }

                    start_time = hb.Key;
                    end_time = start_time + interval_in_min * 60;
                    averageAux = 0;
                    count = 0;
                }

                //Enquanto a leitura pertence ao intervalo
                if (hb.Key <= end_time)
                {
                    count++;
                    averageAux += hb.Value;
                }
            }
        }
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
            writer.Write("Interval " + interval_in_min + " min\n");
            foreach (KeyValuePair<int, PairEndTimeHB> time_hb in time_HrBeat)
            {
                string s = "<" + time_hb.Key + "; " + time_hb.Value + ">\n";
                writer.Write(s);
                writer.Flush();
            }
            stream.Position = 0;
            return reader.ReadToEnd();
        }
    }
}
