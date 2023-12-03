package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account registerAccount(Account account)
    {
        // Username must not be blank and password length must be <= 4
        if (account.username == "" || account.password.length() < 4)
        {
            return null;
        }
        
        return this.accountDAO.registerAccount(account);
    }

    public Account login(Account account)
    {
        return this.accountDAO.login(account);
    }
}
