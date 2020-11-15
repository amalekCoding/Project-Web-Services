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
  
  public int getClientBankBalance(long clientId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.getClientBankBalance(clientId);
  }
  
  public long[] getPurchasedVehicles(long clientId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.getPurchasedVehicles(clientId);
  }
  
  public boolean authenticateEmployee(java.lang.String login, java.lang.String password) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.authenticateEmployee(login, password);
  }
  
  public boolean vehicleExists(long vehicleId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.vehicleExists(vehicleId);
  }
  
  public void addGrade(java.lang.String date, long employeeId, long vehicleId, int vehicleGrade, int conditionGrade) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    dataBase.addGrade(date, employeeId, vehicleId, vehicleGrade, conditionGrade);
  }
  
  public boolean employeeExists(long employeeId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.employeeExists(employeeId);
  }
  
  public boolean clientExists(long clientId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.clientExists(clientId);
  }
  
  public void debiteClient(long clientId, int amount) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    dataBase.debiteClient(clientId, amount);
  }
  
  public long[] getVehiclesId() throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.getVehiclesId();
  }
  
  public boolean hasRentedVehicle(long employeeId, long vehicleId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.hasRentedVehicle(employeeId, vehicleId);
  }
  
  public void registerRental(java.lang.String date, long employeeId, long vehicleId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    dataBase.registerRental(date, employeeId, vehicleId);
  }
  
  public void registerPurchase(java.lang.String date, long clientId, long vehicleId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    dataBase.registerPurchase(date, clientId, vehicleId);
  }
  
  public boolean authenticateClient(java.lang.String login, java.lang.String password) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.authenticateClient(login, password);
  }
  
  public int getRentalsNumber(long vehicleId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.getRentalsNumber(vehicleId);
  }
  
  public long[] getRentedVehicles(long employeeId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.getRentedVehicles(employeeId);
  }
  
  public java.lang.String getDateFormat() throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.getDateFormat();
  }
  
  public double getVehicleBuyingPrice(long vehicleId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.getVehicleBuyingPrice(vehicleId);
  }
  
  public double getVehicleRentalPrice(long vehicleId) throws java.rmi.RemoteException{
    if (dataBase == null)
      _initDataBaseProxy();
    return dataBase.getVehicleRentalPrice(vehicleId);
  }
  
  
}