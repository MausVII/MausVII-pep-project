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
        return this.accountDAO.registerAccount(account);
    }

    public Account login(Account account)
    {
        return this.accountDAO.login(account);
    }
}
