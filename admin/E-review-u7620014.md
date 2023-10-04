## Code Review

Reviewed by: Haobo Zou, u7620014

Reviewing code written by: Le Thanh Nguyen, u7594144

Component: `public char getWinner()` and `public int getNumOfVisibleSquares(Player player)` from `GameState` class

### Comments

Pros:
- conforms with the rules of the game and passes GetWinnerTest
- Javadoc is appropriately written for both methods
- follows Java code conventions, methods and variables are clearly named, the style is consistent throughout

Cons:
- implementation for getWinner() has a lot of lines (37 lines) and lacks inline comments, could be hard to read
- it is a little more complex than it needs to be
- could use getter method getColourChar() instead of directly accessing colourChar