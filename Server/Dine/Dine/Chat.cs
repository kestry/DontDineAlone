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
        private Dictionary<string, User> users = null;
        public Chat(string chatId)
        {
            this.chatId = chatId;
            this.users = new Dictionary<string, User>();
        }

        public void add(string userId, User u)
        {
            if (users.ContainsKey(userId))
                return;
            users.Add(userId, u);
        }

        public void processChat(User u, string message)
        {
            string str = u.getName() + ": " + message;
            foreach(User x in users.Values)
            {
                try
                {
                    Writer w = new Writer(0x04);
                    w.writeStr(str);
                    x.send(w);
                }
                catch { }
            }
        }

        public string getChatId()
        {
            return chatId;
        }

        public bool removeUser(string userId)
        {
            return users.Remove(userId);
        }

        public int getUsers()
        {
            return users.Count;
        }
    }
}
