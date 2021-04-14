package com.xylope.betriot.layer.service.user_v2.account.model;

import com.xylope.betriot.exception.user.WrongAccountProgressException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
    private boolean isPolicyAccept;
    @Setter
    private boolean isRiotAccountAuthorize;

    public boolean getIsPolicyAccept() {
        if(progressLevel < POLICY_UNCHECKED.progressLevel) {
            throw new WrongAccountProgressException();
        }
        return isPolicyAccept;
    }

    public boolean getIsRiotAccountAuthorize() {
        if(progressLevel < RIOT_AUTHORIZE_ACCOUNT.progressLevel) {
            throw new WrongAccountProgressException();
        }
        return isRiotAccountAuthorize;
    }

    public NewRegisterAccountProgress nextStep() {
        return values()[ordinal()+1];
    }
}
