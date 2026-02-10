package com.micro.accounts.constants;

public class AccountsConstants {

    private AccountsConstants()
    {
        //we have kept the constructor private as we don't want any outside class to create an object or instance of this class
        //as it contains only constants that's why we don't even have any getter or setters.
    }

    public static final String SAVINGS="Savings";
    public static final String ADDRESS="123 Main Street , New York";
    public static final String STATUS_201="201";
    public static final String MESSAGE_201="Account created successfully";
    public static final String STATUS_200="200";
    public static final String MESSAGE_200="Request processed successfully";
    public static final String STATUS_500="500";
    public static final String MESSAGE_500="An error occured. Please try again or contact the dev team";
}
