package g62727.dev3.oxono.console;

import g62727.dev3.oxono.model.Game;

import java.util.Scanner;

public class AppConsole {
    private static Game model;
    private static Scanner input;
    public static void main(String[] args) {
        /*model = new Game();
        input = new Scanner(System.in);
        System.out.println("Jouons ensemble !");
        model.startGame();
        boolean game = true;
        while (game) {
            model.displayBoard();
            model.playTextMove();
            System.out.print("Choisissez le totem à déplacer (O/X), ou q pour abandonner : ");
            String totem = input.next().toUpperCase();

            while (!totem.equals("O") && !totem.equals("X") && !totem.equals("Q")) {
                System.out.print("Vous n'avez pas entré une commande valide, réessayez : ");
                totem =  input.next().toUpperCase();
            }

            if (totem.equals("Q")) {
                model.surrender();
                game = false;
            }

            if (totem.equals("O") || totem.equals("X")) {
                if (!model.canMoveTotem(totem)) {
                    System.out.println("Vous n'avez plus assez de jetons du même symbole pour joueur...");
                } else {
                    int[] rowCol;
                    boolean moved;
                    do {
                        rowCol = getValidCoords("Choisissez une position pour déplacer le totem !");
                        moved = model.moveTotem(model.findTotem(totem), rowCol[0], rowCol[1]);
                        if (!moved) {
                            System.out.println("Vous ne pouvez pas déplacer le totem ici... Réessayez !");
                        }
                    } while (!moved);

                    model.displayBoard();
                    model.playTextInsert();

                    int[] rowColToken;
                    boolean inserted;
                    do {
                        rowColToken = getValidCoords("Choisissez où le placer !");
                        inserted = model.insertToken(model.tokenToInsert(), rowColToken[0], rowColToken[1]);
                        if(!inserted) {
                            System.out.println("Vous ne pouvez pas placer votre jeton ici... Réessayez !");
                        }
                    } while(!inserted);
                }
            }

            if (model.win()) {
                model.winText();
                game = false;
            } else if (model.draw()) {
                model.drawText();
                game = false;
            }
        }
        model.displayBoard();
        System.out.println("Fin de partie ! Merci d'avoir joué !!!");*/
    }

    /*public static int[] getValidCoords(String prompt) {
        System.out.println(prompt);
        int[] tabCoords = new int[2];
        int row, col;
        do {
            System.out.print("Entrez la ligne (0-5) : ");
            row = input.nextInt();
            System.out.print("Entrez la colonne (0-5) : ");
            col = input.nextInt();

            if (row < 0 || row > 5 || col < 0 || col > 5) {
                System.out.println("Coordonnées invalides. Veuillez réessayer.");
            }
        } while (row < 0 || row > 5 || col < 0 || col > 5);
        tabCoords[0] = row;
        tabCoords[1] = col;
        return tabCoords;
    }*/
}
