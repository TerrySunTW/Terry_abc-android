package com.abc.terry_sun.abc.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by terry_sun on 2015/7/2.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class CardInfo {
    String RepresentativeID;
    String EntityCardID;
    String CardName;
    String CardImage;
}
