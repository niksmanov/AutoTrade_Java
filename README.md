## Auto Trade


Auto Trade is a Java 25 application with React and Redux for selling vehicles and for connecting sellers with buyers. Visitors can search for a vehicle, while registered users can add or edit advertisements. Administrators has separated section with many functionalities like adding, editing and deleting users, vehicle types etc.. Also they can make other users administrators.


### Used technologies:

* Java 25
* Spring Boot 4
* React 16.6
* Redux 3.7

Create a local SQL login for development:

```sql
CREATE DATABASE AutoTrade;
CREATE LOGIN autotrade WITH PASSWORD = 'AutoTrade_dev_123', CHECK_POLICY = OFF;
USE AutoTrade;
CREATE USER autotrade FOR LOGIN autotrade;
ALTER ROLE db_owner ADD MEMBER autotrade;
```

To override the SQL Server connection, set `DB_URL` before running:

```powershell
$env:DB_URL="jdbc:sqlserver://localhost:1433;databaseName=AutoTrade;encrypt=true;trustServerCertificate=true"
$env:DB_USERNAME="autotrade"
$env:DB_PASSWORD="your-password"
.\mvnw.cmd spring-boot:run
```


:rocket: [**Website of the application**](https://autotrade-bulgaria.herokuapp.com/)
