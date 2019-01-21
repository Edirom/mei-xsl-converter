package de.edirom.meigarage.mei;

public enum Conversion {

    /*
      supported formats
     */
    MUSICXMLTIMEWISETOMEI21(
            "musicxmltimewisetomei21", // "id"
            "text/xml", // "mime type for target"
            "musicxml-timewise", // "input format id"
            "MusicXML Document (XML), timewise", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "mei21", // "output format id"
            "MEI 2.1 XML Document (.xml)", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9 // "cost"
    ),
    
    MUSICXMLPARTWISETOTIMEWISE(
            "musicxmlpartwisetotimewise", // "id"
            "text/xml", // "mime type for target"
            "musicxml-partwise", // "input format id"
            "MusicXML Document (XML), partwise", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "musicxml-timewise", // "output format id"
            "MusicXML Document (XML), timewise", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9 // "cost"
    ),
    
    MUSICXMLTIMEWISETOPARTWISE(
            "musicxmltimewisetopartwise", // "id"
            "text/xml", // "mime type for target"
            "musicxml-timewise", // "input format id"
            "MusicXML Document (XML), timewise", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "musicxml-partwise", // "output format id"
            "MusicXML Document (XML), partwise", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9 // "cost"
    ),

    MEI21TOMEI30(
            "mei21tomei30", // "id"
            "text/xml", // "mime type for target"
            "mei21", // "input format id"
            "MEI 2.1 XML Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "mei30", // "output format id"
            "MEI 3.0 XML Document (.xml)", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9 // "cost"
    );


    private String id;
    private String mimeType;
    private String iFormatId;
    private String iDescription;
    private String iType;
    private String oFormatId;
    private String oDescription;
    private String oType;
    private boolean visible;
    private int cost;

    Conversion(String id, String mimeType, String iFormatId, String iDescription, String iType, String oFormatId, String oDescription, String oType, boolean visible, int cost){
        this.id = id;
        this.mimeType = mimeType;
        this.iFormatId = iFormatId;
        this.iDescription = iDescription;
        this.iType = iType;
        this.oFormatId = oFormatId;
        this.oDescription = oDescription;
        this.oType = oType;
        this.visible = visible;
        this.cost = cost;
    }

    public String getId(){
        return id;
    }

    public String getMimeType(){
        return mimeType;
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
}
