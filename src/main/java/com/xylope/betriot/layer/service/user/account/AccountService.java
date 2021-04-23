package com.xylope.betriot.layer.service.user.account;

import com.xylope.betriot.exception.user.WrongAccountProgressException;
import com.xylope.betriot.layer.domain.event.OnSecondEvent;
import com.xylope.betriot.layer.service.user.account.controller.AccountController;
import com.xylope.betriot.layer.service.user.account.model.NewRegisterAccount;
import com.xylope.betriot.layer.service.user.account.model.NewRegisterAccountProgress;
import com.xylope.betriot.manager.TimeCounter;
import com.xylope.betriot.manager.TimeListenerAdapter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccountService {
    private final AccountController controller;
    private final TimeCounter timeCounter;

    public void createAccount(long discordId) {
        new CreateAccountLifeCycle(timeCounter, controller, discordId);
    }

    public void removeAccount(long discordId) {
        controller.removeAccount(discordId);
    }
}

class CreateAccountLifeCycle extends TimeListenerAdapter {
    private final TimeCounter timeCounter;
    private final AccountController controller;
    private final long accountId;

    CreateAccountLifeCycle(TimeCounter timeCounter, AccountController controller, long discordId) {
        this.timeCounter = timeCounter;
        this.controller = controller;
        this.accountId = discordId;

        controller.createAccount(new NewRegisterAccount(discordId));

        controller.sendRegisterMessage(accountId);
        controller.policyCheck(accountId); //이용약관 메세지를 보낸다
        controller.nextStep(accountId); //POLICY_UNCHECKED

        timeCounter.addTimeListener(this);
    }

    @Override
    public void onTimeSecond(OnSecondEvent event) {
        if(controller.checkProgress(accountId, NewRegisterAccountProgress.POLICY_UNCHECKED)) {
            boolean isPolicyChecked = controller.checkIsPolicyChecked(accountId);
            if(isPolicyChecked){
                controller.nextStep(accountId); //POLICY_CHECKED
            }
            else return;
        }
        if(controller.checkProgress(accountId, NewRegisterAccountProgress.POLICY_CHECKED)) {
            boolean isUserAcceptPolicy;
            try {
                isUserAcceptPolicy = controller.policyLogic(accountId);
            } catch (WrongAccountProgressException e) {
                return;
            }
            if(!isUserAcceptPolicy) {//만약 유저가 약관에 동의 하지 않았을 경우
                controller.close(accountId);
                timeCounter.removeTimeListener(this);
                return;
            }
            //유저가 약관에 동의 하였을경우,
            controller.authorizeRiotName(accountId); //라이엇 인증 (소환사 닉네임) 메세지 보냄
            controller.nextStep(accountId); //RIOT_AUTHORIZE_NAME
        }
        if(controller.checkProgress(accountId, NewRegisterAccountProgress.RIOT_AUTHORIZE_NAME)) {
            if(controller.checkAuthorizeRiotName(accountId)) {//인증 메세지 회신 확인
                controller.authorizeRiotAccount(accountId);
                controller.nextStep(accountId); //RIOT_AUTHORIZE_ACCOUNT
            } else return;
        }
        if(controller.checkProgress(accountId, NewRegisterAccountProgress.RIOT_AUTHORIZE_ACCOUNT)) {
            if(controller.checkAuthorizeRiotAccount(accountId)) {//인증 확인
                if(controller.authorizeRiotAccountLogic(accountId)) { //인증 성공
                    controller.nextStep(accountId); //REGISTERED
                } else { //인증 실패
                    controller.close(accountId);
                    timeCounter.removeTimeListener(this);
                    return;
                }
            } else return;
        }
        if(controller.checkProgress(accountId, NewRegisterAccountProgress.REGISTERED)) {
            controller.register(accountId);
            controller.close(accountId);
            timeCounter.removeTimeListener(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateAccountLifeCycle that = (CreateAccountLifeCycle) o;

        return accountId == that.accountId;
    }

    @Override
    public int hashCode() {
        return (int) accountId;
    }
}
