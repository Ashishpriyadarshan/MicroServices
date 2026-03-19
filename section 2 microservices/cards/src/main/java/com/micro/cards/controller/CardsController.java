package com.micro.cards.controller;

import com.micro.cards.constants.CardsConstant;
import com.micro.cards.dto.CardsDto;
import com.micro.cards.dto.ResponseDto;
import com.micro.cards.service.ICardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class CardsController {

    @Qualifier("CardsServiceImplV1")
    private final ICardsService iCardsService;



    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(@RequestParam
                                                  String mobileNumber)
    {
        iCardsService.createCard(mobileNumber);

        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(new ResponseDto(CardsConstant.STATUS_201,CardsConstant.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> fetchCard(@RequestParam String mobileNumber)
    {

        CardsDto cardsDto =  iCardsService.fetchCardDetails(mobileNumber);

        return ResponseEntity.
                status(HttpStatus.OK).
                body(cardsDto);
    }

}
