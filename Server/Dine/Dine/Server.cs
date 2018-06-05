using Dine.Handlers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Dine
{
    public class Server
    {
        private static Socket s = null;
        private static List<User> sessions = null;
        private static Dictionary<short, IHandler> handlers = null;

        public static void start(string address, int port)
        {
            createHandlers();
            sessions = new List<User>();
            s = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            s.Bind(new IPEndPoint(IPAddress.Parse(address), port));
            s.Listen(10);
            s.BeginAccept(new AsyncCallback(OnAccept), null);
            Console.WriteLine("Server : " + s.LocalEndPoint + " - Online!");
        }

        private static void createHandlers()
        {
            handlers = new Dictionary<short, IHandler>();
            handlers.Add(0x02, new MATCH());
            handlers.Add(0x03, new CANCEL_MATCH());
            handlers.Add(0x04, new MESSAGE());
            handlers.Add(0x05, new LEAVE_CHAT());
        }

        private static void OnAccept(IAsyncResult ar)
        {
            Socket c = s.EndAccept(ar);
            Console.WriteLine("Connection: " + c.RemoteEndPoint);
            createSession(c);
            s.BeginAccept(new AsyncCallback(OnAccept), null);
        }
        public static User getUser(string id)
        {
            if (sessions == null)
                return null;
            User u = sessions.Where(x => x.getId() == id).FirstOrDefault();
            return u;
        }

        private static void createSession(Socket c)
        {
            User u = new User(c);
            u.OnPacket += OnPacketReceived;
            u.OnClose += OnClose;
            sessions.Add(u);
        }

        private static void OnClose(User u)
        {
            sessions.Remove(u);
            Matching.remove(u.getId());
            Messenger.removeUser(u.getId());
            Console.WriteLine("Closed: " + u.getAddr());
        }

        private static void OnPacketReceived(User u, Reader r)
        {
            Console.WriteLine(BitConverter.ToString(r.getData()).Replace("-", " "));
            short opcode = r.read16();
            Console.WriteLine("Opcode: 0x0" + opcode);
            if(Monitor.TryEnter(handlers, 1000))
            {
                try
                {
                    IHandler handler = null;
                    if(handlers.TryGetValue(opcode, out handler))
                    {
                        handler.Invoke(u, r);
                    }
                }
                finally
                {
                    Monitor.Exit(handlers);
                }
            }
        }
    }
}
