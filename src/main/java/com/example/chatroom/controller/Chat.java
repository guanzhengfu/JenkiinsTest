package com.example.chatroom.controller;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

//该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。类似Servlet的注解mapping。无需在web.xml中配置。
//chatDemo：表示访问的具体url。{sendUser}：表示在url上的参数
@ServerEndpoint("/chat/{sendUser}")
@Component
public class Chat{
    private static int onLineCount = 0;
    private static ConcurrentHashMap<Integer, Chat> webSocketMap = new ConcurrentHashMap<Integer, Chat>();
    private Session session;
    private Integer sendUser;
    private Integer toUser;
    private String message;
    @OnOpen
    public void onOpen(@PathParam("sendUser") Integer sendUser,Session session) throws IOException {
        this.sendUser = sendUser;
        this.session = session;
        addOnlineCount();
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount() + " 当前session是" + session.hashCode());

        webSocketMap.put(sendUser, this);
        for (Chat chat : webSocketMap.values()) {
            chat.sendMessage("count",getOnlineCount() + "");
        }
    }

    @OnClose
    public void onClose() throws IOException {
        subOnlineCount();

        for (Chat chat : webSocketMap.values()) {
            if(chat.session != this.session){
                chat.sendMessage("count", getOnlineCount() + "");
            }
        }
        webSocketMap.remove(sendUser);
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
    @OnMessage
    public void onMessage(String jsonMsg, Session session) throws IOException {
        JSONObject jsonOject = JSONObject.fromObject(jsonMsg);
        sendUser = Integer.parseInt(jsonOject.getString("sendUser"));
        toUser = Integer.parseInt(jsonOject.getString("toUser"));
        message = jsonOject.getString("message");
        message = "来自:" + sendUser + "用户发给" + toUser + "用户的信息:" + message + " \r\n";
        String[] split = message.split(":");
        // 得到接收人
        if(toUser==-1){
            message = toUser+":"+sendUser + "用户说"+ split[2] + " \r\n";
            for(Chat chat: webSocketMap.values()){
                if(chat!=this){
                    chat.sendMessage("send",message);
                }
            }
        }else {
            Chat user = webSocketMap.get(toUser);
            if(user!=null){
                user.sendMessage("send",message);
            }
            else {
                sendMessage("send","系统:  用户不在线,请稍后再试!");
            }
        }
    }
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }
    public void sendMessage(String type,String message) throws IOException {
        if(type.equals("count")){
            this.session.getBasicRemote().sendText("count:" + message);
        }else{
            this.session.getBasicRemote().sendText(message);
        }
    }
    public static synchronized int getOnlineCount() {
        return onLineCount;
    }
    public static synchronized void addOnlineCount() {
        Chat.onLineCount++;
    }
    public static synchronized void subOnlineCount() {
        Chat.onLineCount--;
    }
}