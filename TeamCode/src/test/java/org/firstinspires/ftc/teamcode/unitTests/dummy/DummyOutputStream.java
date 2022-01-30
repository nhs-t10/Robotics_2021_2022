package org.firstinspires.ftc.teamcode.unitTests.dummy;

import java.io.IOException;
import java.io.OutputStream;

public class DummyOutputStream extends OutputStream {
    private boolean closed;

    @Override
    public void write(int i) throws IOException {
        if(closed) throw new IOException("Stream closed");
    }
    public void write(byte[] b) throws IOException {
        write(0);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        write(0);
    }

    public void flush() throws IOException {
        write(0);
    }

    public void close() throws IOException {
        this.closed = true;
    }
}
