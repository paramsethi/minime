package me.mini.utils;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * XML Helper util functions
 * 
 * @author parampreetsethi
 * 
 */
public class XMLHelper {

	public static <T> String convertToXML(T entity) throws MiniMeException {
		String xml = null;
		try {

			final StringWriter stringWriter = new StringWriter();

			JAXBContext context = JAXBContext.newInstance(entity.getClass());

			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(entity, stringWriter);
			xml = stringWriter.toString();

		} catch (JAXBException e) {
			e.printStackTrace();
			throw new MiniMeException(e);
		}
		return xml;
	}
}
