package com.micro.message.functions;

import com.micro.message.dto.AccountsMessageDto;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class MessageFunctions {

    public static final Logger logger = LoggerFactory.getLogger(MessageFunctions.class);


    @Bean
    public Function<AccountsMessageDto,AccountsMessageDto> email()
    {
        return (accountsMessageDto) ->
        {
            logger.info("Sending Email with the details :" + accountsMessageDto.toString());
            return accountsMessageDto;
        };
    }

    @Bean
    public Function<AccountsMessageDto,Long> sms()
    {
        return (accountsMessageDto) ->
        {
            logger.info("Sending sms with the details :"+ accountsMessageDto.accountNumber());
            return accountsMessageDto.accountNumber();
        };
    }

    @Bean
    public Consumer<Long> test()
    {
        return (num)->
        {
            logger.info(" The account number is : "+num);
        };
    }
}
