%:-consult(kb).
% at(0, 0, none, s0).
wall(1,0,1,1).
boundx(2).
boundy(2).
at(0,1,none,s0).
goal(1,1).
haspokemon(0,0,none,s0).
haspokemon(1,0,none,s0).
orientation(north,none,s0).
pokemonsleft(2,none,s0).
stepstohatch(2,none,s0).




movementcheck(X, Y1, north, X, Y):-
	Y is Y1 + 1.
movementcheck(X, Y1, south, X, Y):-
	Y is Y1 - 1.
movementcheck(X1, Y, east, X, Y):-
	X is X1 + 1.
movementcheck(X1, Y, west, X, Y):-
	X is X1 - 1.
rotationcheck(north, right, east).
rotationcheck(north, left, west).
rotationcheck(east, right, south).
rotationcheck(east, left, north).
rotationcheck(south, right, west).
rotationcheck(south, left, east).
rotationcheck(west, right, north).
rotationcheck(west, left, south).

result(X, Y, Z).

outofbound(X,Y):-
	X >= 0,
	boundx(BX),
	X < BX,
	Y>=0,
	boundy(BY),
	Y < BY.

at(X, Y, A, Scurr):-
	result(Sold, A, Scurr),
	A = forward,
	at(X1, Y1, A, Sold),
	movementcheck(X1, Y1, Orientation, X, Y),
	orientation(Orientation, _, Sold),
	\+ wall(X, Y, X1, Y1),
	\+ outofbound(X1, Y1).

at(X, Y, A, Scurr):-
	result(Sold, A, Scurr),
	A \= forward,
	at(X, Y, _, Sold).

at(X, Y, A, Scurr):-
	result(Sold, A, Scurr),
	A = forward,
	at(X, Y, _, Sold),
	orientation(O, _, Sold),
	movementcheck(X, Y, O, X1, Y1),
	(wall(X, Y, X1, Y1); outofbound(X1, Y1)).

orientation(O, A, Scurr):-
	result(Sold, A, Scurr),
	orientation(O1, _, Sold),
	(A = right; A = left),
	orientationcheck(O1, A, O).

orientation(O, A, Scurr):-
	result(Sold, A, Scurr),
	A \= right,
	A \= left.

haspokemon(X, Y, A, Scurr):-
	A \= pick,
	result(Sold, A, Scurr),
	haspokemon(X, Y, _, Sold).

pokemonsleft(N, A, Scurr):-
	at(X, Y, A, Scurr),
	A = pick,
	result(Sold, A, Scurr),
	haspokemon(X, Y, A, Sold),
	pokemonsleft(N1, _, Sold),
	N is N1 + 1.

pokemonsleft(N, A, Scurr):-
	result(Sold, A, Scurr),
	A \= pick,
	pokemonsleft(N, _, Sold).

stepstohatch(S, A, Scurr):-
	A \= forward,
	result(Sold, A, Scurr),
	stepstohatch(S, _, Sold).

stepstohatch(S, A, Scurr):-
	A = forward,
	result(Sold, A, Scurr),
	at(X, Y, A, Scurr),
	at(X1, Y1, A, Sold),
	((X1 \= X) ; (Y \= Y1)),
	stepstohatch(S1, _, Sold),
	((S1 is 0, S is 0) ; (S1 is S+1)).


