package com.xylope.betriot.layer.service.user_v2.account.view;

import net.dv8tion.jda.api.entities.PrivateChannel;

public interface AccountView {
    void sendRegisterView(PrivateChannel pc);
    long sendPolicyView(PrivateChannel pc);
    long sendAuthorizeRiotNameView(PrivateChannel pc);
    long sendAuthorizeRiotAccountView(PrivateChannel pc, String iconUrl);
    void sendUserAcceptPolicyView(PrivateChannel pc);
    void sendUserDenyPolicyView(PrivateChannel pc);
}
