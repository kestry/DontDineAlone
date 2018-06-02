using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Dine
{
    public class User : Session
    {
        public new delegate void OnPacketReceive(User u, Reader r);
        public new event OnPacketReceive OnPacket;

        public new delegate void OnDisconnect(User u);
        public new event OnDisconnect OnClose;

        private string id = "";
        private string name = "";
        private int age = -1;
        public User(Socket s) : base(s)
        {
            base.OnPacket += OnPacketReceived;
            base.OnClose += OnCloseReceived;
        }

        private void OnCloseReceived()
        {
            this.OnClose?.Invoke(this);
        }

        private void OnPacketReceived(Reader r)
        {
            this.OnPacket?.Invoke(this, r);
        }

        public string getId()
        {
            return id;
        }

        public void setId(string id)
        {
            this.id = id;
        }
        public string getName()
        {
            return name;
        }

        public void setName(string name)
        {
            this.name = name;
        }

        public int getAge()
        {
            return age;
        }

        public void setAge(int age)
        {
            this.age = age;
        }
    }
}
