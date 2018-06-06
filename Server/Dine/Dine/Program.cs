using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.NetworkInformation;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Dine
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.Title = "Dine";
            Server.start("127.0.0.1", 7575);
            Console.Read();
        }
    }
}
