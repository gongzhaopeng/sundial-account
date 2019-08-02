package cn.benbenedu.sundial.account.repository;

import cn.benbenedu.sundial.account.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository
        extends MongoRepository<Client, String> {
}
