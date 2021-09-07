package com.m4case.service;

import com.m4case.model.Player;

public interface IPlayerService extends IGeneralService<Player> {
    Player findByEmail(String email);
}
