using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine
{
    public class Messenger
    {
        private static ConcurrentDictionary<string, Chat> chats = new ConcurrentDictionary<string, Chat>();

        public static void add(string chatId, Chat c)
        {
            if (chats.ContainsKey(chatId))
                return;
            chats.GetOrAdd(chatId, c);
        }

        public static Chat getChat(string userId)
        {
            return chats.Where(x => x.Key.Contains(userId)).FirstOrDefault().Value;
        }

        public static bool removeUser(string userId)
        {
            Chat c = null;
            try
            {
                string key = chats.Where(x => x.Key.Contains(userId)).FirstOrDefault().Key;
                if (chats.TryGetValue(key, out c))
                {
                    c.removeUser(userId);
                    if (c.getUsers() <= 0)
                        chats.TryRemove(key, out c);
                }
            }
            catch { }
            return c != null;
        }
    }
}
