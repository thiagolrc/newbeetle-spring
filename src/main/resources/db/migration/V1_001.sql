create table vehicle_group (
  id uuid primary key not null,
  name varchar not null,
  created_at numeric not null,
  updated_at numeric not null,
  creator_id uuid not null,
  fleet_id uuid not null
);

create table vehicle (id uuid not null, fleet_id uuid not null, license_plate varchar(255), primary key (id));

create table vehicle_group_vehicles (vehicle_group_id uuid not null, vehicles_id uuid not null);

alter table vehicle_group_vehicles add constraint FK_vehicle_group_to_vehicle foreign key (vehicles_id) references vehicle;
alter table vehicle_group_vehicles add constraint FK_vehicle_to_vehicle_group foreign key (vehicle_group_id) references vehicle_group;