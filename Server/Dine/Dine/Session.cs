using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Dine
{
    public class Session
    {
        protected delegate void OnPacketReceive(Reader r);
        protected event OnPacketReceive OnPacket;

        protected delegate void OnDisconnect();
        protected event OnDisconnect OnClose;

        private Socket s = null;
        private byte[] buffer = null;
        private string addr = "";
        public Session(Socket s)
        { 
            this.s = s;
            this.addr = s.RemoteEndPoint.ToString();
            listen(256);
        }

        private void listen(int size)
        {
            buffer = new byte[size];
            s.BeginReceive(buffer, 0, size, SocketFlags.None, new AsyncCallback(OnReceive), null);
        }

        private void OnReceive(IAsyncResult ar)
        {
            int receive = s.EndReceive(ar);

            if(receive <= 0)
            {
                close();
                return;
            }
            Reader r = new Reader(buffer);
            this.OnPacket?.Invoke(r);
            listen(256);
        }

        public void send(Writer w)
        {
            try
            {
                byte[] data = w.getData();
                s.BeginSend(data, 0, data.Length, SocketFlags.None, new AsyncCallback(OnSend), null); 
                
            }
            catch
            {
                close();
            }
        }

        private void OnSend(IAsyncResult ar)
        {
            s.EndSend(ar);
        }

        public string getAddr()
        {
            return addr;
        }

        public void close()
        {
            try
            {
                s.Close();
                s.Dispose();
            }
            finally
            {
                OnClose?.Invoke();
            }
        }
    }
}
