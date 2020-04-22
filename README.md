# Chinese Chess built in Java

A two player Chinese Chess Game implemented in JAVA using the swing libraries for COP 3252.

Beyond implementing the rules of standard Chinese Chess, this game includes bonus features such as move timers, theming, exporting move history, a live event log, and a fully fledged gui.

To run the game, simply download the jar file, and use the command java -jar ChineseChess.jar.

## Extra Features
* Themeing with an intuitive start menu
* Game Profile (option to change time limit, player color, and player name)
* Player timers (switches between turns)
* Clean user interface
* Live event log
* End Game/Restart Game option
* Chinese and English piece icons (Under View > Language)
* Menu Bar (Including direct link to rules, about page)
* Save Game (Choose file name and path, includes all moves made, player name, time elapsed)
* Open source and object oriented design
* Java Documentation


## <a href=https://en.wikipedia.org/wiki/Xiangqi#Rules>How to Play</a>

1. Download the jar file from <a href=https://github.com/ChiliPaneer/ChineseChess-JAVA_Swing>here</a>
2. On a machine with at least Java 8, run the following command: "java jar ChineseChess-JAVA_Swing.jar
3. Configure your theme on the start menu, or just press the begin button to launch the board.
4. Select a piece to highlight it
5. Select an endpoint to attempt to move the piece there   (*note that the bottom player always goes first*)
6. The game will automatically inform you if a checkmate or stalemate has been reached by standard rules.
7. If you wish to end the game early, click on the End Game button.
8. To play again, exit the game and repeast steps 2 through 7.

## Roles
Michael handled most of the GUI stuff in game, Venkat handled most of the game logic stuff, and Andy handled most of the bonus feature stuff. We mostly focused on our individual parts but communicated a lot to make sure everything would be easily compatible. 

##### Michael Yu:
* Created the visual game board
* Managed the languages and icon handling
* Created the event box

##### Venkat Pamulapati
* Implemented the game logic including the board, the checking/validation, pieces, edge case handling, etc.
* Created the Start Menu
* Implemented theming

##### Andy Jiang
* Implemented move timers and move logger
* Created the GUI for the timers and asynchronous updating
* Implemented game saving

## In-depth feature overview:

##### Timer and Timer GUI:
The Timer class keeps track of the start and stop time in nanoseconds (System.nanoTime()) to determine the time elapsed, which 
is then converted into milliseconds. Nanoseconds was used over milliseconds for a more precise measurement of time elapsed. It is then implemented
into the Player class to create a timer for each individual player. All methods of timer is called through the Player class.
The Player class interacts with the Timer class to get all necessary information about the player's time elapsed during each turn.
Then, it formats it in mm:ss format using SimpleDateFormat and make it accessible for the TurnTimerPanel to get.

The TurnTimerPanel is a GridLayout (2x1) of two timer panel (one for each player). Inside each timer panel, there is a
number panel, centered inside timer panel using BorderLayout, which includes the time label. The time label is inside
a panel to create extra space between the number and border.

In order for the two timer panel to update the time left for each player, threading must be used. Without threads, when
updating the label inside a while loop, the game does not function until while loop is exited. Thus, the time labels
must be updated asynchronously with the game. Players' timer is stopped when their turn is over and started again when
their turn start. Thus, the updating of each label is started and stopped, to create a animation of switching. However,
dead threads can not be restarted, thus a new thread is created each time the label is suppose to update.

The timer also acts as current player indicator by fading or highlihgting it's colors which are set by the current theme.

##### Game Logic and structure
The game is essentially handled by manipulating a two-dimensional array of Point objects. Each point object holds a piece, which are differnciated
by their move patterns. In order to make a move, the player creates a move object (input through the gui) which is then subject to a series of check
which can be categorized into two categories pre-move checking and post-move checking.

Shallow checking concerns only the piece and the move, and ignores the rest of the board. For example, if I want to move a horse all I need to 
do is check that the given move object has a valid move pattern (ex: 2 up 1 right) and see if it has any pieces in the way. We can actually see this without
moving the piece at all, since we know exactly where the piece is going. The problem with shallow checking
is that it doesn't account for factors such as:

 * Does moving this piece put our general in check?
 * Does this moving this piece make the generals face each other?
 
Deep checking solves these problems. The way post move checking works is that it executes the move privately and then 
proceeds to run tests to see if the move is legal. If it fails, it simply undoes the move privately. You may wonder, why 
not just use deep checking for everything then? Besides saving resources
(looking for a checkmate is O(n^3) currently) there are several cases where it is needed. For example, in order to see if there's
a checkmate, we need to see what pieces are delivering a check. A piece can deliver a check, even if the move to actually attak
the general is illegal! (For example, if moving the piece that is delivering the check would cause it's own general to 
fall into check.)

Once a move has been thoroughly checked, it is then executed on the array.

Beyond move validation, there is also checking for when the board has reached checkmate or stalemate. Once these cases have 
been reached neither player can move. Some stalemate edge cases include when the board has no pieces that can cross the river or when 
the only pieces remaining are the generals and a single cannon.

The board is designed to be easily accessed by the other packages of the game with minimal knowledge of how it functions.

#####Game Board

#####Start menu
Instead of having command line arguments, the start up menu allows users to easily set their preferences. It includes 
a decent user interface for setting up player names and the current theme with a live preview of what the board will look like
if they use the english verison. The themeing is applied throughout the game board, even in subtle areas like the timer. The theming 
very extensible because it carries the data in a Profile object which is stored as core member data. This way, any part of the application 
can easily access what it needs.

