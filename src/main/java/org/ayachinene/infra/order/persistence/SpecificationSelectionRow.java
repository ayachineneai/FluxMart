package org.ayachinene.infra.order.persistence;

public final class SpecificationSelectionRow {

    private String specificationCode;
    private String specificationName;
    private String specificationValueCode;
    private String specificationValue;

    public String getSpecificationCode() { return specificationCode; }
    public void setSpecificationCode(String value) { specificationCode = value; }

    public String getSpecificationName() { return specificationName; }
    public void setSpecificationName(String value) { specificationName = value; }

    public String getSpecificationValueCode() { return specificationValueCode; }
    public void setSpecificationValueCode(String value) { specificationValueCode = value; }

    public String getSpecificationValue() { return specificationValue; }
    public void setSpecificationValue(String value) { specificationValue = value; }
}
