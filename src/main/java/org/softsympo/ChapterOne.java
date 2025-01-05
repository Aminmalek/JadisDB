package org.softsympo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class ChapterOne {
     public static void saveData1(String path, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path, false)) {
            fos.write(data);
        }
    }

    public static void saveData2(String path, byte[] data) throws IOException {
        String tmpPath = path + ".tmp." + randomInt();
        File tmpFile = new File(tmpPath);

        if (!tmpFile.createNewFile()) {
            throw new IOException("Temporary file already exists: " + tmpPath);
        }

        try (FileOutputStream fos = new FileOutputStream(tmpFile)) {
            fos.write(data);
        } catch (IOException e) {
            tmpFile.delete();
            throw e;
        }

        Files.move(tmpFile.toPath(), new File(path).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void saveData3(String path, byte[] data) throws IOException {
        String tmpPath = path + ".tmp." + randomInt();
        File tmpFile = new File(tmpPath);

        if (!tmpFile.createNewFile()) {
            throw new IOException("Temporary file already exists: " + tmpPath);
        }

        try (FileOutputStream fos = new FileOutputStream(tmpFile)) {
            fos.write(data);
            fos.getFD().sync(); // Equivalent to fsync
        } catch (IOException e) {
            tmpFile.delete();
            throw e;
        }

        Files.move(tmpFile.toPath(), new File(path).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static RandomAccessFile logCreate(String path) throws IOException {
        return new RandomAccessFile(path, "rw");
    }

    public static void logAppend(RandomAccessFile file, String line) throws IOException {
        byte[] buf = (line + "\n").getBytes();
        file.write(buf);
        file.getChannel().force(true); // Equivalent to fsync
    }

    private static int randomInt() {
        return (int) (Math.random() * Integer.MAX_VALUE);
    }
}
