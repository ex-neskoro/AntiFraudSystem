package antifraud.service;

import antifraud.exception.CardAlreadyExistException;
import antifraud.exception.CardNotFoundException;
import antifraud.exception.WrongCardFormatException;
import antifraud.model.StatusDTO;
import antifraud.model.card.Card;
import antifraud.model.card.CardDTO;
import antifraud.rep.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {
    private CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card addCard(CardDTO dto) {
        String number = dto.getNumber();

        if (cardRepository.existsByNumber(number)) {
            throw new CardAlreadyExistException();
        }

        if (!checkCardFormat(number)) {
            throw new WrongCardFormatException();
        }

        return cardRepository.save(new Card(number));
    }

    public boolean checkCardFormat(Card card) {
        return checkCardFormat(card.getNumber());
    }

    public boolean checkCardFormat(String number) {
        return checkLuhn(number);
    }

    private boolean isPassingLuhn(long num) {
        int sum = 0;
        int currDigit = 0;
        for (int i = 0; i < 16; i++) {
            currDigit = (int) (num % 10);
            if (i % 2 == 1) {
                currDigit *= 2;
                if (currDigit > 9) {
                    currDigit -= 9;
                }
            }
            sum += currDigit;
            num /= 10;
        }

        return sum % 10 == 0;
    }

    private boolean checkLuhn(String number)
    {
        int nDigits = number.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--)
        {

            int d = number.charAt(i) - '0';

            if (isSecond)
                d = d * 2;

            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    public Card findCard(String number) {
        if (!checkCardFormat(number)) {
            throw new WrongCardFormatException();
        }
        return cardRepository.findByNumber(number).orElseThrow(CardNotFoundException::new);
    }

    public StatusDTO deleteCard(String number) {
        Card card = findCard(number);
        cardRepository.delete(card);
        return new StatusDTO("Card %s successfully removed!".formatted(number));
    }

    public List<Card> findAllCards() {
        return (ArrayList<Card>) cardRepository.findAll();
    }

    public boolean isExist(String number) {
        return cardRepository.existsByNumber(number);
    }
}
