package com.crudzaso.codewordle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theme {
    private Long id;
    private String name;
    private String description;
}