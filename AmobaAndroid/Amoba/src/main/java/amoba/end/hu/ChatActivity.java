package amoba.end.hu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import amoba.end.hu.interfaces.NewChatMessage;

public class ChatActivity extends Activity implements View.OnClickListener, TextView.OnEditorActionListener, NewChatMessage {
    private EditText tbMsg;
    private ListView listMessages;
    private ChatAdapter chatAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        GameActivity.instance().setReceiveChatMessageCallback(this);

        tbMsg = (EditText) findViewById(R.id.tbMsg);
        listMessages = (ListView) findViewById(R.id.chatMessages);

        chatAdapter = new ChatAdapter<ChatMessage>(this, R.layout.chat_item, GameActivity.instance().getChatMessages());
        listMessages.setAdapter(chatAdapter);

        findViewById(R.id.btnSend).setOnClickListener(this);
        findViewById(R.id.btnClose).setOnClickListener(this);
        tbMsg.setOnEditorActionListener(this);
    }

    protected void sendMessage(){
        GameActivity.instance().sendChatMessage(tbMsg.getText().toString());
        tbMsg.setText("");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSend:
                sendMessage();
                break;
            case R.id.btnClose:
                finish();
                break;
        }

    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    public void onGotChatMessage(ChatMessage message) {
        chatAdapter.notifyDataSetChanged();
        listMessages.smoothScrollToPosition(GameActivity.instance().getChatMessages().size());
    }

    @Override
    public void onMessage(JSONObject json) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_NULL
                && event.getAction() == KeyEvent.ACTION_DOWN){
            sendMessage();
        }
        return true;
    }
}
