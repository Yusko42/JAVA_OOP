package ru.nsu.fit.yus.mafia.model.roles;

import ru.nsu.fit.yus.mafia.model.GameContext;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.Message;

import java.util.List;

public class Mafia implements Role {

    public String getRoleName() {
        return "Mafia member";
    }

    public String getRoleDescription(){
        return "Your task is to reduce the population to at least the number of mafia members.";
    }

    public String getAbilityDescription(){
        return "You can discuss whom to kill during the night and take part in the discussion during the day.";
    }

    @Override
    public boolean isMafia() {
        return true;
    }

    @Override
    public boolean ignoresTrustShift(Player from, Player to, GameContext context) {
        return context.getAliveMafia().contains(from) && context.getAliveMafia().contains(to);
    }

    @Override
    public boolean isAlly(Player self, Player other, GameContext context) {
        return context.getAliveMafia().contains(other);
    }


    public Player nightAction(Player self, List<Player> livingCivilians){
        return self.getDecisionProvider().chooseLowestTrust(self, livingCivilians);
    }

    public Message dayDiscussion(Player self, GameContext context) {
        return self.getDecisionProvider().chooseMafiaMessage(self, context);
    }
}
