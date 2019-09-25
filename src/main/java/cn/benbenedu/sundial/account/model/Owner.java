package cn.benbenedu.sundial.account.model;

import lombok.Data;

@Data
public class Owner {

    private String id;
    private String name;
    private String nickname;

    public static Owner of(Account account) {

        final var owner = new Owner();
        owner.setId(account.getId());
        owner.setName(account.getName());
        owner.setNickname(account.getNickname());

        return owner;
    }
}
