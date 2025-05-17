package ru.nsu.fit.yus.mafia.model.roles;

import ru.nsu.fit.yus.mafia.model.Player;

import java.util.List;

public interface Role {
    String getRoleName();
    String getRoleDescription();
    String getAbilityDescription();

    // return Player - выбираем жертву/пациента, а в модели уже всё исполняется (всё из-за неоднозначности выбора)
    // self - для определения, бот или нет;
    // playersList - для выбора человека как жертву/пациента/т. п.

    Player nightAction(Player self, List<Player> playersList);

    default void dayDiscussion(Player self) {
        //Связь с сообщениями
    }

    default Player dayVote(Player self, List<Player> livingPlayers) {
        return self.getDecisionProvider().chooseLowestTrust(self, livingPlayers);
    }
}
