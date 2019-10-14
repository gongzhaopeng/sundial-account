package cn.benbenedu.sundial.account.model;

import lombok.Data;

@Data
public class Creator {

    private String id;
    private String name;
    private String nickname;

    public static Creator of(SundialUserDetails userDetails) {

        final var creator = new Creator();
        creator.setId(userDetails.getId());
        creator.setName(userDetails.getName());
        creator.setNickname(userDetails.getNickname());

        return creator;
    }
}
