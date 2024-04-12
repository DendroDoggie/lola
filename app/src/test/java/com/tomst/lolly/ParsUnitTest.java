package com.tomst.lolly;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import com.tomst.lolly.core.TMereni;
import com.tomst.lolly.core.pars;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParsUnitTest {
    @Mock
    Context MockContext;

    private static final double DEFAULT_TEMP = -200.0;
    private static final double FLOAT_TEST_TOLERANCE = 0.01;

    public pars parser;

    @Before
    public void setup() {
         parser = new pars();
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void copyInt_isCorrect() {
        int expected = 25;
        String input = "25";

        int actual = pars.copyInt(input, 1, 2);

        assertEquals(expected, actual);
    }

    @Test
    public void copyIntGTM_isCorrect() {
        int expected = 4;
        String input = "04";

        int actual = pars.copyIntGTM(input, 1, 2);

        assertEquals(expected, actual);
    }

    @Test
    public void copyIntGTM_isCorrectHex() {
        int expected = -28;
        String input = "A8";

        int actual = pars.copyIntGTM(input, 1, 2);

        assertEquals(expected, actual);
    }

    @Test
    public void copyHex_isCorrect() {
        int expected = 168;
        String input = "A8";

        int actual = pars.copyHex(input, 1, 2);

        assertEquals(expected, actual);
    }

    @Test
    public void disassembleDate_isCorrect() {
        TMereni expected = new TMereni();
        expected.year = 2024;
        expected.month = 1;
        expected.day = 15;
        expected.hh = 8;
        expected.mm = 34;
        expected.ss = 13;
        expected.gtm = -28;

        String input = "DD20240115083413A8";

        TMereni actual = new TMereni();

        parser.disassembleDate(input, actual);

        assertEquals(expected.year, actual.year);
        assertEquals(expected.month, actual.month);
        assertEquals(expected.day, actual.day);
        assertEquals(expected.hh, actual.hh);
        assertEquals(expected.mm, actual.mm);
        assertEquals(expected.ss, actual.ss);
        assertEquals(expected.gtm, actual.gtm);
    }

    @Test
    public void disassembleData_ADC_isCorrect() {
        TMereni expected = new TMereni();
        expected.hh = 13;
        expected.mm = 30;
        expected.ss = 0;
        expected.adc = 255;
        expected.hum = 8890;
        expected.Err = 0;
        expected.t1 = 25.4375;
        expected.t2 = DEFAULT_TEMP;
        expected.t3 = DEFAULT_TEMP;

        String input = "D133000FADCFF31972";

        TMereni actual = new TMereni();

        parser.disassembleData(input, actual);

        assertEquals(expected.hh, actual.hh);
        assertEquals(expected.mm, actual.mm);
        assertEquals(expected.ss, actual.ss);
        assertEquals(expected.adc, actual.adc);
        assertEquals(expected.hum, actual.hum);
        assertEquals(expected.Err, actual.Err);
        assertEquals(expected.t1, actual.t1, FLOAT_TEST_TOLERANCE);
        assertEquals(expected.t2, actual.t2, FLOAT_TEST_TOLERANCE);
        assertEquals(expected.t3, actual.t3, FLOAT_TEST_TOLERANCE);
    }

    @Test
    public void disassembleData_nonADC_isCorrect() {
        TMereni expected = new TMereni();
        expected.hh = 13;
        expected.mm = 30;
        expected.ss = 0;
        expected.mvs = 2;
        expected.hum = 15;
        expected.Err = 0;
        expected.t1 = -0.8125;
        expected.t2 = -0.8125;
        expected.t3 = 25.4375;

        String input = "D133000FFF3FF31972";

        TMereni actual = new TMereni();

        parser.disassembleData(input, actual);

        assertEquals(expected.hh, actual.hh);
        assertEquals(expected.mm, actual.mm);
        assertEquals(expected.ss, actual.ss);
        assertEquals(expected.mvs, actual.mvs);
        assertEquals(expected.hum, actual.hum);
        assertEquals(expected.Err, actual.Err);
        assertEquals(expected.t1, actual.t1, FLOAT_TEST_TOLERANCE);
        assertEquals(expected.t2, actual.t2, FLOAT_TEST_TOLERANCE);
        assertEquals(expected.t3, actual.t3, FLOAT_TEST_TOLERANCE);
    }
}
