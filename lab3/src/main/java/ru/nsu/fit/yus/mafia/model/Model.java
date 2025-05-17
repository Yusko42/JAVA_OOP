package ru.nsu.fit.yus.mafia.model;

import ru.nsu.fit.yus.mafia.model.decisionProvider.DecisionProvider;
import ru.nsu.fit.yus.mafia.model.roles.Civilian;
import ru.nsu.fit.yus.mafia.model.roles.Mafia;
import ru.nsu.fit.yus.mafia.model.roles.Role;
import ru.nsu.fit.yus.mafia.model.roles.Sheriff;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Model {
    private List<Player> playersList = new ArrayList<>();
    private GameContext context;

    private List<Role> availableRoles;

    /// Не вижу смысла больше в конструкторе модели.
    /*public Model(int numberOfPlayers){
        // 1. Список ролей
        availableRoles = createRoleList(numberOfPlayers);
        //initializePlayers(numberOfPlayers, realPlayerName);
        //context = new GameContext(playersList);    //Night - 0
    }*/

    // 1. Создание списка ролей
    public void createListOfAvailableRoles(int numberOfPlayers) {
        availableRoles = createRoleList(numberOfPlayers);
    }

    // 2. Назначение игроков - в Main

    // 3. Назначение доверия
    private void initializePlayers(List<Player> playersList) {
        //Первую роль из перемешанной стопки - реальному игроку
        /*Player realPlayer = new Player(realPlayerName, availableRoles.removeFirst(), false);
        playersList.add(realPlayer);

        //Создание ботов
        for (int i = 1; i < numberOfPlayers; i++) {
            Role botRole = availableRoles.removeFirst();
            Player bot = new Player("Bot " + i, botRole, true);
            playersList.add(bot);
        }*/

        //3. Назначение начального доверия игрокам
        this.playersList = playersList;

        // Инициализируем доверие
        trustInitializer();
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


    public Role getRoleForNewPlayer() {
        return availableRoles.removeFirst();
    }

    public void trustInitializer() {
        for (Player p : playersList) {
            for (Player other : playersList) {
                if (!p.equals(other)) {
                    p.getTrustLevels().put(other, 0.0); // Стартовое доверие нейтральное
                }
            }
        }
    }



    public void startDay() { context.newDay(); }
    public void startNight() { context.newNight(); }

    /// НОЧЬ

    private Player possibleVictim;

    // Ночное голосование МАФИИ
    public void mafiaVote(/*GameContext context*/) {
        Map<Player, Integer> potentialVictims = new HashMap<>();

        //Собираем статистику по голосованию, БОЛЬШИЙ ВЕС У ЧЕЛОВЕКА
        for (Player mafiosi : context.getAliveMafia()) {
            Player victim = mafiosi.getPlayerRole().nightAction(mafiosi, context.getAliveCivilian());
            if (!mafiosi.isBot())
                potentialVictims.merge(victim, 2, Integer::sum);
            else //может и задержку для ботов прописать?
                potentialVictims.merge(victim, 1, Integer::sum);
        }

        // Теперь выбор мафии проверяется:
        // либо устраняют того, за кого проголосовало большинство,
        // либо определяет рандом
        Player victim = getMostVotedVictim(potentialVictims);

        possibleVictim = victim; //Доктор его может сделать null
        this.kill(victim);
    }

    // Ночное патрулирование ШЕРИФА
    // Исключение если он мертв надо обрабатывать...
    public void sheriffCheck(/*GameContext context*/) {
        Player sheriff = context.getAliveSheriff();
        Player target = sheriff.getPlayerRole().nightAction(sheriff, context.getAlivePlayers());

        //Теперь он должен понять, мафия это или нет
        if (target.getPlayerRole() instanceof Mafia)
            sheriff.getTrustLevels().put(target, -0.8);
        else
            sheriff.getTrustLevels().put(target, 0.8);
    }

    /// ДЕНЬ

    // Объявление об убийстве, иначе - ДОБРОЕ УТРО СТРАНА!
    public String announcement() {
        String announce;
        if (possibleVictim != null)
            announce = ("EXTRA: " + possibleVictim.getPlayerName() + " was killed last night. RIP...");
        else
            announce = ("Good morning! It was a peaceful night. For now...");

        possibleVictim = null;
        return announce;
    }

    //Обсуждение днем случившегося ПЕРЕД голосованием
    public void discussion(/*GameContext context*/) {
        for (Player p : context.getAlivePlayers()) {

        }
    }

    // Дневное голосование ВСЕХ ЖИВЫХ ИГРОКОВ
    public void vote(/*GameContext context*/) {
        Map<Player, Integer> potentialSuspects = new HashMap<>();

        for (Player player : context.getAlivePlayers()) {
            Player suspect = player.getPlayerRole().dayVote(player, context.getAliveCivilian());
            potentialSuspects.merge(suspect, 1, Integer::sum);
        }

        // Теперь выбор городских проверяется:
        // либо устраняют того, за кого проголосовало большинство,
        // либо повторное голосование (но ПОКА что рандом)
        Player victim = getMostVotedVictim(potentialSuspects);

        this.kill(victim);
        //Сообщение надо бы вывести что ль...
    }

    // Применяется как после голосования днем, так и после голосования мафии ночью
    private void kill(Player victim) {
        victim.kill();
    }

    private Player getMostVotedVictim(Map<Player, Integer> potentialVictims) {
        int maxVotes = potentialVictims.values().stream().max(Integer::compareTo).orElse(0);
        // Список всех кандидатов с этим числом голосов
        List<Player> topCandidates = potentialVictims.entrySet().stream()
                .filter(entry -> entry.getValue() == maxVotes)
                .map(Map.Entry::getKey)
                .toList();
        // Если несколько — выбираем случайно
        return topCandidates.get(ThreadLocalRandom.current().nextInt(topCandidates.size()));
    }

    private Player getMostVotedSuspect(Map<Player, Integer> potentialSuspects) {
        int maxVotes = potentialSuspects.values().stream().max(Integer::compareTo).orElse(0);
        List<Player> topCandidates = potentialSuspects.entrySet().stream()
                .filter(entry -> entry.getValue() == maxVotes)
                .map(Map.Entry::getKey)
                .toList();

        if (topCandidates.size() == 1)
            return topCandidates.removeFirst();
        else {
            // Исключение и повторное голосование?
        }
    }

    // Проверка: убито/посажено достаточное количество игроков для завершения игры
    public boolean isGameOver() {
        return context.getAliveMafia().size() >= context.getAliveCivilian().size();
    }
}
