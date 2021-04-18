package com.xylope.betriot.layer.service.user_v2.account.model;

import com.xylope.betriot.exception.user.WrongAccountProgressException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;

@AllArgsConstructor
@RequiredArgsConstructor
public enum NewRegisterAccountProgress {
    UNREGISTERED(0), //아직 계정 생성 관련 로직이 실행 되지 않았 을때
    POLICY_UNCHECKED(1), //이용 약관 체크 이전
    POLICY_CHECKED(2), //이용 약관 체크 이후
    RIOT_AUTHORIZE_NAME(3), //라이엇 본인 계정 이름 인증 이전
    RIOT_AUTHORIZE_ACCOUNT(4), //라이엇 본인 계정 여부 인증 이전
    REGISTERED(5); //계정 가입이 완료된 경우

    @Getter
    private final int progressLevel;
    @Setter
    private AtomicBoolean isPolicyAccept;
    @Setter
    private boolean isRiotAccountAuthorize = false;

    public boolean getIsPolicyAccept() {
        try {
            System.out.println("getPolicy is" + isPolicyAccept.get() + " - " + System.identityHashCode(this));
            return isPolicyAccept.get();
        } catch (NullPointerException e) {
            System.out.println("null씨발 - " + System.identityHashCode(this));
            throw new WrongAccountProgressException(e);
        }
    }

    public boolean getIsRiotAccountAuthorize() {
        return isRiotAccountAuthorize;
    }

    public NewRegisterAccountProgress nextStep() {
        NewRegisterAccountProgress progress =  values()[ordinal()+1];
        progress.setIsPolicyAccept(isPolicyAccept);
        progress.setRiotAccountAuthorize(isRiotAccountAuthorize);
        return progress;
    }

    public void setPolicyAccept(boolean isPolicyAccept) {
        System.out.println("setPolicy to " + isPolicyAccept + " - " + hashCode());
        this.isPolicyAccept = new AtomicBoolean(isPolicyAccept);
    }
}
