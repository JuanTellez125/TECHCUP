package edu.dosw.TECHCUP.core.service.impl;

import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.repository.UserRepository;
import edu.dosw.TECHCUP.core.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class AdministratorService extends UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

}
