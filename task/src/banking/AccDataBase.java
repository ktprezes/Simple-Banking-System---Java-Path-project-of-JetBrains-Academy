package banking;

import banking.constants.DBConst;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static banking.constants.AppConst.DEBUG_LVL;
import static banking.constants.AppConst.DO_LOG;

class AccDataBase implements DBConst {

    // LoggedConsoleIO is a 'singleton' class
    // it processes all System.in/out operations for our application
    // via its methods: 'read' nad 'write' with optional logging
    // because it's a singleton class, the value of 'loggedIO' variable
    // should be the same here and in the 'Main' class
    //
    static LoggedConsoleIO loggedIO = LoggedConsoleIO.getInstance();

    private static AccDataBase accDB = null;
    private static String dbFileName = null;
    private static String dbUrl = null;
    private static Connection connection = null;


    // private constructor (not publicly accessible)
    // we do need ONLY ONE instance of our database, so we do use 'singleton pattern'
    // and we will create (or get) the object - instance of SimpleBankingDataBase class
    // via the 'openOrCreate' method instead!
    //
    private AccDataBase() {
    } // private SimpleBankingDataBase() constructor


    // this method is the entry point to our 'SimpleBankingDataBase' class
    // we use it to create (only one!) or get already created before,
    // existing object of our SimpleBankingDataBase class
    // inputs:
    //  -  String dbFilePathAndName - eg. 'C:\db_folder\banking_db_file.s3db'
    // returns:
    //  -  reference to already existing, previously created instance
    //     of the 'SimpleBankingDatabase' class;
    //     WARNING!!!
    //     the 'dbFilePathAndName' input String is ignored in this case!!!
    //     one can check the previously used 'dbFileName' via calling the
    //     'getDbFileName()' method
    //  or
    //  -  reference to the newly created 'SimpleBankingDataBase' object with an
    //     open db connection to the database represented by 'dbFilePathAndName'
    //     input String - if everything is ok and there was no such object before
    //  or
    //  -  null if some error occurred - eg: input string is empty or invalid,
    //     or its corresponding database cannot be open nor created,
    //     or establishing the connection with that database is impossible
    //
    static AccDataBase openOrCreate(String dbFilePathAndName) {

        // do we already have some 'SimpleBankingDataBase' object created???
        // => return it!!! it's 'singleton design pattern'!!!
        if (accDB != null) {
            return accDB;
        }

        // here we know 'accDB' is null...
        // let's check the input String - does it promise to represent proper db file name?
        if (dbFilePathAndName == null || "".equals(dbFilePathAndName) || "null".equals(dbFilePathAndName)) {
            // no way - the input string is invalid
            // we won't create the 'accDB' object
            return null;
        }

        dbUrl = DB_CONN_STRING_PREFIX + dbFilePathAndName;

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            if (conn != null) {
                connection = conn;
                dbFileName = dbFilePathAndName;
                accDB = new AccDataBase();
                if (DEBUG_LVL > 0) {
                    loggedIO.print("SimpleBanking: the database " + dbFilePathAndName + "has been opened / created.", DO_LOG);
                }
            }
        } catch (SQLException e) {
            loggedIO.print(e.getMessage(), DO_LOG);
        }

        return accDB;
    } // static SimpleBankingDataBase openOrCreate()


    // returns String representing the name of the file of our db
    String getDbFileName() {
        return dbFileName;
    }


    // returns database's connection
    Connection getConnection() {
        return connection;
    }


    void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            loggedIO.print("SimpleBanking: database: try to close connection with some exception", DO_LOG);
            loggedIO.print(e.getMessage(), DO_LOG);
        } finally {
            dbUrl = null;
            dbFileName = null;
            connection = null;
            accDB = null;
            if (DEBUG_LVL > 0) {
                loggedIO.print("SimpleBanking: database has been closed.", DO_LOG);
            }
        }
    } // void close()

} // class SimpleBankingDataBase
