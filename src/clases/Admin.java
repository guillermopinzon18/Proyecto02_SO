/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.util.HashSet;
import java.util.Random;
import javax.swing.JLabel;
import ui.MainInterfaz;

/**
 *
 * @author Guillermo
 */
public class Admin {

    public static int roundCounter = 0;
    public static Queue<Character> nickelodeonQueue1; // Colas para Nickelodeon 
    public static Queue<Character> nickelodeonQueue2;
    public static Queue<Character> nickelodeonQueue3;
    public static Queue<Character> cartoonNQueue1; // Colas para cartoon network
    public static Queue<Character> cartoonNQueue2;
    public static Queue<Character> cartoonNQueue3;
    public static Queue<Character> reinforcementQueueNickelodeon; // Cola de refuerzo para Nickelodeon
    public static Queue<Character> reinforcementQueueCartoonN; // Cola de refuerzo para cartoon network

    public Admin() {
        // Inicializa las colas para Nickelodeon
        nickelodeonQueue1 = new Queue<>();
        nickelodeonQueue2 = new Queue<>();
        nickelodeonQueue3 = new Queue<>();

        // Inicializa las colas para cartoon network
        cartoonNQueue1 = new Queue<>();
        cartoonNQueue2 = new Queue<>();
        cartoonNQueue3 = new Queue<>();

        // Inicializa las colas de refuerzo
        reinforcementQueueNickelodeon = new Queue<>();
        reinforcementQueueCartoonN = new Queue<>();
    }

    public void initializeCharacters() {
        for (int i = 0; i < 10; i++) {
            
            Character nickelodeonCharacter = Character.createAvatarCharacter(); 
            Character cartoonNCharacter = Character.createShowMasCharacter();

            addToQueue(nickelodeonCharacter, nickelodeonQueue1, nickelodeonQueue2, nickelodeonQueue3);
            addToQueue(cartoonNCharacter, cartoonNQueue1, cartoonNQueue2, cartoonNQueue3);
        }
    }

    public static void addToQueue(Character character, Queue<Character>... queues) {
        int priorityIndex = character.getLevelPriority() - 1;
        queues[priorityIndex].enqueue(character);
    }

    public static void upgradeToQueue(Character character, Queue<Character>... queues) {

        int NewpriorityIndex = character.getLevelPriority() - 1;
        int priorityIndex = character.getLevelPriority();
        // Eliminar el personaje de las colas existentes (si está presente)
        
        queues[NewpriorityIndex].enqueue(character);

    }

    public Character[] selectCharactersForBattle() {
        Character[] charactersForBattle = new Character[2];

        charactersForBattle[0] = selectCharacterFromNextNonEmptyQueue(nickelodeonQueue1, nickelodeonQueue2, nickelodeonQueue3);
        charactersForBattle[1] = selectCharacterFromNextNonEmptyQueue(cartoonNQueue1, cartoonNQueue2, cartoonNQueue3);

        return charactersForBattle;

    }

    private Character selectCharacterFromNextNonEmptyQueue(Queue<Character>... queues) {
        for (int i = 0; i < queues.length; i++) {
            if (!queues[i].isEmpty()) {
                return queues[i].dequeue();
            }
        }
        return null; // Si todas las colas están vacías
    }

    // Ya no lo uso para selecionar los personajes de las colas.
    private Character selectCharacterFromQueue(int queueIndex, Queue<Character>... queues) {
        return queues[queueIndex].isEmpty() ? null : queues[queueIndex].dequeue();
    }

    public static void moveCharacterToPriority(Queue<Character> reinforcementQueue, Queue<Character> priorityQueue) {
        if (!reinforcementQueue.isEmpty()) {
            Character character = reinforcementQueue.dequeue();
            priorityQueue.enqueue(character);
        }
    }

// Sube los personajes en la posicion 1 de las colas. 
    public void updateQueues() {
        for (int i = 1; i < 3; i++) {
            Character nickelodeonCharacter = selectCharacterFromQueue(i, nickelodeonQueue1, nickelodeonQueue2, nickelodeonQueue3);
            if (nickelodeonCharacter != null) {
                switch (i) {
                    case 1:
                        nickelodeonQueue1.enqueue(nickelodeonCharacter);
                        break;
                    case 2:
                        nickelodeonQueue2.enqueue(nickelodeonCharacter);
                        break;
                    case 3:
                        nickelodeonQueue3.enqueue(nickelodeonCharacter);
                        break;
                }
            }

            Character cartoonNCharacter = selectCharacterFromQueue(i, cartoonNQueue1, cartoonNQueue2, cartoonNQueue3);
            if (cartoonNCharacter != null) {
                switch (i) {
                    case 1:
                        cartoonNQueue1.enqueue(cartoonNCharacter);
                        break;
                    case 2:
                        cartoonNQueue2.enqueue(cartoonNCharacter);
                        break;
                    case 3:
                        cartoonNQueue3.enqueue(cartoonNCharacter);
                        break;
                }
            }
        }
    }

    public static boolean shouldGenerateCharacters() {
        // Devolver true con una probabilidad del 80% (Para generar personajes)
        return new Random().nextInt(100) < 80;
    }

    public static void generateCharacters() {
        // CAMBIAR LO DE Character zelda y SF
        Character nickelodeonCharacter = Character.createAvatarCharacter();
        Character cartoonNCharacter = Character.createShowMasCharacter();

        addToQueue(nickelodeonCharacter, nickelodeonQueue1, nickelodeonQueue2, nickelodeonQueue3);
        addToQueue(cartoonNCharacter, cartoonNQueue1, cartoonNQueue2, cartoonNQueue3);
    }

