package com.micro.cards.mapper;

import com.micro.cards.dto.CardsDto;
import com.micro.cards.entity.Cards;


public class CardsMapper {

    public static Cards mapCardsDtoToCards(CardsDto cardsDto)
    {
        Cards cards = new Cards();
        cards.setTotalLimit(cardsDto.getTotalLimit());
        cards.setAvailableAmount(cardsDto.getAvailableAmount());
        cards.setAmountUsed(cardsDto.getAmountUsed());
        cards.setCardNumber(cardsDto.getCardNumber());
        cards.setCardType(cardsDto.getCardType());
        cards.setMobileNumber(cardsDto.getMobileNumber());
        return cards;
    }

    public static CardsDto mapCardsToCardsDto(Cards cards,CardsDto cardsDto)
    {

        cardsDto.setCardType(cards.getCardType());
        cardsDto.setCardNumber(cards.getCardNumber());
        cardsDto.setMobileNumber(cards.getMobileNumber());
        cardsDto.setAmountUsed(cards.getAmountUsed());
        cardsDto.setAvailableAmount(cards.getAvailableAmount());
        cardsDto.setTotalLimit(cards.getTotalLimit());
        return cardsDto;
    }
}
