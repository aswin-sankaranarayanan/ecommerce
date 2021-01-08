INSERT INTO USERS(ID,FIRST_NAME,LAST_NAME,EMAIL_ID,PASSWORD,LOCKED) VALUES(1,'App User','1','app.user1@gmail.com','#$@$#%^^^@$@$@$#$@#$',0);
INSERT INTO USERS(ID,FIRST_NAME,LAST_NAME,EMAIL_ID,PASSWORD,LOCKED) VALUES(2,'App User','2','app.user2@gmail.com','#$@$#%^^^@$@$@$#$@#$',0);
INSERT INTO USERS(ID,FIRST_NAME,LAST_NAME,EMAIL_ID,PASSWORD,ROLE,LOCKED)
VALUES(3,'Admin User','1','admin.user1@gmail.com','#$@$#%^^^@$@$@$#$@#$','ADMIN',0);


INSERT INTO INVENTORY(ITEM,DESCRIPTION,PRICE,CATEGORY,AVAILABLE) VALUES('Test Item1','Item1 Description',75,'Book', 1);
INSERT INTO INVENTORY(ITEM,DESCRIPTION,PRICE,CATEGORY,AVAILABLE) VALUES('Test Item2','Item2 Description',175,'Book',1);
INSERT INTO INVENTORY(ITEM,DESCRIPTION,PRICE,CATEGORY,AVAILABLE) VALUES('Test Item3','Item3 Description',305,'Book',1);
INSERT INTO INVENTORY(ITEM,DESCRIPTION,PRICE,CATEGORY,AVAILABLE) VALUES('Test Item4','Item4 Description',75,'Book',1);
INSERT INTO INVENTORY(ITEM,DESCRIPTION,PRICE,CATEGORY,AVAILABLE) VALUES('Test Item5','Item5 Description',175,'Book',1);
INSERT INTO INVENTORY(ITEM,DESCRIPTION,PRICE,CATEGORY,AVAILABLE) VALUES('Test Item6','Item6 Description',305,'Book',1);


INSERT INTO CART(ID,USER_FK) VALUES(1000,2);

INSERT INTO CART_ITEMS(ID,CART_FK,INVENTORY_FK,QUANTITY) VALUES(1001,1000,1,1);
INSERT INTO CART_ITEMS(ID,CART_FK,INVENTORY_FK,QUANTITY) VALUES(1002,1000,2,2);