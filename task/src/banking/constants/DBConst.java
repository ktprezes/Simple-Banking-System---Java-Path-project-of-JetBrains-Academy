package banking.constants;

public interface DBConst {

    // as of stage#3 of the project we introduce SQLite db support
    // with a file name given as a command line parameter
    // this default name will be used in case
    // the CLI parameter will be missing
    // in case we won't be able to create/open the DB file
    // using both names - CLI and the default one
    // the application will terminate
    //
    String DB_FILE_DEF_NAME = "SimpleBankingDB.s3db";
    String DB_IN_MEMORY_NAME = ":memory:";

    String DB_CONN_STRING_PREFIX = "jdbc:sqlite:";


    // as of 'stage#3' of this project the 'card' table should have the following columns:
    //        id      INTEGER
    //        number  TEXT
    //        pin     TEXT
    //        balance INTEGER DEFAULT 0
    //
    String SQL_TABLE_CARD_CREATING_STRING =
            "CREATE TABLE IF NOT EXISTS card (\n"
                    + "  id      INTEGER PRIMARY KEY\n"
                    + ", number  TEXT NOT NULL\n"
                    + ", pin     TEXT NOT NULL\n"
                    + ", balance INTEGER DEFAULT 0\n"
                    + ");";

    String SQL_TABLE_CARD_SELECT_ALL                       = "SELECT id, number, pin, balance FROM card;";

    String SQL_TABLE_CARD_QUERY_CARD_NUMBERS               = "SELECT number FROM card;";

    String SQL_TABLE_CARD_INSERT_NEW_CARD                  = "INSERT INTO card(number,pin) VALUES(?,?);";

    String SQL_TABLE_CARD_QUERY_CARD_BY_NUMBER_AND_PIN     = "SELECT * FROM card WHERE number  = ? AND pin = ?;";
    String SQL_TABLE_CARD_QUERY_CARD_BY_NUMBER             = "SELECT * FROM card WHERE number = ?;";

    String SQL_TABLE_CARD_UPDATE_CARD_NO_BALANCE           = "UPDATE card SET balance = ? WHERE number = ?;";

    String SQL_TABLE_CARD_DELETE_CARD_NO                   = "DELETE FROM card WHERE number = ?;";

} // public interface DBConst
