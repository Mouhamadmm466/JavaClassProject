import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 * Tracks their hand, score, and GUI window.
 */
public class Player {

  /** The player's ID number (1-based). */
  private final int id;

  /** The list of cards in the player's hand (coordinate strings). */
  private final List<String> hand;

  /** The player's current score. */
  private int score;

  /** The JavaFX Stage for this player's hand window. */
  private Stage handStage;

  /**
   * Constructs a new Player.
   *
   * @param id The player's ID.
   */
  public Player(int id) {
    this.id = id;
    this.hand = new ArrayList<>();
    this.score = 0;
    this.handStage = null;
  }

  /**
   * Returns the player's ID.
   *
   * @return The ID.
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the player's hand.
   *
   * @return The list of card strings.
   */
  public List<String> getHand() {
    return hand;
  }

  /**
   * Returns the player's current score.
   *
   * @return The score.
   */
  public int getScore() {
    return score;
  }

  /**
   * Adds points to the player's score.
   *
   * @param points The points to add.
   */
  public void addScore(int points) {
    this.score += points;
  }

  /**
   * Returns the player's hand stage.
   *
   * @return The Stage.
   */
  public Stage getHandStage() {
    return handStage;
  }

  /**
   * Sets the player's hand stage.
   *
   * @param handStage The Stage to set.
   */
  public void setHandStage(Stage handStage) {
    this.handStage = handStage;
  }

  /**
   * Adds a card to the player's hand.
   *
   * @param card The card coordinate string.
   */
  public void addCard(String card) {
    hand.add(card);
  }

  /**
   * Removes a card from the player's hand.
   *
   * @param card The card coordinate string.
   */
  public void removeCard(String card) {
    hand.remove(card);
  }
}
