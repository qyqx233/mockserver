package me.zh;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;



class Ax extends InputStream {

    @Override
    public int read() throws IOException {
        return 0;
    }
}

public class A {
    public static void main(String[] args) {

        Base64.getDecoder().decode("abac");
        byte[] bs = {'s', 'b'};
    }
}


interface XX {
    void fx();

    void fx(String s);

    static void sss() {

    }
}

class AA implements XX {

    @Override
    public void fx() {

    }

    @Override
    public void fx(String s) {

    }
}
