INSERT INTO CUSTOMER (TITLE, FIRSTNAME, SURNAME, TELEPHONE_NUMBER) VALUES
  ('Mr', 'Teddy', 'Thompson', '02083333333'),
  ('Miss', 'Louise', 'Belcher', '02081111111'),
  ('Mr','Bob', 'Belcher', '02082222222');

INSERT INTO CUSTOMER_ADDRESS (STREET_NAME, CITY, POSTCODE) VALUES
  ('street1', 'city1', 'SW4 4NF'),
  ('street2', 'city2', 'SE3 3SF'),
  ('street3' ,'city3', 'W2 2NF');

INSERT INTO C_A (CUSTOMER_ID, ADDRESS_ID) VALUES
  (1, 1),
  (2, 2),
  (3, 3);