package com.company;

import java.sql.*;
import java.util.*;

class Database {
    // This class handles all query's sent to the database and returns data in the requested type

    public final ResourceBundle rd = ResourceBundle.getBundle("com/company/dbconfig");
    private final String url = rd.getString("url");
    private final String username = rd.getString("username");
    private final String password = rd.getString("password");
    public Connection connection = DriverManager.getConnection(url, username, password);
    // Stored procedure
    public Statement statement = connection.createStatement();
    ResultSet results;
    // Creating a stored procedure
    // lists all the userid in the user table

    Database() throws SQLException {
    }

    // Returns the Results from a query.
    public ResultSet runQuery(String _query) {
        try {
            results = statement.executeQuery(_query);
            results.next();
        } catch (Exception e) {
            System.out.println("ERROR: " + _query);
            results = null;
        }
        return results;
    }

    // Appends query like strings for the runQuery
    public ResultSet query(String _table, String _row, String _where, int _value) {
        return runQuery("SELECT " + _row + " FROM " + _table + " WHERE " + _where + " = " + _value + ";");
    }

    public ResultSet query(String _table, String _row) {
        return runQuery("SELECT " + _row + " FROM " + _table + " WHERE USERID IS NOT NULL;");
    }

    // These grab the first element from the query() and returns that type.
    public double queryDouble(String _table, String _row, String _value, int _id) throws SQLException {
        return query(_table, _row, _value, _id).getDouble(1);
    }

    public boolean queryBoolean(String _table, String _row, String _value, int _id) throws SQLException {
        return queryInt(_table, _row, _value, _id) == 1;
    }

    public int queryInt(String _table, String _row, String _value, int _id) throws SQLException {
        return query(_table, _row, _value, _id).getInt(1);
    }

    public int queryInt(String _table, String _row, int _accountid, int _userid) throws SQLException {
        return runQuery("SELECT " + _row + " FROM " + _table + " WHERE ACCOUNTID = " + _accountid + " AND USERID = " + _userid + ";").getInt(1);
    }


    public String queryString(String _table, String _row, String _value, int _id) throws SQLException {
        return query(_table, _row, _value, _id).getString(1);
    }

    public int queryCount(String _table) throws SQLException {
        ResultSet _results = query(_table, "COUNT(*)");
        return _results.getInt(1);
    }


    // Inserts value(s) into the specified class
    public boolean insertInto(String _table, String _values) {
        boolean flag;
        String sql = "INSERT INTO " + _table + " VALUES (" + _values + ");";
        try {
            statement.execute(sql);
            flag = true;
            // the statement compiled successfully
        } catch (Exception e) {
            System.out.println("InsertIntoFail: " + sql);
            flag = false;
        }
        return flag;
    }

    public boolean createUser(int userID, String password, String firstname, String middlename, String lastname, String email, int defaultacct) {
        String _query = userID + ",\"" + password + "\",\"" + firstname + "\",\"" + middlename + "\",\"" + lastname + "\",\"" + email + "\"," + defaultacct;
        return insertInto("USER", _query);
    }

    public void createAccount(int _accountid, int _userid, double _balance, String _accounttype) {
        String _query = _accountid + ",\"" + _userid + "\",\"" + 1 + "\",\"" + _balance + "\",\"" + _accounttype + "\"";
        insertInto("ACCOUNT", _query);
    }


    // Gives a new value to a single row with inside a table with the matching id.
    // Using overloaded methods for different update syntax
    public void update(String _table, String _column, int _value, String _where, int _id) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "UPDATE " + _table + " SET " + _column + " = " + _value + " WHERE " + _where + " like " + _id + ";";

        try {
            statement.execute(sql);
        } catch (Exception e) {
            System.out.println("ERROR: " + sql);
        }
    }

    public void update(String _table, String _column, int _value, String _where, int _match, int _userid) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "UPDATE " + _table + " SET " + _column + " = " + _value + " WHERE " + _where + " like " + _match + " AND USERID = " + _userid + ";";

        try {
            statement.execute(sql);
        } catch (Exception e) {
            System.out.println("ERROR: " + sql);
        }
    }

    public void update(String _table, String _column, double _value, String _where, int _id) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "UPDATE " + _table + " SET " + _column + " = " + _value + " WHERE " + _where + " like " + _id + ";";

        try {
            statement.execute(sql);
        } catch (Exception e) {
            System.out.println("ERROR: " + sql);
        }
    }

    public void update(String _table, String _column, String _value, String _where, int _id) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "UPDATE " + _table + " SET " + _column + " = " + _value + " WHERE " + _where + " like " + _id + ";";

        try {
            statement.execute(sql);
        } catch (Exception e) {
            System.out.println("ERROR: " + sql);
        }
    }
}

