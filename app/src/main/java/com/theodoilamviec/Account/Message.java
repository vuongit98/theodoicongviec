package com.theodoilamviec.Account;

public class Message {
    private String uidSentMessage;
    private String nameSentMessage;
    private String idMessage;
    private String message;
    private int typeMessage ;
    private Long timeSent;

    public Message() {
    }

    public String getNameSentMessage() {
        return nameSentMessage;
    }

    public void setNameSentMessage(String nameSentMessage) {
        this.nameSentMessage = nameSentMessage;
    }

    public int getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(int typeMessage) {
        this.typeMessage = typeMessage;
    }

    public Message(String uidSentMessage, String nameSentMessage, String idMessage, String message, Long timeSent) {
        this.uidSentMessage = uidSentMessage;
        this.idMessage = idMessage;
        this.message = message;
        this.timeSent = timeSent;
        this.nameSentMessage = nameSentMessage;
    }

    public String getUidSentMessage() {
        return uidSentMessage;
    }

    public void setUidSentMessage(String uidSentMessage) {
        this.uidSentMessage = uidSentMessage;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Long timeSent) {
        this.timeSent = timeSent;
    }
}
