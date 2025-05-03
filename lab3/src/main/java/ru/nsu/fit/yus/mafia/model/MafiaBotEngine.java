package ru.nsu.fit.yus.mafia.model;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

// ОБЯЗАТЕЛЬНО - Механику обновления доверия к игрокам добавить сюда!
// mafiaMembers - ТОЛЬКО БОТЫ! Реальные игроки имеют преимущество над ботами.

public class MafiaBotEngine {
    public static Player chooseVictim(List<Player> mafiaMembers, List<Player> potentialVictims) {
        // Самый простой вариант: случайная жертва среди живых не-мафиози
        // НУЖНО ПЕРЕПИСАТЬ с учетом рейтинга доверия
        List<Player> validTargets = potentialVictims.stream()
                .filter(p -> !mafiaMembers.contains(p) && p.isAlive())
                .collect(Collectors.toList());

        if (validTargets.isEmpty()) return null;

        Random random = new Random();
        return validTargets.get(random.nextInt(validTargets.size()));
    }

    public static void updateTrustRatings(List<Player> mafiaMembers) {

    }
}
