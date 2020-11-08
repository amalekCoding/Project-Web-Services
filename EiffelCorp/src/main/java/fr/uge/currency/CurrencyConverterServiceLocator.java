/**
 * CurrencyConverterServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.uge.currency;

public class CurrencyConverterServiceLocator extends org.apache.axis.client.Service implements fr.uge.currency.CurrencyConverterService {

    public CurrencyConverterServiceLocator() {
    }


    public CurrencyConverterServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CurrencyConverterServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CurrencyConverter
    private java.lang.String CurrencyConverter_address = "http://localhost:8080/CurrencyConverter/services/CurrencyConverter";

    public java.lang.String getCurrencyConverterAddress() {
        return CurrencyConverter_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CurrencyConverterWSDDServiceName = "CurrencyConverter";

    public java.lang.String getCurrencyConverterWSDDServiceName() {
        return CurrencyConverterWSDDServiceName;
    }

    public void setCurrencyConverterWSDDServiceName(java.lang.String name) {
        CurrencyConverterWSDDServiceName = name;
    }

    public fr.uge.currency.CurrencyConverter getCurrencyConverter() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CurrencyConverter_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCurrencyConverter(endpoint);
    }

    public fr.uge.currency.CurrencyConverter getCurrencyConverter(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            fr.uge.currency.CurrencyConverterSoapBindingStub _stub = new fr.uge.currency.CurrencyConverterSoapBindingStub(portAddress, this);
            _stub.setPortName(getCurrencyConverterWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCurrencyConverterEndpointAddress(java.lang.String address) {
        CurrencyConverter_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (fr.uge.currency.CurrencyConverter.class.isAssignableFrom(serviceEndpointInterface)) {
                fr.uge.currency.CurrencyConverterSoapBindingStub _stub = new fr.uge.currency.CurrencyConverterSoapBindingStub(new java.net.URL(CurrencyConverter_address), this);
                _stub.setPortName(getCurrencyConverterWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CurrencyConverter".equals(inputPortName)) {
            return getCurrencyConverter();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://currency.uge.fr", "CurrencyConverterService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://currency.uge.fr", "CurrencyConverter"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CurrencyConverter".equals(portName)) {
            setCurrencyConverterEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
