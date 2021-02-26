package com.xylope.betriot;

import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JDAEventManager {
    @Setter
    private JDA jda;
    @Setter
    private ListenerAdapter[] listeners;

    public void loadListener() {
        System.out.println(listeners.length);
        jda.addEventListener((Object[]) listeners); //배열 내의 인수를 가져와 가변인수로 전달하기 위해 Object[] 로 캐스팅
    }
}
