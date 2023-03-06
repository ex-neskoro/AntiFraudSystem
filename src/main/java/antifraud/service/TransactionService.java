package antifraud.service;

import antifraud.exception.*;
import antifraud.model.transaction.*;
import antifraud.rep.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardService cardService;
    private final IPService ipService;
    private long allowedLimit;
    private long manualLimit;

    private final String LIMITS_PATH = "./src/main/resources/limits.txt";

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CardService cardService, IPService ipService) {
        this.transactionRepository = transactionRepository;
        this.cardService = cardService;
        this.ipService = ipService;
        loadLimits();
    }

    private void loadLimits() {
        try {
            Scanner sc = new Scanner(new File(LIMITS_PATH));
            allowedLimit = sc.nextLong();
            manualLimit = sc.nextLong();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveLimits() {
        try (FileWriter limits = new FileWriter(LIMITS_PATH)) {
            limits.write(String.valueOf(allowedLimit));
            limits.append('\n');
            limits.append(String.valueOf(manualLimit));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TransactionStatusDTO processTransaction(TransactionDTO dto) {
        TransactionStatusDTO statusDTO = new TransactionStatusDTO();

        checkAmount(dto.getAmount(), statusDTO);
        checkCard(dto.getNumber(), statusDTO);
        checkIP(dto.getIp(), statusDTO);
        checkIPCorrelation(statusDTO, dto);
        checkRegionCorrelation(statusDTO, dto);

        Transaction transaction = new Transaction(dto);
        transaction.setStatus(statusDTO.getResult());

        statusDTO.cleanInfo();
        transactionRepository.save(transaction);

        return statusDTO;
    }

    public Transaction processTransactionFeedback(Feedback feedback) {
        Transaction transaction = transactionRepository.findById(feedback.getTransactionId())
                .orElseThrow(TransactionNotFoundException::new);

        if (transaction.getFeedback() != null) {
            throw new TransactionAlreadyHaveFeedbackException();
        }

        transaction.setFeedback(feedback.getStatus());

        TransactionStatus transactionStatus = transaction.getStatus();
        TransactionStatus feedbackStatus = feedback.getStatus();
        long transactionAmount = transaction.getAmount();

        switch (feedbackStatus) {
            case ALLOWED -> {
                switch (transactionStatus) {
                    case ALLOWED -> throw new SameFeedbackStatusException();
                    case MANUAL_PROCESSING -> increaseAllowedLimit(transactionAmount);
                    case PROHIBITED -> {
                        increaseAllowedLimit(transactionAmount);
                        increaseManualLimit(transactionAmount);
                    }
                }
            }
            case MANUAL_PROCESSING -> {
                switch (transactionStatus) {
                    case ALLOWED -> decreaseAllowedLimit(transactionAmount);
                    case MANUAL_PROCESSING -> throw new SameFeedbackStatusException();
                    case PROHIBITED -> {
                        increaseManualLimit(transactionAmount);
                    }
                }
            }
            case PROHIBITED -> {
                switch (transactionStatus) {
                    case ALLOWED -> {
                        decreaseAllowedLimit(transactionAmount);
                        decreaseManualLimit(transactionAmount);
                    }
                    case MANUAL_PROCESSING -> decreaseManualLimit(transactionAmount);
                    case PROHIBITED -> throw new SameFeedbackStatusException();
                }
            }
        }

        return transactionRepository.save(transaction);
    }

    private void checkRegionCorrelation(TransactionStatusDTO statusDTO, TransactionDTO dto) {
        int regionsFromOneHourCount = transactionRepository.countRegionsFromLastHour(dto.getDateTime().minusHours(1),
                dto.getDateTime(),
                dto.getCountryCode());

        if (regionsFromOneHourCount > 2) {
            statusDTO.setResult(TransactionStatus.PROHIBITED);
            statusDTO.addToInfo("region-correlation");
        } else if (regionsFromOneHourCount == 2) {
            statusDTO.setResult(TransactionStatus.MANUAL_PROCESSING);
            statusDTO.addToInfo("region-correlation");
        }
    }

    private void checkIPCorrelation(TransactionStatusDTO statusDTO, TransactionDTO dto) {
        int ipsFromOneHourCount = transactionRepository.countIPsFromLastHourByNotIp(dto.getDateTime().minusHours(1),
                dto.getDateTime(),
                dto.getIp());

        if (ipsFromOneHourCount > 2) {
            statusDTO.setResult(TransactionStatus.PROHIBITED);
            statusDTO.addToInfo("ip-correlation");
        } else if (ipsFromOneHourCount == 2) {
            statusDTO.setResult(TransactionStatus.MANUAL_PROCESSING);
            statusDTO.addToInfo("ip-correlation");
        }
    }

    private void checkIP(String ip, TransactionStatusDTO dto) {
        if (!ipService.checkIPFormat(ip)) {
            throw new WrongIPFormatException();
        }

        boolean ipExist = ipService.isExist(ip);

        if (ipExist && dto.getResult() != TransactionStatus.MANUAL_PROCESSING) {
            dto.setResult(TransactionStatus.PROHIBITED);
            dto.addToInfo("ip");
        } else if (ipExist) {
            dto.setResult(TransactionStatus.PROHIBITED);
            dto.setInfo("ip, ");
        }
    }

    private void checkCard(String number, TransactionStatusDTO dto) {
        if (!cardService.checkCardFormat(number)) {
            throw new WrongCardFormatException();
        }

        boolean cardExist = cardService.isExist(number);

        if (cardExist && dto.getResult() != TransactionStatus.MANUAL_PROCESSING) {
            dto.setResult(TransactionStatus.PROHIBITED);
            dto.addToInfo("card-number");
        } else if (cardExist) {
            dto.setResult(TransactionStatus.PROHIBITED);
            dto.setInfo("card-number, ");
        }
    }

    private void checkAmount(long amount, TransactionStatusDTO dto) {
        if (amount <= allowedLimit) {
            dto.setResult(TransactionStatus.ALLOWED);
        } else if (amount <= manualLimit) {
            dto.setResult(TransactionStatus.MANUAL_PROCESSING);
            dto.addToInfo("amount");
        } else {
            dto.setResult(TransactionStatus.PROHIBITED);
            dto.addToInfo("amount");
        }
    }

    public Transaction saveTran(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    private long increaseLimit(long limit, long transactionAmount) {
        return (long) Math.ceil(0.8 * limit + 0.2 * transactionAmount);
    }

    private long decreaseLimit(long limit, long transactionAmount) {
        return (long) Math.ceil(0.8 * limit - 0.2 * transactionAmount);
    }

    private void increaseAllowedLimit(long transactionAmount) {
        allowedLimit = increaseLimit(allowedLimit, transactionAmount);
        saveLimits();
    }

    private void decreaseAllowedLimit(long transactionAmount) {
        allowedLimit = decreaseLimit(allowedLimit, transactionAmount);
        saveLimits();
    }

    private void increaseManualLimit(long transactionAmount) {
        manualLimit = increaseLimit(manualLimit, transactionAmount);
        saveLimits();
    }

    private void decreaseManualLimit(long transactionAmount) {
        manualLimit = decreaseLimit(manualLimit, transactionAmount);
        saveLimits();
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findByOrderByIdAsc();
    }

    public List<Transaction> findAllByCardNumber(String number) {
        if (!cardService.checkCardFormat(number)) {
            throw new WrongCardFormatException();
        }

        List<Transaction> list = transactionRepository.findByNumberOrderByIdAsc(number);

        if (list.isEmpty()) {
            throw new CardNotFoundException();
        }

        return list;
    }
}
