package de.edirom.meigarage.mei;

import java.util.HashMap;

public enum Conversion {

    /*
      supported formats
     */
    MUSICXMLTIMEWISETOMEI30(
            "musicxmltimewisetomei30", // "id"
            "text/xml", // "input mime type"
            "musicxml-timewise", // "input format id"
            "MusicXML Document (timewise)", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output format id"
            "mei30", // "output format id"
            "MEI 3.0 (2016) Document", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9, // "cost"
            new HashMap<String, Property>() {
                {
                    put("musicxmltimewisetomei21.tieStyle",
                            new Property("musicxmltimewisetomei21.tieStyle", "attr,elem,both", "array"));
                }
            }
    ),
    
    MUSICXMLPARTWISETOTIMEWISE(
            "musicxmlpartwisetotimewise", // "id"
            "text/xml", // "input mime type"
            "musicxml-partwise", // "input format id"
            "MusicXML Document (partwise)", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output format id"
            "musicxml-timewise", // "output format id"
            "MusicXML Document (timewise)", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9, // "cost"
            new HashMap<String, Property>() {}
    ),
    
    MUSICXMLTIMEWISETOPARTWISE(
            "musicxmltimewisetopartwise", // "id"
            "text/xml", // "input mime type"
            "musicxml-timewise", // "input format id"
            "MusicXML Document (timewise)", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output format id"
            "musicxml-partwise", // "output format id"
            "MusicXML Document (partwise)", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9, // "cost"
            new HashMap<String, Property>() {}
    ),

    MARCXMLTOMEI30(
            "marcxmltomei30", // "id"
            "text/xml", // "input mime type"
            "marcxml", // "input format id"
            "MARC XML Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output format id"
            "mei30", // "output format id"
            "MEI 3.0 (2016) Document", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9, // "cost"
            new HashMap<String, Property>() {}
    ),

    MEI30TOMEI40(
            "mei30tomei40", // "id"
            "text/xml", // "input mime type"
            "mei30", // "input format id"
            "MEI 3.0 (2016) Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output format id"
            "mei40", // "output format id"
            "MEI 4.0 (2018) Document", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9, // "cost"
            new HashMap<String, Property>() {}
    ),

    MEI21TOMEI30(
            "mei21tomei30", // "id"
            "text/xml", // "input mime type"
            "mei21", // "input format id"
            "MEI 2.1 (2013) Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output format id"
            "mei30", // "output format id"
            "MEI 3.0 (2016) Document", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9, // "cost"
            new HashMap<String, Property>() {}
    ),
    
    MEI2010TO2012(
            "mei2010to2012", // "id"
            "text/xml", // "input mime type"
            "mei2010", // "input format id"
            "MEI 1.0 (2010) Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output format id"
            "mei2012", // "output format id"
            "MEI 2.0 (2012) Document", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9, // "cost"
            new HashMap<String, Property>() {}
    ),

    MEI2012TOMEI21(
            "mei2012toMEI21", // "id"
            "text/xml", // "input mime type"
            "mei2012", // "input format id"
            "MEI 2.0 (2012) Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output format id"
            "mei21", // "output format id"
            "MEI 2.1 (2013) Document", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9, // "cost"
            new HashMap<String, Property>() {}
    ),

    MEI40TOLILYPOND(
            "mei40toLilyPond", // "id"
            "text/xml", // "input mime type"
            "mei40", // "input format id"
            "MEI 4.0 (2018) Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/x-lilypond", // "output format id"
            "lilypond", // "output format id"
            "LilyPond Document", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9, // "cost"
            new HashMap<String, Property>() {}
    ),

    COMPAREFILES(
            "compareFiles", // "id"
            "text/xml", // "input mime type"
            "mei40Corpus", // "input format id"
            "MEI 4.0 (2018) Corpus Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output format id"
            "mei40Diff", // "output format id"
            "MEI 4.0 (2018) Diff Document", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9, // "cost"
            new HashMap<String, Property>() {}
    )

    ;


    private String id;
    private String iMimeType;
    private String iFormatId;
    private String iDescription;
    private String iType;
    private String oMimeType;
    private String oFormatId;
    private String oDescription;
    private String oType;
    private boolean visible;
    private int cost;
    private HashMap<String, Property> properties;


    Conversion(String id, String iMimeType, String iFormatId, String iDescription, String iType, String oMimeType,
               String oFormatId, String oDescription, String oType, boolean visible, int cost,
               HashMap<String, Property> properties){
        this.id = id;
        this.iMimeType = iMimeType;
        this.iFormatId = iFormatId;
        this.iDescription = iDescription;
        this.iType = iType;
        this.oMimeType = oMimeType;
        this.oFormatId = oFormatId;
        this.oDescription = oDescription;
        this.oType = oType;
        this.visible = visible;
        this.cost = cost;
        this.properties = properties;
    }

    public String getId(){
        return id;
    }

    public String getIMimeType(){
        return iMimeType;
    }

    public String getOMimeType(){
        return oMimeType;
    }

    public String getOFormatId(){
        return oFormatId;
    }

    public String getIFormatId(){
        return iFormatId;
    }

    public String getInputDescription() {
        return iDescription;
    }

    public String getInputType() {
        return iType;
    }

    public String getOutputDescription() {
        return oDescription;
    }

    public String getOutputType() {
        return oType;
    }


    public boolean getVisible() {
        return visible;
    }

    public int getCost() {
        return cost;
    }

    public HashMap<String, Property> getProperties() { return properties; }



    protected static class Property {

        private String key;
        private String values;
        private String type;

        public Property(String key, String values, String type) {
            this.key = key;
            this.values = values;
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public String getValues() {
            return values;
        }

        public String getType() {
            return type;
        }
    }
}

