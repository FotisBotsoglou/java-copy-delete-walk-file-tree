
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

// Copy and delete directories that are not empty.
public class Main {
    public static void dirCopy(Path source,Path target) throws IOException{
        Files.walkFileTree(source,new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = target.resolve(source.relativize(dir));
                try {
                    Files.copy(dir,targetDir);
                }catch (FileAlreadyExistsException e){
                    if (Files.isDirectory(targetDir)){
                        throw e;
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file,target.resolve(source.relativize(file)));
                return FileVisitResult.CONTINUE;
            }
        });
    }
    public static void dirDelete(Path dir)throws IOException{
        Files.walkFileTree(dir,new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc==null){
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
                else throw exc;
            }
        });
    }

    public static void main(String[] args) throws IOException {
        Path source=Path.of(""); // <- the path of the directory that we want to copy.
        Path target= Path.of(""); // <- the destination that we want the copied dir to be placed.
        dirCopy(source,target);
        dirDelete(target);



    }
}