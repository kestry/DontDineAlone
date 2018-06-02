using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine.Handlers
{
    public class MESSAGE : IHandler
    {
        public void Invoke(User u, Reader r)
        {
            Chat c = Messenger.getChat(u.getId().ToString());
            if (c == null)
                return;
            string message = r.readStr();
            Console.WriteLine("Message: " + message);
            c.processChat(u, message);
        }
    }
}
