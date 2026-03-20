package com.micro.cards.controller;

import com.micro.cards.constants.CardsConstant;
import com.micro.cards.dto.CardsDto;
import com.micro.cards.dto.ResponseDto;
import com.micro.cards.entity.Cards;
import com.micro.cards.service.ICardsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
public class CardsController {

    @Qualifier("CardsServiceImplV1")
    private final ICardsService iCardsService;



    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(@RequestParam
                                                  @Pattern(regexp = "[0-9]{10}",message = "Mobile Number must be 10 digits long")
                                                      String mobileNumber)
    {
        iCardsService.createCard(mobileNumber);

        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(new ResponseDto(CardsConstant.STATUS_201,CardsConstant.MESSAGE_201));
    }


    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> fetchCard(@RequestParam
                                                  @Pattern(regexp ="[0-9]{10}",message = "Mobile number must be 10 digits long")
                                                   String mobileNumber)
    {

        CardsDto cardsDto =  iCardsService.fetchCardDetails(mobileNumber);

        return ResponseEntity.
                status(HttpStatus.OK).
                body(cardsDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCardDetails(@RequestBody @Valid CardsDto cardsDto)
    {
        if(iCardsService.updateCardDetails(cardsDto))
        {
            return ResponseEntity.status(HttpStatus.OK).
                    body(new ResponseDto
                            (CardsConstant.STATUS_200,CardsConstant.MESSAGE_200));
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(CardsConstant.STATUS_417,CardsConstant.MESSAGE_417_UPDATE));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCardDetails(@RequestParam
                                                             @Pattern(regexp = "[0-9]{10}",message = "Mobile Number must be 10 digits long")
                                                              String mobileNumber)
    {
        boolean isDeleted = iCardsService.deleteCardDetails(mobileNumber);

        if(isDeleted)
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstant.STATUS_200,CardsConstant.MESSAGE_200));
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(CardsConstant.STATUS_417, CardsConstant.MESSAGE_417_DELETE));
    }

}
