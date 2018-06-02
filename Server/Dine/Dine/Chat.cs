using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine
{
    public class Chat
    {
        private string chatId = "";
        private User a = null;
        private User b = null;
        public Chat(string chatId, User a, User b)
        {
            this.chatId = chatId;
            this.a = a;
            this.b = b;
        }

        public void processChat(User u, string message)
        {
            string str = u.getName();
            str += (" : " + message);
            try
            {
                Writer w = new Writer(0x04);
                w.writeStr(str);
                a.send(w);
                b.send(w);
            }
            catch { }
        }

        public string getChatId()
        {
            return chatId;
        }
    }
}
