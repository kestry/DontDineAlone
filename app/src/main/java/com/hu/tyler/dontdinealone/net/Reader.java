package com.hu.tyler.dontdinealone.net;

import java.io.DataInputStream;
import java.io.IOException;

public class Reader {

    private DataInputStream input = null;

    public Reader(DataInputStream input) {
        this.input = input;
    }

    public byte read8() {
        byte b = 0;
        try {
            b = (byte) input.readByte();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public short read16() {
        return (short) (read8() + (read8() << 8));
    }

    public int read32() {
        return read8() + (read8() << 8) + (read8() << 16) + (read8() << 24);
    }

    public long read64()
    {
        long one = read8();
        long two = read8();
        long three = read8();
        long four = read8();
        long five = read8();
        long six = read8();
        long seven = read8();
        long eight = read8();
        return (eight << 56) + (seven << 48) + (six << 40) + (five << 32) + (four << 24) + (three << 16) + (two << 8) + one;
    }

    public final String readStr() {
        int size = read16();
        char str[] = new char[size];
        for (int i = 0; i < size; i++) {
            str[i] = (char) read8();
        }
        return String.valueOf(str);
    }

    public void terminate()
    {
        try {
            input.skipBytes(input.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
