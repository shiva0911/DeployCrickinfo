package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import com.example.demo.entity.Match;
import com.example.demo.services.MatchServiceImpl;
@RestController
@RequestMapping("/match")
@CrossOrigin("*")
public class MatchController 
{
	@Autowired
	private MatchServiceImpl matchServiceImpl;
	@GetMapping("/live")
	public ResponseEntity<List<Match>>getLiveMatches()
	{
		return new ResponseEntity<>(this.matchServiceImpl.getLiveMatches(),HttpStatus.OK);
		
	}
	@GetMapping
	public ResponseEntity<List<Match>> getAllMatches()
	{
		return new ResponseEntity<>(this.matchServiceImpl.getAllMatches(),HttpStatus.OK);
	}
	@GetMapping("/table")
	public ResponseEntity<?>getPointTable()
	{
		return new ResponseEntity<>(this.matchServiceImpl.getPointTable(),HttpStatus.OK); 
	}
	{
		
	}
	
	
}
