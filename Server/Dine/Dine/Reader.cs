using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine
{
    public class Reader
    {
        private byte[] buffer = null;
        private MemoryStream ms = null;
        private BinaryReader br = null;
        public Reader(byte[] buffer)
        {
            this.buffer = buffer;
            ms = new MemoryStream(this.buffer);        
            br = new BinaryReader(ms);
        }

        public byte read8()
        {
            return br.ReadByte();
        }

        public short read16()
        {
            return br.ReadInt16();
        }

        public int read32()
        {
            return br.ReadInt32();
        }

        public long read64()
        {
            return br.ReadInt64();
        }

        public string readStr()
        {
            short size = read16();
            return Encoding.ASCII.GetString(br.ReadBytes(size));
        }

        public byte[] getData()
        {
            return buffer;
        }
    }
}
