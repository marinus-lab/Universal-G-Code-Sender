package com.willwinder.universalgcodesender.utils;

import com.willwinder.universalgcodesender.types.GcodeCommand;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GcodeStreamReaderTest {
    @Test(expected = GcodeStreamReader.NotGcodeStreamFile.class)
    public void readingEmptyFileShouldThrowNotGcodeStreamException() throws GcodeStreamReader.NotGcodeStreamFile {
        new GcodeStreamReader(new BufferedReader(new StringReader("")));
    }

    @Test
    public void readingEmptyPreprocessedFile() throws GcodeStreamReader.NotGcodeStreamFile, IOException {
        GcodeStreamReader gcodeStreamReader = new GcodeStreamReader(new BufferedReader(new StringReader("gsw_meta:0")));
        assertEquals(0, gcodeStreamReader.getNumRows());
        assertEquals(0, gcodeStreamReader.getNumRowsRemaining());
        assertNull(gcodeStreamReader.getNextCommand());
    }

    @Test
    public void readingPreprocessedFileShouldReturnCommand() throws GcodeStreamReader.NotGcodeStreamFile, IOException {
        GcodeStreamReader gcodeStreamReader = new GcodeStreamReader(new BufferedReader(new StringReader("gsw_meta:1\n" + "G01; test" + GcodeStream.FIELD_SEPARATOR + "G01" + GcodeStream.FIELD_SEPARATOR + "1" + GcodeStream.FIELD_SEPARATOR + "test")));
        assertEquals(1, gcodeStreamReader.getNumRows());
        assertEquals(1, gcodeStreamReader.getNumRowsRemaining());

        GcodeCommand nextCommand = gcodeStreamReader.getNextCommand();
        assertEquals(1, nextCommand.getCommandNumber());
        assertEquals("G01; test", nextCommand.getOriginalCommandString());
        assertEquals("G01", nextCommand.getCommandString());
        assertEquals("test", nextCommand.getComment());

        assertEquals(1, gcodeStreamReader.getNumRows());
        assertEquals(0, gcodeStreamReader.getNumRowsRemaining());
    }
}
