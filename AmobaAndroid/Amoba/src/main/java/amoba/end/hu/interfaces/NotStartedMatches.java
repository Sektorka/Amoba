package amoba.end.hu.interfaces;

import java.util.List;

public interface NotStartedMatches extends ResponseJSONMessage{
    public void onGotNotStartedMatchesList(List<NotStartedMatch> notStartedMatchList);
}
