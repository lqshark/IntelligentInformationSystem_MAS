// Kitchen

+food(kitchen) : true <- giveA(food);
						.send(waiter,tell,warning).
+supplier(kitchen) : true <-stop(table).

