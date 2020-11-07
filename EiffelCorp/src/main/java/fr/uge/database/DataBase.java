/**
 * DataBase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.uge.database;

public interface DataBase extends java.rmi.Remote {
    public void addGrade(long clientId, long vehicleId, int vehicleGrade, int conditionGrade) throws java.rmi.RemoteException;
    public boolean vehicleExists(long vehicleId) throws java.rmi.RemoteException;
    public boolean clientExists(long clientId) throws java.rmi.RemoteException;
    public int getVehiclePrice(long vehicleId) throws java.rmi.RemoteException;
}
