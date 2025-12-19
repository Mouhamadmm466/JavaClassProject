import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the game board and contains the core game logic.
 * Handles adjacency checks, company formation, and merging.
 */
public class Board {

  /** The 9x12 grid of spaces. */
  private final Space[][] grid;

  /** The list of all available companies. */
  private final List<Company> companies;

  /**
   * Constructs a new Board.
   * Initializes the grid and companies.
   *
   * @param companies The list of companies to use.
   */
  public Board(List<Company> companies) {
    this.companies = companies;
    this.grid = new Space[9][12];
    for (int r = 0; r < 9; r++) {
      for (int c = 0; c < 12; c++) {
        grid[r][c] = new Space(r, c);
      }
    }
  }

  /**
   * Returns the space at the given coordinates.
   *
   * @param row The row index.
   * @param col The column index.
   * @return The Space object.
   */
  public Space getSpace(int row, int col) {
    if (row < 0 || row >= 9 || col < 0 || col >= 12) {
      return null;
    }
    return grid[row][col];
  }

  /**
   * Gets the list of valid adjacent spaces (up, down, left, right).
   *
   * @param s The center space.
   * @return A list of adjacent Space objects.
   */
  public List<Space> getAdjacentSpaces(Space s) {
    List<Space> adj = new ArrayList<>();
    int r = s.getRow();
    int c = s.getCol();

    int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    for (int[] d : dirs) {
      Space neighbor = getSpace(r + d[0], c + d[1]);
      if (neighbor != null) {
        adj.add(neighbor);
      }
    }
    return adj;
  }

  /**
   * Resolves the logic when a player places a tile on the board.
   * Handles placing, forming companies, and merging.
   *
   * @param s      The space being placed.
   * @param player The player placing the tile.
   * @return True if the move was valid and processed, false if illegal (e.g., cannot form 8th company).
   */
  public boolean resolveMove(Space s, Player player) {
    s.setOwner(player);

    List<Space> adjOwned = new ArrayList<>();
    for (Space neighbor : getAdjacentSpaces(s)) {
      if (neighbor.isOwned()) {
        adjOwned.add(neighbor);
      }
    }

    // Case 1: Starter Space (No adjacent owned spaces)
    if (adjOwned.isEmpty()) {
      return true; // Just placed, no further action
    }

    Set<Company> adjCompanies = new HashSet<>();
    for (Space neighbor : adjOwned) {
      if (neighbor.isPartOfCompany()) {
        adjCompanies.add(neighbor.getCompany());
      }
    }

    // Case 2: New Company Formation (Adjacent are owned but no companies)
    if (adjCompanies.isEmpty()) {
      if (getActiveCompanyCount() >= 7) {
        // Illegal move: Cannot form 8th company
        s.setOwner(null); // Revert
        return false;
      }
      formNewCompany(s, adjOwned);
      return true;
    }

    // Case 3: Joining a Company (Only 1 unique adjacent company)
    if (adjCompanies.size() == 1) {
      Company c = adjCompanies.iterator().next();
      c.addSpace(s);
      // Also add any adjacent starter blocks to this company
      for (Space neighbor : adjOwned) {
        if (!neighbor.isPartOfCompany()) {
          c.addSpace(neighbor);
        }
      }
      return true;
    }

    // Case 4: Merging Companies (>1 unique adjacent companies)
    if (adjCompanies.size() > 1) {
      mergeCompanies(new ArrayList<>(adjCompanies), s, adjOwned, player);
      return true;
    }

    return true;
  }

  /**
   * Helper to count active companies.
   */
  private int getActiveCompanyCount() {
    int count = 0;
    for (Company c : companies) {
      if (c.isOnBoard()) count++;
    }
    return count;
  }

  /**
   * Forms a new company from a placed space and its neighbors.
   */
  private void formNewCompany(Space center, List<Space> neighbors) {
    Company newCompany = null;
    for (Company c : companies) {
      if (!c.isOnBoard()) {
        newCompany = c;
        break;
      }
    }
    
    if (newCompany != null) {
      newCompany.addSpace(center);
      for (Space n : neighbors) {
        newCompany.addSpace(n);
      }
    }
  }

  /**
   * Merges multiple companies.
   */
  private void mergeCompanies(List<Company> mergingCompanies, Space center, List<Space> adjOwned, Player currentPlayer) {
    // 1. Identify Acquiring Company (Largest)
    // Sort by size (desc), then by player shares (asc) - wait, logic says fewest shares breaks ties?
    // "Tie-breaker 1: If tied for largest size, the acquiring company is the one among the tied companies where the current player owns the fewest shares."
    // Note: This simplified version doesn't track shares per company yet (as per requirements, shares are just spaces owned).
    // Let's assume "shares" means "owned spaces in that company".

    mergingCompanies.sort((c1, c2) -> {
      if (c1.getSize() != c2.getSize()) {
        return c2.getSize() - c1.getSize(); // Descending size
      }
      // Tie-breaker: Fewest shares owned by current player
      long shares1 = c1.getSpaces().stream().filter(s -> s.getOwner() == currentPlayer).count();
      long shares2 = c2.getSpaces().stream().filter(s -> s.getOwner() == currentPlayer).count();
      return Long.compare(shares1, shares2); // Ascending shares
    });

    Company acquirer = mergingCompanies.get(0);
    List<Company> defunct = mergingCompanies.subList(1, mergingCompanies.size());

    // 2. Scoring for defunct companies
    for (Company c : defunct) {
      scoreCompany(c);
      
      // Transfer spaces to acquirer
      for (Space s : c.getSpaces()) {
        // Note: In real Acquire, tiles might become dead or swapped. 
        // Here: "All spaces of the acquired companies... join the acquiring company."
        // We need to be careful not to modify the list we are iterating if possible, 
        // but c.getSpaces() returns the list. We will clear it later.
      }
      // Actually, we should just iterate and reassign.
    }

    // 3. Perform Acquisition
    acquirer.addSpace(center);
    
    // Add adjacent starters
    for (Space n : adjOwned) {
      if (!n.isPartOfCompany()) {
        acquirer.addSpace(n);
      }
    }

    // Merge defunct spaces
    for (Company c : defunct) {
      // Create a copy to iterate safely
      List<Space> oldSpaces = new ArrayList<>(c.getSpaces());
      for (Space s : oldSpaces) {
        acquirer.addSpace(s);
      }
      c.reset(); // Make available again
    }
  }

  /**
   * Calculates and awards points for a company being acquired.
   */
  private void scoreCompany(Company c) {
    // This requires access to all players to award points.
    // Since Board doesn't know about all players directly in this design, 
    // we might need to pass the player list or handle scoring in BeAcquired.
    // However, spaces know their owners. We can iterate spaces to find share counts.
    
    // For this implementation, we will calculate points and add them to players directly here.
    // We need to find all unique owners in this company.
    Set<Player> owners = new HashSet<>();
    for(Space s : c.getSpaces()) {
      if(s.getOwner() != null) owners.add(s.getOwner());
    }

    for (Player p : owners) {
      long shares = c.getSpaces().stream().filter(s -> s.getOwner() == p).count();
      if (shares > 0) {
        int points = (int) (shares * (c.getSize() - 1));
        p.addScore(points);
      }
    }
    

  }
}
