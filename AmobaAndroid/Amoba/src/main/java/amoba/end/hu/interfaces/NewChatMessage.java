package amoba.end.hu.interfaces;

import amoba.end.hu.ChatMessage;

public interface NewChatMessage extends ResponseJSONMessage {
    public void onGotChatMessage(ChatMessage message);
}
