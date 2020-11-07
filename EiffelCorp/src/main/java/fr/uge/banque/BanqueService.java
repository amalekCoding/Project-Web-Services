/**
 * BanqueService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.uge.banque;

public interface BanqueService extends java.rmi.Remote {
    public boolean makePurchase(long clientId, int amount) throws java.rmi.RemoteException;
}
