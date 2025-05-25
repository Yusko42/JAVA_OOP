package ru.nsu.fit.yus.mafia.model;

import ru.nsu.fit.yus.mafia.EventType;
import ru.nsu.fit.yus.mafia.Observable;
import ru.nsu.fit.yus.mafia.Observer;
import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.LastWordType;
import ru.nsu.fit.yus.mafia.model.messages.Message;
import ru.nsu.fit.yus.mafia.model.messages.MessageType;
import ru.nsu.fit.yus.mafia.model.roles.Civilian;
import ru.nsu.fit.yus.mafia.model.roles.Mafia;
import ru.nsu.fit.yus.mafia.model.roles.Role;
import ru.nsu.fit.yus.mafia.model.roles.Sheriff;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Model implements Observable {
    private List<Player> playersList = new ArrayList<>();
    private GameContext context;

    private List<Role> availableRoles;
    private int voteCount = 0;

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
    }

    /// Сколько мафиози, в каком (случайном) порядке - всё здесь
    private List<Role> createRoleList(int numberOfPlayers) {
        List<Role> availableRoles = new ArrayList<>();

        // Примерные пропорции: 1 шериф, 2-3 мафиози, остальные мирные
        int mafiaCount = Math.min(3, numberOfPlayers / 3);
        int sheriffCount = 1;
        int civilianCount = numberOfPlayers - mafiaCount - sheriffCount;

        for (int i = 0; i < sheriffCount; i++) availableRoles.add(new Sheriff(mafiaCount));
        for (int i = 0; i < mafiaCount; i++) availableRoles.add(new Mafia());
        for (int i = 0; i < civilianCount; i++) availableRoles.add(new Civilian());

        Collections.shuffle(availableRoles);
        return availableRoles;
    }


    public Role getRoleForNewPlayer() {
        if (availableRoles.isEmpty()) {
            throw new IllegalStateException("No roles left to assign."); // На перспективу...
        }
        return availableRoles.removeFirst();

    }

    public void trustInitializer() {
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



    public void startDay() {
        context.newDay();
        notifyObservers(EventType.DAY_STARTED, null);
    }
    public void startNight() {
        context.newNight();
        notifyObservers(EventType.NIGHT_STARTED, null);
    }

    /// НОЧЬ

    private Player possibleVictim;

    // Ночное голосование МАФИИ
    public void mafiaVote(/*GameContext context*/) {
        Map<Player, Integer> potentialVictims = new HashMap<>();

        //Собираем статистику по голосованию, БОЛЬШИЙ ВЕС У ЧЕЛОВЕКА
        for (Player mafiosi : context.getAliveMafia()) {
            Player victim = mafiosi.getPlayerRole().nightAction(mafiosi, context.getAliveCivilian());

            // Каждый член мафии высказывает мнение
            Map<String, Object> mes = new HashMap<>();
            mes.put("player", mafiosi.getPlayerName());
            mes.put("message", victim.getPlayerName());
            notifyObservers(EventType.PLAYER_SPOKEN, mes);


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
        Player target = sheriff.getPlayerRole().nightAction(sheriff, context.getAlivePlayersExcept(sheriff));

        //Теперь он должен понять, мафия это или нет
        if (target.getPlayerRole().isMafia())
            sheriff.getTrustLevels().put(target, -2.0);
        else
            sheriff.getTrustLevels().put(target, 2.0);
    }

    public void doctorCheck() {
        Player doctor = context.getAliveDoctor();
        Player target = doctor.getPlayerRole().nightAction(doctor, context.getAlivePlayersExcept(doctor));

        // Cures the target
        target.heal();
        if (possibleVictim == target)
            possibleVictim = null;
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
            mes.put("message", "Good morning! It was a peaceful night. For now...");
            notifyObservers(EventType.SHOW_MESSAGE, mes);
        }
    }

    public void lastWordOfTheMurdered() {
        // Последнее слово жертвы мафии
        if (possibleVictim != null) {
            LastWord last_mes = possibleVictim.getPlayerRole().dayLastWord(possibleVictim, context);

            if (last_mes.getLastWordType() == LastWordType.NAME_THE_KILLER) {
                Player suspect = last_mes.getSuspect();

                for (Player player : context.getAlivePlayersExcept(suspect)) {
                    // Если игрок - шериф и он знает подозреваемого, его константные значения доверия не меняем!
                    if (player.getPlayerRole().isSheriff()
                            && player.getPlayerRole().ignoresTrustShift(player, suspect, context)) {
                        continue;
                        // Рейтинг к коллегам в мафии тоже не меняем
                    } else if ((player.getPlayerRole().isMafia())
                        && (player.getTrustLevels().get(suspect) == 2.0)) {
                        continue;
                    } else { // Иначе
                        player.getTrustLevels()
                                .put(suspect, Math.max(-1.0, suspect.getTrustLevels().get(suspect) - 0.08));
                    }
                }

            }

            //Логруем жертвы в отдельном Map
            context.getLastWordLog().put(context.getNumberOfStage(), last_mes);

        }
        possibleVictim = null;
    }

    //Обсуждение днем случившегося ПЕРЕД голосованием
    public void discussion() {
        context.getMessageLog().put(context.getNumberOfStage(), new ArrayList<>()); // Новый список сообщений на день
        for (Player player : context.getAlivePlayers()) {
            Message mes = player.getPlayerRole().dayDiscussion(player, context);

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
                        List<Player> mafiaMembers = context.getAliveMafia();
                        for (Player mafia : mafiaMembers)
                            mafia.getTrustLevels().put(author, Math.max(-1.0, mafia.getTrustLevels().get(author) - 0.2));
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
                Player author = mes.getAuthor(); // Шериф??
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
                                mafia.getTrustLevels().put(author, Math.max(-1.0, mafia.getTrustLevels().get(author) - 0.3));
                        }
                    }
                } else { //Мирный поймёт ложь
                    target.getTrustLevels().put(author, Math.max(-1.0, target.getTrustLevels().get(author) - 0.8));
                }
            }

            context.getMessageLog().get(context.getNumberOfStage()).add(mes);
        }
    }

    // Дневное голосование ВСЕХ ЖИВЫХ ИГРОКОВ
    public void vote() {
        Map<Player, Integer> potentialSuspects = new HashMap<>();

        for (Player player : context.getAlivePlayers()) {
            Player suspect = player.getPlayerRole().dayVote(player, context.getAlivePlayersExcept(player));
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
        else if (voteCount < 3) {
            voteCount++;
            throw new RuntimeException("NEW VOTE FOT THR KILLER!");
        } else {
            throw new RuntimeException("No one can be imprisoned!");
        }
    }

    // Часть, отвечающая за отправку данных подписчикам

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

    // Проверка: убито/посажено достаточное количество игроков для завершения игры
    public boolean isMafiaWon() {
        boolean result = false;
        if (context.getAliveMafia().size() >= context.getAliveCivilian().size()) {
            Map<String, Object> data = new HashMap<>();
            data.put("winner", "Mafia");
            notifyObservers(EventType.GAME_ENDED, data);
            result = context.getAliveMafia().size() >= context.getAliveCivilian().size();
        }
        return result;
    }

    public boolean isCiviliansWon() {
        boolean result = false;
        if (context.getAliveMafia().isEmpty()) {
            Map<String, Object> data = new HashMap<>();
            data.put("winner", "Civilians");
            notifyObservers(EventType.GAME_ENDED, data);
            result = context.getAliveMafia().isEmpty();
        }
        return result;
    }

    public int getMaximumNumberOfPlayers() {
        return 10;
    }
}
