/*
 * Copyright (c) 2013, Arno Moonen <info@arnom.nl>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.verapdf.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public final class XmlSerialiser {
	private static final String NOID = "no-id"; //$NON-NLS-1$

	private XmlSerialiser() {

	}

	/**
	 * Convert a string to an object of a given class.
	 *
	 * @param clz
	 *            Type of object
	 * @param source
	 *            Input string
	 * @return Object of the given type
	 * @throws JAXBException
	 */
	public static <T> T typeFromXml(Class<T> clz, String source) throws JAXBException {
		return typeFromXml(clz, new StringReader(source));
	}

	/**
	 * Convert the contents of a file to an object of a given class.
	 *
	 * @param clz
	 *            Type of object
	 * @param source
	 *            File to be read
	 * @return Object of the given type
	 * @throws JAXBException
	 */
	public static <T> T typeFromXml(Class<T> clz, File source) throws JAXBException {
		return typeFromXml(clz, new StreamSource(source));
	}

	/**
	 * Convert the contents of a Reader to an object of a given class.
	 *
	 * @param clz
	 *            Type of object
	 * @param source
	 *            Reader to be read
	 * @return Object of the given type
	 * @throws JAXBException
	 */
	public static <T> T typeFromXml(Class<T> clz, Reader source) throws JAXBException {
		return typeFromXml(clz, new StreamSource(source));
	}

	/**
	 * Convert the contents of an InputStream to an object of a given class.
	 *
	 * @param clz
	 *            Type of object
	 * @param source
	 *            InputStream to be read
	 * @return Object of the given type
	 * @throws JAXBException
	 */
	public static <T> T typeFromXml(Class<T> clz, InputStream source) throws JAXBException {
		return typeFromXml(clz, new StreamSource(source));
	}

	/**
	 * Convert the contents of a Source to an object of a given class.
	 *
	 * @param clz
	 *            Type of object
	 * @param source
	 *            Source to be used
	 * @return Object of the given type
	 * @throws JAXBException
	 */
	public static <T> T typeFromXml(Class<T> clz, Source source) throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(clz);
		Unmarshaller u = ctx.createUnmarshaller();
		return u.unmarshal(source, clz).getValue();
	}

	/**
	 * Converts the contents of the string to a List with objects of the given
	 * class.
	 *
	 * @param clz
	 *            Type to be used
	 * @param source
	 *            Input string
	 * @return List with objects of the given type
	 * @throws JAXBException
	 */
	public static <T> List<T> collectionFromXml(Class<T> clz, String source) throws JAXBException {
		return collectionFromXml(clz, new StringReader(source));
	}

	/**
	 * Converts the contents of the Reader to a List with objects of the given
	 * class.
	 *
	 * @param clz
	 *            Type to be used
	 * @param source
	 *            Input
	 * @return List with objects of the given type
	 * @throws JAXBException
	 */
	public static <T> List<T> collectionFromXml(Class<T> clz, Reader source) throws JAXBException {
		return collectionFromXml(clz, new StreamSource(source));
	}

	/**
	 * Converts the contents of the InputStream to a List with objects of the
	 * given class.
	 *
	 * @param clz
	 *            Type to be used
	 * @param source
	 *            Input
	 * @return List with objects of the given type
	 * @throws JAXBException
	 */
	public static <T> List<T> collectionFromXml(Class<T> clz, InputStream source) throws JAXBException {
		return collectionFromXml(clz, new StreamSource(source));
	}

	/**
	 * Converts the contents of the Source to a List with objects of the given
	 * class.
	 *
	 * @param clz
	 *            Type to be used
	 * @param source
	 *            Input
	 * @return List with objects of the given type
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> collectionFromXml(Class<T> clz, Source source) throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(JAXBCollection.class, clz);
		Unmarshaller u = ctx.createUnmarshaller();
		JAXBCollection<T> collection = u.unmarshal(source, JAXBCollection.class).getValue();
		return collection.getItems();
	}

	/**
	 * Convert an object to a string.
	 *
	 * @param obj
	 *            Object that needs to be serialized / marshalled.
	 * @return String representation of obj
	 * @throws JAXBException
	 */
	public static <T> String toXml(T obj, boolean format, boolean fragment) throws JAXBException {
		StringWriter sw = new StringWriter();
		toXml(obj, sw, format, fragment);
		return sw.toString();
	}

	/**
	 * Convert an object to a string and send it to a Writer.
	 *
	 * @param obj
	 *            Object that needs to be serialized / marshalled
	 * @param wr
	 *            Writer used for outputting the marshalled object
	 * @throws JAXBException
	 */
	public static <T> void toXml(T obj, Writer wr, boolean format, boolean fragment) throws JAXBException {
		Marshaller m = marshaller(JAXBContext.newInstance(obj.getClass()), format, fragment);
		m.marshal(obj, wr);
	}

	/**
	 * Convert an object to a string and send it to a Writer.
	 *
	 * @param obj
	 *            Object that needs to be serialized / marshalled
	 * @param wr
	 *            Writer used for outputting the marshalled object
	 * @throws JAXBException
	 */
	public static <T> void toXml(T obj, XMLStreamWriter wr, boolean format, boolean fragment) throws JAXBException {
		Marshaller m = marshaller(JAXBContext.newInstance(obj.getClass()), format, fragment);
		m.marshal(obj, wr);
	}

	/**
	 * Convert an object to a string and save it to a File.
	 *
	 * @param obj
	 *            Object that needs to be serialized / marshalled
	 * @param f
	 *            Save file
	 * @throws JAXBException
	 */
	public static <T> void toXml(T obj, File f, boolean format, boolean fragment) throws JAXBException {
		Marshaller m = marshaller(JAXBContext.newInstance(obj.getClass()), format, fragment);
		m.marshal(obj, f);
	}

	/**
	 * Convert an object to a string and send it to an OutputStream.
	 *
	 * @param obj
	 *            Object that needs to be serialized / marshalled
	 * @param source
	 *            Stream used for output
	 * @throws JAXBException
	 */
	public static <T> void toXml(T obj, OutputStream source, boolean format, boolean fragment) throws JAXBException {
		Marshaller m = marshaller(JAXBContext.newInstance(obj.getClass()), format, fragment);
		m.marshal(obj, source);
	}

	/**
	 * Convert a collection to a string.
	 *
	 * @param rootName
	 *            Name of the XML root element
	 * @param c
	 *            Collection that needs to be marshalled
	 * @return String representation of the collection
	 * @throws JAXBException
	 */
	public static <T> String toXml(String rootName, Collection<T> c, boolean format, boolean fragment)
			throws JAXBException {
		StringWriter sw = new StringWriter();
		toXml(rootName, c, sw, format, fragment);
		return sw.toString();
	}

	/**
	 * Convert a collection to a string and sends it to the Writer.
	 *
	 * @param rootName
	 *            Name of the XML root element
	 * @param c
	 *            Collection that needs to be marshalled
	 * @param w
	 *            Output
	 * @throws JAXBException
	 */
	public static <T> void toXml(String rootName, Collection<T> c, Writer w, boolean format, boolean fragment)
			throws JAXBException {
		Marshaller m = marshaller(JAXBContext.newInstance(findTypes(c)), format, fragment);

		// Create wrapper collection
		JAXBElement<?> element = createCollectionElement(rootName, c);
		m.marshal(element, w);
	}

	/**
	 * Convert a collection to a string and stores it in a File.
	 *
	 * @param rootName
	 *            Name of the XML root element
	 * @param c
	 *            Collection that needs to be marshalled
	 * @param f
	 *            Output file
	 * @throws JAXBException
	 */
	public static <T> void toXml(String rootName, Collection<T> c, File f, boolean format, boolean fragment)
			throws JAXBException {
		Marshaller m = marshaller(JAXBContext.newInstance(findTypes(c)), format, fragment);

		// Create wrapper collection
		JAXBElement<?> element = createCollectionElement(rootName, c);
		m.marshal(element, f);
	}

	/**
	 * Convert a collection to a string and sends it to the OutputStream.
	 *
	 * @param rootName
	 *            Name of the XML root element
	 * @param c
	 *            Collection that needs to be marshalled
	 * @param dest
	 *            Output
	 * @throws JAXBException
	 */
	public static <T> void toXml(String rootName, Collection<T> c, OutputStream dest, boolean format, boolean fragment)
			throws JAXBException {
		Marshaller m = marshaller(JAXBContext.newInstance(findTypes(c)), format, fragment);
		JAXBElement<?> element = createCollectionElement(rootName, c);
		m.marshal(element, dest);
	}

	public static <T> void schema(T obj, Writer schemaDest) throws IOException, JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(obj.getClass());
		ctx.generateSchema(new WriterSchemaOutputResolver(schemaDest));
	}

	public static <T> void schema(T obj, OutputStream schemaDest) throws IOException, JAXBException {
		Writer writer = new OutputStreamWriter(schemaDest);
		schema(obj, writer);
	}

	public static <T> String schema(T obj) throws IOException, JAXBException {
		Writer writer = new StringWriter();
		schema(obj, writer);
		return writer.toString();
	}

	/**
	 * Discovers all the classes in the given Collection. These need to be in
	 * the JAXBContext if you want to marshal those objects. Unfortunatly
	 * there's no way of getting the generic type at runtime.
	 *
	 * @param c
	 *            Collection that needs to be scanned
	 * @return Classes found in the collection, including JAXBCollection.
	 */
	protected static <T> Class[] findTypes(Collection<T> c) {
		Set<Class> types = new HashSet<>();
		types.add(JAXBCollection.class);
		for (T o : c) {
			if (o != null) {
				types.add(o.getClass());
			}
		}
		return types.toArray(new Class[0]);
	}

	/**
	 * Create a JAXBElement containing a JAXBCollection. Needed for marshalling
	 * a generic collection without a seperate wrapper class.
	 *
	 * @param rootName
	 *            Name of the XML root element
	 * @param c
	 * @return JAXBElement containing the given Collection, wrapped in a
	 *         JAXBCollection.
	 */
	protected static <T> JAXBElement<?> createCollectionElement(String rootName, Collection<T> c) {
		JAXBCollection<T> collection = new JAXBCollection<>(c);
		return new JAXBElement<>(new QName(rootName), JAXBCollection.class, collection);
	}

	private static Marshaller marshaller(JAXBContext ctx, boolean format, boolean fragment) throws JAXBException {
		Marshaller m = ctx.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.valueOf(format));
		if (fragment) {
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		}
		return m;
	}

	private static class WriterSchemaOutputResolver extends SchemaOutputResolver {
		private final Writer out;

		/**
		 * @param out
		 *            a Writer for the generated schema
		 */
		public WriterSchemaOutputResolver(final Writer out) {
			super();
			this.out = out;
		}

		/**
		 * { @inheritDoc }
		 */
		@Override
		public Result createOutput(String namespaceUri, String suggestedFileName) {
			final StreamResult result = new StreamResult(this.out);
			result.setSystemId(NOID);
			return result;
		}

	}
}
