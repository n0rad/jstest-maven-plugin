package net.awired.jstest.result;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import com.google.common.base.Objects;

@XmlAccessorType(XmlAccessType.NONE)
public class TestResult {

    @XmlAttribute
    private String classname;
    @XmlAttribute
    private String name;
    @XmlElement(name = "system-out")
    private String sysout;
    @XmlElement(name = "system-err")
    private String syserr;
    @XmlElement
    private Boolean skipped;
    @XmlElement
    private ErrorDescription error;
    @XmlElement
    private FailureDescription failure;

    private long duration;

    @XmlAttribute(name = "time")
    public double getTime() {
        return duration / 1000.0;
    }

    public boolean isSuccess() {
        return !isSkipped() && error == null && failure == null;
    }

    public boolean isFailure() {
        return error == null && failure != null;
    }

    public boolean isError() {
        return error != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TestResult) {
            final TestResult other = (TestResult) obj;
            return Objects.equal(name, other.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    /////////////////////////////////////////////////////////

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setSysout(String sysout) {
        this.sysout = sysout;
    }

    public String getSysout() {
        return sysout;
    }

    public void setSyserr(String syserr) {
        this.syserr = syserr;
    }

    public String getSyserr() {
        return syserr;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public ErrorDescription getError() {
        return error;
    }

    public void setError(ErrorDescription error) {
        this.error = error;
    }

    public FailureDescription getFailure() {
        return failure;
    }

    public void setFailure(FailureDescription failure) {
        this.failure = failure;
    }

    public boolean isSkipped() {
        return skipped != null && skipped != false;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }

}
