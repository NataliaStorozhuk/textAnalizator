package sample.FileConverter;

import sample.DTO.BookProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class FileLoader {


    //Ходим по папке, собираем файлы в объекты BookProfile
    public static ArrayList<BookProfile> listFilesFromFolder(File folder) {

        ArrayList<BookProfile> books = new ArrayList<>();
      /*  for (final File fileEntry : folder.listFiles()) {

            System.out.println(fileEntry.getName());
            books.add(fileToBookConverter.getBookFromFile(fileEntry));

        }*/
        CompletableFuture[] futures = Arrays.stream(folder.listFiles())
                .filter(File::isFile)
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    books.add(FileToBookConverter.getBookFromFile(file));
                    return null;
                }))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();
        return books;
    }
}
