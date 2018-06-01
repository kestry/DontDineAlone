package com.hu.tyler.dontdinealone.net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class Writer {

    private DataOutputStream output = null;
    private ByteArrayOutputStream out = null;

    public Writer()
    {
        out = new ByteArrayOutputStream();
        output = new DataOutputStream(out);
    }

    public Writer(short opcode) {
        out = new ByteArrayOutputStream();
        output = new DataOutputStream(out);
        write16(opcode);
    }

    public void write8(byte b) {
        try {
            output.writeByte(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write16(int i) {
        try {
            output.writeByte((byte) (i & 0xFF));
            output.writeByte((byte) ((i >>> 8) & 0xFF));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void write32(int i) {
        try {
            output.writeByte((byte) (i & 0xFF));
            output.writeByte((byte) ((i >>> 8) & 0xFF));
            output.writeByte((byte) ((i >>> 16) & 0xFF));
            output.writeByte((byte) ((i >>> 24) & 0xFF));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void write64(long l) {
        try {
            output.writeByte((byte) (l & 0xFF));
            output.writeByte((byte) ((l >>> 8) & 0xFF));
            output.writeByte((byte) ((l >>> 16) & 0xFF));
            output.writeByte((byte) ((l >>> 24) & 0xFF));
            output.writeByte((byte) ((l >>> 32) & 0xFF));
            output.writeByte((byte) ((l >>> 40) & 0xFF));
            output.writeByte((byte) ((l >>> 48) & 0xFF));
            output.writeByte((byte) ((l >>> 56) & 0xFF));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeStr(String s) {
        try {
            write16(s.length());
            output.write(s.getBytes("ASCII"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public byte[] getData()
    {
        return out.toByteArray();
    }
}
