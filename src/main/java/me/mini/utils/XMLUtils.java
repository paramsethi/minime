package me.mini.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * XML util functions to marshal objects
 *
 * @author parampreetsethi
 */
public class XMLUtils {

    /**
     * Convert any generic object to XML using JAXB
     *
     * @param obj
     * @param <T>
     * @return
     * @throws MinimeException
     */
    public static <T> String convertToXML(T obj) throws MinimeException {
        final StringWriter stringWriter = new StringWriter();
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(obj, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new MinimeException(e);
        }
    }
}
