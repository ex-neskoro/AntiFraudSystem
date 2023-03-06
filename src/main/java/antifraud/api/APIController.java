package antifraud.api;

import antifraud.model.StatusDTO;
import antifraud.model.card.Card;
import antifraud.model.card.CardDTO;
import antifraud.model.ip.IP;
import antifraud.model.transaction.Feedback;
import antifraud.model.transaction.Transaction;
import antifraud.model.transaction.TransactionStatusDTO;
import antifraud.model.user.UserStatusDTO;
import antifraud.model.transaction.TransactionDTO;
import antifraud.model.user.User;
import antifraud.model.user.UserLockChangeDTO;
import antifraud.model.user.UserRoleChangeDTO;
import antifraud.service.CardService;
import antifraud.service.IPService;
import antifraud.service.TransactionService;
import antifraud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class APIController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final IPService ipService;
    private final CardService cardService;

    @Autowired
    public APIController(TransactionService transactionService, UserService userService, IPService ipService, CardService cardService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.ipService = ipService;
        this.cardService = cardService;
    }

    @PostMapping("/api/antifraud/transaction")
    public TransactionStatusDTO processTran(@Valid @RequestBody TransactionDTO transaction) {
        return transactionService.processTransaction(transaction);
    }

    @PutMapping("/api/antifraud/transaction")
    public Transaction processFeedback(@Valid @RequestBody Feedback feedback) {
        return transactionService.processTransactionFeedback(feedback);
    }

    @GetMapping("/api/antifraud/history")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/api/antifraud/history/{number}")
    public List<Transaction> getAllTransactionsByCardNumber(@PathVariable String number) {
        return transactionService.findAllByCardNumber(number);
    }

//    @PostMapping("/api/saveTran")
//    public Transaction saveTran(@RequestBody Transaction transaction) {
//        return transactionService.saveTran(transaction);
//    }

    @PostMapping("/api/auth/user")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        user = userService.registerUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/api/auth/list")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/api/auth/user/{username}")
    public UserStatusDTO deleteUser(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        return userService.deleteUser(user);
    }

    @PutMapping("/api/auth/role")
    public User setUserRole(@RequestBody UserRoleChangeDTO dto) {
        return userService.changeUserRole(dto);
    }

    @PutMapping("/api/auth/access")
    public StatusDTO changeUserLockStatus(@RequestBody UserLockChangeDTO dto) {
        return userService.changeUserStatus(dto);
    }

    @PostMapping("/api/antifraud/suspicious-ip")
    public IP addSuspisiousIP(@Valid @RequestBody IP ip) {
        return ipService.addIP(ip);
    }

    @DeleteMapping("/api/antifraud/suspicious-ip/{ip}")
    public StatusDTO deleteIP(@PathVariable String ip) {
        return ipService.deleteIP(ip);
    }

    @GetMapping("/api/antifraud/suspicious-ip")
    public List<IP> getAllIPs() {
        return ipService.getAllIPs();
    }

    @PostMapping("/api/antifraud/stolencard")
    public Card addStolenCard(@RequestBody CardDTO dto) {
        return cardService.addCard(dto);
    }

    @DeleteMapping("/api/antifraud/stolencard/{number}")
    public StatusDTO deleteCard(@PathVariable String number) {
        return cardService.deleteCard(number);
    }

    @GetMapping("/api/antifraud/stolencard")
    public List<Card> getAllStolenCards() {
        return cardService.findAllCards();
    }
}
