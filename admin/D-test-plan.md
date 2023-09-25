# Test plan

## List of classes

* List below all classes in your implementation that should have unit tests.
* For each class, list methods that can be tested in isolation.
* For each class, if there are conditions on the class' behaviour that cannot
  be tested by calling one method in isolation, give at least one example of
  a test for such a condition.

Do **not** include in your test plan the `Marrakech` class or the predefined
static methods for which we have already provided unit tests.

|     Class      | Methods                                                                                                                                                                                                                         |
|:--------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|   `GameSate`   | `void nextPlayer()`<br/>`void removeCurrentPlayer()`<br/>`boolean isGameOver()`<br/>`Player findPlayer(Colour colour)`                                                                                                          |
|    `Board`     | `void placeRug(Rug rug)`<br/>`String generateAssamString()`<br/>`String generateBoardString()`                                                                                                                                  |
|    `Player`    | `void placeRug()`<br/>`void makePayment(Player otherPlayer, int amount)`<br/>`String generatePlayerString()`                                                                                                                    |
|     `Tile`     | `boolean isAdjacent(Tile that)`<br/>`boolean hasRug()`                                                                                                                                                                          |
|  `Direction`   | `Direction rotate(int rotation)`<br/>`Direction angleToDirection(int angle)`<br/>`Direction charToDirection(char directionChar)`                                                                                                |
|    `Colour`    | `Colour charToColour(char colourChar)`<br/>`boolean isColourValid(char colourChar)`<br/>`String colourToString(Colour colour)`<br/>`Color getFrontEndColor(Colour colour)`                                                      |
| `StringToTile` | `Tile getTileFromString(Tile[][] tiles, String coordinates)`<br/>`Tile getTileFromString(String coordinates)`<br/>`int[] getIndicesFromString(String coordinates)`<br/>`Tile getTileFromIndices(Tile[][] tiles, int[] indices)` |