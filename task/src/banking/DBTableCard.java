package banking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBTableCard {

    List<TblCardEntry> table = new ArrayList<>();

    List<Integer> getIDsAsList() {
        List<Integer> listOfIDs = new ArrayList<>();

        for (TblCardEntry e : table) {
            listOfIDs.add(e.id);
        }
        return listOfIDs;
    } // List<Integer> getIDsAsList()

    Set<Integer> getIDsAsSet() {
        Set<Integer> setOfIDs = new HashSet<>();

        for (TblCardEntry e : table) {
            setOfIDs.add(e.id);
        }
        return setOfIDs;
    } // Set<Integer> getIDsAsSet()

    List<String> getNumbersAsList() {
        List<String> listOfNumbers = new ArrayList<>();

        for (TblCardEntry e : table) {
            listOfNumbers.add(e.number);
        }
        return listOfNumbers;
    } // List<Integer> getIDsAsList()

    Set<String> getNumbersAsSet() {
        Set<String> setOfNumbers = new HashSet<>();

        for (TblCardEntry e : table) {
            setOfNumbers.add(e.number);
        }
        return setOfNumbers;
    } // Set<Integer> getIDsAsSet()

    class TblCardEntry {
        int id;
        String number;
        String pin;
        int balance;
    } // class Entry
} // class DBTableCard