class Input {
    // The input class gets inputs from the scanner and validates them into what was requested then returns it.
    Scanner scanner = new Scanner(System.in);

    double getdouble() {
        double temp = 0;
        boolean flag;
        do {
            try {
                temp = scanner.nextDouble();
                flag = false;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Please enter a valid number.\n");
                flag = true;
                // clear the scanner
                scanner.next();
            }
        } while (flag);
        return Math.abs(temp);
    }

    int getint() {
        int temp = 0;
        boolean flag;
        do {
            try {
                temp = scanner.nextInt();
                flag = false;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Please enter a valid number.\n");
                flag = true;
                // clear the scanner
                scanner.next();
            }
        } while (flag);
        return Math.abs(temp);
    }

    String getstring() {
        String temp = "";
        boolean flag = true;
        while (flag) {
            try {
                temp = scanner.next();
                flag = false;
            } catch (Exception e) {
                System.out.println("Please enter a valid sentence.\n");
            }
        }
        return temp;
    }

    String getemail() {
        // A special getstring variant that has validation for emails
        String temp = "";
        boolean flag = true;
        while (flag) {
            try {
                temp = scanner.next();
                if (temp.contains("@") && (temp.contains(".com") || temp.contains(".net"))) {
                    flag = false;
                } else {
                    System.out.println("Please enter a valid email.");
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid sentence.");
            }
        }
        return temp;
    }
}

class UI {
    // the UI class will handle all navigation through the program and program flow
    // because we aren't using an ui, I'm using these methods as navigation through the program.

    public Input input = new Input();
    public Database database = new Database();

    public UI() throws SQLException {
        System.out.println("""
                1 - Apply for a customer account.
                2 - Login
                3 - Exit.""");
        int temp = input.getint();
        switch (temp) {
            case 1 -> createUser();
            case 2 -> login();
            case 3 -> System.exit(0);
            case 4 -> Accounts();
            case 5 -> userMenu(2);
        }

    }

    void userMenu(int _userid) throws SQLException {

        // auto checks for incoming money transfers on login
        // * As a customer, I can accept a money transfer from another account.
        searchTransaction(_userid);

        // list the accounts tied to this user
        listUserAccounts(_userid);

        System.out.println("""

                What would you like to do?
                1 - Deposit into account.
                2 - Withdraw from account.
                3 - Transfer to account.
                4 - Create account.
                5 - Close.
                """);

        switch (input.getint()) {
            case 1:
                deposit("deposit", _userid);
                break;
            case 2:
                withdraw("withdraw", _userid);
                break;
            case 3:
                transfer(_userid);
                break;
            case 4:
                createAccount(_userid);
                break;
            case 5:
                System.exit(0);
                break;
            default:
                userMenu(_userid);
                break;
        }
    }

    void deposit(String _term, int _userid) throws SQLException {
        listUserAccounts(_userid);
        System.out.println("Which account would you like to " + _term + "?");
        int _accountid = input.getint();
        viewAccount(_accountid);
        System.out.println("How much would you like to " + _term + "?");
        // _amount changes the method from a deposit to a withdrawal
        double _value = input.getdouble();

        System.out.println("Money " + _term);
        _value = _value + database.queryDouble("account", "balance", "accountid", _accountid);
        _value = Math.abs(_value);
        database.update("account", "balance", _value, "accountid", _accountid);
        userMenu(_userid);
    }

    void withdraw(String _term, int _userid) throws SQLException {
        listUserAccounts(_userid);
        System.out.println("Which account would you like to " + _term + "?");
        int _accountid = input.getint();
        boolean flag = true;
        viewAccount(_accountid);
        System.out.println("How much would you like to " + _term + "?");
        // _amount changes the method from a deposit to a withdrawal
        double _value = input.getdouble();

        if (_value > database.queryDouble("account", "balance", "accountid", _accountid)) {
            System.out.println("Please enter a valid amount that is smaller than your current balance.\n");
            userMenu((_userid));
        } else if (_value < 0) {
            System.out.println("Please enter a valid amount over zero.\n");
            userMenu(_userid);
        } else {
            System.out.println("Money " + _term);
            _value = database.queryDouble("account", "balance", "accountid", _accountid) - _value;
            _value = Math.abs(_value);
            database.update("account", "balance", _value, "accountid", _accountid);
        }
        userMenu(_userid);
    }

