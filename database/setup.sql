CREATE TABLE RestaurantUser(
  id SERIAL PRIMARY KEY,
  firstName VARCHAR(25) NOT NULL,
  lastName VARCHAR(25) NOT NULL,
  userName VARCHAR(25) NOT NULL,
  password VARCHAR(10) NOT NULL,
  type CHARACTER(1) NOT NULL,
  active BOOLEAN DEFAULT true,
  hireDate BIGINT
);

CREATE TABLE RestaurantTable(
  id SERIAL PRIMARY KEY,
  locationX NUMERIC(6,2) NOT NULL,
  locationY NUMERIC(6,2) NOT NULL,
  status CHAR(1) NOT NULL,
  tableType CHAR(1) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Walls(
  id SERIAL PRIMARY KEY,
  locationX NUMERIC(6,2) NOT NULL,
  locationY NUMERIC(6,2) NOT NULL,
  width NUMERIC(6,2) NOT NULL,
  height NUMERIC(6,2) NOT NULL,
  wallType CHAR(1) NOT NULL
);

CREATE TABLE RestaurantOrder(
  id SERIAL PRIMARY KEY,
  seatedTime BIGINT NOT NULL,
  orderTaken BIGINT,
  orderReady BIGINT,
  orderDelivered BIGINT,
  orderPaid BIGINT,
  customerLeft BIGINT,
  restaurantTable INTEGER NOT NULL,
  CONSTRAINT fk_order_table FOREIGN KEY (restaurantTable) REFERENCES RestaurantTable(id)
);

CREATE TABLE HoursWorked(
  id SERIAL PRIMARY KEY,
  restaurantUser INTEGER NOT NULL,
  timeStarted BIGINT NOT NULL,
  timeFinished BIGINT
);

CREATE TABLE WorkSchedule(
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  description VARCHAR(250)
);

CREATE TABLE ScheduleAssignment(
  restaurantTable INTEGER,
  waiter INTEGER,
  workSchedule INTEGER,
  PRIMARY KEY(restaurantTable, waiter, workSchedule),
  CONSTRAINT fk_schedule_table FOREIGN KEY (restaurantTable) REFERENCES RestaurantTable(id),
  CONSTRAINT fk_schedule_waiter FOREIGN KEY (waiter) REFERENCES RestaurantUser(id),
  CONSTRAINT fk_schedule_schedule FOREIGN KEY (workSchedule) REFERENCES WorkSchedule(id)
);

CREATE TABLE ScheduleTracker(
  id SERIAL PRIMARY KEY,
  workSchedule INTEGER NOT NULL,
  startTime BIGINT NOT NULL,
  endTime BIGINT,
  CONSTRAINT fk_scheduleTracker_schedule FOREIGN KEY (workSchedule) REFERENCES WorkSchedule(id)
);

CREATE TABLE Tax(
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  description VARCHAR(250),
  percentage NUMERIC(3,3),
  isActive BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE MenuItem(
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  description VARCHAR(250),
  price NUMERIC(7, 2) NOT NULL,
  menuType CHAR(1) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE OrderItem(
  id SERIAL PRIMARY KEY,
  menuItem INTEGER NOT NULL,
  restaurantOrder INTEGER NOT NULL,
  quantity INTEGER NOT NULL,
  menuType CHAR(1) NOT NULL,
  notes VARCHAR(250),
  CONSTRAINT fk_orderItem_menuItem FOREIGN KEY (menuItem) REFERENCES MenuItem (id),
  CONSTRAINT fk_orderItem_order FOREIGN KEY (restaurantOrder) REFERENCES RestaurantOrder (id)
);

CREATE TABLE TaxMenuItemRelated(
  tax INTEGER NOT NULL,
  menuItem INTEGER NOT NULL,
  PRIMARY KEY (tax,menuItem),
  CONSTRAINT FK_TaxMenuItemRelated_0 FOREIGN KEY (tax) REFERENCES Tax (id),
  CONSTRAINT FK_TaxMenuItemRelated_1 FOREIGN KEY (menuItem) REFERENCES MenuItem (id)
);

CREATE TABLE AdjustmentMaster(
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  description VARCHAR(250),
  active BOOLEAN NOT NULL DEFAULT true,
  defaultAmount NUMERIC(6,2) NOT NULL
);

CREATE TABLE Adjustment(
  id SERIAL PRIMARY KEY,
  adjustmentMaster INTEGER NOT NULL,
  restaurantOrder INTEGER NOT NULL,
  amount NUMERIC(6,2) NOT NULL,
  CONSTRAINT fk_adjustment_master FOREIGN KEY (adjustmentMaster) REFERENCES AdjustmentMaster(id),
  CONSTRAINT fk_adjustment_order FOREIGN KEY (restaurantOrder) REFERENCES RestaurantOrder(id)
);

CREATE TABLE Payment(
  id SERIAL PRIMARY KEY,
  amount NUMERIC(6,2) NOT NULL,
  paymentType CHAR(1) NOT NULL,
  tip NUMERIC(6,2) NOT NULL,
  restaurantOrder INTEGER NOT NULL,
  CONSTRAINT fk_payment_order FOREIGN KEY (restaurantOrder) REFERENCES RestaurantOrder(id)
);

CREATE TABLE CreditCompany(
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  description VARCHAR(250),
  active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE CreditPayment(
  id INTEGER PRIMARY KEY,
  cardNumber CHAR(16) NOT NULL,
  cardName VARCHAR(50) NOT NULL,
  cardType INTEGER NOT NULL,
  expireMonth INTEGER NOT NULL,
  expireYear INTEGER NOT NULL,
  CONSTRAINT fk_creditPayment_payment FOREIGN KEY (id) REFERENCES Payment(id),
  CONSTRAINT fk_creditPayment_company FOREIGN KEY (cardType) REFERENCES CreditCompany(id) 
);
