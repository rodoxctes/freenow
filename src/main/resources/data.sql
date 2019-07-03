/**
 * CREATE Script for init of DB
 */

-- Create 3 OFFLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username) values (1, now(), false, 'OFFLINE',
'driver01pw', 'driver01');

insert into driver (id, date_created, deleted, online_status, password, username) values (2, now(), false, 'OFFLINE',
'driver02pw', 'driver02');

insert into driver (id, date_created, deleted, online_status, password, username) values (3, now(), false, 'OFFLINE',
'driver03pw', 'driver03');


-- Create 3 ONLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username) values (4, now(), false, 'ONLINE',
'driver04pw', 'driver04');

insert into driver (id, date_created, deleted, online_status, password, username) values (5, now(), false, 'ONLINE',
'driver05pw', 'driver05');

insert into driver (id, date_created, deleted, online_status, password, username) values (6, now(), false, 'ONLINE',
'driver06pw', 'driver06');

-- Create 1 OFFLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username)
values
 (7,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'OFFLINE',
'driver07pw', 'driver07');

-- Create 1 ONLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username)
values
 (8,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'ONLINE',
'driver08pw', 'driver08');

-- manufacturers
insert into manufacturer(id, date_created, name) values (1, now(), 'BMW');
insert into manufacturer(id, date_created, name) values (2, now(), 'Peugeot');
insert into manufacturer(id, date_created, name) values (3, now(), 'Tesla');



-- Create 3 Cars

insert into car (id, date_created, license_plate, seat_count, convertible, deleted, rating, engine_type, manufacturer_id, selected)
values (1, now(), 'ABC123', 4, FALSE, FALSE, 4, 'GAS', 1, TRUE);

insert into car (id, date_created, license_plate, seat_count, convertible, deleted, rating, engine_type, manufacturer_id, selected)
values (2, now(), 'XYZ321', 4, FALSE, FALSE, 5, 'ELECTRIC', 3, FALSE);

insert into car (id, date_created, license_plate, seat_count, convertible, deleted, rating, engine_type, manufacturer_id, selected)
values (3, now(), 'QWE456', 4, FALSE, FALSE, 2, 'GAS', 2, FALSE);

-- Create 1 user

insert into user (id, date_created, username, password, deleted)
values (1, now(), 'myTaxi', '$2a$10$ROT6W3leTgFv2440B2unz.yt6bXm8LUxYljs3ri/IHSWEI2FQuXJy', FALSE);

--- Create a driver with car selected
insert into driver (id, date_created, deleted, online_status, password, username, car_id) values (10, now(), false, 'ONLINE',
'driver10pw', 'driver10', 1);
