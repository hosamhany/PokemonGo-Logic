at(0, 0, none, s0).

movement_check(X, Y1, north, X, Y):-
	Y is Y1 + 1.
movement_check(X, Y1, south, X, Y):-
	Y is Y1 - 1.
movement_check(X1, Y, east, X, Y):-
	X is X1 + 1.
movement_check(X1, Y, west, X, Y):-
	X is X1 - 1.
rotation_check(north, right, east).
rotation_check(north, left, west).
rotation_check(east, right, south).
rotation_check(east, left, north).
rotation_check(south, right, west).
rotation_check(south, left, east).
rotation_check(west, right, north).
rotation_check(west, left, south).

result(X, Y, Z).


at(X, Y, A, Scurr):-
	result(Sold, A, Scurr),
	A = forward,
	at(X1, Y1, Sold),
	movement_check(X1, Y1, Orientation, X, Y),
	orientation(Orientation, _, Sold).
	not wall(X, Y, X1, Y1),
	not out_of_bound(X1, Y1).

at(X, Y, A, Scurr):-
	result(Sold, A, Scurr),
	A \= forward,
	at(X, Y, _, Sold).

at(X, Y, A, Scurr):-
	result(Sold, A, Scurr),
	A = forward,
	at(X, Y, _, Sold),
	orientation(O, _, Sold),
	movement_check(X, Y, O, X1, Y1),
	(wall(X, Y, X1, Y1); out_of_bound(X1, Y1)).

orientation(O, A, Scurr):-
	result(Sold, A, Scurr),
	orientation(O1, _, Sold),
	(A = right; A = left),
	orientation_check(O1, A, O).

orientation(O, A, Scurr):-
	result(Sold, A, Scurr),
	A \= right,
	A \= left.

pokemons_left(N, A, Scurr):-
	at(X, Y, A, Scurr),
	A = pick,
	has_pokemon(X, Y),
	result(Sold, A, Scurr),
	pokemons_left(N1, _, Sold),
	N is N1 + 1.

pokemons_left(N, A, Scurr):-
	result(Sold, A, Scurr),
	A \= pick,
	pokemons_left(N, _, Sold).


steps_to_hatch(S, A, Scurr):-
	A = forward,
	result(Sold, A, Scurr),
	at(X, Y, A, Scurr),
	at(X1, Y1, A, Sold),
	((X1 \= X) ; (Y \= Y1)),
	steps_to_hatch(S1, _, Sold),
	((S1 is 0, S is 0) ; (S1 is S+1)).

steps_to_hatch(S, A, Scurr):-
	A \= forward,
	result(Sold, A, Scurr),
	steps_to_hatch(S, _, Sold).


