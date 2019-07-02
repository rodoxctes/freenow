package com.mytaxi.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class UpdateCarRatingRequest
{
    @Min(1)
    @Max(5)
    private int rating;

}
