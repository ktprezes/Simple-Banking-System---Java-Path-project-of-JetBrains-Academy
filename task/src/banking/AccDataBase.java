package banking;

import banking.constants.DBConst;

import java.sql.*;

import static banking.constants.AppConst.*;

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
    private static Statement sqlStmt = null;
    private static PreparedStatement sqlPstmtNewCard = null;
    private static PreparedStatement sqlPstmtQueryCardNoPin = null;
    private static PreparedStatement sqlPstmtQueryCardNo = null;


    // private constructor (not publicly accessible)
    // we do need ONLY ONE instance of our database, so we do use 'singleton pattern'
    // and we will create (or get) the object - instance of AccDataBase class
    // via the 'openOrCreate' method instead!
    //
    private AccDataBase() {
    } // private AccDataBase() constructor


    // this method is the entry point to our 'AccDataBase' class
    // we use it to create (only one!) or get already created before,
    // existing object of our AccDataBase class
    // inputs:
    //  -  String dbFilePathAndName - eg. 'C:\db_folder\banking_db_file.s3db'
    // returns:
    //  -  reference to already existing, previously created instance
    //     of the 'AccDataBase' class;
    //     WARNING!!!
    //     the 'dbFilePathAndName' input String is ignored in this case!!!
    //     one can check the previously used 'dbFileName' via calling the
    //     'getDbFileName()' method
    //  or
    //  -  reference to the newly created 'AccDataBase' object with an
    //     open db connection to the database represented by 'dbFilePathAndName'
    //     input String - if everything is ok and there was no such object before
    //  or
    //  -  null if some error occurred - eg: input string is empty or invalid,
    //     or its corresponding database cannot be open nor created,
    //     or establishing the connection with that database is impossible
    //
    static AccDataBase openOrCreate(String dbFilePathAndName) {

        // do we already have some 'AccDataBase' object created???
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

        try {
            connection = DriverManager.getConnection(dbUrl);
            if (connection != null) {
                dbFileName = dbFilePathAndName;
                sqlStmt = connection.createStatement();
                sqlPstmtNewCard = connection.prepareStatement(SQL_TABLE_CARD_INSERT_NEW_CARD);
                sqlPstmtQueryCardNoPin = connection.prepareStatement(SQL_TABLE_CARD_QUERY_CARD_BY_NUMBER_AND_PIN);
                sqlPstmtQueryCardNo = connection.prepareStatement(SQL_TABLE_CARD_QUERY_CARD_BY_NUMBER);
                accDB = new AccDataBase();
                if (DEBUG_LVL > 0) {
                    loggedIO.print(APP_NAME + ": the '" + dbFilePathAndName + "' database has been opened / created.", DO_LOG);
                }
            }
        } catch (SQLException e) {
            loggedIO.print(APP_NAME + ": an error occurred while trying to open / create the database '" + dbFilePathAndName + "':", DO_LOG);
            loggedIO.print(e.getMessage(), DO_LOG);
        }

        return accDB;
    } // static AccDataBase openOrCreate()


    // returns String representing the name of the file of our db
    String getDbFileName() {
        return dbFileName;
    }


    // returns database's connection
    Connection getConnection() {
        return connection;
    }


    // as of 'stage#3' of this project the 'card' table should have the following columns:
    //        id      INTEGER
    //        number  TEXT
    //        pin     TEXT
    //        balance INTEGER DEFAULT 0
    boolean ensureTableCardStructure() {

        if (connection == null) {
            loggedIO.print(APP_NAME + ": inside 'ensureCardTableStructure()' - connection is null", DO_LOG);
            loggedIO.print(APP_NAME + ": no way to ensure the 'card' table proper structure", DO_LOG);
            return false;
        }

        boolean result = false;

        try { // sql statement was created just after establishing the connection to the database
            sqlStmt.execute(SQL_TABLE_CARD_CREATING_STRING);

            // if we got to this point, then probably everything is fine :)
            result = true;

        } catch (SQLException e) {
            loggedIO.print(APP_NAME + ": an error occurred while trying to create the DB table 'card'", DO_LOG);
            loggedIO.print(e.getMessage(), DO_LOG);
        }

        return result;
    } // boolean ensureTableCardStructure()


    // returns the 'ResultSet' representing all rows
    // in the table 'card' of our database
    // getting all these rows from that 'ResultSet' - eg.:
    // rs = db.getAllCards();
    // while(rs != null && rs.next()) {
    //      System.out.format("id: %d, number: %s, pin: %s, balance = %d%n",
    //          rs.getInt("id"), rs.getString("number"), rs.getString("pin"), rs.getInt("balance"));
    // };
    //
    ResultSet getAllCards() {
        ResultSet rs = null;
        try {
            rs = sqlStmt.executeQuery(SQL_TABLE_CARD_SELECT_ALL);
        } catch (SQLException e) {
            loggedIO.print(APP_NAME + ": an error occurred while trying to select all rows from the DB table 'card'", DO_LOG);
            loggedIO.print(e.getMessage(), DO_LOG);
        }

        return rs;
    } // ResultSet getAllCards()


    // boolean addNewCard(String no, String pin)
    // adds new card data int the table 'card' of our database
    //
    boolean addNewCard(String no, String pin) {
        boolean retVal = false;
        try {
            sqlPstmtNewCard.setString(1, no);
            sqlPstmtNewCard.setString(2, pin);
            sqlPstmtNewCard.executeUpdate();
            if (DEBUG_LVL > 0) {
                loggedIO.print(APP_NAME + ": new card data has been added into the DB table 'card'", DO_LOG);
            }
            retVal = true;
        } catch (SQLException e) {
            loggedIO.print(APP_NAME + ": an error occurred while trying to new card data into the DB table 'card'", DO_LOG);
            loggedIO.print(e.getMessage(), DO_LOG);

        } // try {} catch(){}
        return retVal;
    } // boolean addNewCard(String, String)


    boolean containsAccNoAndPin(String no, String pin) {
        ResultSet rs = null;

        boolean retVal = false;
        try {
            sqlPstmtQueryCardNoPin.setString(1, no);
            sqlPstmtQueryCardNoPin.setString(2, pin);
            rs = sqlPstmtQueryCardNoPin.executeQuery();
            while (rs.next()) {
                if (rs.getString("number").equals(no) && rs.getString("pin").equals(pin)) {
                    retVal = true;
                    break;
                }
            }
        } catch (SQLException e) {
            loggedIO.print(APP_NAME + ": an error occurred while trying to query the DB table 'card'", DO_LOG);
            loggedIO.print(APP_NAME + ": for the card no: " + no + " with pin: " + pin, DO_LOG);
            loggedIO.print(e.getMessage(), DO_LOG);
        } // try {} catch(){}

        return retVal;

    } // boolean containsAccNoAndPin(String no, String pin)


    boolean containsAccNo(String no) {
        ResultSet rs = null;

        boolean retVal = false;
        try {
            sqlPstmtQueryCardNo.setString(1, no);
            rs = sqlPstmtQueryCardNo.executeQuery();
            while (rs.next()) {
                if (rs.getString("number").equals(no)) {
                    retVal = true;
                    break;
                }
            }
        } catch (SQLException e) {
            loggedIO.print(APP_NAME + ": an error occurred while trying to query the DB table 'card'", DO_LOG);
            loggedIO.print(APP_NAME + ": for the card no: " + no, DO_LOG);
            loggedIO.print(e.getMessage(), DO_LOG);
        } // try {} catch(){}

        return retVal;

    } // boolean containsAccNo(String no)


    int getBalanceOfAccNo(String no) {
        ResultSet rs = null;

        int balance = 0;

        try {
            sqlPstmtQueryCardNo.setString(1, no);
            rs = sqlPstmtQueryCardNo.executeQuery();
            while (rs.next()) {
                if (rs.getString("number").equals(no)) {
                    balance = rs.getInt("balance");
                    break;
                }
            }
        } catch (SQLException e) {
            loggedIO.print(APP_NAME + ": an error occurred while trying to query the DB table 'card'", DO_LOG);
            loggedIO.print(APP_NAME + ": for the card no: " + no, DO_LOG);
            loggedIO.print(e.getMessage(), DO_LOG);
        } // try {} catch(){}

        return balance;

    } // int getBalanceOfAccNo(String no)


    // close existing database connection
    //
    void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            loggedIO.print(APP_NAME + ": an error occurred while trying to close the database '" + dbFileName + "':", DO_LOG);
            loggedIO.print(e.getMessage(), DO_LOG);
        } finally {
            if (DEBUG_LVL > 0) {
                loggedIO.print(APP_NAME + ": the '" + dbFileName + "' database has been closed.", DO_LOG);
            }
            dbFileName = null;
            sqlPstmtQueryCardNo = null;
            sqlPstmtQueryCardNoPin = null;
            sqlPstmtNewCard = null;
            sqlStmt = null;
            connection = null;
            dbUrl = null;
            accDB = null;
        }
    } // void close()

} // class AccDataBase
