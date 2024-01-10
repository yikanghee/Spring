package com.spring.common.domain.user.entity;

import com.spring.common.domain.user.dto.ProfileRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Profile {

    @Column
    private String nickName;

    @Column
    private String img_url = "";

    public Profile(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 서비스 메소드
     */
    public void update(ProfileRequestDto request) {
        this.nickName = request.getNickname();
        this.img_url = request.getImg_url();
    }

}
