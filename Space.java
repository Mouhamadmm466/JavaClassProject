/**
 * Represents a single space on the game board.
 * Tracks its coordinates, ownership, and company affiliation.
 */
public class Space {

  /** The row index of this space (0-8). */
  private final int row;

  /** The column index of this space (0-11). */
  private final int col;

  /** The player who owns this space, or null if unowned. */
  private Player owner;

  /** The company this space belongs to, or null if none. */
  private Company company;

  /**
   * Constructs a new Space at the given coordinates.
   *
   * @param row The row index.
   * @param col The column index.
   */
  public Space(int row, int col) {
    this.row = row;
    this.col = col;
    this.owner = null;
    this.company = null;
  }

  /**
   * Returns the row index.
   *
   * @return The row index.
   */
  public int getRow() {
    return row;
  }

  /**
   * Returns the column index.
   *
   * @return The column index.
   */
  public int getCol() {
    return col;
  }

  /**
   * Returns the owner of this space.
   *
   * @return The owning Player, or null.
   */
  public Player getOwner() {
    return owner;
  }

  /**
   * Sets the owner of this space.
   *
   * @param owner The Player to set as owner.
   */
  public void setOwner(Player owner) {
    this.owner = owner;
  }

  /**
   * Returns the company this space belongs to.
   *
   * @return The Company, or null.
   */
  public Company getCompany() {
    return company;
  }

  /**
   * Sets the company this space belongs to.
   *
   * @param company The Company to set.
   */
  public void setCompany(Company company) {
    this.company = company;
  }

  /**
   * Checks if the space is currently owned by a player.
   *
   * @return true if owned, false otherwise.
   */
  public boolean isOwned() {
    return owner != null;
  }

  /**
   * Checks if the space is part of a company.
   *
   * @return true if part of a company, false otherwise.
   */
  public boolean isPartOfCompany() {
    return company != null;
  }

  /**
   * Returns the coordinate string (e.g., "A1").
   *
   * @return The coordinate string.
   */
  @Override
  public String toString() {
    char rowChar = (char) ('A' + row);
    return "" + rowChar + (col + 1);
  }
}