    void withdraw(int _userid, int _accountid, double _value) throws SQLException {
        _value = database.queryDouble("account", "balance", "accountid", _accountid) - _value;
        _value = Math.abs(_value);
        database.update("account", "balance", _value, "accountid", _accountid);

    }

    void deposit(int _userid, int _accountid, double _value) throws SQLException {
        _value = _value + database.queryDouble("account", "balance", "accountid", _accountid);
        _value = Math.abs(_value);
        database.update("account", "balance", _value, "accountid", _accountid);
    }

    void createAccount(int _userid) throws SQLException {
        int _accountid = database.queryCount("account") + 1;
        System.out.println("What is the starting amount?");
        double _balance = input.getdouble();

        String _accounttype = null;
        boolean flag;
        do {
            System.out.println("What type of account would you like?");
            System.out.println("1 - Checking.\n" +
                    "2 - Savings.");
            int _input = input.getint();
            switch (_input) {
                case 1 -> {
                    _accounttype = "Checking";
                    flag = false;
                }
                case 2 -> {
                    _accounttype = "Savings";
                    flag = false;
                }
                default -> {
                    {
                        flag = true;
                    }
                    System.out.println("Please enter either a 1 or 2");
                }
            }
        }
        while (flag);
        System.out.println(_userid);
        database.createAccount(_accountid, _userid, _balance, _accounttype);
        System.out.println("Account created.");
        userMenu(_userid);
    }

    void transfer(int _userid) throws SQLException {
        listUsers();
        System.out.println("Which account to transfer to?");
        listUserAccounts(_userid);
        int _targetacct = input.getint();
        // we need to create a input.getaccount which takes a user parameter and verifys that the account we are about to use is under that user's control
        listUserAccounts(_userid);
        System.out.println("which account to transfer from");
        int _from = input.getint();

        System.out.println("How much to transfer?");
        double _amount = input.getdouble();

        if (_amount > database.queryDouble("account", "balance", "accountid", _from)) {
            System.out.println("Please enter a valid amount that is lower than your current balance.\n");
            userMenu((_userid));
        } else if (_amount < 0) {
            System.out.println("Please enter a valid amount over zero.\n");
            userMenu(_userid);
        } else {
            withdraw(_userid, _from, _amount);
            deposit(_userid, _targetacct, _amount);
            database.insertInto("logs", (_userid + 1) + ",\" Transferred money from " + _from + ", to " + _targetacct + ".\"");
            userMenu(_userid);
        }
    }

    void searchTransaction(int _userid) throws SQLException {


        // ____________________________________

        // make it so that the user table has a field defining the default account so that we can send money back
        // or recieve it

        // _________________________]
        for (int x = 1; x <= database.queryCount("user"); x++) {
            // We need to get the Result object and parse through it
            ResultSet _query = database.runQuery("SELECT * FROM TRANSACTION WHERE targetacct = " + _userid);
            try {
                _query.next();
                int _accountid = _query.getInt(2);
                int _from = _query.getInt(1);
                double _amount = _query.getDouble(3);
                System.out.println(_from + " is sending you " + _amount + "$");
                System.out.println("Do you accept this amount?" +
                        "1 - Yes" +
                        "2 - No");
                switch (input.getint()) {
                    case 1:
                        double _value = _amount + database.queryInt("account", "balance", "_accountid", _accountid);

                        database.update("account", "balance", _value, "_accountid", _accountid);
                        database.insertInto("logs", _userid + ",\" Received " + _amount + "amount from " + _from + ".\",");
                        userMenu(_userid);
                        database.runQuery("delete * from account where userid = " + _userid + " and accountid = " + _accountid);
                        break;
                    case 2:
                        database.update("transaction", "approved", 0, "userid", _from, _userid);
                        break;
                }

            } catch (Exception ignored) {
            }

        }
    }

    void listUsers() throws SQLException {
        System.out.println("Current users.");
        for (int _userid = 1; _userid <= database.queryCount("user"); _userid++) {
            System.out.println("User " + _userid);
        }
    }

