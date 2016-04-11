package amoba.end.hu.interfaces;

import java.util.List;

public interface PlayedGames extends ResponseJSONMessage {
    public void onGotNotStartedMatchesList(List<PlayedGame> playedGames);
}
