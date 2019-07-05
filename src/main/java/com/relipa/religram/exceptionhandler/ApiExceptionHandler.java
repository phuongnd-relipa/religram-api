package com.relipa.religram.exceptionhandler;


import com.relipa.religram.errors.ErrorMap;
import com.relipa.religram.errors.ErrorMessage;
import com.relipa.religram.errors.ErrorMessageValid;
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
    public ErrorMessageValid handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<ErrorMap> errorMapList = new ArrayList<>();
        for (ObjectError objectError: ex.getBindingResult().getAllErrors()
             ) {
            String messageErrors = objectError.getDefaultMessage();
            String fieldErrors = ((FieldError) objectError).getField();
            errorMapList.add(new ErrorMap(fieldErrors,messageErrors));
        }

        return new ErrorMessageValid(10002,errorMapList);
    }

    /**
     * UserAlreadyExistException sẽ được xử lý riêng tại đây
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistException.class)
    public ErrorMessage TodoException(Exception ex) {

        return new ErrorMessage(10003,ex.getMessage());
    }
}
