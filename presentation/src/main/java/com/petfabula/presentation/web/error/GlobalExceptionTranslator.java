package com.petfabula.presentation.web.error;

import com.petfabula.domain.exception.*;
import com.petfabula.presentation.web.api.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionTranslator {

    static String NOT_FOUND_KEY = "entityId";

    @Autowired
    ResourceBundleMessageSource messageSource;

    @ExceptionHandler(ExternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleBotMessageException(ExternalServerException ex, HttpServletRequest request) {
        log.error(ex.toString());
        Response res = Response.failedOnValidation();
        res.setMessage(ex.getMessage());
        return res;
    }

    @ExceptionHandler(InvalidValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleInvalidValueException(InvalidValueException ex, HttpServletRequest request) {
        log.error(ex.toString());
        Response res = Response.failedOnValidation();
//        res.setMessage(ex.getMessage());
        res.getErrors().put(ex.getName(),
                messageSource.getMessage(ex.getMsg(), null, LocaleContextHolder.getLocale()));

        return res;
    }

    @ExceptionHandler(DomainAuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleDomainAuthenticationException(DomainAuthenticationException ex, HttpServletRequest request) {
        log.error(ex.toString());
        Response res = Response.failedOnValidation();
        res.getErrors().put(ex.getField(), messageSource.getMessage(ex.getMsg(), null, LocaleContextHolder.getLocale()));

        return res;
    }

    @ExceptionHandler(InvalidOauthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleInvalidOauthException(InvalidOauthException ex, HttpServletRequest request) {
        log.error(ex.toString());
        Response res = Response.failed(Response.ResponseCode.AUTHENTICATION_FAILED);
        res.setMessage(ex.getMessage());

        return res;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        log.error(ex.toString());
        Response res = Response.failed(Response.ResponseCode.NOT_FOUND);
        res.getErrors().put(NOT_FOUND_KEY, ex.getEntityId());
        res.setMessage(messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale()));

        return res;
    }

    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleInvalidOperationException(InvalidOperationException ex, HttpServletRequest request) {
        ex.printStackTrace();
        log.error(ex.toString());
        Response res = Response.failed(Response.ResponseCode.INVALID_OPERATION);
        //res.setMessage(ex.getMessage());
        res.setMessage(messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale()));

        return res;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException
                                                  manve, HttpServletRequest request) {
        log.error(manve.getMessage());
        BindingResult result = manve.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        Response res = Response.failedOnValidation();
        //res.setMsg(manve.getMessage());
        fieldErrors.stream().forEach(e -> res.getErrors().put(e.getField(), e.getDefaultMessage()));
        return res;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.toString());
        Response res = Response.failed(Response.ResponseCode.INVALID_OPERATION);

        return res;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleRuntimeException(RuntimeException ex) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        log.error("[Runtime error] - url: {}", request.getRequestURL());
        log.error(ex.toString());
        ex.printStackTrace();
        Response res = Response.failedInternal();

        return res;
    }
}
