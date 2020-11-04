package de.edirom.meigarage.mei;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

import static de.edirom.meigarage.mei.ConverterConfiguration.STYLESHEETS_PATH;

public class XMLUtils {

    /**
     * Reads a file into a JAXP XML document.
     *
     * @param file The input file.
     * @return The JAXP XML document.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document readInputFileIntoJAXPDoc(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder;
        Document doc = null;

        docBuilder = docBuilderFactory.newDocumentBuilder();
        docBuilder.setEntityResolver(new EntityResolver() {
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {

                String musicxmlPath = STYLESHEETS_PATH + "/w3c-musicxml/schema/";
                String fileName = systemId.substring(systemId.lastIndexOf("/"));

                if(systemId.contains("www.musicxml.org/dtds") ||
                        publicId.contains("-//Recordare//ELEMENTS") ||
                        publicId.contains("ISO 8879:1986//ENTITIES")) {

                    File f = new File(musicxmlPath + fileName);
                    if(f.exists()) {
                        InputStream in = new FileInputStream(f);
                        return new InputSource(new InputStreamReader(in));
                    }
                }

                return null;
            }
        });

        doc = docBuilder.parse(file);

        return doc;
    }
}
