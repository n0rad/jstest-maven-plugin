package net.awired.jstest.result;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.NONE)
public class FailureDescription {

    @XmlAttribute
    private String message;

    @XmlAttribute
    private String type;

    private String value;

    private String expected;

    private String actual;

    @XmlMixed
    List<?> getContent() {
        List<Object> res = new ArrayList<Object>();
        if (expected != null) {
            res.add(new JAXBElement<String>(new QName("expected"), String.class, expected));
        }
        if (actual != null) {
            res.add(new JAXBElement<String>(new QName("actual"), String.class, actual));
        }
        res.add(value);
        return res;
    }

    //////////////////:

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
