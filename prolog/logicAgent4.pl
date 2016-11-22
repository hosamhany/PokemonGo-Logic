%:-consult(kb).
% at(0, 0, _, s0).


notwall(0,0,0,1).
notwall(0,0,1,0).
notwall(0,1,1,1).
notwall(0,1,0,0).
notwall(1,0,0,0).
wall(1,0,1,1).
wall(1,1,1,0).
notwall(1,1,0,1).
boundx(2).
boundy(2).
at(0,1,_,s0, 0, []).
goal(1,1).
haspokemon(0,0,_,s0, []).
haspokemon(1,0,_,s0, []).
orientation(north,_,s0, []).
pokemonsleft(2,_,s0, []).
stepstohatch(2,_,s0).




movementcheck(X, Y1, north, X, Y):-
	nonvar(Y1),
	Y is Y1 + 1.
movementcheck(X, Y1, north, X, Y):-
	nonvar(Y),
	Y1 is Y - 1.
movementcheck(X, Y1, south, X, Y):-
	nonvar(Y1),
	Y is Y1 - 1.
movementcheck(X, Y1, south, X, Y):-
	nonvar(Y),
	Y1 is Y + 1.
movementcheck(X1, Y, east, X, Y):-
	nonvar(X1),
	X is X1 + 1.
movementcheck(X1, Y, east, X, Y):-
	nonvar(X),
	X1 is X - 1.
movementcheck(X1, Y, west, X, Y):-
	nonvar(X1),
	X is X1 - 1.
movementcheck(X1, Y, west, X, Y):-
	nonvar(X),
	X1 is X + 1.


rotationcheck(north, right, east).
rotationcheck(north, left, west).
rotationcheck(east, right, south).
rotationcheck(east, left, north).
rotationcheck(south, right, west).
rotationcheck(south, left, east).
rotationcheck(west, right, north).
rotationcheck(west, left, south).

result(s0, _, X):-
	X \== s0.
result(X, _, Z):-
	X \== s0,
	Z \== s0,
	X \== Z.


win:-
	goal(X, Y),
	at(X, Y, A, S),
	pokemonsleft(0, A, S),
	stepstohatch(0, A, S).

inbound(X,Y):-
	X >= 0,
	boundx(BX),
	X < BX,
	Y>=0,
	boundy(BY),
	Y < BY.

at(X, Y, A, Scurr, Steps, [A | Tail]):-
	A = forward,
	notwall(X, Y, X1, Y1),
	inbound(X1, Y1),
	movementcheck(X1, Y1, Orientation, X, Y),
	result(Sold, A, Scurr),
	at(X1, Y1, _, Sold, Steps1, Tail),
	Steps is Steps1 + 1,
	orientation(Orientation, _, Sold, Tail).

at(X, Y, A, Scurr, Steps, [A | Tail]):-
	(A = left; A = right; A = pick),
	result(Sold, A, Scurr),
	at(X, Y, _, Sold, Steps, Tail).

at(X, Y, A, Scurr, Steps, [A | Tail]):-
	A = forward,
	movementcheck(X, Y, O, X1, Y1),
	\+ (notwall(X, Y, X1, Y1), inbound(X1, Y1)),
	result(Sold, A, Scurr),
	orientation(O, _, Sold, Tail),
	at(X, Y, _, Sold, Steps, Tail).

orientation(O, A, Scurr, [A|Tail]):-
	(A = right; A = left),
	rotationcheck(O1, A, O),
	result(Sold, A, Scurr),
	orientation(O1, _, Sold, Tail).

orientation(O, A, Scurr, [A|Tail]):-
	%A \= right,
	%A \= left,
	(A = forward; A = pick),
	result(Sold, A, Scurr),
	orientation(O, _, Sold, Tail).



haspokemon(X, Y, A, Scurr, [A | Tail]):-
	%A \= pick,
	(A = left; A = right; A = forward),
	haspokemon(X, Y, _, Sold, Tail),
	result(Sold, A, Scurr).

pokemonsleft(N, A, Scurr, [A | Tail]):-
	A = pick,
	N1 is N + 1,
	haspokemon(X, Y, A, Sold, Tail),
	at(X, Y, A, Scurr, _, Tail),
	pokemonsleft(N1, _, Sold, Tail),
	result(Sold, A, Scurr).

pokemonsleft(N, A, Scurr, [A | Tail]):-
	%A \= pick,
	(A = left; A = right; A = forward),
	pokemonsleft(N, _, Sold, Tail),
	result(Sold, A, Scurr).

stepstohatch(S, A, Scurr):-
	A = forward,
	at(X, Y, A, Scurr),
	at(X1, Y1, _, Sold),
	result(Sold, A, Scurr),
	((X1 \== X) ; (Y \== Y1)),
	stepstohatch(S1, _, Sold),
	((S1 is 0, S is 0) ; (S1 is S+1)).

stepstohatch(S, A, Scurr):-
	(A = left; A = right; A = pick),
	stepstohatch(S, _, Sold),
	result(Sold, A, Scurr).