    void createUser() throws SQLException {

        // This creates a user, the database RN is already setup to handle having a parent-child relationship between the user and account tables. This would let a single person have multiple checking and savings accounts. But the current requirements only need a 1:1, one user one account;
        // current number of users + 1, userID is a primary key, must be unique; tried to get the auto_increment to work, need to recreate the table;
        int _userID = database.queryCount("user") + 1;
        System.out.println("Please enter your new password.");
        String _password = input.getstring();
        System.out.println("Enter your first name.");
        String _firstname = input.getstring();
        System.out.println("Enter your middle name.");
        String _middlename = input.getstring();
        System.out.println("Enter your last name.");
        String _lastname = input.getstring();
        System.out.println("What is your email?");
        String _email = input.getemail();
        if (database.createUser(_userID, _password, _firstname, _middlename, _lastname, _email, 0)) {
            System.out.println("Congratulations on your new account!");
            //database.insertInto("logs", _userID + ",\"Created account for user " + _userID + ".\"");
        } else {
            System.out.println("System error.");
            System.exit(0);
        }
        createAccount(_userID);
    }

    void login() throws SQLException {

        // Get the user's input for the login.
        System.out.println("Please enter your userID: ");
        int _userid = input.getint();
        System.out.println("Please enter your password:");
        String pass = input.getstring();

        // Check the user's password attempt against the database's password for that user.
        if (Objects.equals(pass, database.queryString("user", "password", "userid", _userid))) {
            if (_userid == 1) {
                System.out.println("\n\nWelcome Admin.\n");
                adminMenu();
            } else {
                userMenu(_userid);
            }
        } else {
            System.out.println("Please try the password again");
        }
    }

    void adminMenu() throws SQLException {
        System.out.println("""
                1 - check accounts.
                2 - view logs.
                3 - Exit.
                """);
        int x = input.getint();
        boolean flag = true;
        switch (x) {
            case 1:
                Accounts();
                break;
            case 2: // output all logs
                listAll("logs");
                adminMenu();
                break;
            case 0:
                System.exit(0);
                break;
            default:
                break;
        }

    }

    void listUserAccounts(int _userid) throws SQLException {
        System.out.println("Current Accounts");
        for (int _accountid = 1; _accountid <= database.queryCount("account"); _accountid++) {
            int _tempuserid = 0;
            try {

                // GET THE USER ID FOR THE _ACCOUNTID (FIRST ACCOUNT)
                _tempuserid = database.queryInt("account", "userid", "accountid", _accountid);


            } catch (Exception ignored) {
            }
            if (_userid == _tempuserid) {
                viewAccount(_accountid);
            }
        }
    }

    void viewAccount(int _accountid) throws SQLException {
        String _status;
        if (database.queryBoolean("account", "enabled", "accountid", _accountid)) {
            _status = "open. ";
        } else {
            _status = "closed. ";
        }

        int _userid = database.queryInt("account", "userid", "accountid", _accountid);
        String _account = database.queryString("account", "account", "accountid", _accountid);
        double _balance = database.queryDouble("account", "balance", "accountid", _accountid);

        System.out.println("AccountID " + _accountid + " owned by userID " + _userid + " is " + _status + "The " + _account + " balance is " + _balance + "$.");
    }

    void viewLogs(int _id) throws SQLException {
        String _info = database.queryString("logs", "actioninfo", "userid", _id);
        System.out.println("Log " + _id + ". " + _info);
    }

    void listAll(String _table) throws SQLException {
        System.out.println("Current Accounts");
        for (int _counter = 1; _counter <= database.queryCount(_table); _counter++) {
            _table = _table.toLowerCase();
            switch (_table) {
                case "account":
                    viewAccount(_counter);
                    break;
                case "user":
                    break;
                case "logs":
                    viewLogs(_counter);
                    break;
                case "transactions":
                    break;
            }
        }
    }

    void Accounts() throws SQLException {
        // reject, approve, and view accounts
        listAll("account");
        System.out.println("\nWhich account would you like to manage? Enter the account ID, or go back with 0.");
        int _id = input.getint();
        if (_id == 0) {
            adminMenu();
        } else if (_id < 0) {
            Accounts();
        } else if (_id > database.queryCount("account")) {
            Accounts();
        }
        boolean _foo = database.queryBoolean("account", "enabled", "userid", _id);
        while (true) {
            String text;
            if (!_foo) {
                text = "active. ";
            } else {
                text = "offline. ";
            }
            System.out.println("" +
                    "1 - List Accounts.\n" +
                    "2 - Make account " + text + "\n" +
                    "3 - Back.");
            switch (input.getint()) {
                case 1 -> listAll("account");
                case 2 -> {
                    String _data;
                    if (_foo) {
                        _data = "0";
                        _foo = false;
                    } else {
                        _data = "1";
                        _foo = true;
                    }
                    database.update("account", "enabled", _data, "userid", _id);
                }
                case 3 -> Accounts();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws SQLException {
        UI ui = new UI();

    }
}
