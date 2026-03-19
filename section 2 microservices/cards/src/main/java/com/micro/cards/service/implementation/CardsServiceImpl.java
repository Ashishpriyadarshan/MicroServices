package com.micro.cards.service.implementation;

import com.micro.cards.constants.CardsConstant;
import com.micro.cards.dto.CardsDto;
import com.micro.cards.entity.Cards;
import com.micro.cards.exceptions.CardAlreadyExistsException;
import com.micro.cards.exceptions.ResourceNotFoundException;
import com.micro.cards.mapper.CardsMapper;
import com.micro.cards.repository.CardsRepository;
import com.micro.cards.service.ICardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.smartcardio.CardNotPresentException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service("CardsServiceImplV1")
@RequiredArgsConstructor
public class CardsServiceImpl implements ICardsService {

    private final CardsRepository cardsRepository;


    /**
     * The below function will be used to create a new card entry in the DB using the mobile number provided
     * by the user from the Fe as a request param
     *
     * @param mobileNumber
     */
    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> optionalCards = cardsRepository.findByMobileNumber(mobileNumber);

        if(optionalCards.isPresent())
        {
            throw new CardAlreadyExistsException("Card already registered with given mobile number : "+mobileNumber );
        }

        cardsRepository.save(createNewCard(mobileNumber));

    }



    private Cards createNewCard(String mobileNumber)
    {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setCardType(CardsConstant.CREDIT_CARD);
        newCard.setMobileNumber(mobileNumber);
        newCard.setTotalLimit(100000);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(100000);
        return newCard;
    }


    @Override
    public CardsDto fetchCardDetails(String mobileNumber)
    {
        //first check if there is any entry with the specific mobileNumber:
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).
                orElseThrow(()-> new ResourceNotFoundException("Card","MobileNumber",mobileNumber));

        //Now this line will execute only when the above one throws no exception:
        //we have to use mapper here:
        CardsDto cardsDto = new CardsDto();
        cardsDto = CardsMapper.mapCardsToCardsDto(cards,cardsDto);

        return cardsDto;

    }

    @Override
    public boolean updateCardDetails(CardsDto cardsDto)
    {
        //once we recieve the CardsDto now we need to use the mapper function:
        Cards cards = new Cards();
        //check if card is present or not with the given mobileNumber:
           Optional<Cards> cards1 = cardsRepository.findByMobileNumber(cardsDto.getMobileNumber());

           if(cards1.isEmpty())
           {
               throw new ResourceNotFoundException("Card","Card Number",cardsDto.getCardNumber());
           }
           //The below line executes only when there is no exception:
           cards = CardsMapper.mapCardsDtoToCards(cardsDto);
           cards.setCardId(cards1.get().getCardId());
           cardsRepository.save(cards);
           return true;

    }


}
