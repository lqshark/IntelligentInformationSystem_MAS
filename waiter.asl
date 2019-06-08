// waiter

/* Initial beliefs */
//limit(food,15);

at(A) :- pos(A,X,Y) & pos(waiter,X,Y).

/* Initial goal */

!searchTable(grids).
		   +hello[source(A)] <- .print("I received a 'warning' from ",A).

/* Plans */

+!searchTable(grids) : not food(waiter)  // if the table is not in this location, the waiter will move the next grid.
   <- move(table);
      !searchTable(grids).
+!searchTable(grids).

+!check_supplier(kitchen) : not supplier(kitchen) // check the food resevers of the chef
   <- stop(S);
   !check_supplier(kitchen).
+!check_supplier(kitchen).	  
//+hello[source(A)] <- .print("I received a 'hello' from ",A).

@give_return[atomic]

+food(waiter) : not .desire(return(kitchen))
   <- !return(kitchen).

+!return(R)
   <- !return_K(food,R); //return kitchen
      !at(kitchen);      
      !searchTable(grids). 

+!return_K(S,L) : true
   <- !ensure_delivery(S);
      !at(L);
      get_next(S).


+!ensure_delivery(S) : food(waiter) // 
   <- delivery(food);
      !ensure_delivery(S).
+!ensure_delivery(_).

+!at(L) : at(L).
+!at(L) <- ?pos(L,X,Y);
           return_kitchen(X,Y);
           !at(L).


		   	   


