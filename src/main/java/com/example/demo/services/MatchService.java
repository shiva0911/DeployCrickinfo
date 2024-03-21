package com.example.demo.services;
import java.util.List;
import java.util.Map;

import com.example.demo.entity.Match;

public interface MatchService {
	List<Match> getAllMatches();
	List<Match> getLiveMatches();
	List<List<String>>getPointTable();

}
