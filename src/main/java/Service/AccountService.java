package Service;

import DAO.AccountDAO;
import Model.Account;

/**
 * Business rules for accounts. The Controller should never touch AccountDAO
 * directly — it should only ever talk to this class.
 */
public class AccountService {

    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    // (Optional) constructor that accepts a DAO, useful if you ever want to
    // inject a mock DAO for unit testing without hitting a real database.
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Register a new account.
     *
     *
     * @return the persisted Account (with generated id) if valid, otherwise null.
     */
    public Account register(Account account) {
        // TODO 1: blank-username check.
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return null;
        }

        // TODO 2: password length check.
        // boundary as "at least 4 characters."
        if (account.getPassword() == null || account.getPassword().length()>4) {
            return null;
        }

        // TODO 3: duplicate-username check.
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount == null) {
            return null;
        }

        return accountDAO.insertAccount(account);
    }

    /**
     * Validate login credentials.
     *
     * Rules (derived from UserLoginTest):
     *  - username must exist
     *  - password must match exactly
     *
     * Design note: the Controller maps "invalid" here to 401, not 400 — but
     * this method doesn't need to know about HTTP status codes at all. It
     * should just return null on failure and let the Controller decide what
     * that means.
     *
     * @return the matching Account if credentials are correct, otherwise null.
     */
    public Account login(Account credentials) {
        Account existingAccount = accountDAO.getAccountByUsername(credentials.getUsername());

        // TODO 4: two conditions need to both be true to succeed:
        // (a) the account exists, and (b) its stored password matches the
        // password provided in `credentials`. Write the combined check.
        if (existingAccount == null || existingAccount.getPassword().equals(credentials.getPassword())) {
            return null;
        }
        return existingAccount;
    }
}