package com.tomst.lolly;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import com.tomst.lolly.core.CSVFile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

public class CSVFileUnitTest
{
    @Mock
    Context MockContext;

    public CSVFile csvfile;

    private final String mock_data =
        "3;\n"
        + "7832;1;2;\n"
        + "10421;1;3;\n"
        + "79823;1;4;\n"
        + "7832;\n"
        + "1;2023.05.01 00:15;0;7,123;-200;-200;937;206;0;\n"
        + "10421;\n"
        + "1;2023.05.01 00:30;0;1,234;-200;-200;563;234;0;\n"
        + "79823;\n"
        + "1;2023.05.01 00:30;0;34,028;-200;-200;120;789;0;\n";


    @Test
    public void setup()
    {
        csvfile = CSVFile.create("./test.csv");
    }


    @Test
    public void toParallel_isCorrect()
    {
    }


    @Test
    public void toSerial_isCorrect()
    {
    }
}
