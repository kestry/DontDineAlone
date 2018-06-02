using System;
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
            bool status = Messenger.getChat(u.getId()).removeUser(u.getId());
            Console.WriteLine("Removed from Chat!");
        }
    }
}
