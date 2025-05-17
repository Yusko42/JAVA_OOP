package ru.nsu.fit.yus.mafia.model.TRASH;

import ru.nsu.fit.yus.mafia.model.Player;

import java.util.List;

public class CivilianBotEngine {
    public static Player chooseSuspect(Player player, List<Player> livingPlayers) {
        /*double ratingScore = 1.0;
        for (Player target: livingPlayers) {
            //Выбираем самого нанадёжного по мнению бота игрока по рейтингу
            ratingScore = Math.min(ratingScore, player.getTrustLevels().get(target));
        }
        double finalRatingScore = ratingScore;
        List<Player> minRatingSuspects = livingPlayers.stream()
                    .filter(p -> (player.getTrustLevels().get(p) == finalRatingScore))
                    .toList();

        //СЛУЧАЙНО, если подозреваемых несколько. ОДНОЗНАЧНО, когда подозреваемый один
        return minRatingSuspects.get(ThreadLocalRandom.current().nextInt(minRatingSuspects.size()));*/
    }


    public static void updateTrustRatings(List<Player> civilianBots) {

    }
}
