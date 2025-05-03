package ru.nsu.fit.yus.mafia.model;

import ru.nsu.fit.yus.mafia.model.roles.Civilian;
import ru.nsu.fit.yus.mafia.model.roles.Mafia;
import ru.nsu.fit.yus.mafia.model.roles.Role;
import ru.nsu.fit.yus.mafia.model.roles.Sheriff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Model {
    private final List<Player> playersList = new ArrayList<>();
    private final GameStage stage;

    public Model(int numberOfPlayers, String realPlayerName){
        initializePlayers(numberOfPlayers, realPlayerName);
        stage = new GameStage();    //Night - 0
        //Игровой контекст тоже добавить?
    }

    private void initializePlayers(int numberOfPlayers, String realPlayerName) {
        List<Role> availableRoles = createRoleList(numberOfPlayers);

        //Первую роль из перемешанной стопки - реальному игроку
        //NullPointer - исключение может быть выброшено!
        Player realPlayer = new Player(realPlayerName, availableRoles.removeFirst(), false);
        playersList.add(realPlayer);

        //Создание ботов
        for (int i = 1; i < numberOfPlayers; i++) {
            Role botRole = availableRoles.removeFirst();
            Player bot = new Player("Bot " + i, botRole, true);
            playersList.add(bot);
        }

        // Инициализируем доверие
        for (Player p : playersList) {
            for (Player other : playersList) {
                if (!p.equals(other)) {
                    p.getTrustLevels().put(other, 0.0); // Стартовое доверие нейтральное
                }
            }
        }

    }

    /// Сколько мафиози, в каком (случайном) порядке - всё здесь
    private List<Role> createRoleList(int numberOfPlayers) {
        List<Role> availableRoles = new ArrayList<>();

        // Примерные пропорции: 1 шериф, 2-3 мафиози, остальные мирные
        int mafiaCount = Math.min(3, numberOfPlayers / 3);
        int sheriffCount = 1;
        int civilianCount = numberOfPlayers - mafiaCount - sheriffCount;

        for (int i = 0; i < mafiaCount; i++) availableRoles.add(new Mafia());
        for (int i = 0; i < sheriffCount; i++) availableRoles.add(new Sheriff());
        for (int i = 0; i < civilianCount; i++) availableRoles.add(new Civilian());

        Collections.shuffle(availableRoles);
        return availableRoles;
    }

    public void startDay() { stage.newDay(); }
    public void startNight() { stage.newNight(); }

    /// НОЧЬ

    // Ночное голосование МАФИИ
    public void mafiaVote() {

    }

    // Ночное патрулирование ШЕРИФА
    public void sheriffCheck() {

    }

    /// ДЕНЬ

    // Объявление об убийстве, иначе - ДОБРОЕ УТРО СТРАНА!
    public void announcement() {

    }

    //Обсуждение днем случившегося ПЕРЕД голосованием
    public void discussion() {

    }

    // Дневное голосование ВСЕХ ИГРОКОВ
    public void vote() {
        for (Player p : playersList) {

        }
    }

    // Применяется как после голосования днем, так и после голосования мафии ночью
    private void kill(GameStage stage, Player victim) {

    }

    // Проверка: убито/посажено достаточное количество игроков для завершения игры
    public boolean isGameOver() {
        boolean isGameOver = false;

        return isGameOver;
    }


}
