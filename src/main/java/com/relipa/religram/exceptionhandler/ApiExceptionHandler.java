package com.relipa.religram.exceptionhandler;

import com.relipa.religram.error.ErrorMap;
import com.relipa.religram.error.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {
    /**
     * Tất cả các Exception không được khai báo sẽ được xử lý tại đây
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleAllException(Exception ex) {
        // quá trình kiểm soat lỗi diễn ra ở đây
        return new ErrorMessage(10001, ex.getMessage());
    }

    /**
     * MethodArgumentNotValidException sẽ được xử lý riêng tại đây
     * Xử lý @Valid của item input
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorMessage handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<ErrorMap> errorMapList = new ArrayList<>();
        for (ObjectError objectError: ex.getBindingResult().getAllErrors()
        ) {
            String messageErrors = objectError.getDefaultMessage();
            String fieldErrors = ((FieldError) objectError).getField();
            errorMapList.add(new ErrorMap(fieldErrors,messageErrors));
        }

        return new ErrorMessage(10002,errorMapList);
    }

}
