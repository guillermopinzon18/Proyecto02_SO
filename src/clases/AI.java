/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package clases;

import ui.MainInterfaz;

/**
 *
 * @author Guillermo
 */
public class AI {

    public static int time = 10;
    private static final double WIN_PROBABILITY = 0.4;
    private static final double DRAW_PROBABILITY = 0.27;
    private static final double NO_COMBAT_PROBABILITY = 0.33;
    public static int ganadoresAvatar = 0;
    public static int ganadoresSM = 0;

    public void processBattle(Character avatarCharacter, Character showMasCharacter) {

        MainInterfaz.setDefStats();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double result = Math.random();

        // Utilizamos los puntos únicos para determinar el resultado
        int avatarPoints = avatarCharacter.getUniquePoints();
        int showMasPoints = showMasCharacter.getUniquePoints();

        // Setear el nombre imagen y puntos unicos de los personajes a pelear.
        MainInterfaz.setZeldaIcon(avatarCharacter.getName(), avatarCharacter.getUniquePoints());
        MainInterfaz.setSFIcon(showMasCharacter.getName(), showMasCharacter.getUniquePoints());
        try {
            if (Admin.roundCounter < 1) {

            } else {
                MainInterfaz.AIState.setText("¡PELEANDO!");
            }
            Thread.sleep(1000 * time); //DECIDIENDO GANADOR
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result < WIN_PROBABILITY) {
            if (avatarPoints > showMasPoints) {
                handleWinnerAvatar(avatarCharacter, showMasCharacter, avatarPoints, showMasPoints);
                MainInterfaz.setCoronaPositionAvatar(MainInterfaz.AvatarIMG);
                ganadoresAvatar++;
                MainInterfaz.marcadorZelda(ganadoresAvatar);
            } else {
                handleWinnerShowMas(avatarCharacter, showMasCharacter, avatarPoints, showMasPoints);
                MainInterfaz.setCoronaPositionSM(MainInterfaz.ShowMasIMG);
                ganadoresSM++;
                MainInterfaz.marcadorSF(ganadoresSM);
            }
        } else if (result < WIN_PROBABILITY + DRAW_PROBABILITY) {
            handleDraw(avatarCharacter, showMasCharacter);
            System.out.println("EMPATE");
            MainInterfaz.setEmpateVisible();
        } else {
            handleNoCombat(avatarCharacter, showMasCharacter);
            MainInterfaz.setCancelVisible(); 
            System.out.println("CANCELADO");
        }
        // Después del combate, probabilidad del 40% de mover un personaje de refuerzo a la cola de prioridad 1
        if (Admin.shouldMoveToPriority1()) {
            Admin.moveCharacterToPriority(Admin.reinforcementQueueNickelodeon, Admin.nickelodeonQueue1);
        }

        if (Admin.shouldMoveToPriority1()) {
            Admin.moveCharacterToPriority(Admin.reinforcementQueueCartoonN, Admin.cartoonNQueue1);
        }
        Admin.incrementRoundCounters(); // Incrementa los contadores de todas las colas
        Character.removeFromQueueN(Admin.nickelodeonQueue2, Admin.nickelodeonQueue3);
        Character.removeFromQueueCN(Admin.cartoonNQueue2, Admin.cartoonNQueue3);
        //Generador de personajes (80% de probabilidad)
        Admin.roundCounter++;
        if (Admin.roundCounter % 2 == 0) { // Se han completado dos rondas
            if (Admin.shouldGenerateCharacters()) {
                Admin.generateCharacters();
                System.out.println("SE GENERARON 2 PERSONAJES NUEVOS!");
            }
        }

        try {
//            MainInterfaz.AIState.setText("RESULTADO");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MainInterfaz.AIState.setText("ESPERANDO");
        MainInterfaz.coronaLabelSM.setVisible(false);
        MainInterfaz.coronaLabelAvatar.setVisible(false);
        MainInterfaz.empateLabel.setVisible(false);
        MainInterfaz.cancelLabel.setVisible(false);
    }

    private void handleWinnerAvatar(Character winner, Character loser, int winnerPoints, int loserPoints) {
        System.out.println("¡Combate terminado! El ganador es: " + winner.getName()
                + " (ID: " + winner.getId() + ") con " + winnerPoints + " puntos únicos.");
        // logica para añadir a la lista de ganadores
        MainInterfaz.agregarGanador(winner.getName()+winner.getId());
    }

    private void handleWinnerShowMas(Character loser, Character winner, int loserPoints, int winnerPoints) {
        System.out.println("¡Combate terminado! El ganador es: " + winner.getName()
                + " (ID: " + winner.getId() + ") con " + winnerPoints + " puntos únicos.");
        // logica para añadir a la lista de ganadores
        MainInterfaz.agregarGanador(winner.getName()+winner.getId());
    }

    private void handleDraw(Character character1, Character character2) {
        System.out.println("¡Combate empatado! Ambos personajes vuelven a la cola de prioridad 1.");
        // logica para que vulevan a la cola de prioridad 1
        Admin.nickelodeonQueue1.enqueue(character1);
        Admin.cartoonNQueue1.enqueue(character2);
    }

    private void handleNoCombat(Character character1, Character character2) {
        System.out.println("No puede llevarse a cabo el combate. Ambos personajes van a la cola de refuerzo.");
        // Se agregan en la cola de refuerzo
        Admin.reinforcementQueueNickelodeon.enqueue(character1);
        Admin.reinforcementQueueCartoonN.enqueue(character2);
    }
}

