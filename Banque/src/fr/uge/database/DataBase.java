/**
 * DataBase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.uge.database;

public interface DataBase extends java.rmi.Remote {
    public java.lang.String getDateFormat() throws java.rmi.RemoteException;
    public int getClientBankBalance(long clientId) throws java.rmi.RemoteException;
    public boolean employeeExists(long employeeId) throws java.rmi.RemoteException;
    public void addGrade(java.lang.String date, long employeeId, long vehicleId, int vehicleGrade, int conditionGrade) throws java.rmi.RemoteException;
    public void registerRental(java.lang.String date, long employeeId, long vehicleId) throws java.rmi.RemoteException;
    public double getVehiclePrice(long vehicleId) throws java.rmi.RemoteException;
    public boolean vehicleExists(long vehicleId) throws java.rmi.RemoteException;
    public boolean hasRentedVehicle(long employeeId, long vehicleId) throws java.rmi.RemoteException;
    public int getRentalsNumber(long vehicleId) throws java.rmi.RemoteException;
    public long[] getVehiclesId() throws java.rmi.RemoteException;
    public void debiteClient(long clientId, int amount) throws java.rmi.RemoteException;
}
