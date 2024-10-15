package org.youcode.DevSync.domain.exceptions;

public class TokenHasAlreadyBeenUsedException extends RuntimeException{

    public TokenHasAlreadyBeenUsedException(String message){
        super(message);
    }
}
