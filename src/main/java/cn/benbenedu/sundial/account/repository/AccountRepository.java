package cn.benbenedu.sundial.account.repository;

import cn.benbenedu.sundial.account.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository
        extends MongoRepository<Account, String> {

    boolean existsByMobile(String mobile);

    boolean existsByIdNumberOrMobileOrEmail(
            String idNumber, String mobile, String email);

    Optional<Account> findByIdNumberOrMobileOrEmail(
            String idNumber, String mobile, String email);
}
