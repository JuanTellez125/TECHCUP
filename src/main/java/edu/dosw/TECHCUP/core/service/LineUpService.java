package edu.dosw.TECHCUP.core.service;

import edu.dosw.TECHCUP.controller.mapper.LineUpMapper;
import edu.dosw.TECHCUP.controller.mapper.TeamMapper;
import edu.dosw.TECHCUP.core.repository.LineUpRepository;
import edu.dosw.TECHCUP.core.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LineUpService {

    private final LineUpRepository lineUpRepository;
    private final LineUpMapper lineUpMapper;



}
