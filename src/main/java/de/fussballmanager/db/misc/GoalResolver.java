package de.fussballmanager.db.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.fussball.kader.ClassicKaderFactory;
import de.fussball.live.ticker.LiveTickerHandler;
import de.fussball.live.ticker.event.Event;
import de.fussballmanager.db.entity.match.Match;
import de.fussballmanager.db.entity.match.MatchService;
import de.fussballmanager.db.entity.matchday.Matchday;
import de.fussballmanager.db.entity.trainer.Trainer;

public class GoalResolver {

	public List<ProcessedEvent> getGoals(Matchday aMatchday) {
		List<ProcessedEvent> returnEvents = new ArrayList<ProcessedEvent>();
		LiveTickerHandler liveTicker = new LiveTickerHandler();
		ClassicKaderFactory ckf = new ClassicKaderFactory();

		List<Match> matches = new MatchService().getAllByMatchday(aMatchday);

		List<Event> resolvedEvents = liveTicker
				.getResolvedLiveTickerEvents(aMatchday);

		for (Event tempEvent : resolvedEvents) {
			Map<Trainer, Set<String>> allPlayer = ckf.getAll(aMatchday);
			try {
				for (Trainer trainer : allPlayer.keySet()) {
					Set<String> team = allPlayer.get(trainer);

					if (team.contains(tempEvent.getResolved())) {

						Match match = getMatch(matches, trainer);
						
						ProcessedEvent processedEvent = new ProcessedEvent();
						processedEvent.setEvent(tempEvent.getEvent());
						processedEvent.setId(tempEvent.getId());
						processedEvent.setName(tempEvent.getName());
						processedEvent.setResolved(tempEvent.getResolved());
						processedEvent.setTrainer(trainer);
						processedEvent.setMatch(match);
						processedEvent.addEventToMatch();
						returnEvents.add(processedEvent);

					}

				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return returnEvents;
	}

	
	public Match getMatch(List<Match> matches, Trainer trainer){
		Match current = null;
		for (Match tempMatch : matches) {
			if (tempMatch.getHome().equals(trainer)
					|| tempMatch.getGuest().equals(trainer)) {
				current = tempMatch;
			}
		}
		return current;
	}
	
}
