package com.example.Practice_Project.OnBoarding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("Banking/")
public class BankingController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/add")
    public @ResponseBody String addUser(@RequestBody User user){
        user.setAccountNo(user.getMobileNo());
        user.setAccountBalance(0);
        userRepository.save(user);

        return "User added successfully";
    }

    @RequestMapping("/all")
    public @ResponseBody Iterable<User> getAllUser(){
        return userRepository.findAll();
    }


    @PostMapping("/login")
    public @ResponseBody  String login(@RequestBody Map<String, Object> detail){
        String username = (String) detail.get("username");
        String password = (String) detail.get("password");

        User user = userRepository.findById(username).orElse(null);
        if(user == null) return "User does not exist";
        else if( ! user.getPassCode().equals(password)) {
            return "Password is wrong";
        }

        return "Successfully Login";
    }


    @PostMapping("/deposit")
    public @ResponseBody  String depositAmount(@RequestBody Map<String, Object> detail){
        String accountNo = (String) detail.get("accountNo");
        Integer money = (Integer) detail.get("money");

        if(money < 0) return "Enter Valid Amount";
        else {
            User user = userRepository.findById(accountNo).orElse(null);
            if(user == null) return "Account No does not exist";
            else{
                Integer accountBalance = user.getAccountBalance();
                accountBalance = accountBalance + money;
                user.setAccountBalance(accountBalance);
                userRepository.save(user);
            }
            Transaction transaction = new Transaction("Deposit Money","Credit",money, user.getAccountBalance(), accountNo);
            transactionRepository.save(transaction);
        }


        return "Success Deposit";
    }

    @PostMapping("/withdraw")
    public @ResponseBody  String withdrawAmount(@RequestBody Map<String, Object> detail){
        String accountNo = (String) detail.get("accountNo");
        Integer money = (Integer) detail.get("money");

        if(money < 0) return "Enter Valid Amount";
        else {
            User user = userRepository.findById(accountNo).orElse(null);
            if(user == null) return "Account No does not exist";
            else{
                Integer accountBalance = user.getAccountBalance();
                if(accountBalance < money) return "Insufficient Account Balance";
                else {
                    accountBalance = accountBalance - money;
                    user.setAccountBalance(accountBalance);
                    userRepository.save(user);
                }
                Transaction transaction = new Transaction("Withdraw Money","Debit",money, user.getAccountBalance(), accountNo);
                transactionRepository.save(transaction);
            }
        }

        return "Success Withdraw";
    }

    @PostMapping("/updatePassCode")
    public @ResponseBody  String updatePassCode(@RequestBody Map<String, Object> detail){
        String accountNo = (String) detail.get("accountNo");
        String oldPassCode = (String) detail.get("oldPassCode");
        String newPassCode = (String) detail.get("newPassCode");

        User user = userRepository.findById(accountNo).orElse(null);
        if(user == null) return "Account No does not exist";
        else{
            if( !oldPassCode.equals(user.getPassCode())) return "Current pass code is wrong";
            else{
                user.setPassCode(newPassCode);
                userRepository.save(user);
            }
        }

        return "Pass Code updated Successfully";
    }


    @PostMapping("/transferAmount")
    public @ResponseBody  String transferAmount(@RequestBody Map<String, Object> detail){
        String toAccountNo = (String) detail.get("toAccountNo");
        String fromAccountNo = (String) detail.get("fromAccountNo");
        Integer money = (Integer) detail.get("money");

        if(money < 0) return "Enter Valid Amount";
        else {
            User toUser = userRepository.findById(toAccountNo).orElse(null);
            User fromUser = userRepository.findById(fromAccountNo).orElse(null);

            if(toUser == null) return "Sender Account No does not exist";
            else if(fromUser == null) return "Reciept Account No does not exist";
            else{

                Integer toAccountBalance = toUser.getAccountBalance();

                if(toAccountBalance < money) return "Insufficient Account Balance in Sender Account";
                else {
                    toAccountBalance -= money;
                    toUser.setAccountBalance(toAccountBalance);
                    fromUser.setAccountBalance(fromUser.getAccountBalance() + money);

                    Transaction toTransaction = new Transaction("Transfer Money to " + fromAccountNo,"Debit",money, toUser.getAccountBalance(), toAccountNo);
                    transactionRepository.save(toTransaction);

                    Transaction fromTransaction = new Transaction("Get Money From " + toAccountNo,"Credit",money, fromUser.getAccountBalance(), fromAccountNo);
                    transactionRepository.save(fromTransaction);

                    userRepository.save(toUser);
                    userRepository.save(fromUser);
                }
            }
        }

        return "Amount successfully transferred";
    }

    @RequestMapping("/get/{id}")
    public @ResponseBody User getUser(@PathVariable("id") String mobileNo){
        return userRepository.findById(mobileNo).orElse(null);

    }

    @RequestMapping(value = "/get/{id}" , method = RequestMethod.PUT)
    public @ResponseBody String updateUser(@RequestBody User user, @PathVariable("id") String mobileNo){
        userRepository.save(user);
        return "Update Successfully";
    }

    @RequestMapping(value = "/get/{id}" , method = RequestMethod.DELETE)
    public @ResponseBody String deleteUser(@PathVariable("id") String mobileNo){
        userRepository.deleteById(mobileNo);
        return "Delete Successfully";
    }

    @PostMapping("/checkBalance")
    public @ResponseBody String checkBalance(@RequestBody Map<String, Object> detail){
        String accountNo = (String) detail.get("accountNo");

        User user = userRepository.findById(accountNo).orElse(null);
        if(user == null) return "Account No does not exist";

        return "Account Balance : " + String.valueOf(user.getAccountBalance());
    }

    @RequestMapping(value = "/get_2/{id}" , method = RequestMethod.PUT)
    public @ResponseBody String updateUser2(@RequestBody Map<String, Object> detail, @PathVariable("id") String mobileNo){
        String name = (String) detail.get("name");

        User user = userRepository.findById(mobileNo).orElse(null);
        if(user == null) return "Account No does not exist";
        user.setName(name);
        userRepository.save(user);
        return "Update Successfully";
    }

    @PostMapping("/miniStatement")
    public @ResponseBody List<Transaction> miniStatement(@RequestBody Map<String, Object> detail){
         String mobileNo  = (String) detail.get("mobileNo");

         User user = userRepository.findById(mobileNo).orElse(null);
         if(user == null) return null;

         List<Transaction> transactions = new ArrayList<>();

         transactionRepository.findByUserMobileNo(mobileNo).forEach(transactions::add);

         return transactions.subList(Math.max(transactions.size()-10,0),transactions.size());
    }
}
