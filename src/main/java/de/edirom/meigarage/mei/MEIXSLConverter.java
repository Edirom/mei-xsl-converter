package de.edirom.meigarage.mei;

import net.sf.saxon.s9api.*;
import org.apache.log4j.Logger;
import org.tei.utils.SaxonProcFactory;
import org.tei.utils.XMLUtils;
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
import javax.xml.transform.TransformerException;
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

	private static final Logger LOGGER = Logger.getLogger(MEIXSLConverter.class);


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

	public void convert(InputStream inputStream, OutputStream outputStream,
			final ConversionActionArguments conversionDataTypes)
			throws ConverterException, IOException {
		boolean found = false;

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
							cadt.getProperties());
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
			DataType fromDataType, DataType toDataType, Map<String, String> properties) throws IOException,
			SaxonApiException, ConverterException {

		// from MusicXML to MEI
		if (fromDataType.getFormat().equals(Conversion.MUSICXMLTIMEWISETOMEI30.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MUSICXMLTIMEWISETOMEI30.getOFormatId())) {

			properties.put("extension", "xml");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/musicxml2mei/musicxml2mei-3.0.xsl", properties);

		}
		else if(fromDataType.getFormat().equals(Conversion.MEI30TOMEI40.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI30TOMEI40.getOFormatId())) {

			properties.put("extension", "xml");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mei30To40/mei30To40.xsl", properties);
		}
		else if(fromDataType.getFormat().equals(Conversion.MEI21TOMEI30.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI21TOMEI30.getOFormatId())) {

			properties.put("extension", "xml");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mei21To30/mei21To30.xsl", properties);
		}
		else if(fromDataType.getFormat().equals(Conversion.MUSICXMLPARTWISETOTIMEWISE.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MUSICXMLPARTWISETOTIMEWISE.getOFormatId())) {

			properties.put("extension", "xml");
			performXsltTransformation(inputStream, outputStream, "w3c-musicxml/schema/parttime.xsl", properties);
		}
		else if(fromDataType.getFormat().equals(Conversion.MUSICXMLTIMEWISETOPARTWISE.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MUSICXMLTIMEWISETOPARTWISE.getOFormatId())) {

			properties.put("extension", "xml");
			performXsltTransformation(inputStream, outputStream, "w3c-musicxml/schema/timepart.xsl", properties);
		}
		else if(fromDataType.getFormat().equals(Conversion.MARCXMLTOMEI30.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MARCXMLTOMEI30.getOFormatId())) {

			properties.put("extension", "xml");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/marc2mei/marc2mei.xsl", properties);
		}
		else if(fromDataType.getFormat().equals(Conversion.MEI2010TO2012.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI2010TO2012.getOFormatId())) {

			properties.put("extension", "xml");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mei2010To2012/mei2010To2012.xsl", properties);
		}
		else if(fromDataType.getFormat().equals(Conversion.MEI2012TOMEI21.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI2012TOMEI21.getOFormatId())) {

			properties.put("extension", "xml");
			performXsltTransformation(inputStream, outputStream, "encoding-tools/mei2012To2013/mei2012To2013.xsl", properties);
		}
		else if(fromDataType.getFormat().equals(Conversion.MEI40TOLILYPOND.getIFormatId()) &&
				toDataType.getFormat().equals(Conversion.MEI40TOLILYPOND.getOFormatId())) {

			properties.put("extension", "xml");
			performXsltTransformation(inputStream, outputStream, "meiler/mei2ly.xsl", properties);
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
		File inTempDir = null;
		String uid = UUID.randomUUID().toString();
		inTempDir = new File(EGEConstants.TEMP_PATH + File.separator + uid
				+ File.separator);
		inTempDir.mkdir();
		return inTempDir;
	}

	/*
	 * Performs transformation with XSLT
	 */
	private void performXsltTransformation(InputStream inputStream,
					       OutputStream outputStream, String xslt, Map<String, String> properties)
			throws IOException, SaxonApiException, ConverterException {
		FileOutputStream fos = null;
		InputStream is = null;
		File inTmpDir = null;
		File outTempDir = null;
		File outputDir = null;
		try {
			inTmpDir = prepareTempDir();
			ior.decompressStream(inputStream, inTmpDir);
			// avoid processing files ending in .bin
			File inputFile = searchForData(inTmpDir, "^.*(?<!bin)$");
			if(inputFile!=null) {
			outTempDir = prepareTempDir();
			is = prepareInputData(inputStream, inTmpDir, inputFile);
			Processor proc = SaxonProcFactory.getProcessor();
			XsltCompiler comp = proc.newXsltCompiler();
			XdmNode initialNode = getInitialNode(inputFile);
			String extension = properties.get("extension");
			File resFile = new File(outTempDir + File.separator + "document." + extension);
			fos = new FileOutputStream(resFile);
			XsltExecutable exec = comp.compile(new StreamSource(new FileInputStream(new File(
					ConverterConfiguration.getStylesheetsPath() + File.separator + xslt))));
			Xslt30Transformer transformer = exec.load30();
			transformer.setInitialContextNode(initialNode);
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
