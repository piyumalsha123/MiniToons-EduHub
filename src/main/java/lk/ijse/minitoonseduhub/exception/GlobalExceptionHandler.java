package lk.ijse.minitoonseduhub.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lk.ijse.minitoonseduhub.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleUsernameNotFoundException(UsernameNotFoundException e){
        return new ApiResponse(404,"UserName Not Found",e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleBadCredentialsException(BadCredentialsException e){
        return new ApiResponse(401,"Bad Credentials",
                "Invalid Username or Password");
    }
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleExpiredJwtException(ExpiredJwtException e){
        return new ApiResponse(401,"Expired Token",e.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleRuntimeException(RuntimeException e){
        return new ApiResponse(500,"Internal Server Error",e.getMessage());
    }
}