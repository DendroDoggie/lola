package com.tomst.lolly;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;
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
import java.time.ZoneOffset;
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
    private final String mergedFilename = "./merged_test.csv";
    private final String data = "1;\n678432;1;1;\n1;2024.07.12 07:01;0;73,123;-200;-200;63;200;0;\n";
    private final String other_data = "1;\n277341;1;2;\n1;2023.08.01 15:21;0;17,789;-200;-200;29;100;2;\n";
    private final String merged_data = "2;\n678432;1;1;\n277341;1;2;\n678432;\n1;2024.07.12 07:01;0;73,123;-200;-200;63;200;0;\n277341;\n1;2023.08.01 15:21;0;17,789;-200;-200;29;100;2";

    @Test
    public void setup()
    {
        graphfrag = new GraphFragment();

        if (!CSVFile.exists(testFilename))
        {
            CSVFile.create(testFilename).write(data);
        }
        if (!CSVFile.exists(otherTestFilename))
        {
            CSVFile.create(otherTestFilename).write(other_data);
        }
        if (!CSVFile.exists(mergedFilename))
        {
            CSVFile.create(mergedFilename).write(merged_data);
        }
    }


    public void displayData_isCorrect()
    {
    }


    public void loadCSVFile_isCorrect()
    {
        // test loading a single data set file
        graphfrag.loadCSVFile(testFilename);
        ArrayList<TDendroInfo> actual_dendroInfos =
                graphfrag.getDendroInfos();
        ArrayList<TDendroInfo> expected_dendroInfo = new ArrayList<TDendroInfo>();
        expected_dendroInfo.add(new TDendroInfo(
                "678432", Long.parseLong("1"), Long.parseLong("1")
        ));
        TMereni expected_mer = new TMereni();
        expected_mer.Serial = null;
        expected_mer.dtm = LocalDateTime.parse("2024.07.12 07:01", formatter);
        expected_mer.day = expected_mer.dtm.getDayOfMonth();
        expected_mer.t1 = Float.parseFloat("73.123");
        expected_mer.t2 = Float.parseFloat("-200");
        expected_mer.t3 = Float.parseFloat("-200");
        expected_mer.hum = Integer.parseInt("63");
        expected_mer.mvs = Integer.parseInt("200");
        long originDate = expected_mer.dtm.toEpochSecond(ZoneOffset.MAX);
        float dataNum =
                (expected_mer.dtm.toEpochSecond(ZoneOffset.MAX) - originDate) / 60;

        expected_dendroInfo.get(0).mers.add(expected_mer);
        expected_dendroInfo.get(0).vT1.add(new Entry(dataNum,
                (float) expected_mer.t1
        ));
        expected_dendroInfo.get(0).vT2.add(new Entry(dataNum,
                (float) expected_mer.t2
        ));
        expected_dendroInfo.get(0).vT3.add(new Entry(dataNum,
                (float) expected_mer.t3
        ));
        expected_dendroInfo.get(0).vHA.add(new Entry(dataNum,
                (float) expected_mer.hum
        ));

        assertEquals(actual_dendroInfos.size(), expected_dendroInfo.size());
        assertEquals(actual_dendroInfos.get(0), expected_dendroInfo.get(0));


        // test loading a merged data set file
        graphfrag.loadCSVFile(mergedFilename);
        actual_dendroInfos = graphfrag.getDendroInfos();
        expected_dendroInfo = new ArrayList<TDendroInfo>();
        expected_dendroInfo.add(new TDendroInfo(
                "678432", Long.parseLong("1"), Long.parseLong("1")
        ));
        expected_dendroInfo.add(new TDendroInfo(
                "277341", Long.parseLong("1"), Long.parseLong("2")
        ));
        // add back first data set
        expected_mer = new TMereni();
        expected_mer.Serial = null;
        expected_mer.dtm = LocalDateTime.parse("2024.07.12 07:01", formatter);
        expected_mer.day = expected_mer.dtm.getDayOfMonth();
        expected_mer.t1 = Float.parseFloat("73.123");
        expected_mer.t2 = Float.parseFloat("-200");
        expected_mer.t3 = Float.parseFloat("-200");
        expected_mer.hum = Integer.parseInt("63");
        expected_mer.mvs = Integer.parseInt("200");
        originDate = expected_mer.dtm.toEpochSecond(ZoneOffset.MAX);
        dataNum =
                (expected_mer.dtm.toEpochSecond(ZoneOffset.MAX) - originDate) / 60;

        expected_dendroInfo.get(0).mers.add(expected_mer);
        expected_dendroInfo.get(0).vT1.add(new Entry(dataNum,
                (float) expected_mer.t1
        ));
        expected_dendroInfo.get(0).vT2.add(new Entry(dataNum,
                (float) expected_mer.t2
        ));
        expected_dendroInfo.get(0).vT3.add(new Entry(dataNum,
                (float) expected_mer.t3
        ));
        expected_dendroInfo.get(0).vHA.add(new Entry(dataNum,
                (float) expected_mer.hum
        ));
        // add second data set
        expected_mer = new TMereni();
        expected_mer.Serial = null;
        expected_mer.dtm = LocalDateTime.parse("2023.08.01 15:21", formatter);
        expected_mer.day = expected_mer.dtm.getDayOfMonth();
        expected_mer.t1 = Float.parseFloat("17.789");
        expected_mer.t2 = Float.parseFloat("-200");
        expected_mer.t3 = Float.parseFloat("-200");
        expected_mer.hum = Integer.parseInt("29");
        expected_mer.mvs = Integer.parseInt("100");
        originDate = expected_mer.dtm.toEpochSecond(ZoneOffset.MAX);
        dataNum =
                (expected_mer.dtm.toEpochSecond(ZoneOffset.MAX) - originDate) / 60;

        expected_dendroInfo.get(0).mers.add(expected_mer);
        expected_dendroInfo.get(0).vT1.add(new Entry(dataNum,
                (float) expected_mer.t1
        ));
        expected_dendroInfo.get(0).vT2.add(new Entry(dataNum,
                (float) expected_mer.t2
        ));
        expected_dendroInfo.get(0).vT3.add(new Entry(dataNum,
                (float) expected_mer.t3
        ));
        expected_dendroInfo.get(0).vHA.add(new Entry(dataNum,
                (float) expected_mer.hum
        ));


        assertEquals(actual_dendroInfos.size(), expected_dendroInfo.size());
        assertEquals(actual_dendroInfos.get(0), expected_dendroInfo.get(0));
        assertEquals(actual_dendroInfos.get(1), expected_dendroInfo.get(1));
    }


    public void loadDmdData_isCorrect()
    {
        // TODO: dunno if this *can* actually be tested
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

        String[] expected_lines = merged_data.split(DELIM);

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
        expected_mer.t1 = Float.parseFloat("8.245");
        expected_mer.t2 = Float.parseFloat("-200");
        expected_mer.t3 = Float.parseFloat("-200");
        expected_mer.hum = Integer.parseInt("892");
        expected_mer.mvs = Integer.parseInt("100");

        assertEquals(actual_mer, expected_mer);
    }
}
