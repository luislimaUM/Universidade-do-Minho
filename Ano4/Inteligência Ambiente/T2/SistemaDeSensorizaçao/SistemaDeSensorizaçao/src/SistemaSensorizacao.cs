using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.RegularExpressions;
using System.IO;

namespace SistemaDeSensorizaçao.src
{
    public class SistemaSensorizacao
    {
        private static String heart;
        private static String sleep;
        private static String steps;

        public SistemaSensorizacao(String filepath1, String filepath2, String filepath3)
        {
            heart = String.Copy(filepath1);
            sleep = String.Copy(filepath2);
            steps = String.Copy(filepath3);
        }

        public string getHeart()
        {
            return heart;
        }

        public string getSleep()
        {
            return sleep;
        }

        public string getSteps()
        {
            return steps;
        }

    }
}
