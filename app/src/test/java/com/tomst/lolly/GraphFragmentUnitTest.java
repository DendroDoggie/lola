package com.tomst.lolly;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import com.tomst.lolly.core.CSVFile;
import com.tomst.lolly.core.TDendroInfo;
import com.tomst.lolly.core.TMereni;
import com.tomst.lolly.ui.graph.GraphFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GraphFragmentUnitTest
{
    @Mock
    Context MockContext;

    private static final String DATE_PATTERN = "yyyy.MM.dd HH:mm";
    private static final String DELIM = ";";

    public GraphFragment graphfrag;
    public DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(DATE_PATTERN);

    // CSV file function tests
    private final String testFilename = "./test.csv";
    private final String otherTestFilename = "./other_test.csv";
    private final String data = "1;\n678432;1;1;\n1;2024.07.12 07:01;0;73,123;-200;-200;63;200;0;\n";
    private final String other_data = "1;\n277341;1;2;\n1;2023.08.01 15:21;0;17,789;-200;-200;29;100;2;\n";

    @Test
    public void setup()
    {
        graphfrag = new GraphFragment();

        CSVFile.create("./test.csv").write(data);
        CSVFile.create("./other_test.csv").write(other_data);
    }


    public void mergeCSVFiles_isCorrect()
    {
        String[] filenames = {testFilename, otherTestFilename};

        String mergedFilename = graphfrag.mergeCSVFiles(filenames);

        CSVFile actual_file = CSVFile.open(mergedFilename, CSVFile.READ_MODE);

        String[] currentLine;
        ArrayList<String> actual_lines = new ArrayList<String>();
        while ((currentLine = actual_file.readLine().split(DELIM)).length > 0)
        {
            for (int i = 0; i < currentLine.length; i += 1)
            {
                actual_lines.add(currentLine[i]);
            }
        }

        String[] expected_lines = {
                "2",
                "678432", "1", "1",
                "277341", "1", "2",
                "678432",
                "1", "2024.07.12 07:01", "0", "73,123", "-200", "-200", "63", "200", "0",
                "277341",
                "1", "2023.08.01 15:21", "0", "17,789", "-200", "-200","29", "100", "2"
        };

        assertEquals(actual_lines.size(), expected_lines.length);
        for (int i = 0; i < expected_lines.length; i += 1)
        {
            assertEquals(actual_lines.get(i), expected_lines[i]);
        }
    }


    public void processeLine_isCorrect()
    {
        // test parsing a serial number and lat long pos
        String teststr = "666;45;23";

        TMereni actual_mer = graphfrag.processLine(teststr);
        TMereni expected_mer = new TMereni();
        expected_mer.Serial = "666";

        assertEquals(actual_mer, expected_mer);

        // test parsing data point line
        teststr = "1;2024.09.1 06:01;0;8,245;-200;-200;892;100;0;";
        actual_mer = graphfrag.processLine(teststr);
        expected_mer = new TMereni();
        expected_mer.Serial = null;
        expected_mer.dtm = LocalDateTime.parse("2024.09.1 06:01", formatter);
        expected_mer.day = expected_mer.dtm.getDayOfMonth();
        expected_mer.t1 = Float.parseFloat("8,245");
        expected_mer.t2 = Float.parseFloat("-200");
        expected_mer.t3 = Float.parseFloat("-200");
        expected_mer.hum = Integer.parseInt("892");
        expected_mer.mvs = Integer.parseInt("100");

        assertEquals(actual_mer, expected_mer);
    }
}
