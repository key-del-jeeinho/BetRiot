package com.xylope.betriot.layer.service.user_v2.account.model;

import com.xylope.betriot.exception.user.WrongAccountProgressException;
import com.xylope.betriot.layer.logic.discord.SpecialEmote;
import lombok.Getter;
import lombok.Setter;

public class NewRegisterAccount {
    private NewRegisterAccountProgress progress;

    @Getter
    private final long discordId;
    @Getter @Setter
    private String riotName;
    @Getter @Setter
    private long policyMessageId;
    @Getter @Setter
    private long riotAuthorizeMessageId;

    public NewRegisterAccount(long discordId) {
        this.discordId = discordId;
        progress = NewRegisterAccountProgress.values()[0];
    }

    public void nextStep() {
        progress = progress.nextStep();
    }

    public boolean checkProgress(NewRegisterAccountProgress progress) {
        return this.progress == progress;
    }

    void authorizeRiot(String emote) {
        if(SpecialEmote.RIOT_CHANGE_ICON_DONE.getEmote().equals(emote)) {
            progress.setRiotAccountAuthorize(true);
        }
    }

    void checkIsPolicyChecked(String emote) {
        if(SpecialEmote.TERMS_AGREE.getEmote().equals(emote)) {
            progress.setPolicyAccept(true);
        }else if(SpecialEmote.TERMS_DISAGREE.getEmote().equals(emote)) {
            progress.setPolicyAccept(false);
        }
    }

    public boolean checkIsPolicyChecked() {
        try {
            progress.getIsPolicyAccept();
            return true;
        } catch (WrongAccountProgressException e) {
            return false;
        }
    }

    public boolean getIsPolicyAccept() {
        return progress.getIsPolicyAccept();
    }

    public boolean checkIsRiotAuthorized() {
        try {
            progress.getIsRiotAccountAuthorize();
            return true;
        } catch (WrongAccountProgressException e) {
            return false;
        }
    }

    public NewRegisterAccountDto convertToDto() {
        return new NewRegisterAccountDto(
                progress,
                discordId,
                riotName,
                policyMessageId,
                riotAuthorizeMessageId
        );
    }
}
