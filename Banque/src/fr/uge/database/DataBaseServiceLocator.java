/**
 * DataBaseServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.uge.database;

public class DataBaseServiceLocator extends org.apache.axis.client.Service implements fr.uge.database.DataBaseService {

    public DataBaseServiceLocator() {
    }


    public DataBaseServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DataBaseServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DataBase
    private java.lang.String DataBase_address = "http://localhost:8080/Database/services/DataBase";

    public java.lang.String getDataBaseAddress() {
        return DataBase_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DataBaseWSDDServiceName = "DataBase";

    public java.lang.String getDataBaseWSDDServiceName() {
        return DataBaseWSDDServiceName;
    }

    public void setDataBaseWSDDServiceName(java.lang.String name) {
        DataBaseWSDDServiceName = name;
    }

    public fr.uge.database.DataBase getDataBase() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DataBase_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDataBase(endpoint);
    }

    public fr.uge.database.DataBase getDataBase(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            fr.uge.database.DataBaseSoapBindingStub _stub = new fr.uge.database.DataBaseSoapBindingStub(portAddress, this);
            _stub.setPortName(getDataBaseWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDataBaseEndpointAddress(java.lang.String address) {
        DataBase_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (fr.uge.database.DataBase.class.isAssignableFrom(serviceEndpointInterface)) {
                fr.uge.database.DataBaseSoapBindingStub _stub = new fr.uge.database.DataBaseSoapBindingStub(new java.net.URL(DataBase_address), this);
                _stub.setPortName(getDataBaseWSDDServiceName());
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
        if ("DataBase".equals(inputPortName)) {
            return getDataBase();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://database.uge.fr", "DataBaseService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://database.uge.fr", "DataBase"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DataBase".equals(portName)) {
            setDataBaseEndpointAddress(address);
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
