import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLOutputFactory;

public class XMLConverter {
	/**
	 * Copy/format JSON using {@link XMLEventWriter#add(XMLEventReader)}.
	 * @param args ignored
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public static void main(String[] args) throws XMLStreamException, IOException,NoClassDefFoundError {
		System.out.print("Input filename :");
		Scanner scanner = new Scanner(System.in);
		String path = scanner.next();
		InputStream input = XMLConverter.class.getResourceAsStream(path);
		if(input==null) { 
			System.out.println("fail filename or path");
			return;
			}
		File file = new File("output.json");
		int num=0;
		while(file.exists()) {
			num++;
			file =new File("output"+num+".json");
		}
		System.out.println(file);
		OutputStream output = new FileOutputStream(file);
		/*
		 * If we want to insert JSON array boundaries for multiple elements,
		 * we need to set the <code>autoArray</code> property.
		 * If our XML source was decorated with <code>&lt;?xml-multiple?&gt;</code>
		 * processing instructions, we'd set the <code>multiplePI</code>
		 * property instead.
		 * With the <code>autoPrimitive</code> property set, element text gets
		 * automatically converted to JSON primitives (number, boolean, null).
		 */
		JsonXMLConfig config = new JsonXMLConfigBuilder()
			.autoArray(true)
			.autoPrimitive(true)
			.prettyPrint(true)
			.build();
		try {
			/*
			 * Create reader (XML).
			 */
			XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(input);
	
			/*
			 * Create writer (JSON).
			 */
			XMLEventWriter writer = new JsonXMLOutputFactory(config).createXMLEventWriter(output);
			
			/*
			 * Copy events from reader to writer.
			 */
			writer.add(reader);
			
			/*
			 * Close reader/writer.
			 */
			reader.close();
			writer.close();
		} finally {
			/*
			 * As per StAX specification, XMLEventReader/Writer.close() doesn't close
			 * the underlying stream.
			 */
			output.close();
			input.close();
		}
	}
}