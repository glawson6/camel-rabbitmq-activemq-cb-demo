package com.taptech.ttis.camel;

import java.io.Serializable;

/**
 * Created by tap on 9/30/16.
 */
public class DemoMessage implements Serializable{

    private static final long serialVersionUID = 4707285496353165171L;

    public DemoMessage() {
    }

    public DemoMessage(String tid, boolean transacted,  String messageBroker, String message) {
        this.message = message;
        this.messageBroker = messageBroker;
        this.tid = tid;
        this.transacted = transacted;
    }

    private boolean transacted;
    private String tid;
    private String message;
    private String messageBroker;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageBroker() {
        return messageBroker;
    }

    public void setMessageBroker(String messageBroker) {
        this.messageBroker = messageBroker;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public boolean isTransacted() {
        return transacted;
    }

    public void setTransacted(boolean transacted) {
        this.transacted = transacted;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DemoMessage{");
        sb.append("message='").append(message).append('\'');
        sb.append(", transacted=").append(transacted);
        sb.append(", tid='").append(tid).append('\'');
        sb.append(", messageBroker='").append(messageBroker).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
