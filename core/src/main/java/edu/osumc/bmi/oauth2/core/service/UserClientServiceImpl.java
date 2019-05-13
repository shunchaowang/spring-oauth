package edu.osumc.bmi.oauth2.core.service;

import edu.osumc.bmi.oauth2.core.domain.Client;
import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.domain.UserClient;
import edu.osumc.bmi.oauth2.core.repository.UserClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserClientServiceImpl implements UserClientService {

    @Autowired
    private UserClientRepository userClientRepository;

    @Override
    public UserClient get(long id) {
        return userClientRepository.getOne(id);
    }

    @Override
    public List<UserClient> findByUser(User user) {
        return userClientRepository.findByUser(user);
    }

    @Override
    public List<UserClient> findByUser(String user) {
        return userClientRepository.findByUserUsername(user);
    }

    @Override
    public List<UserClient> findByClient(Client client) {
        return userClientRepository.findByClient(client);
    }

    @Override
    public List<UserClient> findByClient(String client) {
        return userClientRepository.findByClientName(client);
    }

    @Override
    public UserClient findByUserAndClient(User user, Client client) {
        return userClientRepository.findByUserAndClient(user, client);
    }

    @Override
    public UserClient findByUserAndClient(String user, String client) {
        return userClientRepository.findByUserUsernameAndClientName(user, client);
    }

    @Override
    public UserClient save(UserClient userClient) {
        return userClientRepository.save(userClient);
    }
}
