package de.edirom.meigarage.mei;

import net.sf.saxon.s9api.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.tei.utils.SaxonProcFactory;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import pl.psnc.dl.ege.component.Converter;
import pl.psnc.dl.ege.configuration.EGEConfigurationManager;
import pl.psnc.dl.ege.configuration.EGEConstants;
import pl.psnc.dl.ege.exception.ConverterException;
import pl.psnc.dl.ege.types.ConversionActionArguments;
import pl.psnc.dl.ege.types.DataType;
import pl.psnc.dl.ege.utils.EGEIOUtils;
import pl.psnc.dl.ege.utils.IOResolver;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * <p>
 * EGE Converter interface implementation
 * </p>
 *
 * Provides multiple conversions for MEI format.<br>
 * <b>Important : </b> the converter expects only compressed data. Data is
 * compressed with standard EGE IOResolver received from
 * EGEConfigurationManager.
 *
 * @author roewenstrunk
 *
 */
public class MEIXSLConverter implements Converter,ErrorHandler {

	private static final Logger LOGGER = LogManager.getLogger(MEIXSLConverter.class);


	private IOResolver ior = EGEConfigurationManager.getInstance()
			.getStandardIOResolver();


	public void error(TransformerException exception)
			throws TransformerException {
		LOGGER.info("Error: " + exception.getMessage());
	}


	public void fatalError(TransformerException exception)
			throws TransformerException {
		LOGGER.info("Fatal Error: " + exception.getMessage());
		throw exception;
	}


	public void warning(TransformerException exception)
			throws TransformerException {
		LOGGER.info("Warning: " + exception.getMessage());
	}


	public void error(SAXParseException exception) throws SAXException {
		LOGGER.info("Error: " + exception.getMessage());
	}


	public void fatalError(SAXParseException exception) throws SAXException {
		LOGGER.info("Fatal Error: " + exception.getMessage());
		throw exception;
	}


	public void warning(SAXParseException exception) throws SAXException {
		LOGGER.info("Warning: " + exception.getMessage());
	}

	public void convert(InputStream inputStream, OutputStream outputStream, ConversionActionArguments conversionDataTypes) throws ConverterException, IOException{
		convert(inputStream,outputStream,conversionDataTypes, null);
	}

	public void convert(InputStream inputStream, OutputStream outputStream,
			final ConversionActionArguments conversionDataTypes, String tempDir)
			throws ConverterException, IOException {
		boolean found = false;

		System.setProperty("http.agent", "Chrome");

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		try {
			for (ConversionActionArguments cadt : ConverterConfiguration.CONVERSIONS) {
				if (conversionDataTypes.equals(cadt)) {
					String profile = cadt.getProperties().get(ConverterConfiguration.PROFILE_KEY);
					LOGGER.info(dateFormat.format(date) + ": Converting FROM:  "
						    + conversionDataTypes.getInputType().toString()
						    + " TO "
						    + conversionDataTypes.getOutputType().toString()
						    + " WITH profile " + profile );
					convertDocument(inputStream, outputStream, cadt.getInputType(), cadt.getOutputType(),
							cadt.getProperties(), tempDir);
					found = true;
				}
			}
		} catch (SaxonApiException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new ConverterException(ex.getMessage());
		}
		if (!found) {
			throw new ConverterException(
					ConverterException.UNSUPPORTED_CONVERSION_TYPES);
		}
	}

