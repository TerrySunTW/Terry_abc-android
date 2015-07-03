package com.abc.terry_sun.abc.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by terry_sun on 2015/7/3.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class GalleryItem {
    String ItemID;
    String Title;
    String ImageName;
}
