package com.example.player.service;

import com.example.player.model.Player;
import java.util.*;
import com.example.player.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.example.player.model.PlayerRowMapper;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class PlayerH2Service implements PlayerRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Player> getPlayers() {

        List<Player> playerList = db.query("select * from TEAM", new PlayerRowMapper());
        ArrayList<Player> players = new ArrayList<>(playerList);
        return players;
    }

    @Override
    public Player getPlayerById(int playerId) {
        try {
            Player player = db.queryForObject("select * from TEAM where id = ?", new PlayerRowMapper(), playerId);
            return player;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        }

    }

    @Override
    public Player addPlayer(Player player) {
        db.update("insert into TEAM(playername,jerseyNumber, role) values (?,?, ?)", player.getPlayerName(),
                player.getJerseyNumber(), player.getRole());
        Player savedPlayer = db.queryForObject("select * from TEAM where playerName = ? and jerseyName= ? and role=?",
                new PlayerRowMapper(),
                player.getPlayerName(), player.getJerseyNumber(), player.getRole());
        return savedPlayer;

    }

    @Override
    public Player updatePlayer(int playerId, Player player) {
        if (player.getPlayerName() != null) {
            db.update("update TEAM set playerName = ? where id = ?", player.getPlayerName(), playerId);
        }
        if (player.getJerseyNumber() != 0) {
            db.update("update TEAM set jerseryNumber = ? where id = ?", player.getJerseyNumber(), playerId);
        }
        if (player.getRole() != null) {
            db.update("update TEAM set role = ? where id = ?", player.getRole(), playerId);
        }
        return getPlayerById(playerId);

    }

    @Override
    public void deletePlayer(int playerId) {
        db.update("delete from TEAM where id = ?", playerId);
    }

}