	/*
	 * Prepares transformation : based on MIME type.
	 */
	private void convertDocument(InputStream inputStream, OutputStream outputStream,
			DataType fromDataType, DataType toDataType, Map<String, String> properties, String tempDir) throws IOException,
			SaxonApiException, ConverterException {

		// from MusicXML to MEI
		if (fromDataType.getFormat().equals(Conversion.MUSICXMLTIMEWISETOMEI30.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MUSICXMLTIMEWISETOMEI30.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "encoding-tools/musicxml2mei/");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/musicxml2mei/musicxml2mei-3.0.xsl", properties, tempDir);

		}
		else if(fromDataType.getFormat().equals(Conversion.MEI30TOMEI40.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI30TOMEI40.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "encoding-tools/mei30To40/");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mei30To40/mei30To40.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.MEI21TOMEI30.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI21TOMEI30.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "encoding-tools/mei21To30/");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mei21To30/mei21To30.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.MUSICXMLPARTWISETOTIMEWISE.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MUSICXMLPARTWISETOTIMEWISE.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "w3c-musicxml/schema/");
			performXsltTransformation(inputStream, outputStream, "w3c-musicxml/schema/parttime.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.MUSICXMLTIMEWISETOPARTWISE.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MUSICXMLTIMEWISETOPARTWISE.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "w3c-musicxml/schema/");
			performXsltTransformation(inputStream, outputStream, "w3c-musicxml/schema/timepart.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.MARCXMLTOMEI30.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MARCXMLTOMEI30.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "encoding-tools/marc2mei/");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/marc2mei/marc2mei.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.MEI2010TO2012.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI2010TO2012.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "encoding-tools/mei2010To2012/");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mei2010To2012/mei2010To2012.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.MEI2012TOMEI21.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI2012TOMEI21.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "encoding-tools/mei2012To2013/");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mei2012To2013/mei2012To2013.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.MEI40TOLILYPOND.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI40TOLILYPOND.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "meiler/mei2ly.xsl");
			performXsltTransformation(inputStream, outputStream, "meiler/mei2ly.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.COMPAREFILES.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.COMPAREFILES.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "data-configuration/scripts/");
			performXsltTransformation(inputStream, outputStream, "data-configuration/scripts/compare.files.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.MEI2MARC.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI2MARC.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "encoding-tools/mei2marc/");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mei2marc/mei2marc.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.MEI2MODS.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI2MODS.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "encoding-tools/mei2mods/");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mei2mods/mei2mods.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.MEI2MUP.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI2MUP.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "    encoding-tools/mei2mup/");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mei2mup/mei2mup-1.0.3.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.MEI2MUSICXML.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI2MUSICXML.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "    encoding-tools/mei2musicxml/");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mei2musicxml/mei2musicxml.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.MEI40TO50.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI40TO50.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "    encoding-tools/mei40To50/");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mei40To50/mei40To50.xsl", properties, tempDir);
		}
		else if(fromDataType.getFormat().equals(Conversion.MNX2MEI.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MNX2MEI.getOFormatId())) {

			properties.put("extension", "xml");
			properties.put("base", "    encoding-tools/mnx2mei/");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mnx2mei/mnx2mei.xsl", properties, tempDir);
		}
	}

	/*
	 * prepares received data - decompress, search for file to convert and open file stream.
	 */
	private InputStream prepareInputData(InputStream inputStream, File inTempDir)
			throws IOException, ConverterException {
		ior.decompressStream(inputStream, inTempDir);
		File sFile = searchForData(inTempDir, "^.*\\.((?i)xml)$");
		if (sFile == null) {
			//search for any file
			sFile = searchForData(inTempDir, "^.*");
			if(sFile == null){
				throw new ConverterException("No file data was found for conversion");
			}
		}
		FileInputStream fis = new FileInputStream(sFile);
		return fis;
	}

	/*
	 * prepares received data - decompress and open file stream, doesn't search for xml file, it's supplied as argument
	 */
	private InputStream prepareInputData(InputStream inputStream, File inTempDir, File inputFile)
			throws IOException, ConverterException {
		if (inputFile == null) {
			//search for any file
			inputFile = searchForData(inTempDir, "^.*");
			if(inputFile == null){
				throw new ConverterException("No file data was found for conversion");
			}
		}
		FileInputStream fis = new FileInputStream(inputFile);
		return fis;
	}

	/*
	 * Search for specified by regex file
	 */
	private File searchForData(File dir, String regex) {
		for (File f : dir.listFiles()) {
			if (!f.isDirectory() && Pattern.matches(regex, f.getName())) {
				return f;
			} else if (f.isDirectory()) {
				File sf = searchForData(f, regex);
				if (sf != null) {
					return sf;
				}
			}
		}
		return null;
	}

	private File prepareTempDir() {
		return prepareTempDir(null);
	}
	private File prepareTempDir(String tempDir) {
		File inTempDir = null;
		String uid = UUID.randomUUID().toString();
		if(tempDir!=null){
			inTempDir = new File(tempDir + File.separator + uid
					+ File.separator);
		} else {
			inTempDir = new File(EGEConstants.TEMP_PATH + File.separator + uid
					+ File.separator);
		}
		inTempDir.mkdir();
		return inTempDir;
	}

	/*
	 * Performs transformation with XSLT
	 */
	private void performXsltTransformation(InputStream inputStream,
										   OutputStream outputStream, String xslt, final Map<String, String> properties, String tempDir)
			throws IOException, SaxonApiException, ConverterException {
		FileOutputStream fos = null;
		InputStream is = null;
		File inTmpDir = null;
		File outTempDir = null;
		File outputDir = null;
		try {
			inTmpDir = prepareTempDir(tempDir);
			ior.decompressStream(inputStream, inTmpDir);
			// avoid processing files ending in .bin
			File inputFile = searchForData(inTmpDir, "^.*(?<!bin)$");
			if(inputFile!=null) {
			outTempDir = prepareTempDir(tempDir);
			is = prepareInputData(inputStream, inTmpDir, inputFile);
			Processor proc = SaxonProcFactory.getProcessor();
			DocumentBuilder documentBuilder = proc.newDocumentBuilder();
			//required to prevent xxe injections
			try {
				documentBuilder.build(inputFile);
			} catch (SaxonApiException e) {
				LOGGER.error("There is a Doctype Declaration present in the source document that cannot be processed due to security reasons. Please remove it from your file and try again. "
						+ e.getMessage());
				return;
			}
			XsltCompiler comp = proc.newXsltCompiler();
			XdmNode initialNode = getInitialNode(inputFile);
			String extension = properties.get("extension");
			File resFile = new File(outTempDir + File.separator + "document." + extension);
			fos = new FileOutputStream(resFile);

			final URIResolver resolver = comp.getURIResolver();

			comp.setURIResolver(new URIResolver() {
				public Source resolve(String href, String base) throws TransformerException {

					Source s = resolver.resolve(href, ConverterConfiguration.getStylesheetsPath() +
							File.separator + File.separator + properties.get("base"));

					if(s != null) {
						File fs = new File(s.getSystemId());
						if(fs.exists())
							return s;
					}

					return resolver.resolve(href, base);
				}
			});

			XsltExecutable exec = comp.compile(new StreamSource(new FileInputStream(new File(
					ConverterConfiguration.getStylesheetsPath() + File.separator + xslt))));
			Xslt30Transformer transformer = exec.load30();
			transformer.setGlobalContextItem(initialNode);
			Serializer result = proc.newSerializer();
			result.setOutputStream(fos);
			transformer.applyTemplates(initialNode, result);
			ior.compressData(outTempDir, outputStream);
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception ex) {
				// do nothing
			}
			try {
				fos.close();
			} catch (Exception ex) {
				// do nothing
			}
			if (outTempDir != null && outTempDir.exists())
				EGEIOUtils.deleteDirectory(outTempDir);
			if (inTmpDir != null && inTmpDir.exists())
				EGEIOUtils.deleteDirectory(inTmpDir);
			}
	}

	private XdmNode getInitialNode(File inputFile) throws IOException, SAXException, ParserConfigurationException {
		Document dom = XMLUtils.readInputFileIntoJAXPDoc(inputFile);
		Processor proc = SaxonProcFactory.getProcessor();
		net.sf.saxon.s9api.DocumentBuilder builder = proc.newDocumentBuilder();
		return builder.wrap(dom);
	}

	public List<ConversionActionArguments> getPossibleConversions() {
		return ConverterConfiguration.CONVERSIONS;
	}
}
