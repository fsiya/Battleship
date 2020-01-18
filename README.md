# Battleship

This is a basic battleship game made with Java and JavaFX.

This game allows for two players to play against each other over a network when connecting with the Server.

To accomplish this, I used the Observable pattern with `propertyChangeListener()`.

Please note, this game is not complete. There are numerous deficiencies.

## Deficiencies
The following are the known deficiencies of the Battleship game:
- Game will only display "Your Turn" or "Opponent's Turn" when the user has clicked on the enemy board
    - It will not update automatically after the opponent has shot
- Proper turn based
    - I was unable to disable the mouse events on the created Rectangle objects on the enemy board, thus allowing the player to have unlimited turns
- User is able to start playing the game without placing all the ships 
    - Again, this is related to the proper turn based functionality
- If a user disconnects, the other user will not be notified
- User may not be able to reconnect with the server