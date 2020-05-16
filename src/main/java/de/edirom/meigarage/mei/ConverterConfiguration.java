package de.edirom.meigarage.mei;

import org.apache.log4j.Logger;
import pl.psnc.dl.ege.types.ConversionActionArguments;
import pl.psnc.dl.ege.types.DataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConverterConfiguration {

    private static final Logger LOGGER = Logger.getLogger(MEIXSLConverter.class);

    public static final List<ConversionActionArguments> CONVERSIONS = new ArrayList<ConversionActionArguments>();
    public static final String PROFILE_KEY = "de.edirom.meigarage.mei.profileNames";

    public static final String STYLESHEETS_PATH  = "/usr/share/xml/mei/music-stylesheets";

    public static final String SCOREFAMILY = "Scores";
    public static final String SCOREFAMILYCODE = "score";

    public static final String DEFAULTFAMILY = "Other documents";

    static {
        CONVERSIONS.add(getConversionActionArgument(Conversion.MUSICXMLTIMEWISETOMEI30));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MUSICXMLPARTWISETOTIMEWISE));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MUSICXMLTIMEWISETOPARTWISE));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MARCXMLTOMEI30));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI30TOMEI40));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI21TOMEI30));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI2010TO2012));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI2012TOMEI21));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI40TOLILYPOND));
        CONVERSIONS.add(getConversionActionArgument(Conversion.COMPAREFILES));
    }

    private static ConversionActionArguments getConversionActionArgument(Conversion format) {

        StringBuffer sbParams = new StringBuffer();
        sbParams.append("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">");
        sbParams.append("<properties>");

        sbParams.append(propertyToString(PROFILE_KEY, "default", "array"));

        HashMap<String, Conversion.Property> properties = format.getProperties();
        for (Map.Entry<String, Conversion.Property> property : properties.entrySet()) {
            String key = property.getKey();
            String values = properties.get(property.getKey()).getValues();
            String type = properties.get(property.getKey()).getType();
            sbParams.append(propertyToString(key, values, type));
        }

        sbParams.append("</properties>");

        ConversionActionArguments caa = new ConversionActionArguments(
                new DataType(format.getIFormatId(), format.getIMimeType(), format.getInputDescription(),
                        getType(format.getInputType())),
                new DataType(format.getOFormatId(), format.getOMimeType(),
                        format.getOutputDescription(), getType(format.getOutputType())),
                sbParams.toString(), format.getVisible(), format.getCost());

        return caa;
    }

    private static String propertyToString(String profileKey, String values, String type) {
        StringBuffer sbParam = new StringBuffer();
        sbParam.append("<entry key=\"");
        sbParam.append(profileKey);
        sbParam.append("\">");
        sbParam.append(values);
        if(sbParam.charAt(sbParam.length() - 1)==',') sbParam.deleteCharAt(sbParam.length() - 1);
        sbParam.append("</entry><entry key=\"" + profileKey + ".type\">" + type + "</entry>");

        return sbParam.toString();
    }

    public static String getStylesheetsPath() {
        return STYLESHEETS_PATH;
    }

    private static String getType(String typeCode) {
        if(typeCode.equals(SCOREFAMILYCODE))         return SCOREFAMILY;
        return DEFAULTFAMILY;
    }
}
