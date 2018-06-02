using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine
{
    public class CANCEL_MATCH : IHandler
    {
        public void Invoke(User u, Reader r)
        {
            bool status = Matching.remove(u.getId());
            Console.WriteLine("Status: " + status);
            Writer w = new Writer(0x03);
            w.write((byte)(status ? 1: 0));
            u.send(w);
        }
    }
}
