package handlers;

import java.io.*;
import java.nio.channels.FileLock;
import java.nio.file.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class FileHandler {



    private static Path tmpDir = Paths.get("src" + File.separator + "main" + File.separator + "resources" + File.separator);

    public static synchronized void saveState(File ficheiro, Serializable object)  throws Exception {

        byte[] bytes=objectToByte(object);
        Path tmpFilePath = File.createTempFile("atomic", "tmp", tmpDir.toFile()).toPath();
        Files.write(tmpFilePath, bytes, StandardOpenOption.APPEND);
        try {
            Files.move(tmpFilePath, ficheiro.toPath(), StandardCopyOption.ATOMIC_MOVE);

        }catch (Exception e){
            e.printStackTrace();
            if(e instanceof AtomicMoveNotSupportedException){
                System.out.println("Atomic move not supported");
            }
        }




    }


    public static Object loadState(File file) throws Exception {
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream stream = new FileInputStream(file);
        stream.read(bytes);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream is = new ObjectInputStream(bis);
        return is.readObject();
    }

    public static byte[] objectToByte(Serializable object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {

            out = new ObjectOutputStream(bos);

            out.writeObject(object);
            out.flush();
            bos.close();

            return bos.toByteArray();
        } catch (Exception e) {
            // ignore close exception

        }
        return null;
    }
}