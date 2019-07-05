package com.relipa.religram.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageValid {
    private int statusCode;
    private List<ErrorMap> message;

}
