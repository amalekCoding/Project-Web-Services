/**
 * DataBaseService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.uge.database;

public interface DataBaseService extends javax.xml.rpc.Service {
    public java.lang.String getDataBaseAddress();

    public fr.uge.database.DataBase getDataBase() throws javax.xml.rpc.ServiceException;

    public fr.uge.database.DataBase getDataBase(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
