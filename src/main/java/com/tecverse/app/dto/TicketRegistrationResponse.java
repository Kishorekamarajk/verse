package com.tecverse.app.dto;

public class TicketRegistrationResponse {
    private String referenceNumber;
    private String email;
    private String message;
    public TicketRegistrationResponse() {}
    public TicketRegistrationResponse(String referenceNumber, String email, String message) {
        this.referenceNumber=referenceNumber; this.email=email; this.message=message;
    }
    public String getReferenceNumber(){return referenceNumber;} public String getEmail(){return email;} public String getMessage(){return message;}
}
