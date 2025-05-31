package ru.nsu.fit.yus.mafia.model;

import ru.nsu.fit.yus.mafia.EventType;
import ru.nsu.fit.yus.mafia.Observable;
import ru.nsu.fit.yus.mafia.Observer;
import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.LastWordType;
import ru.nsu.fit.yus.mafia.model.messages.Message;
import ru.nsu.fit.yus.mafia.model.messages.MessageType;
import ru.nsu.fit.yus.mafia.model.roles.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Model implements Observable {
    private List<Player> playersList = new ArrayList<>();
    private List<Role> availableRoles;
    private GameContext context;
    //private int voteCount = 0;

    // 1. Создание списка ролей
    public void generateAvailableRoles(int numberOfPlayers) {
        availableRoles = createRoleList(numberOfPlayers);
    }

    public void initializePlayersAndContext(List<Player> playersList) {
        // 2. Назначение игроков - в Main
        this.playersList = playersList;

        // 3. Назначение начального доверия игрокам
        trustInitializer();

        // 4. Создание контекста
        context = new GameContext(this.playersList);

        // 5. Старт, вывод на экран всех игроков
        notifyObservers(EventType.GAME_STARTED, null);
        showAllLivingPlayers();
        delay(5000);
    }

    /// Сколько мафиози, в каком (случайном) порядке - всё здесь
    private List<Role> createRoleList(int numberOfPlayers) {
        List<Role> availableRoles = new ArrayList<>();

        // Примерные пропорции: 1 шериф, 2-3 мафиози, остальные мирные
        int mafiaCount = Math.min(3, numberOfPlayers / 3);
        int sheriffCount = 1;
        int doctorCount = 1;
        int civilianCount = numberOfPlayers - (mafiaCount + sheriffCount + doctorCount);

        for (int i = 0; i < sheriffCount; i++) availableRoles.add(new Sheriff(mafiaCount));
        for (int i = 0; i < mafiaCount; i++) availableRoles.add(new Mafia());
        for (int i = 0; i < doctorCount; i++) availableRoles.add(new Doctor());
        for (int i = 0; i < civilianCount; i++) availableRoles.add(new Civilian());

        Collections.shuffle(availableRoles);
        return availableRoles;
    }


    public Role getRoleForNewPlayer() {
        if (availableRoles.isEmpty()) {
            throw new IllegalStateException("No roles left to assign."); // На перспективу...
        }
        return availableRoles.remove(0);
    }

    private void trustInitializer() {
        for (Player p : playersList) {
            for (Player other : playersList) {
                // У мафиози - зафиксированные значения доверия!
                if (p.getPlayerRole().isMafia() && other.getPlayerRole().isMafia()) {
                    p.getTrustLevels().put(other, 2.0);
                }
                if (!p.equals(other)) {
                    p.getTrustLevels().put(other, 0.0); // Стартовое доверие нейтральное
                }
            }
        }
    }

    //Можно так вывести и роль, и его цель, и способности...

    public void playerRoleReveal(Observer ob, String playerName, String playerRole) {
        // Вывод роли игрока
        Map<String, Object> mes = new HashMap<>();
        mes.put("player", playerName);
        mes.put("role", playerRole);
        notifyConcreteObserver(ob, EventType.PLAYER_ROLE_REVEALED, mes);
        delay(3000);
    }

    // Обновляем список живых игроков, выводим его всем
    private void showAllLivingPlayers() {
        Map<String, Object> mes = new HashMap<>();
        List<String> playersNames = new ArrayList<>();
        for (Player p : context.getAlivePlayers()) {
            playersNames.add(p.getPlayerName());
        }

        mes.put("players", playersNames);
        notifyObservers(EventType.SHOW_LIVING_PLAYERS, mes);
    }

    private void showAllLivingMafia() {
        Map<String, Object> mes = new HashMap<>();
        List<String> mafiaNames = new ArrayList<>();
        for (Player p : context.getAliveMafia()) {
            mafiaNames.add(p.getPlayerName());
        }
        mes.put("members", mafiaNames);
        notifyObservers(EventType.SHOW_LIVING_MAFIA, mes);
    }

    private boolean showLivingSheriff() {
        Map<String, Object> mes = new HashMap<>();
        Player player = context.getAliveSheriff();
        if (player == null)
            return false;
        String playersName = player.getPlayerName();

        mes.put("sheriff", playersName);
        notifyObservers(EventType.SHOW_LIVING_SHERIFF, mes);
        return true;
    }

    private boolean showLivingDoctor() {
        Map<String, Object> mes = new HashMap<>();
        Player player = context.getAliveDoctor();
        if (player == null)
            return false;
        String playersName = player.getPlayerName();

        mes.put("doctor", playersName);
        notifyObservers(EventType.SHOW_LIVING_DOCTOR, mes);
        return true;
    }

    private void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public void startDay() {
        context.newDay();
        notifyObservers(EventType.DAY_STARTED, null);
        delay(1000);
    }
    public void startNight() {
        context.newNight();
        notifyObservers(EventType.NIGHT_STARTED, null);
        delay(1000);
    }

    /// НОЧЬ

    private Player possibleVictim;

    // Ночное голосование МАФИИ
    public void mafiaVote() {
        showAllLivingMafia();
        delay(5000);

        Map<Player, Integer> potentialVictims = new HashMap<>();

        //Собираем статистику по голосованию, БОЛЬШИЙ ВЕС У ЧЕЛОВЕКА
        for (Player mafiosi : context.getAliveMafia()) {
            Player victim = mafiosi.getPlayerRole().nightAction(mafiosi, context.getAliveCivilian());

            // Каждый член мафии высказывает мнение
            // Должно быть видно ТОЛЬКО ЧЛЕНАМ МАФИИ
            Map<String, Object> mes = new HashMap<>();
            mes.put("voter", mafiosi.getPlayerName());
            mes.put("voted", victim.getPlayerName());
            notifyObservers(EventType.MAFIA_VOTED, mes);
            delay(1000);


            if (!mafiosi.isBot())
                potentialVictims.merge(victim, 2, Integer::sum);
            else //может и задержку для ботов прописать?
                potentialVictims.merge(victim, 1, Integer::sum);
        }

        // Теперь выбор мафии проверяется:
        // либо устраняют того, за кого проголосовало большинство,
        // либо определяет рандом
        possibleVictim = getMostVotedVictim(potentialVictims);
        notifyObservers(EventType.VICTIM_CHOSEN, Map.of("victim", possibleVictim.getPlayerName()));
    }

    // Ночное патрулирование ШЕРИФА
    // Исключение если он мертв надо обрабатывать...
    public void sheriffCheck(/*GameContext context*/) {
        if (!showLivingSheriff()) {
            return;
        }
        delay(1000);

        Player sheriff = context.getAliveSheriff();
        Player target = sheriff.getPlayerRole().nightAction(sheriff, context.getAlivePlayersExcept(sheriff));

        notifyObservers(EventType.SHERIFF_CHECK, Map.of(
                "target", target.getPlayerName(),
                "isMafia", target.getPlayerRole().isMafia()
        ));
        delay(2000);

        //Теперь он должен понять, мафия это или нет
        if (target.getPlayerRole().isMafia())
            sheriff.getTrustLevels().put(target, -2.0);
        else
            sheriff.getTrustLevels().put(target, 2.0);
    }

    public void doctorCheck() {
        if (!showLivingDoctor()) {
            return;
        }
        delay(1000);

        Player doctor = context.getAliveDoctor();
        Player target = doctor.getPlayerRole().nightAction(doctor, context.getAlivePlayersExcept(doctor));

        // Cures the target
        target.heal();
        if (possibleVictim == target)
            possibleVictim = null;
        notifyObservers(EventType.PATIENT_CHOSEN, Map.of("patient", target.getPlayerName()));
        delay(2000);
    }

    public void killPossibleVictim() {
        if (possibleVictim != null) {
            this.kill(possibleVictim);
        }
    }

    /// ДЕНЬ

    // Объявление об убийстве, иначе - все хорошо!
    public void announcement() {
        Map<String, Object> mes = new HashMap<>();
        if (possibleVictim != null) {
            mes.put("player", possibleVictim.getPlayerName());
            notifyObservers(EventType.PLAYER_KILLED, mes);
        }
        else {
            mes.put("message", "Доброе утро! Сегодняшняя ночь была необыкновенно спокойной. Пока что...");
            notifyObservers(EventType.SHOW_MESSAGE, mes);
        }
        delay(3000);
        // Выводим живых игроков
        showAllLivingPlayers();
        delay(1000);
    }

    public void lastWordOfTheMurdered() {
        // Последнее слово жертвы мафии
        if (possibleVictim != null) {
            LastWord last_mes = possibleVictim.getPlayerRole().dayLastWord(possibleVictim, context);

            if (last_mes.getLastWordType() == LastWordType.NAME_THE_KILLER) {
                Player suspect = last_mes.getSuspect();

                for (Player player : context.getAlivePlayersExcept(suspect)) {
                    double currentTrust = player.getTrustLevels().getOrDefault(suspect, 0.0);

                    // Не трогаем мафий, которые доверяют своему с 2.0
                    if (player.getPlayerRole().isMafia() && suspect.getPlayerRole().isMafia()) {
                        continue;
                    }

                    // Не трогаем шерифа, если он уже нашёл мафию
                    if (player.getPlayerRole().isSheriff()
                            && player.getPlayerRole().ignoresTrustShift(player, suspect, context)) {
                        continue;
                    }

                    // Остальным уменьшаем доверие
                    player.getTrustLevels().put(suspect, Math.max(-1.0, currentTrust - 0.08));
                }

            }

            //Логруем жертвы в отдельном Map
            context.getLastWordLog().put(context.getNumberOfStage(), last_mes);

            notifyObservers(EventType.PLAYER_LAST_WORD, Map.of(
                    "player", possibleVictim.getPlayerName(),
                    "message", last_mes.getText()
            ));
            delay(2000);

        }
        possibleVictim = null;
    }

    //Обсуждение днем случившегося ПЕРЕД голосованием
    public void discussion() {
        context.getMessageLog().put(context.getNumberOfStage(), new ArrayList<>()); // Новый список сообщений на день
        for (Player player : context.getAlivePlayers()) {
            Message mes = player.getPlayerRole().dayDiscussion(player, context);

            // Вывод всем игрокам
            notifyObservers(EventType.PLAYER_SPOKEN, Map.of(
                    "player", player.getPlayerName(),
                    "message", mes.getText()
            ));

            //ВЛИЯНИЕ СООБЩЕНИЯ НА ДОВЕРИЕ ДРУГОГО ИГРОКА

            // ПОДОЗРЕНИЕ
            if (mes.getMessageType() == MessageType.SUSPICION) {
                Player author = mes.getAuthor();
                Player target = mes.getTarget();
                // Мафия отводит подозрения с себя таким образом. Даже у обвиняемого, может и так сойдёт
                if (target.getPlayerRole().isMafia()) {
                    if (author.getPlayerRole().isMafia()) {
                        continue;
                    } else {
                        List<Player> mafiaMembers = context.getAliveMafia();
                        for (Player mafia : mafiaMembers)
                            mafia.getTrustLevels().put(author, Math.min(1.0, mafia.getTrustLevels().get(author) + 0.06));
                    }
                } else { // Иначе рейтинг доверия к нему падает
                    if ((target.getPlayerRole().isSheriff()) &&
                            target.getPlayerRole().ignoresTrustShift(author, target, context)
                            /*&& (target.getTrustLevels().get(author) == -2.0
                            || target.getTrustLevels().get(author) == 2.0 )*/)
                        continue;
                    else {
                        target.getTrustLevels()
                                .put(author, Math.max(-1.0, target.getTrustLevels().get(author) - 0.08));
                        // МАФИЯ ГОТОВИТСЯ ПОДСТАВЛЯТЬ ОТВЕТЧИКА Б, УБИВАЯ А
                        if (!author.getPlayerRole().isMafia()) {
                            List<Player> mafiaMembers = context.getAliveMafia();
                            for (Player mafia : mafiaMembers) {
                                double currentTrust = mafia.getTrustLevels().getOrDefault(author, 0.0);
                                if (currentTrust == 2.0) continue;
                                mafia.getTrustLevels().put(author, Math.max(-1.0, currentTrust - 0.2));
                            }
                        }
                    }
                }
            }

            // ДОВЕРИЕ
            if (mes.getMessageType() == MessageType.SUPPORT) {
                Player author = mes.getAuthor();
                Player target = mes.getTarget();

                if (target.getPlayerRole().isMafia()) {
                    if (author.getPlayerRole().isMafia()) {
                        continue;
                    } else {
                        List<Player> mafiaMembers = context.getAliveMafia();
                        for (Player mafia : mafiaMembers)
                            mafia.getTrustLevels().put(author, Math.max(-1.0, mafia.getTrustLevels().get(author) - 0.1));
                    }
                } else {
                    if ((target.getPlayerRole().isSheriff())
                            && target.getPlayerRole().ignoresTrustShift(author, target, context)
                            /*&& (target.getTrustLevels().get(author) == -2.0
                            || target.getTrustLevels().get(author) == 2.0 )*/)
                        continue;
                    else
                        target.getTrustLevels()
                                .put(author, Math.min(1.0, target.getTrustLevels().get(author) + 0.06));
                }
            }

            // ШЕРИФ ДОЛОЖИЛ О НАЛИЧИИ МАФИИ
            if (mes.getMessageType() == MessageType.SHERIFF_MAFIA_REPORT) {
                Player author = mes.getAuthor();
                Player target = mes.getTarget();

                if (target.getPlayerRole().isMafia()) {
                    if (author.getPlayerRole().isMafia()) {
                        continue;
                    } else { // Буду устранять шерифа, пока не выдал всех (если остался один, то он постарается отвести подозрения)
                        List<Player> mafiaMembers = context.getAliveMafia();
                        if (mafiaMembers.size() > 1) {
                            target.getTrustLevels().put(author, Math.max(-1.0, target.getTrustLevels().get(author) - 0.07));
                        } else {
                            for (Player mafia : mafiaMembers)
                            {
                                double currentTrust = mafia.getTrustLevels().getOrDefault(author, 0.0);
                                if (currentTrust == 2.0) continue;
                                mafia.getTrustLevels().put(author, Math.max(-1.0, currentTrust - 0.3));
                            }
                        }
                    }
                } else { //Мирный поймёт ложь
                    target.getTrustLevels().put(author, Math.max(-1.0, target.getTrustLevels().get(author) - 0.8));
                }
            }
            delay(1500);
            context.getMessageLog().get(context.getNumberOfStage()).add(mes);
        }
    }

    // Дневное голосование ВСЕХ ЖИВЫХ ИГРОКОВ
    public void vote() {
        int attempts = 0;
        while (attempts < 3) {
            Map<Player, Integer> potentialSuspects = new HashMap<>();
            Player victim;
            for (Player player : context.getAlivePlayers()) {
                Player suspect = player.getPlayerRole().dayVote(player, context.getAlivePlayersExcept(player));
                potentialSuspects.merge(suspect, 1, Integer::sum);

                // Вывод
                notifyObservers(EventType.PLAYER_VOTED, Map.of(
                        "voter", player.getPlayerName(),
                        "voted", suspect.getPlayerName()
                ));
                delay(1500);
            }

            // Теперь выбор городских проверяется:
            // либо устраняют того, за кого проголосовало большинство,
            // либо повторное голосование
            victim = getMostVotedSuspect(potentialSuspects);
            if (victim != null) {
                this.kill(victim);
                notifyObservers(EventType.PLAYER_ELIMINATED, Map.of("player", victim.getPlayerName()));
                delay(1000);
                return;
            }

            attempts++;
            if (attempts < 3) {
                notifyObservers(EventType.NEW_VOTE, null); // запустить новое голосование
            }
        }
        notifyObservers(EventType.NOBODY_PRISONED, null);
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
        List<Player> topCandidates = new ArrayList<>(potentialSuspects.entrySet().stream()
                .filter(entry -> entry.getValue() == maxVotes)
                .map(Map.Entry::getKey)
                .toList());

        if (topCandidates.size() == 1)
            return topCandidates.remove(0);
        else
            return null;
    }

    // Проверка: убито/посажено достаточное количество игроков для завершения игры
    public boolean isMafiaWon() {
       return context.getAliveMafia().size() >= context.getAliveCivilian().size();
    }

    public boolean isCiviliansWon() {
        return context.getAliveMafia().isEmpty();
    }

    public void gameOver() {
        notifyObservers(EventType.GAME_OVER, null);
    }

    public void mafiaWon() {
        notifyObservers(EventType.GAME_ENDED, Map.of("winner", "Mafia"));
    }

    public void civiliansWon() {
        notifyObservers(EventType.GAME_ENDED, Map.of("winner", "Civilians"));
    }

    /// Часть, отвечающая за отправку данных подписчикам

    private final List<Observer> observerList = new ArrayList<>();

    public void addObserver(Observer observer) {
        observerList.add(observer);
    }
    public void removeObserver(Observer observer) {
        observerList.remove(observer);
    }
    public List<Observer> getObservers() {
        return observerList;
    }

    // Общаемся с подписчиками
    public void notifyObservers(EventType type, Map<String, Object> data) {
        for (Observer observer : observerList) {
            observer.onGameEvent(type, data);
        }
    }

    // Или с подписчиком
    public void notifyConcreteObserver(Observer observer, EventType type, Map<String, Object> data) {
        observer.onGameEvent(type, data);
    }
}
