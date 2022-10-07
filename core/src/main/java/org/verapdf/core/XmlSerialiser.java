/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
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
import java.nio.charset.StandardCharsets;
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

/**
 * Acknowledgements to <a href="https://gist.github.com/itavero/5858958">Arno
 * Moonen's gist</a> which got me going.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 3 Nov 2016:15:17:10
 */
public final class XmlSerialiser {
	private static final String NOID = "no-id"; //$NON-NLS-1$

	private XmlSerialiser() {

	}

	/**
	 * Convert a string to an object of a given class.
	 *
	 * @param type
	 *            Type of object
	 * @param source
	 *            Input string
	 * @return Object of the given type
	 * @throws JAXBException
	 */
	public static <T, C> C typeFromXml(Class<T> type, String source) throws JAXBException {
		return typeFromXml(type, new StringReader(source));
	}

	/**
	 * Convert the contents of a file to an object of a given class.
	 *
	 * @param type
	 *            Type of object
	 * @param source
	 *            File to be read
	 * @return Object of the given type
	 * @throws JAXBException
	 */
	public static <T, C> C typeFromXml(Class<T> type, File source) throws JAXBException {
		return typeFromXml(type, new StreamSource(source));
	}

	/**
	 * Convert the contents of a Reader to an object of a given class.
	 *
	 * @param type
	 *            Type of object
	 * @param source
	 *            Reader to be read
	 * @return Object of the given type
	 * @throws JAXBException
	 */
	public static <T, C> C typeFromXml(Class<T> type, Reader source) throws JAXBException {
		return typeFromXml(type, new StreamSource(source));
	}

	/**
	 * Convert the contents of an InputStream to an object of a given class.
	 *
	 * @param type
	 *            Type of object
	 * @param source
	 *            InputStream to be read
	 * @return Object of the given type
	 * @throws JAXBException
	 */
	public static <T, C> C typeFromXml(Class<T> type, InputStream source) throws JAXBException {
		return typeFromXml(type, new StreamSource(source));
	}

	/**
	 * Convert the contents of a Source to an object of a given class.
	 *
	 * @param type
	 *            Type of object
	 * @param source
	 *            Source to be used
	 * @return Object of the given type
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T, C> C typeFromXml(Class<T> type, Source source) throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(type);
		Unmarshaller u = ctx.createUnmarshaller();
		return (C) u.unmarshal(source, type).getValue();
	}

	/**
	 * Converts the contents of the string to a List with objects of the given
	 * class.
	 *
	 * @param type
	 *            Type to be used
	 * @param source
	 *            Input string
	 * @return List with objects of the given type
	 * @throws JAXBException
	 */
	public static <T> List<T> collectionFromXml(Class<T> type, String source) throws JAXBException {
		return collectionFromXml(type, new StringReader(source));
	}

	/**
	 * Converts the contents of the Reader to a List with objects of the given
	 * class.
	 *
	 * @param type
	 *            Type to be used
	 * @param source
	 *            Input
	 * @return List with objects of the given type
	 * @throws JAXBException
	 */
	public static <T> List<T> collectionFromXml(Class<T> type, Reader source) throws JAXBException {
		return collectionFromXml(type, new StreamSource(source));
	}

	/**
	 * Converts the contents of the InputStream to a List with objects of the
	 * given class.
	 *
	 * @param type
	 *            Type to be used
	 * @param source
	 *            Input
	 * @return List with objects of the given type
	 * @throws JAXBException
	 */
	public static <T> List<T> collectionFromXml(Class<T> type, InputStream source) throws JAXBException {
		return collectionFromXml(type, new StreamSource(source));
	}

	/**
	 * Converts the contents of the Source to a List with objects of the given
	 * class.
	 *
	 * @param type
	 *            Type to be used
	 * @param source
	 *            Input
	 * @return List with objects of the given type
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> collectionFromXml(Class<T> type, Source source) throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(JAXBCollection.class, type);
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
	protected static <T> Class<?>[] findTypes(Collection<T> c) {
		Set<Class<?>> types = new HashSet<>();
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
	 * a generic collection without a separate wrapper class.
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
		m.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
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
