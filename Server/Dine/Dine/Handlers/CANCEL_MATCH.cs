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
            try
            {
                bool status = Matching.remove(u.getId());
                Writer w = new Writer(0x03);
                w.write((byte)(status ? 1 : 0));
                u.send(w);
            }
            catch { }
        }
    }
}
