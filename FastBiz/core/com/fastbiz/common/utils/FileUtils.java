package com.fastbiz.common.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

public abstract class FileUtils extends FileSystemUtils{

    public static String newFilePath(String parent, String ... relatives){
        String newPath = parent == null ? "." : parent;
        if (relatives != null) {
            for (String relative : relatives) {
                if (relative != null) {
                    newPath = newPath + File.separator + relative;
                }
            }
        }
        return newPath;
    }

    public static String newFilePath(File parent, String ... relatives){
        Assert.notNull(parent);
        return newFilePath(parent.getAbsolutePath(), relatives);
    }

    public static String getFilename(String path){
        Assert.notNull(path);
        int separatorIndex = path.lastIndexOf(File.separator);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    public static void ensureDirectory(File file) throws IOException{
        Assert.notNull(file);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                String fmt = "Cannot create directory %s";
                throw new IOException(String.format(fmt, file));
            }
        }
    }

    public static int copy(File in, File out) throws IOException{
        return FileCopyUtils.copy(in, out);
    }

    public static boolean directoryHasNormalFile(File directory, final String fileName){
        Assert.notNull(directory);
        Assert.notNull(fileName);
        File[] files = directory.listFiles(new FileFilter(){

            public boolean accept(File pathname){
                return fileName.equals(pathname.getName()) && pathname.isFile();
            }
        });
        return files != null && files.length == 1;
    }

    public static File[] listDirectories(File rootDirectory){
        Assert.notNull(rootDirectory);
        File[] files = rootDirectory.listFiles(new FileFilter(){

            public boolean accept(File file){
                return file.isDirectory();
            }
        });
        return files == null ? new File[0] : files;
    }
}
