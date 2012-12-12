VeriLinks
=========
VeriLinks is a light-weight game platform with the purpose of validating links
in the Web of Data. It consists of core functionality for game administration as
well as game rewarding and balancing. The VeriLinks platform takes over server side functionality, administrative tasks for adding linksets as
well as authentication mechanisms. For this reason, only few methods need to
implemented to create an interlinking game and those games are not restricted
to a particular programming language

Verilinks Server API
--------
An overview of the VeriLinks Server API can be found at:  
https://docs.google.com/spreadsheet/ccc?key=0An8FWTPcYP2kdFdaU0UzWXRzeVZMMVZ5Y0wyeGtRSFE  

VeriLinks Widget
--------
The features of VeriLinks can be integrated into a game with our VeriLinks Javascript widget.  
The widget can be embedded in a web page with two HTML tags: one container tag to indicate where the widget should be placed 
and one script tag to load the widget. The widget automates the displaying process of the links using JsRender templates and it provides access to all
Server API methods.

Games
--------
So far, two different games have been implemented: pea invasion and space ships.

In general, pea invasion is a single player web game with the underlying goal
to validate links in the Web of Data. Players indirectly compete against other
players, i.e. their scores depend on the evaluation of others.

As a second showcase game we have implemented a one versus one space ship
battles. Target of the game is to destroy enemy’s ship before he destroys yours.
The game progresses in turns with each turn taking 30 seconds to complete.
During the turn user must verify as many links as possible. Damage done to
enemy ship is calculated at the end of the turn and it's based on the number of verified
links as well as the precision of verification.


