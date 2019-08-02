package cn.benbenedu.sundial.account.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.CredentialsContainer;

import java.util.Date;
import java.util.List;

@Data
@Document("Account")
public class Account implements CredentialsContainer {

    @Id
    private String id;
    private Long createTime;
    @JsonProperty("lUTime")
    private Long lUTime;
    private Creator creator;

    private AccountState state;

    private AccountAffiliation affiliation;
    private String idNumber;
    private String mobile;
    private String email;
    private String password;
    private WechatInfo wechat;
    private AccountType type;
    private String name;
    private String nickname;
    private AccountGender gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String country;
    private String province;
    private String city;
    private String avatar;
    private List<AccountRole> roles;
    private List<AccountEchain> echains;

    @Override
    public void eraseCredentials() {
        password = null;
    }
}
