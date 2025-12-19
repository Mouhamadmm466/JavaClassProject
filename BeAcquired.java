import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Main application class for the BeAcquired board game.
 * Handles GUI initialization, event handling, and game flow.
 */
public class BeAcquired extends Application {

    private Board board;
    private List<Player> players;
    private LinkedList<String> deck;
    private int currentPlayerIndex;
    private GridPane boardGrid;
    private String selectedCard; // The card currently selected by the active player

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 1. Initialize Game Data
        initializeGame(2); // Default to 2 players

        // 2. Setup Main Board GUI
        primaryStage.setTitle("BeAcquired - Main Board");
        boardGrid = new GridPane();
        boardGrid.setPadding(new Insets(10));
        boardGrid.setHgap(5);
        boardGrid.setVgap(5);

        // Create buttons for the 9x12 grid
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 12; c++) {
                Button btn = new Button();
                Space s = board.getSpace(r, c);
                btn.setText(s.toString());
                btn.setPrefSize(50, 50);

                // Event Handler for Board Clicks
                final int row = r;
                final int col = c;
                btn.setOnAction(e -> handleBoardClick(row, col));

                boardGrid.add(btn, c, r);
            }
        }

        Scene scene = new Scene(boardGrid);
        primaryStage.setScene(scene);
        primaryStage.show();

        // 3. Setup Player Windows
        for (Player p : players) {
            createPlayerWindow(p);
        }

        updateBoardUI();
    }

    /**
     * Initializes the game state, deck, and players.
     *
     * @param numPlayers Number of players.
     */
    private void initializeGame(int numPlayers) {
        // Create Companies
        List<Company> companies = new ArrayList<>();
        companies.add(new Company("Red", Color.RED));
        companies.add(new Company("Yellow", Color.YELLOW));
        companies.add(new Company("Green", Color.GREEN));
        companies.add(new Company("Blue", Color.BLUE));
        companies.add(new Company("Orange", Color.ORANGE));
        companies.add(new Company("Purple", Color.PURPLE));
        companies.add(new Company("Cyan", Color.CYAN));

        board = new Board(companies);
        players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player(i));
        }
        currentPlayerIndex = 0;

        // Create and Shuffle Deck
        deck = new LinkedList<>();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 12; c++) {
                char rowChar = (char) ('A' + r);
                deck.add("" + rowChar + (c + 1));
            }
        }
        Collections.shuffle(deck);

        // Deal Initial Hands
        for (Player p : players) {
            for (int i = 0; i < 6; i++) {
                if (!deck.isEmpty()) {
                    p.addCard(deck.removeFirst());
                }
            }
        }
    }

    /**
     * Creates the GUI window for a player's hand.
     *
     * @param p The player.
     */
    private void createPlayerWindow(Player p) {
        Stage stage = new Stage();
        p.setHandStage(stage);
        updatePlayerHandUI(p);
        stage.setX(100 + (p.getId() * 300)); // Offset windows
        stage.setY(100);
        stage.show();
    }

    /**
     * Updates the UI for a player's hand window.
     * Re-creates buttons based on current hand.
     *
     * @param p The player.
     */
    private void updatePlayerHandUI(Player p) {
        Stage stage = p.getHandStage();
        if (stage == null)
            return;

        stage.setTitle("Player " + p.getId() + ": " + p.getScore());

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10));
        pane.setHgap(5);
        pane.setVgap(5);

        int col = 0;
        for (String card : p.getHand()) {
            Button btn = new Button(card);
            btn.setPrefSize(60, 40);

            // Highlight logic
            if (p == players.get(currentPlayerIndex) && card.equals(selectedCard)) {
                btn.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
            }

            btn.setOnAction(e -> {
                // Only allow current player to select
                if (p == players.get(currentPlayerIndex)) {
                    selectedCard = card;
                    updatePlayerHandUI(p); // Refresh to show highlight
                }
            });

            pane.add(btn, col++, 0);
        }

        Scene scene = new Scene(pane);
        stage.setScene(scene);
    }

    /**
     * Handles clicks on the main board.
     * Attempts to play the selected card on the clicked space.
     *
     * @param row Row index.
     * @param col Column index.
     */
    private void handleBoardClick(int row, int col) {
        if (selectedCard == null)
            return;

        Space clickedSpace = board.getSpace(row, col);
        if (clickedSpace.toString().equals(selectedCard)) {
            Player currentPlayer = players.get(currentPlayerIndex);

            // Attempt move
            boolean success = board.resolveMove(clickedSpace, currentPlayer);

            if (success) {
                // Remove card and draw new one
                currentPlayer.removeCard(selectedCard);
                if (!deck.isEmpty()) {
                    currentPlayer.addCard(deck.removeFirst());
                }
                selectedCard = null;

                // Update UIs
                updateBoardUI();
                updatePlayerHandUI(currentPlayer);

                // Next Turn
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

                // Refresh all player windows (to update titles/scores)
                for (Player p : players) {
                    updatePlayerHandUI(p);
                }
            }
        }
    }

    /**
     * Updates the main board buttons (colors and text).
     */
    private void updateBoardUI() {
        for (javafx.scene.Node node : boardGrid.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                int c = GridPane.getColumnIndex(node);
                int r = GridPane.getRowIndex(node);
                Space s = board.getSpace(r, c);

                if (s.isOwned()) {
                    btn.setText(String.valueOf(s.getOwner().getId()));
                    if (s.isPartOfCompany()) {
                        btn.setBackground(new Background(
                                new BackgroundFill(s.getCompany().getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
                    } else {
                        // Starter tile color (e.g., Light Gray or default)
                        btn.setBackground(
                                new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                } else {
                    btn.setText(s.toString());
                    btn.setBackground(null); // Default
                }
            }
        }
    }
}
