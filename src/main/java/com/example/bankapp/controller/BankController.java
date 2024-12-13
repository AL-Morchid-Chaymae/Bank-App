package com.example.bankapp.controller;

import com.example.bankapp.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.bankapp.service.AccountService;


import org.springframework.ui.Model;
import java.math.BigDecimal;

@Controller
public class BankController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        model.addAttribute("account", account);
        return "dashboard";
    }


    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("register")
    public String registerAccount(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            accountService.registerAccount(username, password);
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam BigDecimal amount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);  // Corrected method name
        accountService.deposit(account, amount);
        return "redirect:/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam BigDecimal amount, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);  // Corrected method call

        try {
            accountService.withdraw(account, amount);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("account", account);
            return "dashboard";
        }
        return "redirect:/dashboard";
    }


    @GetMapping("/transaction")
    public String transactionHistory(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        model.addAttribute("transaction", accountService.getTransactionHistory(account));
        return "transaction";
    }

    @PostMapping("/transfer")
    public String transferAmount(@RequestParam String toUsername, @RequestParam BigDecimal amount, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account fromAccount = accountService.findAccountByUsername(username);

        try {
            // Effectuer le transfert d'argent en passant le nom d'utilisateur du destinataire
            accountService.transferAmount(fromAccount, toUsername, amount);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("fromAccountUsername", fromAccount.getUsername());
            model.addAttribute("fromAccountBalance", fromAccount.getBalance());
            return "dashboard";
        }
        return "redirect:/dashboard";
    }
}

