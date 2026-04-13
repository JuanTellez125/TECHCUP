package edu.dosw.TECHCUP.core.exception;

public class VenueNotFoundException extends RuntimeException{
    public VenueNotFoundException(long vanueId){
        super("Court with ID" + vanueId + "not found");
    }
}
