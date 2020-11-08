package fr.uge.currency;

public class CurrencyConverterProxy implements fr.uge.currency.CurrencyConverter {
  private String _endpoint = null;
  private fr.uge.currency.CurrencyConverter currencyConverter = null;
  
  public CurrencyConverterProxy() {
    _initCurrencyConverterProxy();
  }
  
  public CurrencyConverterProxy(String endpoint) {
    _endpoint = endpoint;
    _initCurrencyConverterProxy();
  }
  
  private void _initCurrencyConverterProxy() {
    try {
      currencyConverter = (new fr.uge.currency.CurrencyConverterServiceLocator()).getCurrencyConverter();
      if (currencyConverter != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)currencyConverter)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)currencyConverter)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (currencyConverter != null)
      ((javax.xml.rpc.Stub)currencyConverter)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public fr.uge.currency.CurrencyConverter getCurrencyConverter() {
    if (currencyConverter == null)
      _initCurrencyConverterProxy();
    return currencyConverter;
  }
  
  public double convertEuroTo(java.lang.String currency, double amount) throws java.rmi.RemoteException{
    if (currencyConverter == null)
      _initCurrencyConverterProxy();
    return currencyConverter.convertEuroTo(currency, amount);
  }
  
  
}