    public static boolean shouldMoveToPriority1() {
        // Devuelve true con un 40% de probabilidad (para sacar de la cola de refuerzo)
        return new Random().nextInt(100) < 40;
    }
    //Lógica para aumentar contadores de Characters

    public static void incrementRoundCounters() {
        Queue<Character>[] allQueues = new Queue[]{
            nickelodeonQueue1, nickelodeonQueue2, nickelodeonQueue3,
            cartoonNQueue1, cartoonNQueue2, cartoonNQueue3
        };

        for (Queue<Character> queue : allQueues) {
            incrementQueueRoundCounters(queue);
        }
    }
// Lógica para aumentar el nivel de prioridad (Subir de Queue)

    private static void incrementQueueRoundCounters(Queue<Character> queue) {
        Queue<Character> tempQueue = new Queue<>();
        while (!queue.isEmpty()) {
            Character character = queue.dequeue();
            character.increaseRoundCounter();
            tempQueue.enqueue(character);
        }
        while (!tempQueue.isEmpty()) {
            queue.enqueue(tempQueue.dequeue());
        }
    }

    // Método para actualizar las colas en los JLabel
    public static void actualizarColasEnInterfaz() {
        // Para Nickelodeon
        for (int i = 0; i < 4; i++) {
            StringBuilder resultadoAvatar = new StringBuilder();
            JLabel colasAvatar = MainInterfaz.getColasZelda(i + 1);

            if (colasAvatar != null) {
                Queue<Character> currentQueue;

                // Determinar la cola actual según el índice
                if (i < 3) {
                    resultadoAvatar.append("Nickelodeon Queue " + (i + 1) + ":\n");
                    currentQueue = getNickelodeonQueue(i + 1);
                } else {
                    resultadoAvatar.append("Reinforcement Queue Nickelodeon:\n");
                    currentQueue = reinforcementQueueNickelodeon;
                }

                resultadoAvatar.append(printQueueToString(currentQueue));
                colasAvatar.setText(resultadoAvatar.toString());
            }
        }

        // Para Cartoon Network
        for (int i = 0; i < 4; i++) {
            StringBuilder resultadoSM = new StringBuilder();
            JLabel colasSM = MainInterfaz.getColasSF(i + 1);

            if (colasSM != null) {
                Queue<Character> currentQueue;

                // Determinar la cola actual según el índice
                if (i < 3) {
                    resultadoSM.append("Cartoon Network Queue " + (i + 1) + ":\n");
                    currentQueue = getCartoonNQueue(i + 1);
                } else {
                    resultadoSM.append("Reinforcement Queue Cartoon Network:\n");
                    currentQueue = reinforcementQueueCartoonN;
                }

                resultadoSM.append(printQueueToString(currentQueue));
                colasSM.setText(resultadoSM.toString());
            }
        }
    }

    private static Queue<Character> getNickelodeonQueue(int index) {
        switch (index) {
            case 1:
                return nickelodeonQueue1;
            case 2:
                return nickelodeonQueue2;
            case 3:
                return nickelodeonQueue3;
            case 4:
                return reinforcementQueueNickelodeon;
            default:
                return null;
        }
    }

    private static Queue<Character> getCartoonNQueue(int index) {
        switch (index) {
            case 1:
                return cartoonNQueue1;
            case 2:
                return cartoonNQueue2;
            case 3:
                return cartoonNQueue3;
            case 4:
                return reinforcementQueueCartoonN;
            default:
                return null;
        }
    }
  
    private static <T> String printQueuesToString(Queue<T>... queues) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < queues.length; i++) {
            result.append("Queue ").append(i + 1).append(": ");
            result.append(printQueueToString(queues[i]));
        }
        return result.toString();
    }

    private static <T> String printQueueToString(Queue<T> queue) {
        Queue<T> tempQueue = new Queue<>();
        StringBuilder result = new StringBuilder();
        while (!queue.isEmpty()) {
            T data = queue.dequeue();
            result.append(data).append(" ");
            tempQueue.enqueue(data);
        }
        result.append("\n");
        while (!tempQueue.isEmpty()) {
            queue.enqueue(tempQueue.dequeue());
        }
        return result.toString();
    }

    public void printQueues() {
        System.out.println("Nickelodeon Queues:");
        printQueues(nickelodeonQueue1, nickelodeonQueue2, nickelodeonQueue3);

        System.out.println("\nReinforcement Queue Nickelodeon:");
        printQueue(reinforcementQueueNickelodeon);

        System.out.println("\nCartoon Network Queues:");
        printQueues(cartoonNQueue1, cartoonNQueue2, cartoonNQueue3);

        System.out.println("\nReinforcement Queue Cartoon Network :");
        printQueue(reinforcementQueueCartoonN);
    }

    private <T> void printQueues(Queue<T>... queues) {
        for (int i = 0; i < queues.length; i++) {
            System.out.print("Queue " + (i + 1) + ": ");
            printQueue(queues[i]);
        }
    }

    private <T> void printQueue(Queue<T> queue) {
        Queue<T> tempQueue = new Queue<>();
        while (!queue.isEmpty()) {
            T data = queue.dequeue();
            System.out.print(data + " ");
            tempQueue.enqueue(data);
        }
        System.out.println();
        while (!tempQueue.isEmpty()) {
            queue.enqueue(tempQueue.dequeue());
        }
    }
}
