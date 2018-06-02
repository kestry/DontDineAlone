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

        public static Chat getChat(string chatId)
        {
            return chats.Where(x => x.Key.Contains(chatId)).FirstOrDefault().Value;
        }

        public static bool removeChat(string chatId)
        {
            Chat c = null;
            try
            {
                string key = chats.Where(x => x.Key.Contains(chatId)).FirstOrDefault().Key;
                chats.TryRemove(key, out c);
            }
            catch { }
            return c != null;
        }
    }
}
