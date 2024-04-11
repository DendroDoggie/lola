package com.tomst.lolly;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import com.tomst.lolly.core.TMereni;
import com.tomst.lolly.ui.graph.GraphFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GraphFragmentUnitTest
{
    @Mock
    Context MockContext;

    private static final String DATE_PATTERN = "yyyy.MM.dd HH:mm";

    public GraphFragment graphfrag;
    public DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(DATE_PATTERN);

    @Test
    public void setup()
    {
        graphfrag = new GraphFragment();
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
