package com.micro.cards.service;

import com.micro.cards.dto.CardsDto;

public interface ICardsService {

    /**
     * The below function will be used to create a new card entry in the DB using the mobile number provided
     * by the user from the Fe as a request parma
     * @param mobileNumber
     */
    void createCard(String mobileNumber);

    CardsDto fetchCardDetails(String mobileNumber);

    /**
     *
     * @param cardsDto
     * @return The end user will send the Card details in the form of the CardsDto and finally upon updating a bool value will be returned
     */
    boolean updateCardDetails(CardsDto cardsDto);

    /**
     *
     * @param mobileNumber
     * @return boolean type it will tell whether the Card with the given mobile Number was removed from the DB or not
     */
    boolean deleteCardDetails(String mobileNumber);
}
