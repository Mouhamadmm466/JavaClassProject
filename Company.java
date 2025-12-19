import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a company in the game.
 * Tracks its color, member spaces, and status.
 */
public class Company {

  /** The name of the company. */
  private final String name;

  /** The color representing the company on the board. */
  private final Color color;

  /** The list of spaces that belong to this company. */
  private final List<Space> spaces;

  /** Whether the company is currently on the board. */
  private boolean onBoard;

  /**
   * Constructs a new Company.
   *
   * @param name  The name of the company.
   * @param color The color of the company.
   */
  public Company(String name, Color color) {
    this.name = name;
    this.color = color;
    this.spaces = new ArrayList<>();
    this.onBoard = false;
  }

  /**
   * Returns the name of the company.
   *
   * @return The name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the color of the company.
   *
   * @return The Color.
   */
  public Color getColor() {
    return color;
  }

  /**
   * Returns the list of spaces in this company.
   *
   * @return The list of spaces.
   */
  public List<Space> getSpaces() {
    return spaces;
  }

  /**
   * Checks if the company is currently on the board.
   *
   * @return true if on board, false otherwise.
   */
  public boolean isOnBoard() {
    return onBoard;
  }

  /**
   * Sets the on-board status of the company.
   *
   * @param onBoard The status to set.
   */
  public void setOnBoard(boolean onBoard) {
    this.onBoard = onBoard;
  }

  /**
   * Adds a space to this company.
   *
   * @param space The space to add.
   */
  public void addSpace(Space space) {
    spaces.add(space);
    space.setCompany(this);
    // If this is the first space, the company is now on the board
    if (!spaces.isEmpty()) {
      onBoard = true;
    }
  }

  /**
   * Returns the size of the company (number of spaces).
   *
   * @return The size.
   */
  public int getSize() {
    return spaces.size();
  }

  /**
   * Clears the company data (used when a company is defunct/removed, though in this game they merge).
   * Actually, in Acquire, once a company is formed it stays or merges.
   * If it merges into another, it might become available again.
   * This method resets the company to be available.
   */
  public void reset() {
    for (Space s : spaces) {
      s.setCompany(null);
    }
    spaces.clear();
    onBoard = false;
  }
}
