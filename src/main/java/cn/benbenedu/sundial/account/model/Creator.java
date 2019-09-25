package cn.benbenedu.sundial.account.model;

import lombok.Data;

import java.util.Map;

@Data
public class Creator {

    private String id;
    private String name;
    private String nickname;

    public static Creator of(Map properties) {

        final var creator = new Creator();
        creator.setId((String) properties.get("id"));
        creator.setName((String) properties.get("name"));
        creator.setName((String) properties.get("nickname"));

        return creator;
    }
}
