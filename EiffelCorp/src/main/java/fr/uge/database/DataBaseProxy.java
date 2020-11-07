package fr.uge.database;

public class DataBaseProxy implements fr.uge.database.DataBase {
  private String _endpoint = null;
  private fr.uge.database.DataBase dataBase = null;
  
  public DataBaseProxy() {
    _initDataBaseProxy();
  }
  
  public DataBaseProxy(String endpoint) {
    _endpoint = endpoint;
    _initDataBaseProxy();
  }
  
  private void _initDataBaseProxy() {
    try {
      dataBase = (new fr.uge.database.DataBaseServiceLocator()).getDataBase();
      if (dataBase != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)dataBase)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)dataBase)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (dataBase != null)
      ((javax.xml.rpc.Stub)dataBase)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public fr.uge.database.DataBase getDataBase() {
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase;
  }
  
  public void addGrade(long clientId, long vehicleId, int vehicleGrade, int conditionGrade) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    dataBase.addGrade(clientId, vehicleId, vehicleGrade, conditionGrade);
  }
  
  public boolean vehicleExists(long vehicleId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.vehicleExists(vehicleId);
  }
  
  public boolean clientExists(long clientId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.clientExists(clientId);
  }
  
  public int getVehiclePrice(long vehicleId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.getVehiclePrice(vehicleId);
  }
  
  
}