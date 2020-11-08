/**
 * CurrencyConverterService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.uge.currency;

public interface CurrencyConverterService extends javax.xml.rpc.Service {
    public java.lang.String getCurrencyConverterAddress();

    public fr.uge.currency.CurrencyConverter getCurrencyConverter() throws javax.xml.rpc.ServiceException;

    public fr.uge.currency.CurrencyConverter getCurrencyConverter(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
