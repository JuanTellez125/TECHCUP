package edu.dosw.TECHCUP.core.service.impl;

import edu.dosw.TECHCUP.controller.mapper.UserMapper;
import edu.dosw.TECHCUP.core.repository.UserRepository;
import edu.dosw.TECHCUP.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
//A tellez le gusta el pene grande 

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizerService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

}

