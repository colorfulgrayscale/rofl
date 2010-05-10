insert into RestaurantUser (firstname, lastname, username, password, type) values ('manager', 'manager', 'manager', 'manager', 'M');
insert into RestaurantUser (firstname, lastname, username, password, type) values ('cook', 'cook', 'cook', 'cook', 'C');
insert into RestaurantUser (firstname, lastname, username, password, type) values ('host', 'host', 'host', 'host', 'H');
insert into RestaurantUser (firstname, lastname, username, password, type) values ('waiter', 'waiter', 'waiter', 'waiter', 'W');
insert into RestaurantUser (firstname, lastname, username, password, type) values ('busboy', 'busboy', 'busboy', 'busboy', 'B'); 
insert into RestaurantTable (locationX, locationY, status, tableType) values (2,2,'O','R'); 
insert into RestaurantTable (locationX, locationY, status, tableType) values (2,200,'X','S'); 
insert into MenuItem (name, description, price, menuType) values ('Chicken Fajita Rollup', 'Were on a roll with this unique take on the classic fajita. Smoky chipotle chicken rolled in a flour tortilla with melted Monterey Jack and cheddar cheeses, shredded lettuce and pico de gallo. Served with Mexi-ranch dipping sauce.', 8.99, 'E'); 
insert into MenuItem (name, description, price, menuType) values ('California Turkey Club', 'Slices of roasted turkey piled high on toasted bread and topped with Jack cheese, lettuce, sliced tomatoes, Applewood smoked bacon and creamy avocado-ranch.', 7.99, 'E'); 
insert into MenuItem (name, description, price, menuType) values ('Fire Pit Bacon Burger', 'Piled with pepper-Jack cheese and slices of real Applewood smoked bacon and served on a toasted bun with smoky chipotle spread, lettuce, tomato, pickles and onions.', 2.29, 'E');
insert into MenuItem (name, description, price, menuType) values ('Quesadilla Burger', 'Take your taste buds for a Southwest joyride. A juicy burger with our signature Mexi-ranch sauce with pico de gallo and shredded lettuce. Served in a mouth-watering pepper-Jack and cheddar quesadilla (with bacon).', 8.59, 'E');
insert into MenuItem (name, description, price, menuType) values ('Cheeseburger Sliders', 'Three juicy burgers topped with American cheese, grilled onions and signature burger sauce on toasted mini buns.', 6.99, 'E');
insert into MenuItem (name, description, price, menuType) values ('Boneless Buffalo Wings', 'No bones about it, these are great. Lightly breaded boneless chicken with your choice of classic, hot, honey BBQ, Southern BBQ, or sweet & spicy sauce.', 8.29, 'E');
insert into MenuItem (name, description, price, menuType) values ('Crunchy Onion Rings', 'Thick-cut rings coated in bread crumbs and golden fried.', 5.99, 'E');
insert into MenuItem (name, description, price, menuType) values ('Chili Cheese Nachos', 'Crisp corn tortilla chips covered with spicy chili, queso cheese, sour cream, lettuce, pico de gallo and jalape�os', 4.99, 'E');
insert into MenuItem (name, description, price, menuType) values ('Wonton Tacos', 'Zesty pulled pork stuffed in crispy wonton shells and topped with crunchy Asian slaw with cilantro.', 5.99, 'E');
insert into MenuItem (name, description, price, menuType) values ('Chicken Quesadilla Grande', 'Grilled chipotle chicken, melted cheese, crisp bacon, freshly made pico de gallo and a hint of chipotle pepper�all tucked inside two large flour tortillas and ready to wow your taste buds.', 5.99, 'A');
insert into MenuItem (name, description, price, menuType) values ('Chips & Salsa', 'Just Plain old salsa.', 2.99, 'A');
insert into MenuItem (name, description, price, menuType) values ('Chocolate Mousse', 'Decadent Oreo� chunks, chocolate mousse and whipped cream make this a rich and creamy treat.', 2.99, 'D');
insert into MenuItem (name, description, price, menuType) values ('Hot Fudge Sundae', 'Delicious hot fudge drizzled over vanilla ice cream, topped with whipped cream and a maraschino cherry. Every day should be sundae.', 2.99, 'D');
insert into MenuItem (name, description, price, menuType) values ('Strawberry Cheesecake', 'Classic cheesecake, graham cracker crumbs, strawberry sauce and whipped cream make this one to savor.', 4.99, 'D');
insert into MenuItem (name, description, price, menuType) values ('Chocolate Chip Cookie Sundae', 'A sundae we built for the whole table! We take a huge, warm chocolate chunk cookie, top it with vanilla ice cream, hot fudge, OREO� cookie crumbs, and whipped cream.', 5.99, 'D');
insert into MenuItem (name, description, price, menuType) values ('Pomegranate Margarita', 'This citrus fusion of Sauza Hornitos 100% Agave tequila, POM Wonderful Pomegranate juice and orange liquor is served with a fresh lime and cherry and a side shaker tin.', 2.99, 'D');
insert into MenuItem (name, description, price, menuType) values ('Flavored Perfect Margarita', 'Perfectly blended with 1800 Reposado 100% Blue Agave tequila, Cointreau, and Grand Marnier. Served with a side shaker tin. Your choice of strawberry, mango, raspberry, kiwi or original flavor.', 2.99, 'D');
insert into MenuItem (name, description, price, menuType) values ('Kiwi Melon Sangria', 'A tropical twist of Sutter Home White Zinfandel, DeKuyper Luscious Melon Dew liqueur with kiwi, other tropical juices and lemon-lime soda. Served Mucho size over ice with fresh fruit.', 2.99, 'D');
insert into MenuItem (name, description, price, menuType) values ('White Peach Sangria', 'Just Plain old salsa.', 2.99, 'D');
insert into MenuItem (name, description, price, menuType) values ('Classic Mixed Drinks', 'Your favorites with tonic or cola over ice: Captain Morgan, BACARDI, Jack Daniels, Crown Royal, Tanqueray gin, and ABSOLUT vodka.', 2.99, 'D');
insert into MenuItem (name, description, price, menuType) values ('Mozzarella Sticks', 'Golden fried and served with a tasty marinara sauce.', 5.99, 'A');
insert into MenuItem (name, description, price, menuType) values ('Spinach & Artichoke Dip', 'A dish that made us famous. A warm crock of creamy spinach, tender artichokes, and melted Asiago and Parmesan cheeses, with salsa and tortilla chips for dipping.', 7.69, 'A');
insert into MenuItem (name, description, price, menuType) values ('Appetizer Sampler', 'A platter to please everybuddy at the table. Must-have Mozzarella Sticks, our famous Spinach & Artichoke Dip, delicious Cheese Quesadilla Grande (with bacon), and favorite Boneless Buffalo Wings.', 10.49, 'A');
insert into RestaurantOrder (seatedTime, orderTaken, restaurantTable) values ($NOW$, $NOW$, 1);
insert into OrderItem (menuItem, restaurantOrder, quantity, menuType) values (1, 1, 1, 'E');
insert into OrderItem (menuItem, restaurantOrder, quantity, menuType) values (2, 1, 2, 'E');
insert into RestaurantOrder (seatedTime, orderTaken, restaurantTable) values ($NOW$, $NOW$, 2);
insert into OrderItem (menuItem, restaurantOrder, quantity, menuType) values (3, 2, 3, 'A');
insert into OrderItem (menuItem, restaurantOrder, quantity, menuType) values (2, 2, 1, 'E');