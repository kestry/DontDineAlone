using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine
{
    public class Writer
    {
        MemoryStream ms = null;
        BinaryWriter bw = null;
        public Writer(short opcode)
        {
            ms = new MemoryStream();
            bw = new BinaryWriter(ms);
            write((short)opcode);
        }
        public void write(byte b)
        {
            bw.Write(b);
        }

        public void write(short s)
        {
            bw.Write(s);
        }

        public void write(int i)
        {
            bw.Write(i);
        }

        public void write(long l)
        {
            bw.Write(l);
        }

        public void writeStr(string str)
        {
            write((short)str.Length);
            for (int i = 0; i < str.Length; i++)
                bw.Write(str[i]);
        }

        public byte[] getData()
        {
            return ms.GetBuffer();
        }
    }
}
