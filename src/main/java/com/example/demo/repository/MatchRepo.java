package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Match;

public interface MatchRepo extends JpaRepository<Match, Integer>{
	Optional<Match> findByTeamHeading(String teamHeading);

}
