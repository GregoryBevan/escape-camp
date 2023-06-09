--liquibase formatted sql

-- Escamp camp event log

-- changeset elgregos:create-game-event-sequence
create sequence game_event_sequence;
--rollback drop sequence game_event_sequence;

-- changeset elgregos:create-game-event-table
create table if not exists game_event (
  id uuid primary key,
  sequence_num bigint not null default nextval('game_event_sequence'),
  created_at timestamp not null default (now() at time zone 'utc'),
  created_by uuid not null,
  version int not null,
  event_type varchar(100) not null,
  aggregate_id uuid not null,
  event jsonb not null);
--rollback drop table if exists game_event;

-- changeset elgregos:create-game-table-sequence
create sequence game_sequence;
--rollback drop sequence game_sequence;

-- changeset elgregos:create-game-table
create table if not exists game (
 id uuid primary key,
 sequence_num bigint not null default nextval('game_sequence'),
 version int not null,
 created_at timestamp not null,
 updated_at timestamp not null,
 details jsonb not null);
--rollback drop table if exists game;

-- changeset elgregos:add-user-fields-to-game-table
alter table game add column created_by uuid not null default '2d399fb8-d137-47a5-b848-1755bb80e62e';
alter table game add column updated_by uuid not null default '2d399fb8-d137-47a5-b848-1755bb80e62e';
