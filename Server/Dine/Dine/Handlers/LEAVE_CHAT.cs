﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine.Handlers
{
    public class LEAVE_CHAT : IHandler
    {
        public void Invoke(User u, Reader r)
        {
            try
            {
                string chatId = r.readStr();
                bool status = Messenger.getChat(chatId).removeUser(u.getId());
            }
            catch { }
        }
    }
}
