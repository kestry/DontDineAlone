using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine
{
    public class Util
    {
        private static char[] alphabet = new char[] {'A', 'a', 'B', 'b', 'C', 'c', 'D', 'd', 'E', 'e', 'F', 'f', 'G', 'g', 'H', 'h', 'I', 'i', 'J', 'j', 'K', 'k',
                    'L', 'l','M', 'm', 'N', 'n', 'O', 'o', 'P', 'p', 'Q', 'q', 'R', 'r', 'S', 's', 'T', 't', 'U', 'u', 'V', 'v', 'W', 'w', 'X', 'x', 'Y', 'y', 'Z', 'z' };
        private static char[] number = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

        private static Random r = new Random(DateTime.Now.Millisecond);

        public static string generateId()
        {
            string id = "";
            for(int i = 0; i < 10; i++)
            {
                try
                {
                    char a = alphabet[r.Next(0, alphabet.Length - 1)];
                    char n = number[r.Next(0, number.Length - 1)];
                    id += a;
                    id += n;
                }
                catch { }
            }
            return id;
        }
    }
}
