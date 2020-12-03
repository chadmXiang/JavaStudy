
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class ZipDemo {

    // https://programmer.help/blogs/zip-rar-7z-tar-gz-tar.gz-bz2-tar.bz2.html
    public static void main(String[] args) {

        StringBuilder builder = new StringBuilder();
        builder.append("D:")
                .append(File.separator).append("work")
                .append(File.separator).append("Java")
                .append(File.separator).append("zip")
                .append(File.separator).append("我的.zip");
        findFileFromZip(builder.toString(), "filename.txt", "D:\\work\\Java\\zip\\unzip");
    }

    /**
     * https://sevenzip.osdn.jp/chm/cmdline/switches/output_dir.htm
     *
     * @param rarFile
     * @param target
     * @param des
     */
    public static void findFileInRar(String rarFile, String target, String des) throws IOException {
        StringBuilder rarBuilder = new StringBuilder();
        String path7z = "D:\\7-Zip\\7z.exe ";
        rarBuilder.append(path7z).append(rarFile).append(" x -y ").append(target).append(" -o").append(des);
        Runtime.getRuntime().exec(rarBuilder.toString());
        File file = new File(des);
        Optional<File> optionalFile =  Arrays.stream(file.listFiles())
                .filter(file1 -> file1.getName().equals(target))
                .findFirst();
        if(optionalFile.isPresent()) {
            System.out.println("find target in " + file.toString());
            return;
        }

    }

    /**
     *
     * https://github.com/srikanth-lingala/zip4j
     *
     * @param zipFile
     * @param target
     * @param des out put dic
     */
    public static void findFileFromZip(String zipFile, String target, String des) {
        ZipFile file = new ZipFile(zipFile);
        List<FileHeader> fileHeaders = null;
        try {
            fileHeaders = file.getFileHeaders();
            Optional<FileHeader> targetHeader = fileHeaders.stream()
                    .filter(fileHeader -> fileHeader.getFileName().equals(target))
                    .findFirst();
            // find target file ,extract to
            if (targetHeader.isPresent()) {
                file.extractFile(targetHeader.get(), des);
                System.out.println("find target in " + file.toString());
                return;
            }

            fileHeaders.stream()
                    .forEach(s -> System.out.println(s.getFileName()));

            // find target recurse
            fileHeaders.stream()
                    .filter(fileHeader -> fileHeader.getFileName().endsWith(".zip"))
                    .forEach(s -> {
                        try {
                            file.extractFile(s, des);
                            findFileFromZip(des + File.separator + s.getFileName(), target, des);
                        } catch (ZipException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}
