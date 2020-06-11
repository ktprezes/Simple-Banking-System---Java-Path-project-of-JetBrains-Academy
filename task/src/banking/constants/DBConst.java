package banking;

interface DbConst {

    // as of stage#3 of the project we introduce SQLite db support
    // with a file name given as a command line parameter
    // this default name will be used in case
    // the CLI parameter will be missing
    // in case we won't be able to create/open the DB file
    // using both names - CLI and the default one
    // the application will terminate
    //
    String DB_FILE_DEF_NAME = "SimpleBankingDB.s3db";

    String DB_CONN_STRING_PREFIX = "jdbc:sqlite:";

}
