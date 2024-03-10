package com.tomst.lolly.core;


import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;


/**
 * Wraps file creation and IO operations into a concise interface
 * specifically for operating on TOMST dendrometer datasets.
 */
public class CSVFile
{
    // public constants
    public static final char APPEND_MODE = 'a';
    public static final char READ_MODE = 'r';
    public static final char WRITE_MODE = 'w';

    // private constants
    private static final int HEADER_LINE_LENGTH = 3;
    private static final int LINE_LENGTH = 0;
    private static final String TAG = "CSV";


    // operational members
    private final char mode;
    private File file;
    private FileOutputStream writer;
    private Scanner reader;


    /**
     * Instantiates a new CSVFile with which a CSV file's contents can be
     * written or read.
     *
     * @param file File object representing a file in the filesystem
     * @throws IOException
     */
    private CSVFile(File file, char mode)
    {
        this.mode = mode;
        this.file = file;

        try
        {
            if (mode == READ_MODE)
            {
                this.reader = new Scanner(file);
            }
            else
            {
                this.writer = new FileOutputStream(file, (mode == APPEND_MODE));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    // opening, closing, and creating new files
    /**
     * Closes a CSVFile object.
     *
     * @apiNote This function can be called directly; otherwise, the garbage
     * collector calls this function on destruction
     */
    public void close()
    {
        try
        {
            if (mode == WRITE_MODE || mode == APPEND_MODE)
            {
                this.writer.close();
            }
            else
            {
                this.reader.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Creates a new file in the filesystem. Shorthand for creating a file and
     * calling CSVFile.open().
     *
     * @param path The path at which to create the file, including the file name
     * @return Reference to a CSVFile through which a file can be interacted
     */
    public static CSVFile create(String path)
    {
        CSVFile csvFile = null;

        try
        {
            File file = new File(path);

            if (file.createNewFile())
            {
                Log.d(TAG, "Created file named: " + path);

                csvFile = open(path, WRITE_MODE);
            }
            else
            {
                Log.d(TAG, path + " file already exists.");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return csvFile;
    }


    /**
     * Removes a file at the given path from the filesystem.
     *
     * @param path File path from which a file is to be removed
     */
    public static void delete(String path)
    {
        if (!new File(path).delete())
        {
            Log.e(TAG, path + " could not be delete!");
        }
    }


    /**
     * Tests if a file has already been created.
     *
     * @param path Path to the file which is expected to exist
     * @return True if file has already been created; false, otherwise
     */
    public static boolean exists(String path)
    {
        return (new File(path)).exists();
    }


    /**
     * Opens a file for IO operations. The file specified must exist.
     *
     * @param path The path at which to open the file, include the file name
     * @return Reference to a CSVFile through which a file can be interacted
     */
    public static CSVFile open(String path, char mode)
    {
        File file = new File(path);

        return new CSVFile(file, mode);
    }


    // reading and writing to files
    /**
     * Copies contents of a source file in a destination file given the file
     * paths.
     * @apiNote Provides a more succinct way of a file.
     *
     * @param srcPath File with contents to copy
     * @param destPath File into which source file contents will be copied
     */
    public static void copy(String srcPath, String destPath)
    {
        CSVFile srcFile = CSVFile.open(srcPath, READ_MODE);
        CSVFile destFile = CSVFile.open(destPath, WRITE_MODE);

        String srcContents = srcFile.readAllLines();
        destFile.write(srcContents);
    }


    /**
     * Writes to a file.
     *
     * @param buffer Contents to be written
     */
    public void write(String buffer)
    {
        try
        {
            this.writer.write(buffer.getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Reads the next line from file.
     *
     * @return Line read from the file
     */
    public String readLine()
    {
        // TODO: maybe should let the user do this?
        if (this.reader.hasNextLine())
        {
            return this.reader.nextLine();
        }

        return "";
    }


    /**
     * Reads contents of a file.
     *
     * @return Contents of a file
     */
    public String readAllLines()
    {
        String line = "";
        String contents = "";

        while((line = readLine()) != "")
        {
            contents += line;
        }

        return contents;
    }


    // merging files
    public static String mergeCSVFiles(String[] fileNames)
    {
        final String LAST_OCCURENCE = ".*/";
        // final String parent_dir = file_names[0].split(LAST_OCCURENCE)[0];
        // for testing purposes only
        final String parentDir = "/storage/emulated/0/Documents/";
        String tempFileName = parentDir + "temp.csv";
        String mergedFileName = parentDir + fileNames[0]
                .split(LAST_OCCURENCE)[1]
                .replace(".csv", "");
        for (int i = 1; i < fileNames.length; i += 1)
        {
            mergedFileName += "-" + fileNames[i]
                    .split(LAST_OCCURENCE)[1]
                    .replace(".csv", "");
        }
        mergedFileName += ".csv";

        if (CSVFile.exists(mergedFileName))
        {
            CSVFile.delete(mergedFileName);
        }
        else if (CSVFile.exists(tempFileName))
        {
            CSVFile.delete(tempFileName);
        }

        int dataSetCnt = 0;
        String header = "";
        CSVFile tempFile = CSVFile.create(tempFileName);
        for (String fileName : fileNames)
        {
            CSVFile csvFile = CSVFile.open(fileName, CSVFile.READ_MODE);
            // count the data sets
            String currentLine = csvFile.readLine();
            dataSetCnt += Integer.parseInt(currentLine.split(";")[0]);
            // read serial number(s) is always first line in data set
            while((currentLine = csvFile.readLine())
                    .split(";").length == HEADER_LINE_LENGTH
            ) {
                header += currentLine + "\n";
            }
            // write serial number
            tempFile.write(currentLine + "\n");

            while((currentLine = csvFile.readLine()).contains(";"))
            {
                tempFile.write(currentLine + "\n");
            }
            csvFile.close();
        }
        tempFile.close();

        header = dataSetCnt + ";\n" + header;

        CSVFile mergedFile = CSVFile.create(mergedFileName);
        mergedFile.write(header);
        tempFile = CSVFile.open(tempFileName, CSVFile.READ_MODE);
        String line = "";
        while ((line = tempFile.readLine()) != "")
        {
            mergedFile.write(line + "\n");
        }
        mergedFile.close();
        tempFile.close();
        CSVFile.delete(parentDir + "temp.csv");

        return mergedFileName;
    }
}
