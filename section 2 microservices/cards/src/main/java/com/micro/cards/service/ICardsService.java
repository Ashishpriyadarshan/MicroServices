package com.micro.cards.service;

public interface ICardsService {

    /**
     * The below function will be used to create a new card entry in the DB using the mobile number provided
     * by the user from the Fe as a request parma
     * @param mobileNumber
     */
    void createCard(String mobileNumber);
}
