package org.flowant.backend.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtil {

    public static String readStringFromLittleFile(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }

    public static String readStringFromLittleFile(String path) throws IOException {
        return readStringFromLittleFile(Paths.get(path));
    }

    public static void writeStringToFile(String path, String contents) throws IOException {
        writeStringToFile(Paths.get(path), contents);
    }

    public static void writeStringToFile(Path path, String contents) throws IOException {
        Files.write(path, contents.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }

    public static String toSafePath(String path) {
        char invalid[] = { '"', '/', ':', '*', '<', '>', '\\', '|', '?' };
        for (char c : invalid) {
            path = path.replace(c, ' ');
        }
        return path.trim();
    }

    public static void mkdirs(String path) throws IOException {
        mkdirs(Paths.get(path));
    }

    public static void mkdirs(Path path) throws IOException {
        if (Files.exists(path) == false)
            Files.createDirectory(path);
    }

    public static void rmdirs(String path) throws IOException {
        rmdirs(Paths.get(path));
    }

    public static void rmdirs(Path path) throws IOException {
        Files.deleteIfExists(path);
    }

    public static Stream<String> listFiles(String path) throws IOException {
        return listFiles(Paths.get(path)).map(Path::toString);
    }

    public static Stream<Path> listFiles(Path path) throws IOException {
        return Files.list(path).filter(Files::isRegularFile);
    }
}
