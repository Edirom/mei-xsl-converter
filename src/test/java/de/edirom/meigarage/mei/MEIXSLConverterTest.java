package de.edirom.meigarage.mei;

import pl.psnc.dl.ege.configuration.EGEConfigurationManager;
import pl.psnc.dl.ege.exception.ConverterException;
import pl.psnc.dl.ege.types.ConversionActionArguments;
import pl.psnc.dl.ege.types.DataType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class MEIXSLConverterTest {
    private MEIXSLConverter converter;

    @org.junit.Before
    public void setUp() throws Exception {
        converter = new MEIXSLConverter();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        converter = null;
    }

    @org.junit.Test
    public void convert() throws IOException, ConverterException {
        InputStream is = new FileInputStream("src/test/resources/test-input.mei.zip");
        OutputStream os = new FileOutputStream("src/test/resources/test-output.ly.zip");
        DataType inputType = new DataType("mei40","text/xml");
        DataType outputType = new DataType("lilypond","text/x-lilypond");
        ConversionActionArguments conversionActionArguments = new ConversionActionArguments(inputType, outputType, null);
        String tempDir = "src/test/temp";
        converter.convert(is, os, conversionActionArguments, tempDir);
        assertNotNull(new File("src/test/resources/test-output.png.zip"));
        InputStream isout = new FileInputStream("src/test/resources/test-output.ly.zip");
        EGEConfigurationManager.getInstance().getStandardIOResolver().decompressStream(isout, new File("src/test/resources/test-output.ly"));
        //System.out.println(new String(Files. readAllBytes(Paths.get("src/test/resources/test-output.txt/result.txt")), "UTF-8"));
        assertNotEquals("", new String(Files.readAllBytes(Paths.get("src/test/resources/test-output.ly/document.xml")), "UTF-8"));
        is.close();
        os.close();
        isout.close();
    }

    @org.junit.Test
    public void getPossibleConversions() {
        assertNotNull(converter.getPossibleConversions());
        System.out.println(converter.getPossibleConversions());
    }
}