package ru.nsu.fit.yus.mafia.console.controller;

import ru.nsu.fit.yus.mafia.model.Model;

//Простая накидочка того, что должно здесь быть

public class Controller {
    private Model model = new Model(8, "Daniel"); //ОБЯЗАТЕЛЬНО В МЕЙН!
    public void game(){
        while (true) {
            model.mafiaVote();
            model.sheriffCheck();

            model.startDay();
            model.announcement();
            // Если мафия устранила достаточное кол-во игроков
            if (model.isGameOver()) { break; }
            model.discussion();
            model.vote();

            // Если избавились от мафии совсем
            if (model.isGameOver()) { break; }
            model.startNight();
        }

    }
}
