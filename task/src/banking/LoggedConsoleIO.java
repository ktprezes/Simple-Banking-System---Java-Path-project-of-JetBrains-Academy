package banking;

import java.util.ArrayList;


// we do need _only_one_ log for our app
// so let's utilize the 'singleton' design pattern

class Log {
    private static Log logSingleton = null;
    private static ArrayList<String> actualLog = null;

    private Log(){
        actualLog = new ArrayList<>();
    }

    static Log getInstance() {
        if(logSingleton!= null && actualLog != null) {
            return logSingleton;
        }

        return logSingleton = new Log();
    } // static Log getInstance()
} // class Log
