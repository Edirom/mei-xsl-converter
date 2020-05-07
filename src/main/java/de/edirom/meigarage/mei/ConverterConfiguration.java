package de.edirom.meigarage.mei;

import org.apache.log4j.Logger;
import pl.psnc.dl.ege.types.ConversionActionArguments;
import pl.psnc.dl.ege.types.DataType;

import java.util.ArrayList;
import java.util.List;

public class ConverterConfiguration {

    private static final Logger LOGGER = Logger.getLogger(MEIXSLConverter.class);

    public static final List<ConversionActionArguments> CONVERSIONS = new ArrayList<ConversionActionArguments>();
    public static final String PROFILE_KEY = "de.edirom.meigarage.mei.profileNames";

    public static final String STYLESHEETS_PATH  = "/usr/share/xml/mei/music-stylesheets";

    public static final String SCOREFAMILY = "Scores";
    public static final String SCOREFAMILYCODE = "score";

    public static final String DEFAULTFAMILY = "Other documents";

    static {
        CONVERSIONS.add(getConversionActionArgument(Conversion.MUSICXMLTIMEWISETOMEI21));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MUSICXMLPARTWISETOTIMEWISE));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MUSICXMLTIMEWISETOPARTWISE));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MARCXMLTOMEI30));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI30TOMEI40));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI21TOMEI30));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI2010TO2012));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI2012TOMEI21));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI40TOLILYPOND));
    }

    private static ConversionActionArguments getConversionActionArgument(Conversion format) {

        StringBuffer sbParams = new StringBuffer();
        sbParams.append("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">");
        sbParams.append("<properties>");
        sbParams.append("<entry key=\"");
        sbParams.append(PROFILE_KEY);
        sbParams.append("\">");
        sbParams.append("default");
        if(sbParams.charAt(sbParams.length() - 1)==',') sbParams.deleteCharAt(sbParams.length() - 1);
        sbParams.append("</entry><entry key=\"" + PROFILE_KEY + ".type\">array</entry>");
        sbParams.append("</properties>");

        ConversionActionArguments caa = new ConversionActionArguments(
                new DataType(format.getIFormatId(), format.getMimeType(), format.getInputDescription(),
                        getType(format.getInputType())),
                new DataType(format.getOFormatId(), format.getMimeType(),
                        format.getOutputDescription(), getType(format.getOutputType())),
                sbParams.toString(), format.getVisible(), format.getCost());

        return caa;
    }

    public static String getStylesheetsPath() {
        return STYLESHEETS_PATH;
    }

    private static String getType(String typeCode) {
        if(typeCode.equals(SCOREFAMILYCODE))         return SCOREFAMILY;
        return DEFAULTFAMILY;
    }
}
