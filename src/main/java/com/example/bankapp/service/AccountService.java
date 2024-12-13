package com.example.bankapp.service;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Transaction;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Méthode pour trouver un compte par username
    public Account findAccountByUsername(String username) {
        return accountRepository.findByUsername(username).orElse(null);  // Utilisation du repository pour trouver l'utilisateur
    }

    // Méthode de UserDetailsService pour charger l'utilisateur
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findAccountByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Username or Password not found");
        }

        // Retourne l'instance de Account avec les rôles (authorities) pour l'authentification
        return new org.springframework.security.core.userdetails.User(
                account.getUsername(),
                account.getPassword(),
                authorities() // Autorités (rôles)
        );
    }

    // Méthode pour récupérer les rôles de l'utilisateur (ex: "USER")
    public Collection<? extends GrantedAuthority> authorities() {
        return Arrays.asList(new SimpleGrantedAuthority("User"));
    }

    // Méthode pour enregistrer un compte
    public Account registerAccount(String username, String password) {
        if (accountRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists.");
        }
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password)); // Encodage du mot de passe
        account.setBalance(BigDecimal.ZERO); // Initialisation du solde
        return accountRepository.save(account); // Sauvegarde du compte dans la base de données
    }

    // Méthode pour effectuer un dépôt
    public void deposit(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        // Création de la transaction de dépôt
        Transaction transaction = new Transaction(amount, "Deposit", LocalDateTime.now(), account);
        transactionRepository.save(transaction);
    }

    // Méthode pour effectuer un retrait
    public void withdraw(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        // Création de la transaction de retrait
        Transaction transaction = new Transaction(amount, "Withdrawal", LocalDateTime.now(), account);
        transactionRepository.save(transaction);
    }

    // Méthode pour récupérer l'historique des transactions
    public List<Transaction> getTransactionHistory(Account account) {
        return transactionRepository.findByAccountId(account.getId());
    }

    // Méthode pour transférer de l'argent entre comptes
    public void transferAmount(Account fromAccount, String toUsername, BigDecimal amount) {
        // Vérification du solde du compte expéditeur
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Recherche du compte destinataire par le nom d'utilisateur
        Account toAccount = accountRepository.findByUsername(toUsername)
                .orElseThrow(() -> new RuntimeException("Recipient account not found"));

        // Débiter l'expéditeur
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);

        // Créer une transaction de débit pour l'expéditeur
        Transaction debitTransaction = new Transaction(amount, "Transfer Out to " + toAccount.getUsername(), LocalDateTime.now(), fromAccount);
        transactionRepository.save(debitTransaction);

        // Ajouter le montant au compte destinataire
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(toAccount);

        // Créer une transaction de crédit pour le destinataire
        Transaction creditTransaction = new Transaction(amount, "Transfer In from " + fromAccount.getUsername(), LocalDateTime.now(), toAccount);
        transactionRepository.save(creditTransaction);
    }
}
