using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine.Handlers
{
    public class UPDATE_USER : IHandler
    {
        public void Invoke(User u, Reader r)
        {
            string id = r.readStr();
            string name = r.readStr();

            u.setId(id);
            u.setName(name);

            Console.WriteLine("id: " + u.getId() + " name: " + u.getName());
            Writer w = new Writer(0x01);
            w.write((byte)1);
            u.send(w);
            //Console.WriteLine("Sent: " + BitConverter.ToString(w.getData()).Replace("-", " "));
        }
    }
}
