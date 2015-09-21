package com.abc.terry_sun.abc.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by terry_sun on 2015/9/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class BaseReturnModel {
    int Status;
    int Code;
    String Message;
}
