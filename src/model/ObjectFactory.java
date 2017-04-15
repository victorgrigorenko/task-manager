package model;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
	
	private final static QName Q_NAME = new QName(XMLConstants.NULL_NS_URI, "data");
	
	// Должен быть указан рутовский элемент (у нас это Journal)
    @SuppressWarnings("rawtypes")
	@XmlElementDecl(name = "jrnl")
	public JAXBElement<Journal> createJournal(Journal value){
		
		return new JAXBElement<Journal>(Q_NAME, Journal.class, null, value);
	}
}
