import okio.*;

import java.io.File;

import java.io.IOException;
import java.util.Map;

public class OkioDemo {

    static final String READ = System.getProperty("user.dir") + File.separator + "read.txt";
    static final String WRITE = System.getProperty("user.dir") + File.separator + "write.txt";

    public static void main(String[] args) throws IOException {
        readLines(new File(READ));
        writeEnv(new File(WRITE));
        exploreCharset();
        hashing();
    }

    static void readLines(File file) throws IOException {
        // The try-with-resources statement is a try statement that declares one or more resources.
        // A resource is an object that must be closed after the program is finished with it.
        // The try-with-resources statement ensures that each resource is closed at the end of the statement.
        // Any object that implements java.lang.AutoCloseable,
        // which includes all objects which implement java.io.Closeable, can be used as a resource.
        try (BufferedSource bufferedSource = Okio.buffer(Okio.source(file))) {
            while (true) {
                String line = bufferedSource.readUtf8Line();
                if (line == null) break;
                if (line.contains("chadm")) {
                    System.out.println(line);
                }
            }
        }
    }

    static void writeEnv(File file) throws IOException {
        try (BufferedSink bufferedSink = Okio.buffer(Okio.sink(file))) {
            for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
                bufferedSink.writeUtf8((String) entry.getKey())
                        .writeUtf8("=")
                        .writeUtf8((String) entry.getValue())
                        .writeUtf8("\n");
            }
        }
    }

    static void exploreCharset() {
        dumpStringData("Café \uD83C\uDF69"); // NFC: é is one code point.
        dumpStringData("Café \uD83C\uDF69"); // NFD: e is one code point, its accent is another.
    }

    static void dumpStringData(String str) {
        System.out.println("                       " + str);
        System.out.println("        String.length: " + str.length());
        System.out.println("String.codePointCount: " + str.codePointCount(0, str.length()));
        System.out.println("            Utf8.size: " + Utf8.size(str));
        System.out.println("          UTF-8 bytes: " + ByteString.encodeUtf8(str));
    }

    static void hashing() throws IOException {
        BufferedSource bufferedSource = Okio.buffer(Okio.source(new File(READ)));
        ByteString byteString = bufferedSource.readByteString();
        System.out.println("   md5: " + byteString.md5().hex());
        System.out.println("  sha1: " + byteString.sha1().hex());
        System.out.println("sha256: " + byteString.sha256().hex());
        System.out.println("sha512: " + byteString.sha512().hex());

        Buffer buffer = bufferedSource.getBuffer();
        System.out.println("   md5: " + buffer.md5().hex());
        System.out.println("  sha1: " + buffer.sha1().hex());
        System.out.println("sha256: " + buffer.sha256().hex());
        System.out.println("sha512: " + buffer.sha512().hex());
        bufferedSource.close();
    }

}
