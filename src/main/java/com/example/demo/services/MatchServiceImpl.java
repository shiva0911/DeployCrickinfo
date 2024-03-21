package com.example.demo.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Match;
import com.example.demo.repository.MatchRepo;

@Service
public class MatchServiceImpl implements MatchService 
{
	@Autowired
    private MatchRepo matchRepo;

    @Override
    public List<Match> getAllMatches() {
        return this.matchRepo.findAll();
    }

    public MatchServiceImpl(MatchRepo matchRepo) {
        this.matchRepo = matchRepo;
    }

    @Override
    public List<Match> getLiveMatches() {
        List<Match> matches = new ArrayList<>();
        try {
            String url = "https://www.cricbuzz.com/cricket-match/live-scores";
            Document document = Jsoup.connect(url).get();
            Elements liveScoreElements = document.select("div.cb-mtch-lst.cb-tms-itm");
            for (Element match : liveScoreElements) {
                HashMap<String, String> liveMatchInfo = new LinkedHashMap<>();
                String teamsHeading = match.select("h3.cb-lv-scr-mtch-hdr").select("a").text();
                String matchNumberVenue = match.select("span").text();
                Elements matchBatTeamInfo = match.select("div.cb-hmscg-bat-txt");
                String battingTeam = matchBatTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String score = matchBatTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                Elements bowlTeamInfo = match.select("div.cb-hmscg-bwl-txt");
                String bowlTeam = bowlTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String bowlTeamScore = bowlTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                String textLive = match.select("div.cb-text-live").text();
                String textComplete = match.select("div.cb-text-complete").text();
                // Getting match link
                String matchLink = match.select("a.cb-lv-scrs-well.cb-lv-scrs-well-live").attr("href").toString();

                Match match1 = new Match();
                match1.setTeamHeading(teamsHeading);
                match1.setMatchNumberVenu(matchNumberVenue);
                match1.setBattingTeam(battingTeam);
                match1.setBattingTeamScoure(score);
                match1.setBowlTeam(bowlTeam);
                match1.setBowlTeamScour(bowlTeamScore);
                match1.setLiveText(textLive);
                match1.setMatchLink(matchLink);
                match1.setTextComplete(textComplete);
                // This line assumes you have implemented the setMatchStatus method in the Match entity
                match1.setMatchStatus();

                matches.add(match1);

                // Update the match in the database
                updateMatch(match1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }

    private void updateMatch(Match match1) {
        // Check if the match already exists in the database
        Match existingMatch = this.matchRepo.findByTeamHeading(match1.getTeamHeading()).orElse(null);
        if (existingMatch == null) {
            // If match does not exist, save the new match
            this.matchRepo.save(match1);
        } else {
            // If match already exists, update its fields
            existingMatch.setMatchNumberVenu(match1.getMatchNumberVenu());
            existingMatch.setBattingTeam(match1.getBattingTeam());
            existingMatch.setBattingTeamScoure(match1.getBattingTeamScoure());
            existingMatch.setBowlTeam(match1.getBowlTeam());
            existingMatch.setBowlTeamScour(match1.getBowlTeamScour());
            existingMatch.setLiveText(match1.getLiveText());
            existingMatch.setMatchLink(match1.getMatchLink());
            existingMatch.setTextComplete(match1.getTextComplete());
            // This line assumes you have implemented the setMatchStatus method in the Match entity
            existingMatch.setMatchStatus();
            // Save the updated match
            this.matchRepo.save(existingMatch);
        }
    }

    @Override
    public List<List<String>> getPointTable() {
        List<List<String>> pointTable = new ArrayList<>();
        String tableURL = "https://www.cricbuzz.com/cricket-series/6732/icc-cricket-world-cup-2023/points-table";
        try {
            Document document = Jsoup.connect(tableURL).get();
            Elements table = document.select("table.cb-srs-pnts");
            Elements tableHeads = table.select("thead>tr>*");
            List<String> headers = new ArrayList<>();
            tableHeads.forEach(element -> {
                headers.add(element.text());
            });
            pointTable.add(headers);
            Elements bodyTrs = table.select("tbody>*");
            bodyTrs.forEach(tr -> {
                List<String> points = new ArrayList<>();
                if (tr.hasAttr("class")) {
                    Elements tds = tr.select("td");
                    String team = tds.get(0).select("div.cb-col-84").text();
                    points.add(team);
                    tds.forEach(td -> {
                        if (!td.hasClass("cb-srs-pnts-name")) {
                            points.add(td.text());
                        }
                    });
                    pointTable.add(points);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pointTable;
    }

}
