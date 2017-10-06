package com.kiwi.reactor.service.reactors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BalanceReactorService {

    private final Logger log = LoggerFactory.getLogger(BalanceReactorService.class);

}
