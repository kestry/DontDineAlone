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
            try
            {
                string chatId = r.readStr();
                Chat c = Messenger.getChat(chatId);
                if (c == null)
                    return;
                string message = r.readStr();
                c.processChat(u, message);
            }
            catch { }
        }
    }
}
