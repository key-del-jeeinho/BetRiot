package com.xylope.betriot.layer.service.listener;

import com.xylope.betriot.layer.dataaccess.SummonerDao;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml", "secretContext.xml");
        SummonerDao dao = context.getBean("summonerDao", SummonerDao.class);

        User user = e.getAuthor();
        TextChannel tc = e.getTextChannel();
        Message msg = e.getMessage();
        String content = msg.getContentRaw();
        if(user.isBot()) return;
        System.out.println("채팅이 쳐졌습니다");
        if(content.startsWith("벳라이엇"))
            tc.sendMessage(dao.getByName("엄준식사하셧나요").toString()).queue();
    }
}
